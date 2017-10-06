package com.devianta.repository;

import com.devianta.model.contact.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address, Long> {

    //@Query("SELECT a FROM Address a WHERE a.id = :id")
    Address findById(@Param("id") long id);

}