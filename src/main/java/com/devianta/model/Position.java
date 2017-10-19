package com.devianta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.Tolerate;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@Entity
@Table
@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(doNotUseGetters = true, exclude = {"id", "department", "person"})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(View.COMMON_REST.class)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Department department;

    @NonNull
    @Column(nullable = false, length = 500)
    @JsonView(View.COMMON_REST.class)
    private String namePosition;

    @OneToOne(mappedBy = "position", fetch = FetchType.EAGER, cascade = {MERGE, DETACH, PERSIST, REFRESH})
    @JsonView(View.COMMON_REST.class)
    private Person person;

    @JsonView(View.COMMON_REST.class)
    public Long departmentId() {
        return department.getId();
    }

    @Tolerate
    public Position() {
    }

    public Position normalise() {
        namePosition = Service.safeTrimEmptyToNull(namePosition);
        if (person != null) {
            person.setPosition(this);
            person.normalise();
        }

        if (department == null
                || Service.nullOrLimit(1, 500, namePosition)) {
            throw new IllegalArgumentException("Invalid Position parameters");
        }
        return this;
    }
}
