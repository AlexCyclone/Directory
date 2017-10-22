package com.devianta.servise;

import com.devianta.exception.ObjectNotFoundException;
import com.devianta.model.Department;
import com.devianta.model.Person;
import com.devianta.model.Position;
import com.devianta.model.contact.Contact;
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
    private ContactService contactService;

    @Autowired
    private PositionService positionService;

    @Transactional(readOnly = true)
    public Department findById(Long id) {
        Department department = departmentRepository.findById(id);
        if (department == null) {
            throw new ObjectNotFoundException("Department not found");
        }
        return department;
    }

    @Transactional
    public void saveDepartment(Department department) {
        department.normalise();
        // Has parent
        Department parent = department.getParentDepartment();
        // Find duplicate
        if (parent != null) {
            List<Department> child = parent.getChildDepartments();
            int eqPosition = child.indexOf(department);
            if (eqPosition != -1) {
                if (eqPosition != child.lastIndexOf(department) || child.get(eqPosition).getId() != department.getId()) {
                    throw new IllegalArgumentException("Duplicate department found");
                }
            }
        }
        // Reset contact
        if (department.getContact() != null) {
            contactService.deleteContact(department);
            department.getContact().normalise().setId(0);
        }
        departmentRepository.save(department);
    }

    // Root

    @Transactional(readOnly = true)
    public Department findRoot() throws ObjectNotFoundException {
        List<Department> dept = departmentRepository.findRoot();

        // Return null if root not found
        if (dept.size() == 0) {
            throw new ObjectNotFoundException("Root department not found");
        }
        return dept.get(0);
    }

    @Transactional
    public void saveRootDepartment(Department department) {

        // Rewrite protect
        department.setId(0);
        department.setChildDepartments(null);
        department.setPositions(null);
        department.setParentDepartment(null);

        // If root found rewrite data
        try {
            Department root = findRoot();
            department.setId(root.getId());
            department.updateNullField(root);
        } catch (ObjectNotFoundException e) {
        }
        saveDepartment(department);
    }

    // Department

    @Transactional
    public void modifyDepartment(Long id, Department department) {
        // Check and set id
        Department departmentFromBase = findById(id);

        // Write protect
        department.setChildDepartments(null);
        department.setPositions(null);
        department.setParentDepartment(departmentFromBase.getParentDepartment());

        // Set id
        department.setId(departmentFromBase.getId());
        department.updateNullField(departmentFromBase);
        saveDepartment(department);
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
        department.setId(0);
        department.setParentDepartment(parent);
        saveDepartment(department.normalise());
    }

    // Positions

    @Transactional(readOnly = true)
    public List<Position> findPositions(Long departmentId) {
        Department dept = findById(departmentId);
        if (dept == null) {
            return new ArrayList<>();
        }
        return dept.getPositions();
    }

    @Transactional
    public void savePosition(Long departmentId, Position position) {
        Department dept = findById(departmentId);
        if (dept == null) {
            throw new IllegalArgumentException("Department id not found");
        }

        position.setId(0);
        position.setDepartment(dept);
        Person person = position.getPerson();
        if (person != null) {
            person.setId(0);
        }
        positionService.save(position);
    }

    // Clear database
    @Transactional
    public void clearDatabase() {
        Department root = null;
        try {
            root = findRoot();
        } catch (ObjectNotFoundException e) {
        }
        if (root == null) {
            return;
        }
        removeDependencies(root);
        departmentRepository.delete(root);
    }

    @Transactional
    protected void removeDependencies(Department department) {
        if (department == null) {
            return;
        }
        if (department.hasPositions()) {
            for (Position p : positionService.findByDepartment(department)) {
                positionService.delete(p);
            }
        }
        if (!department.hasChild()) {
            return;
        }
        for (Department d : department.getChildDepartments()) {
            removeDependencies(d);
            departmentRepository.delete(d);
        }
    }

}
