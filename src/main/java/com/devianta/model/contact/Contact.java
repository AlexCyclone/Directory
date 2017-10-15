package com.devianta.model.contact;

import com.devianta.model.Department;
import com.devianta.model.Person;
import com.devianta.model.View;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;

@Entity
@Table
@Builder
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(View.COMMON_REST.class)
    private long id;

    @OneToOne(fetch = LAZY)
    private Department department;

    @OneToOne(fetch = LAZY)
    private Person person;

    @Singular
    @OneToMany(mappedBy = "contact", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JsonView(View.COMMON_REST.class)
    private List<Address> addresses;

    @Singular
    @OneToMany(mappedBy = "contact", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JsonView(View.COMMON_REST.class)
    private List<Email> emails;

    @Singular
    @OneToMany(mappedBy = "contact", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JsonView(View.COMMON_REST.class)
    private List<Phone> phones;

    @Singular
    @OneToMany(mappedBy = "contact", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JsonView(View.COMMON_REST.class)
    private List<OtherInfo> others;

    @Tolerate
    public Contact() {
        addresses = new ArrayList<>();
        emails = new ArrayList<>();
        phones = new ArrayList<>();
        others = new ArrayList<>();
    }

    public Contact normalise() throws IllegalArgumentException {
        normaliseAddresses();
        normaliseEmails();
        normalisePhones();
        normaliseOthers();
        if (department == null && person == null) {
            throw new IllegalArgumentException("Invalid Contact parameters");
        }
        return this;
    }

    private void normaliseAddresses() {
        if (addresses != null) {
            for (Address a: addresses) {
                a.setContact(this);
                a.normalise();
            }
        } else {
            addresses = new ArrayList<>();
        }
    }

    private void normaliseEmails() {
        if (emails != null) {
            for (Email e: emails) {
                e.setContact(this);
                e.normalise();
            }
        } else {
            emails = new ArrayList<>();
        }
    }

    private void normalisePhones() {
        if (phones != null) {
            for (Phone p: phones) {
                p.setContact(this);
                p.normalise();
            }
        } else {
            phones = new ArrayList<>();
        }
    }

    private void normaliseOthers() {
        if (others != null) {
            for (OtherInfo o: others) {
                o.setContact(this);
                o.normalise();
            }
        } else {
            others = new ArrayList<>();
        }
    }
}
