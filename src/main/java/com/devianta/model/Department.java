package com.devianta.model;

import com.devianta.model.contact.DepartmentContact;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(View.COMMON_REST.class)
    private long id;

    @NonNull
    @Column(nullable = false, length = 500)
    @JsonView(View.COMMON_REST.class)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Department parentDepartment;

    @OneToMany(mappedBy = "parentDepartment", fetch = FetchType.LAZY, cascade = ALL)
    private List<Department> childDepartment = new ArrayList<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = ALL)
    private List<Position> positions = new ArrayList<>();

    @OneToOne(mappedBy = "department", fetch = FetchType.LAZY, cascade = ALL)
    @JsonView(View.COMMON_REST.class)
    private DepartmentContact contact;

    @JsonView(View.COMMON_REST.class)
    public boolean hasChild() {
        if (childDepartment.size() > 0) {
            return true;
        }
        return false;
    }

    @JsonView(View.COMMON_REST.class)
    public boolean hasPositions() {
        if (positions.size() > 0) {
            return true;
        }
        return false;
    }

}
