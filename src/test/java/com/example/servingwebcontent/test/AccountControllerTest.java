package com.example.servingwebcontent.test;
import com.example.servingwebcontent.models.Account;
import com.example.servingwebcontent.models.Person;
import com.example.servingwebcontent.repositories.AccountRepository;
import com.example.servingwebcontent.repositories.PeopleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUpPerson() {
    }

    @AfterEach
    void clearPerson() {
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
        List<Person> personList = List.of(person);
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(personList)));
    }
}
