package com.seoki.testinglab.s05_spring_test.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * @DataJpaTest 학습 테스트
 *
 * 핵심 개념:
 * - JPA 관련 컴포넌트만 로드하는 슬라이스 테스트
 * - @Entity, Repository, TestEntityManager 등만 구성
 * - 기본적으로 내장 DB(H2)를 사용하고 트랜잭션 후 롤백
 *
 * 실무 포인트:
 * - Repository 메서드의 쿼리 정확성 검증
 * - Entity 매핑 검증
 * - JPQL, QueryDSL 쿼리 테스트
 * - 실제 DB(MySQL)와 테스트용 DB(H2) 간 차이 주의
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DataJpaTestTest {

    /**
     * 기본 @DataJpaTest 사용법
     * - 내장 H2 데이터베이스 자동 구성
     * - 각 테스트 후 자동 롤백
     */
    @Nested
    @DataJpaTest
    class DataJpaTest_기본_사용법 {

        @Autowired
        TestEntityManager entityManager;

        @Autowired
        MemberRepository memberRepository;

        @Test
        void Repository가_자동_구성된다() {
            assertThat(memberRepository).isNotNull();
        }

        @Test
        void TestEntityManager가_자동_구성된다() {
            // TestEntityManager: 테스트용 EntityManager 래퍼
            // persist, find, flush 등 직접 제어 가능
            assertThat(entityManager).isNotNull();
        }

        @Test
        void 엔티티_저장_및_조회() {
            Member member = new Member("홍길동", "hong@test.com");

            Member savedMember = memberRepository.save(member);
            entityManager.flush(); // 쿼리 즉시 실행
            entityManager.clear(); // 영속성 컨텍스트 초기화 (캐시 제거)

            Optional<Member> found = memberRepository.findById(savedMember.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getName()).isEqualTo("홍길동");
        }

        @Test
        void 각_테스트는_롤백되어_격리된다() {
            // 다른 테스트에서 저장한 데이터가 보이지 않음
            List<Member> members = memberRepository.findAll();

            // 이전 테스트 데이터가 롤백되어 비어있음
            assertThat(members).isEmpty();
        }
    }

    /**
     * TestEntityManager 활용
     * - EntityManager의 테스트 친화적 래퍼
     */
    @Nested
    @DataJpaTest
    class TestEntityManager_활용 {

        @Autowired
        TestEntityManager em;

        @Autowired
        MemberRepository memberRepository;

        @Test
        void persist로_직접_저장() {
            // Repository 대신 직접 persist
            Member member = new Member("직접저장", "direct@test.com");

            Member persisted = em.persist(member);
            em.flush();

            assertThat(persisted.getId()).isNotNull();
        }

        @Test
        void persistAndFlush로_저장과_플러시_동시에() {
            Member member = new Member("플러시", "flush@test.com");

            Member saved = em.persistAndFlush(member);

            assertThat(saved.getId()).isNotNull();
        }

        @Test
        void find로_직접_조회() {
            Member member = em.persistAndFlush(new Member("찾기", "find@test.com"));
            em.clear();

            Member found = em.find(Member.class, member.getId());

            assertThat(found.getName()).isEqualTo("찾기");
        }

        @Test
        void clear로_영속성_컨텍스트_초기화() {
            Member member = em.persistAndFlush(new Member("초기화", "clear@test.com"));

            em.clear(); // 1차 캐시 비움

            Member reloaded = em.find(Member.class, member.getId());
            assertThat(reloaded).isNotSameAs(member); // 다른 인스턴스
        }

        @Test
        void detach로_특정_엔티티만_분리() {
            Member member = em.persistAndFlush(new Member("분리", "detach@test.com"));

            em.detach(member);

            // 분리된 엔티티는 영속성 컨텍스트에서 관리되지 않음
            assertThat(em.getEntityManager().contains(member)).isFalse();
        }
    }
}
