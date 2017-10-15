package com.devianta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(doNotUseGetters = true, exclude = {"id", "position"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(View.COMMON_REST.class)
    private long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(nullable = false)
    private Position position;

    @Column(length = 100)
    @JsonView(View.COMMON_REST.class)
    private String surname;

    @Column(length = 100)
    @JsonView(View.COMMON_REST.class)
    private String name;

    @Column(length = 100)
    @JsonView(View.COMMON_REST.class)
    private String patronymic;

    public static Person getNew(String surname, String name, String patronymic) {
        Person person = new Person();
        person.setSurname(surname);
        person.setName(name);
        person.setPatronymic(patronymic);
        return person;
    }

    public Person normalise() {
        surname = Service.safeTrimEmptyToNull(surname);
        name = Service.safeTrimEmptyToNull(name);
        patronymic = Service.safeTrimEmptyToNull(patronymic);

        if (position == null ||
                Service.allClearOrLimit(1,100, surname, name, patronymic)) {
            throw new IllegalArgumentException("Invalid Person parameters");
        }
        return this;
    }
}
