package com.devianta.controller;

import com.devianta.Application;
import com.devianta.model.Department;
import com.devianta.servise.DepartmentService;
import org.junit.After;
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
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
@WebAppConfiguration
public class CommonRestControllerPositionTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CommonRestController commonRestController;

    private int rootId;
    private int childId;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        Department root = Department.builder().name("root").build();
        Department child = Department.builder().name("child").build();
        departmentService.saveRootDepartment(root);
        departmentService.saveChildDepartment(root.getId(), child);
        rootId = (int) root.getId();
        childId = (int) child.getId();
    }

    @After
    public void clear() throws Exception {
        departmentService.clearDatabase();
    }

    @Test
    public void getNoChildDepartmentTest() throws Exception {
        mockMvc.perform(get("/restapi/department/" + rootId + "/positions"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Positions not found"))
                .andExpect(jsonPath("$.path").value("/restapi/department/" + rootId + "/positions"))
                .andDo(print());
    }
}
