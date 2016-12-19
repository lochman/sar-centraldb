package cz.zcu.sar.centraldb.web.controller;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@RestController
@Secured({ "ROLE_ADMIN" })
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public Collection<Person> getAll() {
        return personRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Person> get(@PathVariable String id) {
        System.out.println("get called "+id);
        return ResponseEntity.ok(personRepository.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addPerson(@RequestBody Person person) {
        System.out.println("addperson called "+person.getName()+ " "+person.getId());
        person.setTemporary(true);
        person.setName(person.getName()+"ppp");
        person = personRepository.save(person);
        System.out.println("2addperson called "+person.getName()+ " "+person.getId());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(person.getId()).toUri();
        return ResponseEntity.created(location).body(person);
    }

    /*@GetMapping(value = "/{name}")
    public Person findPersonByName(@PathVariable String name) {
        return personRepository.findByName(name).get();
    }*/
}
