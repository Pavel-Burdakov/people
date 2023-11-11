package com.example.servingwebcontent.controllers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FirstController {

    @GetMapping("/hello")
    public String helloPage(HttpServletRequest request){
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        System.out.println("Hello " + name + " " + surname);
        return "first/hello";

    }

    @GetMapping("/bye")
    public String byePage(@RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "surname", required = false) String surname,
                          Model model){

        model.addAttribute("message", "Hello " + name + " " + surname);

        return "first/bye";

    }

    @GetMapping("/calc")
    public String calcPage(@RequestParam(value = "firstValue", required = false) int firstValue ,
                           @RequestParam(value = "secondValue", required = false) int secondValue,
                           @RequestParam(value = "option", required = false) String option,
                           Model model){
        int result = 0;
        switch (option) {
            case "sum":
                result = firstValue + secondValue;
                break;
            case "sub":
                result = firstValue - secondValue;
                break;
        }


        model.addAttribute("result", result);



        return "first/calc";
    }

}
