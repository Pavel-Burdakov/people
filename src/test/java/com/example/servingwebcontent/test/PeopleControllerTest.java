
package com.example.servingwebcontent.test;

import com.example.servingwebcontent.models.Person;
import com.example.servingwebcontent.repositories.PeopleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PeopleControllerTest {
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

    @BeforeEach
    void setUpPerson() {
        peopleRepository.save(p1);
        peopleRepository.save(p2);
    }

    @AfterEach
    void clearPerson() {
        peopleRepository.deleteAll();
    }

    @Test
    void getAllPeopleSuccess() throws Exception {
        List<Person> personList = List.of(p1, p2);
        mockMvc.perform(
                        get("/people")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(personList)));
    }

    @Test
    public void createOnePersonSuccess() throws Exception {

        mockMvc.perform(
                        post("/people/new")
                                .content(objectMapper.writeValueAsString(p3))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Test3"))
                .andExpect(jsonPath("$.age").value(33))
                .andExpect(jsonPath("$.email").value("Test3@Test3.com"));
    }

}

    /**
     * поменять статус isCreated на isOk +
     * поменять body
     * разделить базу данных
     * json pas поиск по массиву +
     *
     * в контроллере сделать сделать созадиание и удаление персонов
     */



