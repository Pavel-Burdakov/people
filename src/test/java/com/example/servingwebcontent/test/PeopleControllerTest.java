
package com.example.servingwebcontent.test;

import com.example.servingwebcontent.models.Person;
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@AutoConfigureMockMvc

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PeopleControllerTest {

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
    private PeopleRepository peopleRepository;
    private Person person;
    private final Person p1 = new Person("Test1", 11, "Test1@Test1.com");
    private final Person p2 = new Person("Test2", 22, "Test2@Test2.com");
    private final Person p3 = new Person("Test3", 33, "Test3@Test3.com");

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

        peopleRepository.deleteAll();

    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void getAllPeopleSuccess() throws Exception {

        peopleRepository.save(p1);
        peopleRepository.save(p2);
        List<Person> personList = List.of(p1, p2);
        mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(personList)));
    }

    // TODO: 12.11.2023 сделать проверку того что объект сохранился, дописать методы контроллера
    @Test
    public void createOnePersonSuccess() throws Exception {

        mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").doesNotExist());


        mockMvc.perform(
                        post("/people/new")
                                .content(objectMapper.writeValueAsString(p1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());

        List<Person> personList = List.of(p1);
        System.out.println(p1.getId());
        mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.[0].id").value(p1.getId()))
                .andExpect(jsonPath("$.[0].name").value(p1.getName()))
                .andExpect(jsonPath("$.[0].age").value(p1.getAge()))
                .andExpect(jsonPath("$.[0].email").value(p1.getEmail()));
                //.andExpect(content().json(objectMapper.writeValueAsString(personList)));

    }

}

    /**
     * поменять статус isCreated на isOk +
     * поменять body
     * разделить базу данных +
     * json pas поиск по массиву +
     *
     * в контроллере сделать сделать созадиание и удаление персонов
     */



