package com.devianta.repository;

import com.devianta.model.contact.DepartmentContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentContactRepository extends JpaRepository<DepartmentContact, Long> {

    @Query("SELECT c FROM DepartmentContact c WHERE c.id = :id")
    DepartmentContact findById(@Param("id") long id);

    @Query("SELECT c FROM DepartmentContact c WHERE c.department = id")
    DepartmentContact findByDepartmentId(@Param("id") long id);
}
