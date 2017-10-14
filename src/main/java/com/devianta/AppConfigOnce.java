package com.devianta;

import com.devianta.model.Department;
import com.devianta.model.contact.*;
import com.devianta.servise.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource(value = "classpath:directory.properties", encoding = "UTF-8")
public class AppConfigOnce {
    @Autowired
    DepartmentService departmentService;

    @Value("${directory.department.root.name}")
    private String rootDeptName;

    @Value("${directory.department.root.child}")
    private String[] childDeptNames;

    @Bean
    public CommandLineRunner demo(final DepartmentService departmentService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                if (departmentService.findRoot() == null) {
                    createDefaultHierarchy();
                }
            }
        };
    }

    private void createDefaultHierarchy() {
        Department root = Department.builder().name(rootDeptName).build();
        departmentService.saveRootDepartment(root);

        List<Department> childDepartments = new ArrayList<>();
        for (String childDeptName : childDeptNames) {
            childDepartments.add(Department.builder().name(childDeptName).build());
        }
        for (Department d : childDepartments) {
            departmentService.saveChildDepartment(root.getId(), d);
        }

        setTemporaryStructure(departmentService);
    }

    private void setTemporaryStructure(DepartmentService departmentService) {
        Department root = departmentService.findRoot();
        Department vd = Department.builder()
                .name("  Виконавча дирекція   ")
                .contact(DepartmentContact.builder()
                        .address(Address.builder()
                                .name(" основна  ")
                                .zipCode("  00000  ")
                                .city("  м. Київ  ")
                                .district("  Подільський район  ")
                                .street("  вул. Боричів тік  ")
                                .house("  28  ")
                                .common(true)
                                .build())
                        .address(Address.builder()
                                .name("додаткова")
                                .zipCode("00000")
                                .city("м. Київ")
                                .district("Подільський район")
                                .street("вул. Боричів тік")
                                .house("30")
                                .common(true)
                                .build())
                        .email(Email.getNew("  E-mail  ", " fssu@fssu.gov.ua ", true))
                        .phone(Phone.getNew(" гаряча лінія ", " 0800501892 ", true))
                        .phone(Phone.getNew(" тел./факс ", "  0442060401 ", true))
                        .other(OtherInfo.getNew(" www ", "  http://www.fssu.gov.ua  ", true))
                        .build())
                .build();
        departmentService.saveChildDepartment(root.getId(), vd);

        Department dept1 = Department.builder()
                .name("Департамент фінансово-економічної діяльності")
                .contact(DepartmentContact.builder()
                        .email(Email.getNew("E-mail", "finance@fssu.gov.ua", true))
                        .build())
                .build();
        departmentService.saveChildDepartment(vd.getId(), dept1);

        Department dept11 = Department.builder()
                .name("Відділ бюджетного планування")
                .contact(DepartmentContact.builder()
                        .email(Email.getNew("E-mail", "budget@fssu.gov.ua", true))
                        .build())
                .build();
        departmentService.saveChildDepartment(dept1.getId(), dept11);

        Department dept12 = Department.builder()
                .name("Відділ фінансування видатків")
                .contact(DepartmentContact.builder()
                        .email(Email.getNew("E-mail", "fv@fssu.gov.ua", true))
                        .build())
                .build();
        departmentService.saveChildDepartment(dept1.getId(), dept12);

        Department dept13 = Department.builder()
                .name("Відділ фінансової звітності, економічного аналізу та методологічної роботи")
                .contact(DepartmentContact.builder()
                        .email(Email.getNew("E-mail", "zvit@fssu.gov.ua", true))
                        .build())
                .build();
        departmentService.saveChildDepartment(dept1.getId(), dept13);


        Department dept2 = Department.builder()
                .name("Департамент бухгалтерського обліку та консолідованої звітності")
                .contact(DepartmentContact.builder()
                        .email(Email.getNew("E-mail", "finance@fssu.gov.ua", true))
                        .build())
                .build();
        departmentService.saveChildDepartment(vd.getId(), dept2);

        Department dept21 = Department.builder()
                .name("Відділ бухгалтерського обліку апарату виконавчої дирекції Фонду")
                .contact(DepartmentContact.builder()
                        .email(Email.getNew("E-mail", "buh@fssu.gov.ua", true))
                        .build())
                .build();
        departmentService.saveChildDepartment(dept2.getId(), dept21);

        Department dept22 = Department.builder()
                .name("Відділ консолідованої звітності Фонду")
                .contact(DepartmentContact.builder()
                        .email(Email.getNew("E-mail", "kzv@fssu.gov.ua", true))
                        .build())
                .build();
        departmentService.saveChildDepartment(dept2.getId(), dept22);

        Department dept23 = Department.builder()
                .name("Відділ розрахунків з оплати праці")
                .contact(DepartmentContact.builder()
                        .email(Email.getNew("E-mail", "zp@fssu.gov.ua", true))
                        .build())
                .build();
        departmentService.saveChildDepartment(dept2.getId(), dept23);
    }
}
