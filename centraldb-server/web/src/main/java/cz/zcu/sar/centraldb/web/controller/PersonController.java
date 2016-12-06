package cz.zcu.sar.centraldb.web.controller;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(personRepository.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addPerson(@RequestBody Person person) {
        System.out.println("addperson called");
        person.setTemporary(true);
        personRepository.save(person);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(person.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{name}")
    public Person findPersonByName(@PathVariable String name) {
        return personRepository.findByName(name).get();
    }
}
