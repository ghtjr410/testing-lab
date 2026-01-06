package com.seoki.testinglab.s05_spring_test.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 쿼리 메서드 테스트
     */
    @Nested
    @DataJpaTest
    class 쿼리_메서드_테스트 {

        @Autowired
        TestEntityManager em;

        @Autowired
        MemberRepository memberRepository;

        @BeforeEach
        void setUp() {
            em.persist(new Member("김철수", "kim@test.com"));
            em.persist(new Member("이영희", "lee@test.com"));
            em.persist(new Member("김영수", "kim2@test.com"));
            em.flush();
            em.clear();
        }

        @Test
        void 메서드_이름으로_쿼리_생성() {
            List<Member> kims = memberRepository.findByNameStartingWith("김");

            assertThat(kims).hasSize(2);
            assertThat(kims).extracting(Member::getName).containsExactlyInAnyOrder("김철수", "김영수");
        }

        @Test
        void 이메일로_존재_여부_확인() {
            boolean exists = memberRepository.existsByEmail("kim@test.com");
            boolean notExists = memberRepository.existsByEmail("nobody@test.com");

            assertThat(exists).isTrue();
            assertThat(notExists).isFalse();
        }

        @Test
        void JPQL_쿼리_테스트() {
            List<Member> members = memberRepository.findByEmailDomain("test.com");

            assertThat(members).hasSize(3);
        }

        @Test
        void Native_쿼리_테스트() {
            long count = memberRepository.countAllNative();

            assertThat(count).isEqualTo(3);
        }
    }

    /**
     * 슬라이스 테스트 범위 확인
     */
    @Nested
    @DataJpaTest
    class 슬라이스_테스트_범위 {

        @Autowired
        ApplicationContext applicationContext;

        @Test
        void JPA_관련_빈이_로드된다() {
            // EntityManager 타입으로 정확히 확인
            assertThat(applicationContext.getBeansOfType(EntityManager.class)).isNotEmpty();

            // TestEntityManager도 확인
            assertThat(applicationContext.getBeansOfType(TestEntityManager.class))
                    .isNotEmpty();
        }

        @Test
        void Repository_빈이_로드된다() {
            // Repository 타입으로 확인
            assertThat(applicationContext.getBeansOfType(MemberRepository.class))
                    .isNotEmpty();
        }

        @Test
        void Controller_빈은_로드되지_않는다() {
            // @Controller 어노테이션으로 정확히 확인
            Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);
            Map<String, Object> restControllers = applicationContext.getBeansWithAnnotation(RestController.class);

            assertThat(controllers).isEmpty();
            assertThat(restControllers).isEmpty();
        }

        @Test
        void Service_빈은_로드되지_않는다() {
            // @Service 어노테이션으로 정확히 확인
            Map<String, Object> services = applicationContext.getBeansWithAnnotation(Service.class);

            assertThat(services).isEmpty();
        }
    }

    /**
     * 실제 DB 사용 (내장 DB 대체)
     * - Testcontainers와 함께 사용 권장
     */
    @Nested
    @DataJpaTest
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 내장 DB 사용 안 함
    @TestPropertySource(
            properties = {
                "spring.datasource.url=jdbc:h2:mem:realdb", // 실제로는 MySQL 등 사용
                "spring.jpa.hibernate.ddl-auto=create-drop"
            })
    class 실제_DB_사용 {

        @Autowired
        MemberRepository memberRepository;

        @Test
        void 설정된_DB를_사용한다() {
            // Replace.NONE: 자동 H2 구성 비활성화
            // application.properties 또는 @TestPropertySource의 설정 사용
            Member member = memberRepository.save(new Member("실제DB", "real@test.com"));

            assertThat(member.getId()).isNotNull();
        }
    }

    /**
     * @DataJpaTest의 자동 설정
     * 내부적으로 이것들이 포함되어 있음:
     * @Transactional          ← 자동 트랜잭션 + 롤백
     * @AutoConfigureTestDatabase  ← H2 자동 구성
     * @AutoConfigureDataJpa   ← JPA 설정
     */
    @Nested
    @DataJpaTest
    @TestPropertySource(properties = {"spring.jpa.show-sql=true", "spring.jpa.properties.hibernate.format_sql=true"})
    class 자동_설정_확인 {

        @Autowired
        TestEntityManager em;

        @Autowired
        MemberRepository memberRepository;

        @Test
        void 트랜잭션이_자동_적용된다() {
            // @DataJpaTest는 @Transactional 포함
            // 각 테스트 메서드가 트랜잭션 내에서 실행됨
            Member member = memberRepository.save(new Member("트랜잭션", "tx@test.com"));

            // 트랜잭션 내에서 영속성 컨텍스트 공유
            assertThat(em.getEntityManager().contains(member)).isTrue();
        }

        @Test
        void SQL_로그_확인_가능() {
            // spring.jpa.show-sql=true 설정으로 콘솔에 SQL 출력
            memberRepository.save(new Member("SQL확인", "sql@test.com"));
            em.flush(); // INSERT 쿼리 확인 가능

            memberRepository.findAll(); // SELECT 쿼리 확인 가능
        }
    }

    /**
     * 실무 Best Practice
     */
    @Nested
    class 실무_Best_Practice {

        @Test
        @Disabled("개념 설명용")
        void flush_clear_패턴() {
            /*
             * Repository 테스트 시 필수 패턴:
             *
             * 1. 데이터 저장
             * Member member = memberRepository.save(new Member(...));
             *
             * 2. flush() - 영속성 컨텍스트의 변경사항을 DB에 반영
             * em.flush();
             *
             * 3. clear() - 영속성 컨텍스트 초기화 (1차 캐시 비움)
             * em.clear();
             *
             * 4. 조회 - DB에서 실제로 조회
             * Member found = memberRepository.findById(member.getId());
             *
             * 왜 필요한가?
             * - flush 없이: 실제 쿼리가 나가지 않을 수 있음
             * - clear 없이: 1차 캐시에서 조회하여 실제 DB 상태 검증 불가
             */
        }

        @Test
        @Disabled("개념 설명용")
        void H2_vs_실제DB_차이_주의() {
            /*
             * H2와 MySQL의 차이점:
             *
             * 1. 대소문자 구분
             *    - H2: 기본적으로 대소문자 구분 안 함
             *    - MySQL: 설정에 따라 다름
             *
             * 2. SQL 문법 차이
             *    - LIMIT, OFFSET 등 방언 차이
             *    - 날짜/시간 함수 차이
             *
             * 3. 트랜잭션 격리 수준 기본값
             *    - H2: READ_COMMITTED
             *    - MySQL: REPEATABLE_READ
             *
             * 해결책:
             * - Testcontainers로 실제 MySQL 컨테이너 사용
             * - H2 MySQL 모드: jdbc:h2:mem:testdb;MODE=MySQL
             */
        }

        @Test
        @Disabled("개념 설명용")
        void DataJpaTest_선택_기준() {
            /*
             * @DataJpaTest 사용 적합한 경우:
             * - Repository 쿼리 메서드 검증
             * - JPQL, Native Query 검증
             * - Entity 연관관계 매핑 검증
             * - Auditing (@CreatedDate 등) 검증
             *
             * @DataJpaTest가 부적합한 경우:
             * - Service 레이어 트랜잭션 전파 테스트
             * - 여러 Repository 조합 테스트 → @SpringBootTest
             * - 실제 DB 특성(락, 격리수준) 테스트 → Testcontainers
             */
        }
    }
}
