package com.devianta.repository;

import com.devianta.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PositionRepository extends JpaRepository<Position, Long> {

    //@Query("SELECT p FROM Position p WHERE p.id = :id")
    Position findById(@Param("id") long id);

}
