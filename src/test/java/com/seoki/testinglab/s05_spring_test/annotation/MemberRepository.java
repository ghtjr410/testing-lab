package com.seoki.testinglab.s05_spring_test.annotation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByNameStartingWith(String prefix);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.email LIKE %:domain")
    List<Member> findByEmailDomain(String domain);

    @Query(value = "SELECT COUNT(*) FROM members", nativeQuery = true)
    long countAllNative();
}
