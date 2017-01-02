package cz.zcu.sar.centraldb.web.controller;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.AddressTypeService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import cz.zcu.sar.centraldb.persistence.service.PersonTypeService;
import cz.zcu.sar.centraldb.persistence.wrapper.PageRequestWrapper;
import cz.zcu.sar.centraldb.persistence.wrapper.PersonAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

/**
 * Created by Petr on 12/23/2016.
 */
@RestController
//@Secured({ "ROLE_ADMIN" })
@RequestMapping("/api/")
public class ApiRestController {

    @Autowired
    private PersonTypeService personTypeService;

    @Autowired
    private AddressTypeService addressTypeService;

    @Autowired
    private PersonService personService;

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

    //@Secured({ "ROLE_USER" })
    @PostMapping("person/search/paginated")
    public Page<Person> getPeopleByQuery(@RequestBody PageRequestWrapper request) {
        System.out.println(request.getQueryParams());
        return personService.getPeopleByQuery(request);
    }

    @GetMapping(value = "person/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable String personId) {
        Optional<Person> person;
        try {
            Long id = Long.parseLong(personId);
            person = personService.findOne(id);
        } catch (NumberFormatException e) {
            person = Optional.empty();
        }
        return person.isPresent() ? userNotFound(personId) : ResponseEntity.ok(person);
    }

    private ResponseEntity<?> userNotFound(String id) {
        return new ResponseEntity<>("{\"error\": \"Uživatel s id \'" + id + "\' nebyl nalezen.\"}", HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "person/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable String id, @RequestBody PersonAddress personAddress, Authentication auth) {
        Person person = personAddress.getPerson();
        if (person == null || Long.parseLong(id) != person.getId()) {
            return userNotFound(id);
        }
        personService.save(person);
        person.setModifiedBy(auth.getName());
        System.out.println("update person id" + person.getId() + " adresy " + personAddress.getAddressWrappers());
        personService.saveAddress(person, personAddress, auth.getName());
        System.out.println("person.addressWrappers = " + person.getAddressWrappers());
        return ResponseEntity.ok("{\"status\": true}");
    }

    @GetMapping(value = "person/types")
    public ResponseEntity<?> getPersonTypes() {
        return ResponseEntity.ok(personTypeService.findAll());
    }

    @GetMapping(value = "address/types")
    public ResponseEntity<?> getAddressTypes() {
        return ResponseEntity.ok(addressTypeService.findAll());
    }

    @PostMapping(value = "person")
    public ResponseEntity<?> createNewPerson( @RequestBody PersonAddress personAddress, Authentication auth) {
        Person person = personService.createPerson(personAddress, auth.getName());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(person.getId()).toUri();
        return ResponseEntity.created(location).body("{\"status\": true}");
    }
}
