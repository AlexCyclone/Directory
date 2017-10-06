package com.devianta.model.contact;

import com.devianta.model.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Table
@AllArgsConstructor(staticName = "getNew")
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 100)
    @JsonView(View.COMMON_REST.class)
    private String name;

    @Column(length = 10)
    @JsonView(View.COMMON_REST.class)
    private String zipCode;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String country;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String region;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String district;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String city;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String street;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String house;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String office;

    @Column(nullable = false)
    private boolean common;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn
    private DepartmentContact contact;

}
