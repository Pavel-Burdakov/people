package com.example.servingwebcontent.controllers;
import com.example.servingwebcontent.exception.EntityAlreadyExist;
import com.example.servingwebcontent.exception.EntityNotFoundException;
import com.example.servingwebcontent.models.Account;
import com.example.servingwebcontent.models.Person;
import com.example.servingwebcontent.repositories.AccountRepository;
import com.example.servingwebcontent.repositories.PeopleRepository;
import com.example.servingwebcontent.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final PeopleRepository peopleRepository;

    public AccountController(AccountRepository accountRepository, AccountService accountService, PeopleRepository peopleRepository) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.peopleRepository = peopleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Account>> listAllAccount() {
        List<Account> accounts = accountRepository.findAll();
        return ResponseEntity.ok().body(accounts);
    }
/*
    @PostMapping("/new")
    public ResponseEntity<Account> createAccount(@RequestBody Account account, @RequestBody Person person) throws Exception{
        Optional<Account> a = accountRepository.findById(account.getId());
        if (a.isPresent()){
            throw new EntityAlreadyExist(" " + account.getId());
        }
        Optional<Person> p = peopleRepository.findByEmail(person.getEmail());
        if (!p.isPresent()){
            throw new EntityNotFoundException(person.getId());
        }
        person.addAccount(account);
        peopleRepository.save(person);
        return ResponseEntity.status(201).body(account);


    }*/
}

