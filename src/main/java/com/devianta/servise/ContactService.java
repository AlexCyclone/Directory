package com.devianta.servise;

import com.devianta.model.Department;
import com.devianta.model.contact.Contact;
import com.devianta.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;

    @Transactional(readOnly = true)
    public Contact findByDepartment(Department department) {
        if (department == null || department.getId() == 0) {
            return null;
        }
        Contact c = contactRepository.findByDepartment(department);
        return c;
    }

    @Transactional
    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    @Transactional
    public void deleteContact(Department department) {
        Contact contact = findByDepartment(department);
        if (contact != null) {
            contactRepository.delete(contact);
        }
    }
}
