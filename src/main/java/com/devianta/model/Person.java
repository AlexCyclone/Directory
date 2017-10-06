package com.devianta.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    @Column(nullable = false, length = 100)
    @JsonView(View.COMMON_REST.class)
    private String surname;

    @NonNull
    @Column(nullable = false, length = 100)
    @JsonView(View.COMMON_REST.class)
    private String name;

    @NonNull
    @Column(nullable = false, length = 100)
    @JsonView(View.COMMON_REST.class)
    private String patronymic;

    @NonNull
    @OneToOne(fetch = LAZY)
    @JoinColumn(nullable = false)
    private Position position;
}
