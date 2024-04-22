package com.ngt.cuoiky.model;


import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="email", unique = true, length = 100, nullable = false)
    private String email;

    @Column(name="password", length = 100)
    private String password;

    @Column(name="first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name="last_name", nullable = false, length = 100)
    private String lastName;


    @Column(name="phone", length = 20)
    private String phone;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="dd/MM/yyyy")
    @Column(name="created_at", nullable = false)
    private Date registrationDate;

    @Column(name="is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns=
            @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="role_id", referencedColumnName="id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<Address> addresses;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<Order> orders;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<Review> reviews;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<Cart> carts;


}
