package com.devianta.model;

import com.devianta.model.contact.Contact;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private Contact contact;

    @Tolerate
    public Department() {
    }

    @JsonView(View.COMMON_REST.class)
    public Long parentId() {
        return parentDepartment != null ? parentDepartment.getId() : null;
    }

    @JsonView(View.COMMON_REST.class)
    public boolean hasChild() {
        return childDepartments != null && childDepartments.size() > 0;
    }

    @JsonView(View.COMMON_REST.class)
    public boolean hasPositions() {
        return positions != null && positions.size() > 0;
    }

    public Department normalise() throws IllegalArgumentException {
        name = Service.safeTrimEmptyToNull(name);

        if (contact != null) {
            contact.setDepartment(this);
            contact.normalise();
        }

        if (Service.nullOrLimit(1, 500, name)) {
            throw new IllegalArgumentException("Invalid Department parameters");
        }
        return this;
    }

    public void updateNullField(Department department) {
        if (name == null) {
            name = department.getName();
        }
    }

    public void resetProtectedFields() {
        id = 0;
        childDepartments = null;
        positions = null;
        parentDepartment = null;
    }
}
