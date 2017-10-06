package com.devianta.model.contact;

import com.devianta.model.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table
@AllArgsConstructor(staticName = "getNew")
@NoArgsConstructor
@Getter
@Setter
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 100)
    @JsonView(View.COMMON_REST.class)
    private String name;

    @Column(nullable = false, length = 15)
    @JsonView(View.COMMON_REST.class)
    private String number;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn
    private DepartmentContact contact;

    @Column(nullable = false)
    private boolean common;
}
