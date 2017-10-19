package com.devianta.controller;

import com.devianta.Application;
import com.devianta.model.Department;
import com.devianta.servise.DepartmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
@WebAppConfiguration
public class CommonRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    CommonRestController commonRestController;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    public void getRootDepartmentTest() throws Exception {
        departmentService.saveRootDepartment(Department.builder().name("root").build());

        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("root"))
                .andExpect(jsonPath("$.hasChild").value(false))
                .andExpect(jsonPath("$.hasPositions").value(false))
                .andExpect(jsonPath("$.contact").doesNotExist())
                .andExpect(jsonPath("$.parentId").doesNotExist())
                .andExpect(jsonPath("$.parentDepartment").doesNotExist())
                .andExpect(jsonPath("$.childDepartments").doesNotExist())
                .andExpect(jsonPath("$.positions").doesNotExist())
                .andDo(print());
    }

    @Test
    @Transactional
    public void getEmptyRootDepartmentTest() throws Exception {
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Root department not found"))
                .andExpect(jsonPath("$.path").value("/restapi/department"))
                .andDo(print());
    }
}