package com.devianta.model.contact;

import com.devianta.model.Service;
import com.devianta.model.View;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.Tolerate;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table
@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(doNotUseGetters = true, exclude = {"id", "contact", "name", "common"})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    private Contact contact;

    @Column(nullable = false, length = 50)
    @JsonView(View.COMMON_REST.class)
    private String name;

    @Column(length = 10)
    @JsonView(View.COMMON_REST.class)
    private String zipCode;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String country;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String region;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String district;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String city;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String street;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String house;

    @Column(length = 50)
    @JsonView(View.COMMON_REST.class)
    private String office;

    @Column(nullable = false)
    @JsonView(View.COMMON_REST.class)
    private boolean common;

    @Tolerate
    public Address() {
    }

    public void normalise() throws IllegalArgumentException {
        name = Service.safeTrimEmptyToNull(name);
        zipCode = Service.safeTrimEmptyToNull(zipCode);
        country = Service.safeTrimEmptyToNull(country);
        region = Service.safeTrimEmptyToNull(region);
        district = Service.safeTrimEmptyToNull(district);
        city = Service.safeTrimEmptyToNull(city);
        street = Service.safeTrimEmptyToNull(street);
        house = Service.safeTrimEmptyToNull(house);
        office = Service.safeTrimEmptyToNull(office);

        if (Service.nullOrLimit(1, 50, name)
                || Service.allClearOrLimit(1, 50
                , zipCode, country, region, district, city, street, house, office)) {
            throw new IllegalArgumentException("Invalid Address parameters");
        }
    }

}
