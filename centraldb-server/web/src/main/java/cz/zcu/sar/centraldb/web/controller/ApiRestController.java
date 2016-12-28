package cz.zcu.sar.centraldb.web.controller;

import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.persistence.helper.PersonAddress;
import cz.zcu.sar.centraldb.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.persistence.repository.AddressTypeRepository;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.persistence.repository.PersonTypeRepository;
import cz.zcu.sar.centraldb.persistence.service.PageRequestWrapper;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by Petr on 12/23/2016.
 */
@RestController
//@Secured({ "ROLE_ADMIN" })
@RequestMapping("/api/")
public class ApiRestController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PersonTypeRepository personTypeRepository;

    @Autowired
    private AddressTypeRepository addressTypeRepository;

    @Autowired
    private PersonService personService;


    @GetMapping(value = "user")
    public ResponseEntity<?> getUser(Authentication user) {
        String userJson;
        if (user != null) {
            userJson = "{\"authenticated\":" + user.isAuthenticated() + ",\"username\":\"" + user.getName() + "\",\"isAdmin\":" + user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) + "}";
        }else{
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
    public ResponseEntity<?> getPersonById(@PathVariable String id) {
        Person person = null;
        try{
            person = personRepository.findOne(Long.parseLong(id));
        }
        catch ( NumberFormatException e) {
            //System.out.println(e.getMessage());
        }
        return person == null ? new ResponseEntity<>("{\"error\": \"Uživatel s id \'" + id + "\' nebyl nalezen.\"}", HttpStatus.NOT_FOUND) :
                ResponseEntity.ok(person);
    }

    @GetMapping(value = "person/types")
    public ResponseEntity<?> getPersonTypes() {
        return ResponseEntity.ok(personTypeRepository.findAll());
    }

    @GetMapping(value = "address/types")
    public ResponseEntity<?> getAddressTypes() {
        return ResponseEntity.ok(addressTypeRepository.findAll());
    }

    @PutMapping(value = "person/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable String id, @RequestBody PersonAddress personAddress, Authentication auth) {
        Person person = personAddress.getPerson();
        if(person == null || Long.parseLong(id) != person.getId()){
            return new ResponseEntity<>("{\"error\": \"Uživatel s id \'" + id + "\' nebyl nalezen.\"}", HttpStatus.NOT_FOUND);
        }
        person.setModifiedBy(auth.getName());
        personRepository.save(person);
        System.out.println("update person id" + person.getId() + " adresy " + personAddress.getAddressWrappers());
        for(Address a: personAddress.getAddressWrappers()){
            a.setPerson(person);
            a.setModifiedBy(auth.getName());
            addressRepository.save(a);
        }
        System.out.println(person.getAddressWrappers());
        personRepository.save(person);
        return ResponseEntity.ok("{\"status\": true}");
    }


    @PostMapping(value = "person")
    public ResponseEntity<?> createNewPerson( @RequestBody PersonAddress personAddress, Authentication auth) {
        Person person = personAddress.getPerson();
        person.setModifiedBy(auth.getName());
        System.out.println(person.getBirthDate().getTime());
        personService.savePersonAsTemp(person);
        for(Address a: personAddress.getAddressWrappers()){
            a.setPerson(person);
            a.setModifiedBy(auth.getName());
            addressRepository.save(a);
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(person.getId()).toUri();
        return ResponseEntity.created(location).body("{\"status\": true}");
    }
    /*@GetMapping(value = "/{name}")
    public Person findPersonByName(@PathVariable String name) {
        return personRepository.findByName(name).get();
    }*/
}
