package com.devianta.repository;

import com.devianta.model.Department;
import com.devianta.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("SELECT p FROM Position p WHERE p.id = :id")
    Position findById(@Param("id") long id);

    @Query("SELECT p FROM Position p WHERE p.department = :department")
    List<Position> findByDepartment(@Param("department") Department department);
}
