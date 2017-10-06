package com.devianta;

import com.devianta.model.Department;
import com.devianta.model.Person;
import com.devianta.model.Position;
import com.devianta.model.contact.*;
import com.devianta.servise.DepartmentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.List;

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/WEB-INF/pages/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        resolver.setOrder(1);
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("/WEB-INF/static/");
    }

    @Bean
    public CommandLineRunner demo(final DepartmentService departmentService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                departmentService.saveDepartment(getHierarchy());
            }
        };
    }

    private Department getHierarchy() {
        Department root = new Department("Фонд соціального страхування України");
        List<Department> rootChild = root.getChildDepartment();

        Department dept1 = new Department("Виконавча дирекція");
        rootChild.add(dept1);
        List<Department> dept1Child = dept1.getChildDepartment();
        setVDContact(dept1);
        List<Position> positions = dept1.getPositions();

        Position position = new Position("Директор", dept1);
        position.setPerson(new Person("Іванов","Іван","Іванович", position));
        positions.add(position);

        Position position2 = new Position("Заступник директора", dept1);
        position2.setPerson(new Person("Петренко","Петро","Петрович", position2));
        positions.add(position2);

        Department dept11 = new Department("Департамент фінансово-економічної діяльності");
        dept1Child.add(dept11);
        setContact(dept11, "fin@fssu.gov.ua");

        Department dept12 = new Department("Департамент бухгалтерського обліку та консолідованої звітності");
        dept1Child.add(dept12);
        setContact(dept12, "zv@fssu.gov.ua");

        Department dept13 = new Department("Управління документального забезпечення, роботи зі зверненнями громадян та запитами на публічну інформацію");
        dept1Child.add(dept13);
        setContact(dept13, "doc@fssu.gov.ua");



        return root;
    }

    private void setVDContact(Department dept) {
        DepartmentContact contact = new DepartmentContact(dept);
        dept.setContact(contact);
        List<Address> addresses = contact.getAddresses();
        addresses.add(Address.getNew(0, "юридична адреса", "00000", "Україна", null, "Подільський р-н", "м. Київ", "вул. Боричів Тік", "28", null,true, contact));
        addresses.add(Address.getNew(0, "додаткова адреса", "00000", "Україна", null, "Подільський р-н", "м. Київ", "вул. Боричів Тік", "28", null,true, contact));
        List<Email> emails = contact.getEmails();
        emails.add(Email.getNew(0,"основний", "fssu@fssu.gov.ua", contact, true));
        List<Phone> phones = contact.getPhones();
        phones.add(Phone.getNew(0,"телефон", "0442060401", contact, true));
        phones.add(Phone.getNew(0,"телефон/факс", "0442060401", contact, true));
        List<Site> sites = contact.getSites();
        sites.add(Site.getNew(0, "основний", "fssu.gov.ua", contact, true));
    }

    private void setContact(Department dept, String email, String phone) {
        DepartmentContact contact = new DepartmentContact(dept);
        dept.setContact(contact);
        List<Address> addresses = contact.getAddresses();
        addresses.add(Address.getNew(0, "основна", "00000", "Україна", null, "Подільський р-н", "м. Київ", "вул. Боричів Тік", "28", null,true, contact));
        List<Email> emails = contact.getEmails();
        emails.add(Email.getNew(0,"основний", email, contact, true));
        List<Phone> phones = contact.getPhones();
        phones.add(Phone.getNew(0,"телефон", phone, contact, true));
    }

    private void setContact(Department dept, String email) {
        DepartmentContact contact = new DepartmentContact(dept);
        dept.setContact(contact);
        List<Address> addresses = contact.getAddresses();
        addresses.add(Address.getNew(0, "основна", "00000", "Україна", null, "Подільський р-н", "м. Київ", "вул. Боричів Тік", "28", null,true, contact));
        List<Email> emails = contact.getEmails();
        emails.add(Email.getNew(0,"основний", email, contact, true));
    }


}
