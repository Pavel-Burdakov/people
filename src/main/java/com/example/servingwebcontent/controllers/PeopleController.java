package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.models.Person;
import com.example.servingwebcontent.services.PeopleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/people")

public class PeopleController {


    private final PeopleService peopleService;

    // вот такая инициализация бина и его внедрение по конвенции

    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @GetMapping
    public List<Person> getAllPerson() {
        List<Person> personList = peopleService.findAll();
        return personList;       // model.addAttribute("people", peopleService.findAll());

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
