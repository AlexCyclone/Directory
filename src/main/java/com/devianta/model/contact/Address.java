package com.devianta.model.contact;

import com.devianta.model.Service;
import com.devianta.model.View;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    private DepartmentContact contact;

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
    private Boolean common;

    @Tolerate
    public Address() {
    }

    public boolean isValid() {
        if (Service.containNull(common)
                || Service.containEmptyOrLimit(50, name)) {
            return false;
        }

        if (Service.containAlongEmptyOrLimit(50
                , zipCode, country, region, district, city, street, house, office)) {
            return false;
        }

        if (name.equals("") || name.length() > 50) {
            return false;
        }
        if (zipCode != null && (zipCode.equals("") || zipCode.length() > 50)) {
            return false;
        }
        return true;
    }


    public void normalise() throws IllegalArgumentException {
        name = Service.safeTrim(name);
        zipCode = Service.safeTrim(zipCode);
        country = Service.safeTrim(country);
        region = Service.safeTrim(region);
        district = Service.safeTrim(district);
        city = Service.safeTrim(city);
        street = Service.safeTrim(street);
        house = Service.safeTrim(house);
        office = Service.safeTrim(office);
        common = Service.defaultTrue(common);

        if (!isValid()) {
            throw new IllegalArgumentException("Invalid address parameters");
        }
    }

}
