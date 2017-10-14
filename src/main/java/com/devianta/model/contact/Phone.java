package com.devianta.model.contact;

import com.devianta.model.Service;
import com.devianta.model.View;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private DepartmentContact contact;

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
    private Boolean common;

    public boolean isValid() {
        if (Service.containNull(common, number)
                || Service.containEmptyOrLimit(50, name)) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\+*\\d+");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches() || number.length() > 15) {
            return false;
        }
        return true;
    }

    public void normalise() throws IllegalArgumentException {
        name = Service.safeTrim(name);
        number = Service.safeTrim(number);
        common = Service.defaultTrue(common);

        if (!isValid()) {
            throw new IllegalArgumentException("Invalid phone parameters");
        }
    }
}
