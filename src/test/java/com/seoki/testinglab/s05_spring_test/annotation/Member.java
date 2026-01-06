package com.seoki.testinglab.s05_spring_test.annotation;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected Member() {} // JPA용

    Member(String name, String email) { // package-private
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    // getter도 package-private 가능
    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getEmail() {
        return email;
    }
}
