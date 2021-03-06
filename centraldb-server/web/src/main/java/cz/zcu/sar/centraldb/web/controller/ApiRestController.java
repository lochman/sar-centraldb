package cz.zcu.sar.centraldb.web.controller;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.AddressTypeService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import cz.zcu.sar.centraldb.persistence.service.PersonTypeService;
import cz.zcu.sar.centraldb.persistence.wrapper.PageRequestWrapper;
import cz.zcu.sar.centraldb.persistence.wrapper.PersonAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

/**
 * Created by Petr on 12/23/2016.
 */
@RestController
@RequestMapping("/api/")
public class ApiRestController {

    @Autowired
    private PersonTypeService personTypeService;

    @Autowired
    private AddressTypeService addressTypeService;

    @Autowired
    private PersonService personService;

    @Value("${core.url}")
    private String coreUrl;

    @GetMapping(value = "user")
    public ResponseEntity<?> getUser(Authentication user) {
        String userJson;
        if (user != null) {
            userJson = "{\"authenticated\":" + user.isAuthenticated() + ",\"username\":\"" + user.getName() + "\",\"isAdmin\":" + user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) + "}";
        } else {
            userJson = "{\"authenticated\":false}";
        }
        return ResponseEntity.ok().body(userJson);
    }

    @Secured({ "ROLE_USER" })
    @PostMapping("person/search/paginated")
    public Page<Person> getPeopleByQuery(@RequestBody PageRequestWrapper request) {
        return personService.getPeopleByQuery(request);
    }

    @Secured({ "ROLE_USER" })
    @GetMapping(value = "person/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personService.findOne(id);
        return person.isPresent() ? ResponseEntity.ok(person.get()) : userNotFound(id.toString());
    }

    private ResponseEntity<?> userNotFound(String id) {
        return new ResponseEntity<>("{\"error\": \"Uživatel s id \'" + id + "\' nebyl nalezen.\"}", HttpStatus.NOT_FOUND);
    }

    @Secured({ "ROLE_ADMIN" })
    @PutMapping(value = "person/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @RequestBody PersonAddress personAddress, Authentication auth) {
        Person person = personAddress.getPerson();
        if (person == null || id != person.getId()) {
            return userNotFound(id.toString());
        }
        person = personService.updatePersonWeb(personAddress, auth.getName());
        //try to put it to sync in core application
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try{
            restTemplate.put(coreUrl+"person/"+person.getId(), person, Person.class);
        }catch (HttpClientErrorException e){
            System.out.println(e.toString());
        }
        return ResponseEntity.ok("{\"status\": true}");
    }

    @Secured({ "ROLE_USER" })
    @GetMapping(value = "person/types")
    public ResponseEntity<?> getPersonTypes() {
        return ResponseEntity.ok(personTypeService.findAll());
    }
    @Secured({ "ROLE_USER" })
    @GetMapping(value = "address/types")
    public ResponseEntity<?> getAddressTypes() {
        return ResponseEntity.ok(addressTypeService.findAll());
    }

    @Secured({ "ROLE_ADMIN" })
    @PostMapping(value = "person")
    public ResponseEntity<?> createNewPerson(@RequestBody PersonAddress personAddress, Authentication auth) {
        Person person = personService.createPersonWeb(personAddress, auth.getName());
        //try to put it to sync in core application
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try{
            restTemplate.postForObject(coreUrl+"person", person, Person.class);
        }catch (HttpClientErrorException e){
            System.out.println(e.toString());
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(person.getId()).toUri();
        return ResponseEntity.created(location).body("{\"status\": true}");
    }
}
