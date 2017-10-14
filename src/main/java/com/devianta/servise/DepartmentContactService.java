package com.devianta.servise;

import com.devianta.model.Department;
import com.devianta.model.contact.DepartmentContact;
import com.devianta.repository.DepartmentContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentContactService {
    @Autowired
    DepartmentContactRepository contactRepository;

    public DepartmentContact findByDepartment(Department department) {
        DepartmentContact c = contactRepository.findByDepartment(department);
        return c;
    }

    public void deleteContact(DepartmentContact contact) {
        contactRepository.delete(contact);
    }
}
