package com.devianta.servise;

import com.devianta.model.Department;
import com.devianta.model.Position;
import com.devianta.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable).getContent();
    }

    // used
    @Transactional(readOnly = true)
    public Department findById(Long id) {
        return departmentRepository.findById(id);
    }

    // used
    @Transactional(readOnly = true)
    public Department findRoot() {
        List<Department> dept = departmentRepository.findRoot();
        if (dept.size() == 0) {
            return null;
        }
        return dept.get(0);
    }

    // used
    @Transactional(readOnly = true)
    public List<Department> findChild(Long id) {
        Department dept = findById(id);
        if (dept == null) {
            return new ArrayList<Department>();
        }
        return dept.getChildDepartment();
    }

    @Transactional(readOnly = true)
    public List<Position> findPositionInDepartment(Long id) {
        Department dept = findById(id);
        if (dept == null) {
            return new ArrayList<Position>();
        }
        return dept.getPositions();
    }

    @Transactional
    public void saveDepartment(Department department) {
        configureChild(department);
        departmentRepository.save(department);
    }

    private void configureChild(Department department) {
        if (department.getContact() != null) {
            department.getContact().setDepartment(department);
        }
        if (department.hasChild()) {
            for (Department d: department.getChildDepartment()) {
                d.setParentDepartment(department);
                configureChild(d);
            }
        }
    }

    @Transactional
    public void deleteDepartment(Long id) {
        departmentRepository.delete(id);
    }

    @Transactional
    public void deleteDepartment(Department department) {
        departmentRepository.delete(department.getId());
    }
}
