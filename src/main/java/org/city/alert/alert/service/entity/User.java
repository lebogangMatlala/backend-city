package org.city.alert.alert.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.city.alert.alert.service.enums.Department;
import org.city.alert.alert.service.enums.UserRoles;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String email;
    private String phone;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRoles role; // CITIZEN, TECHNICIAN, ADMIN
    @Enumerated(EnumType.STRING)   // saves enum as text in DB
    private Department department; //Potholes, Electric, Water
    private Integer workloadCount = 0; // Optional: track tasks assigned
}
