package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.models.Person;
import com.example.servingwebcontent.repositories.PeopleRepository;
import com.example.servingwebcontent.services.PeopleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/people")

public class PeopleController {
    private final PeopleService peopleService;
    private final PeopleRepository peopleRepository;


    public PeopleController(PeopleService peopleService, PeopleRepository peopleRepository) {
        this.peopleService = peopleService;
        this.peopleRepository = peopleRepository;

    }
    @GetMapping
    public List<Person> getAllPerson() {
        return  peopleService.findAll();
    }


    @PostMapping("/new")
    public ResponseEntity<Person> createPerson(Person person) {
        Person p1 = peopleRepository.save(person);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(p1);

    }


   /* @GetMapping("/{id}")
    public String show(@PathVariable("id") int id){
        *//*model.addAttribute("person", peopleService.findOne(id));
        return "people/person";*//*
    }
*/

    /*@GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person, Model model){

        return "people/new";
    }*/


   /* @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()){
            return "people/edit";
        }
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        peopleService.delete(id);
        return "redirect:/people";

    }


    @ModelAttribute("headerMessage")
    public String populateHeaderMessage(){
        return "Welcome to our website";
    }
*/
}
