package com.gorkem.manuelmap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String industry;
    private String city;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> users;
}