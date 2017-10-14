package com.devianta.servise;

import com.devianta.model.Department;
import com.devianta.model.Position;
import com.devianta.model.contact.DepartmentContact;
import com.devianta.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentContactService contactService;

    @Transactional(readOnly = true)
    public Department findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Transactional
    public void saveDepartment(Department department) {
        if (department.getId() > 0) {
            clearContact(department);
        }

        // Has root
        Department parent = department.getParentDepartment();
        if (parent == null) {
            departmentRepository.save(department);
            return;
        }

        // Find duplicate
        List<Department> child = parent.getChildDepartments();
        int eqPosition = child.indexOf(department);
        if (eqPosition != -1) {
            if (eqPosition != child.lastIndexOf(department) || child.get(eqPosition).getId() != department.getId()) {
                throw new IllegalArgumentException("Invalid department name");
            }
        }
        departmentRepository.save(department);
    }

    @Transactional
    public void clearContact(Department department) {
        DepartmentContact contact = contactService.findByDepartment(department);
        if (contact != null) {
            contactService.deleteContact(contact);
        }
    }

    // Root

    @Transactional(readOnly = true)
    public Department findRoot() {
        List<Department> dept = departmentRepository.findRoot();

        // Return null if root not found
        if (dept.size() == 0) {
            return null;
        }
        return dept.get(0);
    }

    @Transactional
    public void saveRootDepartment(Department department) {
        department.setId(0);
        department.setParentDepartment(null);

        // Write protect
        department.setChildDepartments(new ArrayList<>());
        department.setPositions(new ArrayList<>());

        // If root found rewrite data
        Department root = findRoot();
        if (root != null) {
            department.setId(root.getId());
        }

        saveDepartment(department.normalise());
    }

    // Department

    @Transactional
    public void modifyDepartment(Long id, Department department) {
        // Check and set id
        Department departmentFromBase = findById(id);
        if (departmentFromBase == null) {
            throw new IllegalArgumentException("Invalid department id");
        }

        // Write protect
        department.setChildDepartments(new ArrayList<>());
        department.setPositions(new ArrayList<>());

        // Set parent
        department.setParentDepartment(departmentFromBase.getParentDepartment());

        // Set id, normalise contact
        department.setId(departmentFromBase.getId());

        saveDepartment(department.normalise());
    }

    // Child

    @Transactional(readOnly = true)
    public List<Department> findChildDepartment(Long parentId) {
        Department dept = findById(parentId);
        return dept.getChildDepartments();
    }

    @Transactional
    public void saveChildDepartment(Long parentId, Department department) {
        Department parent = findById(parentId);
        // If parent not found throw Exception
        if (parent == null) {
            throw new IllegalArgumentException("Invalid department id");
        }

        // Reset id, set parent, normalise contact
        department.normaliseContact().setId(0);
        department.setParentDepartment(parent);
        saveDepartment(department.normalise());
    }

    // Positions

    @Transactional(readOnly = true)
    public List<Position> findPositionInDepartment(Long id) {
        Department dept = findById(id);
        if (dept == null) {
            return new ArrayList<>();
        }
        return dept.getPositions();
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
