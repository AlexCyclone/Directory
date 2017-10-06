package com.devianta.controller;

import com.devianta.model.Department;
import com.devianta.model.Position;
import com.devianta.model.View;
import com.devianta.servise.DepartmentService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restapi/department")
public class CommonRest {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @JsonView(View.COMMON_REST.class)
    public Department getDepartment() {
        return departmentService.findRoot();
    }

    @RequestMapping(value = "/{id}/child", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(View.COMMON_REST.class)
    public List<Department> getDepartment(@PathVariable Long id) {
        return departmentService.findChild(id);
    }

    @RequestMapping(value = "/{id}/positions", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(View.COMMON_REST.class)
    public List<Position> getPosition(@PathVariable Long id) {
        return departmentService.findPositionInDepartment(id);
    }

}
