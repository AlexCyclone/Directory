package com.devianta.model.contact;

import com.devianta.model.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table
@AllArgsConstructor(staticName = "getNew")
@NoArgsConstructor
@Getter
@Setter
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 100)
    @JsonView(View.COMMON_REST.class)
    private String name;

    @Column(nullable = false, length = 100)
    @JsonView(View.COMMON_REST.class)
    private String email;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn
    private DepartmentContact contact;

    @Column(nullable = false)
    private boolean common;

}
