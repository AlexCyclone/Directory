package com.devianta.model.contact;

import com.devianta.model.Service;
import com.devianta.model.View;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "getNew")
@Getter
@Setter
@EqualsAndHashCode(doNotUseGetters = true, exclude = {"id", "contact", "name", "common"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    private Contact contact;

    @NonNull
    @Column(nullable = false, length = 50)
    @JsonView(View.COMMON_REST.class)
    private String name;

    @NonNull
    @Column(nullable = false, length = 15)
    @JsonView(View.COMMON_REST.class)
    private String number;

    @NonNull
    @Column(nullable = false)
    @JsonView(View.COMMON_REST.class)
    private boolean common;

    public void normalise() throws IllegalArgumentException {
        name = Service.safeTrimEmptyToNull(name);
        number = Service.safeTrimEmptyToNull(number);

        if (Service.nullOrLimit(1, 50, name)
                || Service.nullOrLimit(1, 15, number)
                || !Service.patternMatch(number, "\\+?\\d+")) {
            throw new IllegalArgumentException("Invalid Phone parameters");
        }
    }
}
