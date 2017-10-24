package com.devianta.controller;

import com.devianta.model.Department;
import com.devianta.model.Position;
import com.devianta.model.View;
import com.devianta.servise.DepartmentService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/restapi/department")
@CrossOrigin(origins = "*")
public class CommonRestController {

    @Autowired
    private DepartmentService departmentService;

    // Root Department

    @RequestMapping(method = GET)
    @ResponseBody
    @JsonView(View.COMMON_REST.class)
    public Department getRootDepartment() {
        return departmentService.findRoot();
    }

    @RequestMapping(method = PUT)
    @JsonView(View.COMMON_REST.class)
    public void putRootDepartment(@RequestBody Department department) {
        departmentService.saveRootDepartment(department);
    }

    // Department

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    @JsonView(View.COMMON_REST.class)
    public Department getDepartment(@PathVariable Long id) {
        return departmentService.findById(id);
    }

    @RequestMapping(value = "/{id}", method = PUT)
    @JsonView(View.COMMON_REST.class)
    public void putDepartment(@PathVariable Long id, @RequestBody Department department) {
        departmentService.modifyDepartment(id, department);
    }

    // Child Departments

    @RequestMapping(value = "/{parentId}/child", method = GET)
    @ResponseBody
    @JsonView(View.COMMON_REST.class)
    public List<Department> getChildDepartments(@PathVariable Long parentId) {
        return departmentService.findChildDepartment(parentId);
    }

    @RequestMapping(value = "/{parentId}/child", method = POST)
    @JsonView(View.COMMON_REST.class)
    public void putChildDepartment(@PathVariable Long parentId, @RequestBody Department childDepartment) {
        departmentService.saveChildDepartment(parentId, childDepartment);
    }

    // Positions

    @RequestMapping(value = "/{departmentId}/positions", method = GET)
    @ResponseBody
    @JsonView(View.COMMON_REST.class)
    public List<Position> getDepartmentPositions(@PathVariable Long departmentId) {
        return departmentService.findPositions(departmentId);
    }

    @RequestMapping(value = "/{departmentId}/positions", method = POST)
    @ResponseBody
    @JsonView(View.COMMON_REST.class)
    public List<Position> putDepartmentPositions(@PathVariable Long departmentId, @RequestBody Position position) {
        departmentService.savePosition(departmentId, position);
        return departmentService.findPositions(departmentId);
    }
}
