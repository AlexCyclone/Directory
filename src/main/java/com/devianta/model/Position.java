package com.devianta.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    @Column(nullable = false)
    @JsonView(View.COMMON_REST.class)
    private String namePosition;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Department department;

    @OneToOne(mappedBy = "position", fetch = FetchType.LAZY, cascade = ALL)
    @JsonView(View.COMMON_REST.class)
    private Person person;

}
