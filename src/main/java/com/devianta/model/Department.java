package com.devianta.model;

import com.devianta.model.contact.DepartmentContact;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table
@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(doNotUseGetters = true, exclude = {"id", "parentDepartment", "childDepartments", "positions", "contact"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(View.COMMON_REST.class)
    private long id;

    @Column(nullable = false, length = 500)
    @JsonView(View.COMMON_REST.class)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Department parentDepartment;

    @Singular
    @OneToMany(mappedBy = "parentDepartment", fetch = FetchType.LAZY)
    private List<Department> childDepartments;

    @Singular
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Position> positions;

    @OneToOne(mappedBy = "department", fetch = FetchType.EAGER, cascade = ALL)
    @Fetch(FetchMode.JOIN)
    @JsonView(View.COMMON_REST.class)
    private DepartmentContact contact;

    @Tolerate
    public Department() {
        childDepartments = new ArrayList<>();
        positions = new ArrayList<>();
    }

    @JsonView(View.COMMON_REST.class)
    public Long parentId() {
        return parentDepartment != null ? parentDepartment.getId() : null;
    }

    @JsonView(View.COMMON_REST.class)
    public boolean hasChild() {
        if (childDepartments == null || childDepartments.size() > 0) {
            return true;
        }
        return false;
    }

    @JsonView(View.COMMON_REST.class)
    public boolean hasPositions() {
        if (positions == null || positions.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean isValid() {
        if (Service.containEmptyOrLimit(500, name)) {
            return false;
        }
        return true;
    }

    public Department normalise() throws IllegalArgumentException {
        name = Service.safeTrim(name);
        normaliseContact();

        if (!isValid()) {
            throw new IllegalArgumentException("Invalid department name");
        }
        return this;
    }

    public Department normalisePositions() throws IllegalArgumentException {
        return this;
    }

    public Department normaliseContact() throws IllegalArgumentException {
        if (contact == null) {
            return this;
        }
        contact.setDepartment(this);
        contact.normalise();
        return this;
    }
}
