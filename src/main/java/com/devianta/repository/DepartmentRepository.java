package com.devianta.repository;

import com.devianta.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // used
    @Query("SELECT d FROM Department d WHERE d.id = :id")
    Department findById(@Param("id") long id);

    // used
    @Query("SELECT d FROM Department d WHERE d.parentDepartment = null")
    List<Department> findRoot();
}
