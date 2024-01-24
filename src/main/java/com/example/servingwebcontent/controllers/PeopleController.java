package com.example.servingwebcontent.controllers;
import com.example.servingwebcontent.exception.EntityAlreadyExist;
import com.example.servingwebcontent.exception.EntityNotFoundException;
import com.example.servingwebcontent.models.MyUser;
import com.example.servingwebcontent.models.Person;
import com.example.servingwebcontent.repositories.PeopleRepository;
import com.example.servingwebcontent.services.PeopleService;
import com.example.servingwebcontent.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final PeopleRepository peopleRepository;
    private UserService service;

    public PeopleController(PeopleService peopleService, PeopleRepository peopleRepository, UserService service) {
        this.peopleService = peopleService;
        this.peopleRepository = peopleRepository;
        this.service = service;
    }

    @GetMapping("/welcome")
    public String unProtectedPage() {
        return "Welcome to unprotected page";
    }

    @PostMapping("/new-user")
    public String addUser(@RequestBody MyUser myUser) {
        service.addUser(myUser);
        return "User is saved";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<Person>> listAllPersons() {
        List<Person> persons = peopleRepository.findAll();
        return ResponseEntity.ok().body(persons);
    }

    // todo еще раз разобраться с ResponseEntity, RequestBody
    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) throws EntityAlreadyExist {
        Optional<Person> p = peopleRepository.findByEmail(person.getEmail());
        if (p.isPresent()) {
            throw new EntityAlreadyExist(person.getEmail());
        }
        peopleRepository.save(person);
        return ResponseEntity.status(201).body(person);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Person> getPerson(@PathVariable("id") int id) throws EntityNotFoundException {
        Optional<Person> person = peopleRepository.findById(id);
        if (!person.isPresent())
            throw new EntityNotFoundException(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(person.get());
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_MODER')")
    public ResponseEntity<Person> updatePerson(@RequestBody @Valid Person person) throws EntityNotFoundException {
        Optional<Person> p = peopleRepository.findById(person.getId());
        if (!p.isPresent())
            throw new EntityNotFoundException(person.getId());
        Person oldPerson = p.get();
        oldPerson.setName(person.getName());
        oldPerson.setAge(person.getAge());
        oldPerson.setEmail(person.getEmail());
        return ResponseEntity.ok().body(peopleRepository.save(oldPerson));
    }

    @DeleteMapping(value = "delete/{personId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Person> deletePerson(@PathVariable("personId") int personId) throws EntityNotFoundException {
        Optional<Person> p = peopleRepository.findById(personId);
        if (!p.isPresent())
            throw new EntityNotFoundException(personId);
        peopleRepository.deleteById(personId);
        return ResponseEntity.ok().body(p.get());
    }
}
