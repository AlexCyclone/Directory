package com.devianta.controller;

import com.devianta.Application;
import com.devianta.model.Department;
import com.devianta.servise.DepartmentService;
import org.hamcrest.Matchers;
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

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void getNoPositionDepartmentTest() throws Exception {
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

    @Test
    public void postMinimalFilledPosition() throws Exception {
        mockMvc.perform(post("/restapi/department/" + childId + "/positions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(minimalFilledPositionObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department/" + childId + "/positions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("position name"))
                .andExpect(jsonPath("$[0].departmentId").value(childId))
                .andDo(print());
    }

    private JsonObject minimalFilledPositionObject() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        return factory.createObjectBuilder()
                .add("name", "position name")
                .build();
    }

    @Test
    public void postClearPosition() throws Exception {
        mockMvc.perform(post("/restapi/department/" + childId + "/positions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(clearPositionObject().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid Position parameters"))
                .andExpect(jsonPath("$.path").value("/restapi/department/" + childId + "/positions"))
                .andDo(print());
    }

    private JsonObject clearPositionObject() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        return factory.createObjectBuilder()
                .add("name", "")
                .build();
    }

    @Test
    public void postIgnoredFieldsPosition() throws Exception {
        mockMvc.perform(post("/restapi/department/" + childId + "/positions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ignoredFieldsPositionObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department/" + childId + "/positions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(Matchers.not(999999)))
                .andExpect(jsonPath("$[0].name").value("position"))
                .andExpect(jsonPath("$[0].departmentId").value(childId))
                .andExpect(jsonPath("$[0].person.name").value("person"))
                .andExpect(jsonPath("$[0].person.contact.emails[0].email").value("q@q"))
                .andDo(print());
    }

    private JsonObject ignoredFieldsPositionObject() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        return factory.createObjectBuilder()
                .add("id", 999999)
                .add("name", "position")
                .add("departmentId", 1)
                .add("person", factory.createObjectBuilder()
                        .add("name", "person")
                        .add("contact", factory.createObjectBuilder()
                                .add("emails", factory.createArrayBuilder()
                                        .add(factory.createObjectBuilder()
                                                .add("name", "namedata")
                                                .add("email", "q@q")
                                                .add("common", true)))))
                .build();
    }
}
