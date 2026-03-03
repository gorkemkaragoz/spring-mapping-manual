package com.gorkem.manuelmap.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;

    private Integer age;
    private Integer salary;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}