package com.devianta.controller;

import com.devianta.Application;
import com.devianta.model.Department;
import com.devianta.model.contact.Contact;
import com.devianta.model.contact.Phone;
import com.devianta.servise.DepartmentService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.hamcrest.Matchers;
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

    @After
    public void clear() throws Exception {
        departmentService.clearDatabase();
    }

    @Test
    public void getEmptyRootDepartmentTest() throws Exception {
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Root department not found"))
                .andExpect(jsonPath("$.path").value("/restapi/department"))
                .andDo(print());
    }

    @Test
    public void putEmptyRootDepartmentTest() throws Exception {
        mockMvc.perform(put("/restapi/department")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(emptyDepartmentObject().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid Department parameters"))
                .andExpect(jsonPath("$.path").value("/restapi/department"))
                .andDo(print());
    }

    private JsonObject emptyDepartmentObject() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        return factory.createObjectBuilder()
                .add("name", "")
                .build();
    }

    @Test
    public void getRootDepartmentTest() throws Exception {
        departmentService.saveRootDepartment(Department.builder().name("root").build());

        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").isNumber())
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
    public void putMinimalFilledRootDepartmentTest() throws Exception {
        mockMvc.perform(put("/restapi/department")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(minimalFilledDepartmentTestObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("testDept"))
                .andDo(print());
    }

    private JsonObject minimalFilledDepartmentTestObject() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        return factory.createObjectBuilder()
                .add("name", "testDept")
                .build();
    }

    @Test
    public void putRewriteRootDepartmentTest() throws Exception {
        Department department = Department.builder().name("root").build();
        departmentService.saveRootDepartment(department);

        mockMvc.perform(put("/restapi/department")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(minimalFilledDepartmentTestObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value((int) department.getId()))
                .andExpect(jsonPath("$.name").value("testDept"))
                .andDo(print());
    }

    @Test
    public void putIgnoredFieldsRootDepartmentTest() throws Exception {
        mockMvc.perform(put("/restapi/department")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ignoredFieldsDepartmentObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(Matchers.not(999999)))
                .andExpect(jsonPath("$.name").value("dept"))
                .andExpect(jsonPath("$.hasChild").value(false))
                .andExpect(jsonPath("$.hasPositions").value(false))
                .andExpect(jsonPath("$.contact").doesNotExist())
                .andExpect(jsonPath("$.parentId").doesNotExist())
                .andExpect(jsonPath("$.parentDepartment").doesNotExist())
                .andExpect(jsonPath("$.childDepartments").doesNotExist())
                .andExpect(jsonPath("$.positions").doesNotExist())
                .andDo(print());
    }

    private JsonObject ignoredFieldsDepartmentObject() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        return factory.createObjectBuilder()
                .add("id", 999999)
                .add("name", "dept")
                .add("parentID", 99)
                .add("hasChild", true)
                .add("hasPositions", true)
                .add("childDepartments", factory.createArrayBuilder()
                        .add(factory.createObjectBuilder()
                                .add("name", "fake")))
                .add("parentDepartment", factory.createObjectBuilder()
                        .add("name", "fake"))
                .build();
    }

    @Test
    public void putContactFilledRootDepartmentTest() throws Exception {
        mockMvc.perform(put("/restapi/department")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contactFieldsDepartmentObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.contact.addresses[0].name").value("namedata"))
                .andExpect(jsonPath("$.contact.addresses[0].street").value("streetdata"))
                .andExpect(jsonPath("$.contact.addresses[0].common").value(true))
                .andExpect(jsonPath("$.contact.addresses[0].country").doesNotExist())
                .andExpect(jsonPath("$.contact.emails[0].name").value("namedata"))
                .andExpect(jsonPath("$.contact.emails[0].email").value("q@q"))
                .andExpect(jsonPath("$.contact.emails[0].common").value(true))
                .andExpect(jsonPath("$.contact.phones[0].name").value("namedata"))
                .andExpect(jsonPath("$.contact.phones[0].number").value("0000"))
                .andExpect(jsonPath("$.contact.phones[0].common").value(true))
                .andExpect(jsonPath("$.contact.others[0].name").value("namedata"))
                .andExpect(jsonPath("$.contact.others[0].value").value("value"))
                .andExpect(jsonPath("$.contact.others[0].common").value(true))
                .andExpect(jsonPath("$.hasChild").value(false))
                .andExpect(jsonPath("$.hasPositions").value(false))
                .andDo(print());
    }

    private JsonObject contactFieldsDepartmentObject() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        return factory.createObjectBuilder()
                .add("name", "dept")
                .add("contact", factory.createObjectBuilder()
                        .add("addresses", factory.createArrayBuilder()
                                .add(factory.createObjectBuilder()
                                        .add("name", "namedata")
                                        .add("city", "citydata")
                                        .add("street", "streetdata")
                                        .add("common", true)))
                        .add("emails", factory.createArrayBuilder()
                                .add(factory.createObjectBuilder()
                                        .add("name", "namedata")
                                        .add("email", "q@q")
                                        .add("common", true)))
                        .add("phones", factory.createArrayBuilder()
                                .add(factory.createObjectBuilder()
                                        .add("name", "namedata")
                                        .add("number", "0000")
                                        .add("common", true)))
                        .add("others", factory.createArrayBuilder()
                                .add(factory.createObjectBuilder()
                                        .add("name", "namedata")
                                        .add("value", "value")
                                        .add("common", true))))
                .build();
    }

    @Test
    public void putIgnoreContactRewriteRootDepartmentTest() throws Exception {
        Department root = Department.builder()
                .name("root")
                .contact(Contact.builder()
                        .phone(Phone.getNew("number", "1111", true))
                        .build())
                .build();
        departmentService.saveRootDepartment(root);

        mockMvc.perform(put("/restapi/department")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(minimalFilledDepartmentTestObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("testDept"))
                .andExpect(jsonPath("$.contact").exists())
                .andExpect(jsonPath("$.contact.phones[0].number").value("1111"))
                .andDo(print());
    }

    @Test
    public void putRewriteContactIgnoreRootDepartmentTest() throws Exception {
        Department root = Department.builder()
                .name("root")
                .contact(Contact.builder()
                        .phone(Phone.getNew("number", "1111", true))
                        .build())
                .build();
        departmentService.saveRootDepartment(root);

        mockMvc.perform(put("/restapi/department")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(rewriteContactIgnoreDepartmentObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("root"))
                .andExpect(jsonPath("$.contact.emails[0].name").value("namedata"))
                .andExpect(jsonPath("$.contact.emails[0].email").value("q@q"))
                .andExpect(jsonPath("$.contact.emails[0].common").value(true))
                .andExpect(jsonPath("$.contact.phones[0].name").value("namedata"))
                .andExpect(jsonPath("$.contact.phones[0].number").value("0000"))
                .andExpect(jsonPath("$.contact.phones[0].common").value(true))
                .andExpect(jsonPath("$.hasChild").value(false))
                .andExpect(jsonPath("$.hasPositions").value(false))
                .andDo(print());
    }

    private JsonObject rewriteContactIgnoreDepartmentObject() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        return factory.createObjectBuilder()
                .add("contact", factory.createObjectBuilder()
                        .add("emails", factory.createArrayBuilder()
                                .add(factory.createObjectBuilder()
                                        .add("name", "namedata")
                                        .add("email", "q@q")
                                        .add("common", true)))
                        .add("phones", factory.createArrayBuilder()
                                .add(factory.createObjectBuilder()
                                        .add("name", "namedata")
                                        .add("number", "0000")
                                        .add("common", true))))
                .build();
    }

    // DepartmentID

    @Test
    public void getIncorrectDepartmentIDTest() throws Exception {
        mockMvc.perform(get("/restapi/department/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Department not found"))
                .andExpect(jsonPath("$.path").value("/restapi/department/999"))
                .andDo(print());
    }

    @Test
    public void putEmptyDepartmentTest() throws Exception {
        Department root = Department.builder().name("root").build();
        Department child = Department.builder().name("child").build();
        departmentService.saveRootDepartment(root);
        departmentService.saveChildDepartment(root.getId(), child);

        mockMvc.perform(put("/restapi/department/" + child.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(emptyDepartmentObject().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid Department parameters"))
                .andExpect(jsonPath("$.path").value("/restapi/department/" + child.getId()))
                .andDo(print());
    }

    @Test
    public void getDepartmentTest() throws Exception {
        Department root = Department.builder().name("root").build();
        Department child = Department.builder().name("child").build();
        departmentService.saveRootDepartment(root);
        departmentService.saveChildDepartment(root.getId(), child);

        mockMvc.perform(get("/restapi/department/" + child.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value((int) child.getId()))
                .andExpect(jsonPath("$.name").value("child"))
                .andExpect(jsonPath("$.hasChild").value(false))
                .andExpect(jsonPath("$.hasPositions").value(false))
                .andExpect(jsonPath("$.contact").doesNotExist())
                .andExpect(jsonPath("$.parentId").value((int) root.getId()))
                .andExpect(jsonPath("$.parentDepartment").doesNotExist())
                .andExpect(jsonPath("$.childDepartments").doesNotExist())
                .andExpect(jsonPath("$.positions").doesNotExist())
                .andDo(print());
    }

    @Test
    public void putMinimalFilledDepartmentTest() throws Exception {
        Department root = Department.builder().name("root").build();
        Department child = Department.builder().name("child").build();
        departmentService.saveRootDepartment(root);
        departmentService.saveChildDepartment(root.getId(), child);

        mockMvc.perform(put("/restapi/department/" + child.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(minimalFilledDepartmentTestObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department/" + child.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value((int) child.getId()))
                .andExpect(jsonPath("$.name").value("testDept"))
                .andExpect(jsonPath("$.parentId").value((int) root.getId()))
                .andDo(print());
    }

    @Test
    public void putIgnoredFieldsDepartmentTest() throws Exception {
        Department root = Department.builder().name("root").build();
        Department child = Department.builder().name("child").build();
        departmentService.saveRootDepartment(root);
        departmentService.saveChildDepartment(root.getId(), child);

        mockMvc.perform(put("/restapi/department/" + child.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ignoredFieldsDepartmentObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department/" + child.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value((int) child.getId()))
                .andExpect(jsonPath("$.name").value("dept"))
                .andExpect(jsonPath("$.hasChild").value(false))
                .andExpect(jsonPath("$.hasPositions").value(false))
                .andExpect(jsonPath("$.contact").doesNotExist())
                .andExpect(jsonPath("$.parentId").value((int) root.getId()))
                .andExpect(jsonPath("$.parentDepartment").doesNotExist())
                .andExpect(jsonPath("$.childDepartments").doesNotExist())
                .andExpect(jsonPath("$.positions").doesNotExist())
                .andDo(print());
    }

    @Test
    @Ignore("Not implemented")
    public void putIgnoreContactRewriteDepartmentTest() throws Exception {
        Department root = Department.builder()
                .name("root")
                .contact(Contact.builder()
                        .phone(Phone.getNew("number", "1111", true))
                        .build())
                .build();
        departmentService.saveRootDepartment(root);

        mockMvc.perform(put("/restapi/department")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(minimalFilledDepartmentTestObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("testDept"))
                .andExpect(jsonPath("$.contact").exists())
                .andExpect(jsonPath("$.contact.phones[0].number").value("1111"))
                .andDo(print());
    }

    @Test
    @Ignore("Not implemented")
    public void putRewriteContactIgnoreDepartmentTest() throws Exception {
        Department root = Department.builder()
                .name("root")
                .contact(Contact.builder()
                        .phone(Phone.getNew("number", "1111", true))
                        .build())
                .build();
        departmentService.saveRootDepartment(root);

        mockMvc.perform(put("/restapi/department")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(rewriteContactIgnoreDepartmentObject().toString()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/restapi/department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("root"))
                .andExpect(jsonPath("$.contact.emails[0].name").value("namedata"))
                .andExpect(jsonPath("$.contact.emails[0].email").value("q@q"))
                .andExpect(jsonPath("$.contact.emails[0].common").value(true))
                .andExpect(jsonPath("$.contact.phones[0].name").value("namedata"))
                .andExpect(jsonPath("$.contact.phones[0].number").value("0000"))
                .andExpect(jsonPath("$.contact.phones[0].common").value(true))
                .andExpect(jsonPath("$.hasChild").value(false))
                .andExpect(jsonPath("$.hasPositions").value(false))
                .andDo(print());
    }

    @Test
    @Ignore("Not implemented")
    public void putDuplicateNameDepartmentTest() throws Exception {

    }
}