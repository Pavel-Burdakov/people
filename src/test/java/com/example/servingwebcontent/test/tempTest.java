package com.example.servingwebcontent.test;
import com.example.servingwebcontent.models.Account;
import com.example.servingwebcontent.models.Person;
import com.example.servingwebcontent.repositories.AccountRepository;
import com.example.servingwebcontent.repositories.PeopleRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootTest
public class tempTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PeopleRepository peopleRepository;

    @Test
    public void sendOne() {
        Person person = new Person("t7", 7, "t7@t7.t7");
        //Account account = new Account("eur", 0, person);
        person.addAccount(new Account("usdt", 0));
        person.addAccount(new Account("RUB", 0));
        //person.setAccountList(new ArrayList<>(Collections.singletonList(new Account("usdt", 0))));
        peopleRepository.save(person);
        //accountRepository.save(account);
    }
}
