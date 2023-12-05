package com.example.servingwebcontent.test;
import com.example.servingwebcontent.exception.EntityNotFoundException;
import com.example.servingwebcontent.models.Account;
import com.example.servingwebcontent.models.Person;
import com.example.servingwebcontent.models.PersonWrapper;
import com.example.servingwebcontent.repositories.AccountRepository;
import com.example.servingwebcontent.repositories.PeopleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {
    @LocalServerPort
    private Integer port;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PeopleRepository peopleRepository;
    private final Person person = new Person("T1", 11, "t1@t1.com");
    private final Account account = new Account("USD", 100);
    private final Account updAccount = new Account("USD", 500);
    private final PersonWrapper personWrapper = new PersonWrapper(person, account);

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUpAccount() {
    }

    @AfterEach
    void clearAccount() {
        peopleRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void getAllAccontSuccess() throws Exception {
        person.addAccount(account);
        peopleRepository.save(person);
        List<Account> accountList = List.of(account);
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountList)));
    }

    @Test
    void createAccountSuccess() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        mockMvc.perform(
                        post("/account/new")
                                .content(objectMapper.writeValueAsString(personWrapper))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].currency").value(personWrapper.getAccount().getCurrency()))
                .andExpect(jsonPath("$.[0].moneyAvailable").value(personWrapper.getAccount().getMoneyAvailable()))
                .andExpect(jsonPath("$.[0].owner.name").value(personWrapper.getPerson().getName())
                );
    }

    @Test
    void updateAccountSuccess() throws Exception {
        personWrapper.getPerson().addAccount(personWrapper.getAccount());
        peopleRepository.save(personWrapper.getPerson());
        updAccount.setId(account.getId());
        int idAccountToUpdate = personWrapper.getAccount().getId();
        mockMvc.perform(
                        put("/account", idAccountToUpdate)
                                .content(objectMapper.writeValueAsString(updAccount))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.currency").value(updAccount.getCurrency()))
                .andExpect(jsonPath("$.moneyAvailable").value(updAccount.getMoneyAvailable()));
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(account.getId()))
                .andExpect(jsonPath("$.[0].currency").value(updAccount.getCurrency()))
                .andExpect(jsonPath("$.[0].moneyAvailable").value(updAccount.getMoneyAvailable()))
                .andExpect(jsonPath("$.[0].owner.name").value(personWrapper.getPerson().getName()));
        mockMvc.perform(
                        put("/account"))
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    @Test
    public void getAccountByIdSuccess() throws Exception {
        personWrapper.getPerson().addAccount(personWrapper.getAccount());
        peopleRepository.save(personWrapper.getPerson());
        long id = personWrapper.getAccount().getId();
        mockMvc.perform(get("/account/{id}", id))
                .andExpect(jsonPath("$.id").value(personWrapper.getAccount().getId()))
                .andExpect(jsonPath("$.currency").value(personWrapper.getAccount().getCurrency()))
                .andExpect(jsonPath("$.moneyAvailable").value(personWrapper.getAccount().getMoneyAvailable()))
                .andExpect(jsonPath("$.owner.name").value(personWrapper.getPerson().getName()));
        mockMvc.perform(
                        get("/account/5"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    @Test
    public void deleteOneAccountSuccess() throws Exception {
        personWrapper.getPerson().addAccount(personWrapper.getAccount());
        peopleRepository.save(personWrapper.getPerson());
        int idAccountToDelete = personWrapper.getAccount().getId();
        mockMvc.perform(
                        delete("/account/delete/{id}", idAccountToDelete))
                .andExpect(status().isOk());
        mockMvc.perform(get("/account/{id}", idAccountToDelete))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(personWrapper.getPerson().getId()))
                .andExpect(jsonPath("$.[0].name").value(personWrapper.getPerson().getName()))
                .andExpect(jsonPath("$.[0].age").value(personWrapper.getPerson().getAge()))
                .andExpect(jsonPath("$.[0].email").value(personWrapper.getPerson().getEmail()));
    }
}

