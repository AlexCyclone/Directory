package com.devianta.model.contact;

import com.devianta.model.Department;
import com.devianta.model.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;

@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepartmentContact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    @OneToOne(fetch = LAZY)
    @JoinColumn(nullable = false)
    private Department department;

    @OneToMany(mappedBy = "contact", fetch = LAZY, cascade = ALL)
    @JsonView(View.COMMON_REST.class)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "contact", fetch = LAZY, cascade = ALL)
    @JsonView(View.COMMON_REST.class)
    private List<Email> emails = new ArrayList<>();

    @OneToMany(mappedBy = "contact", fetch = LAZY, cascade = ALL)
    @JsonView(View.COMMON_REST.class)
    private List<Phone> phones = new ArrayList<>();

    @OneToMany(mappedBy = "contact", fetch = LAZY, cascade = ALL)
    @JsonView(View.COMMON_REST.class)
    private List<Site> sites = new ArrayList<>();

    @OneToMany(mappedBy = "contact", fetch = LAZY, cascade = ALL)
    @JsonView(View.COMMON_REST.class)
    private List<OtherInfo> otherInfos = new ArrayList<>();

}
