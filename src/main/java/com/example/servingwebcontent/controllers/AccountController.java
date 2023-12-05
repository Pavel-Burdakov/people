package com.example.servingwebcontent.controllers;
import com.example.servingwebcontent.exception.EntityAlreadyExist;
import com.example.servingwebcontent.exception.EntityNotFoundException;
import com.example.servingwebcontent.exception.ServiceException;
import com.example.servingwebcontent.models.Account;
import com.example.servingwebcontent.models.PersonWrapper;
import com.example.servingwebcontent.repositories.AccountRepository;
import com.example.servingwebcontent.repositories.PeopleRepository;
import com.example.servingwebcontent.services.AccountService;
import com.example.servingwebcontent.services.ExchangeRateService;
import jakarta.validation.Valid;
import org.hibernate.boot.jaxb.hbm.internal.ExecuteUpdateResultCheckStyleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.servingwebcontent.services.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final PeopleRepository peopleRepository;
    @Autowired
    ExchangeRateService exchangeRateService;

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

    @PostMapping("/new")
    public ResponseEntity<Account> createAccount(@RequestBody PersonWrapper personWrapper) throws Exception {
        Optional<Account> a = accountRepository.findById(personWrapper.getAccount().getId());
        if (a.isPresent()) {
            throw new EntityAlreadyExist(" " + personWrapper.getAccount().getId());
        }
        /*Optional<Person> p = peopleRepository.findByEmail(personWrapper.getPerson().getEmail());
        if (p.isPresent()){
            throw new EntityNotFoundException(personWrapper.getPerson().getId());
        }*/
        personWrapper.getPerson().addAccount(personWrapper.getAccount());
        peopleRepository.save(personWrapper.getPerson());
        return ResponseEntity.status(201).body(personWrapper.getAccount());
    }

    @PutMapping
    public ResponseEntity<Account> updateAccount(@RequestBody @Valid Account account) throws EntityNotFoundException {
        Optional<Account> a = accountRepository.findById(account.getId());
        if (!a.isPresent())
            throw new EntityNotFoundException(account.getId());
        Account oldAccount = a.get();
        oldAccount.setMoneyAvailable(account.getMoneyAvailable());
        return ResponseEntity.ok().body(accountRepository.save(oldAccount));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") int id) throws EntityNotFoundException {
        Optional<Account> account = accountRepository.findById(id);
        if (!account.isPresent())
            throw new EntityNotFoundException(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(account.get());
    }

    @GetMapping(value = "/{id}/{currency}")
    public ResponseEntity<Double> convertCurrency(@PathVariable("id") int id, @PathVariable("currency") String currency) throws Exception {
        Optional<Account> account = accountRepository.findById(id);
        if (!account.isPresent())
            throw new EntityNotFoundException(id);
        if (!account.get().getCurrency().equals("RUB")) {
            throw new ServiceException("конвертация только рублевого счета ");
        }
        System.out.println(exchangeRateService.getUSDCurrencyRate());
        switch (currency) {
            case "USD" -> {
                String ups = exchangeRateService.getUSDCurrencyRate().replace(',', '.');
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Double.parseDouble(ups) * account.get().getMoneyAvailable());
            }
            case "EUR" -> {
                String ups = exchangeRateService.getEURCurrencyRate().replace(',', '.');
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Double.parseDouble(ups) * account.get().getMoneyAvailable());
            }
            default -> {
                return null;
            }
        }
    }

    @DeleteMapping(value = "delete/{accountId}")
    public ResponseEntity<Account> deletePerson(@PathVariable("accountId") int accountId) throws EntityNotFoundException {
        Optional<Account> a = accountRepository.findById(accountId);
        if (!a.isPresent())
            throw new EntityNotFoundException(accountId);
        accountRepository.deleteById(accountId);
        return ResponseEntity.ok().body(a.get());
    }
}

