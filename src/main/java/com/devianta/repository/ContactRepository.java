package com.devianta.repository;

import com.devianta.model.Department;
import com.devianta.model.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c WHERE c.id = :id")
    Contact findById(@Param("id") long id);

    @Query("SELECT c FROM Contact c WHERE c.department = :department")
    Contact findByDepartment(@Param("department") Department department);

}
