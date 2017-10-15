package com.devianta.servise;

import com.devianta.model.Department;
import com.devianta.model.contact.Contact;
import com.devianta.repository.DepartmentContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentContactService {
    @Autowired
    DepartmentContactRepository contactRepository;

    public Contact findByDepartment(Department department) {
        Contact c = contactRepository.findByDepartment(department);
        return c;
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }
}
