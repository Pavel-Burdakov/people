
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
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    @BeforeEach
    void setUpPerson() {
        person = new Person("Test1", 11, "Test1@Test1.com");
        // создать ptrsona в БД с помощью методов репозитория
    }

    @AfterEach
    void clearDrones() {
        peopleRepository.deleteAll();
    }

    @Test
    void registerDroneSuccess() throws Exception {

        mockMvc.perform(
                        get("/people")
                                .content(objectMapper.writeValueAsString(person))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value("Test1"))
                .andExpect((ResultMatcher) jsonPath("$.age").value(11))
                .andExpect((ResultMatcher) jsonPath("$.email").value("Test1@Test1.com"));
    }

    /**
     * поменять статус isCreated на isOk
     * поменять body
     * разделить базу данных
     * json pas поиск по массиву
     *
     * в контроллере сделать сделать созадиание и удаление персонов
     */

}

