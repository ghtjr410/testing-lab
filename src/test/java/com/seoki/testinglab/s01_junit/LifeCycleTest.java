package com.seoki.testinglab.s01_junit;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LifeCycleTest {

    private static List<String> log = new ArrayList<>();

    @BeforeAll
    static void 전체_테스트_시작_전_한번() {
        log.add("@BeforeAll");
    }

    @AfterAll
    static void 전체_테스트_종료_후_한번() {
        log.add("@AfterAll");
        System.out.println("실행 순서: " + log);
        // 출력: [@BeforeAll, @BeforeEach, @Test1, @AfterEach, @BeforeEach, @Test2, @AfterEach, @AfterAll]
    }

    @BeforeEach
    void 각_테스트_시작_전() {
        log.add("@BeforeEach");
    }

    @AfterEach
    void 각_테스트_종료_후() {
        log.add("@AfterEach");
    }

    @Test
    void 테스트1() {
        log.add("@Test1");
        assertThat(true).isTrue();
    }

    @Test
    void 테스트2() {
        log.add("@Test2");
        assertThat(true).isTrue();
    }

    @Nested
    class BeforeAll_AfterAll {

        /**
         * @BeforeAll / @AfterAll
         * - 전체 테스트 클래스에서 딱 한 번 실행
         * - static 메서드여야 함 (인스턴스 생성 전에 실행되니까)
         * - 용도: DB 연결, 테스트 컨테이너 시작, 공유 리소스 초기화
         */
        @Test
        void static_메서드여야_하는_이유() {
            // JUnit은 각 테스트마다 새 인스턴스 생성
            // @BeforeAll은 인스턴스 생성 전에 실행되므로 static 필수
            assertThat(true).isTrue();
        }
    }

    @Nested
    class BeforeEach_AfterEach {

        private List<String> items;

        @BeforeEach
        void setUp() {
            items = new ArrayList<>();
            items.add("초기값");
        }

        @AfterEach
        void tearDown() {
            items.clear();
        }

        /**
         * @BeforeEach / @AfterEach
         * - 각 테스트 메서드 실행 전/후 매번 실행
         * - 인스턴스 메서드
         * - 용도: 테스트 격리, 객체 초기화, 정리
         */
        @Test
        void 각_테스트는_독립적인_상태로_시작() {
            items.add("추가");
            assertThat(items).hasSize(2);
        }

        @Test
        void 이전_테스트_영향_안받음() {
            // 위 테스트에서 "추가" 했지만 여기선 초기 상태
            assertThat(items).hasSize(1);
            assertThat(items).containsExactly("초기값");
        }
    }

    @Nested
    class 테스트_인스턴스_생성 {

        private int count = 0;

        @Test
        void 테스트마다_새_인스턴스_생성됨_1() {
            count++;
            assertThat(count).isEqualTo(1);
        }

        @Test
        void 테스트마다_새_인스턴스_생성됨_2() {
            count++;
            // 새 인스턴스라서 count는 다시 0부터 시작
            assertThat(count).isEqualTo(1);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestInstance_PER_CLASS {

        private int count = 0;

        /**
         * @TestInstance(PER_CLASS)
         * - 테스트 클래스당 인스턴스 하나만 생성
         * - @BeforeAll, @AfterAll에 static 안 붙여도 됨
         * - 주의: 테스트 간 상태 공유됨 (격리 안 됨)
         */
        @BeforeAll
        void static_아니어도_됨() {
            count = 100;
        }

        @Test
        void 상태_공유됨_1() {
            count++;
            assertThat(count).isEqualTo(101);
        }

        @Test
        void 상태_공유됨_2() {
            count++;
            // 같은 인스턴스라서 이전 테스트 영향 받음
            // 테스트 실행 순서에 따라 결과 달라질 수 있음
            assertThat(count).isGreaterThan(100);
        }
    }

    @Nested
    class Nested_클래스의_생명주기 {

        /**
         * @Nested 클래스는 부모의 @BeforeEach, @AfterEach 상속받음
         * 실행 순서:
         * 1. 부모 @BeforeEach
         * 2. 자식 @BeforeEach
         * 3. 테스트 실행
         * 4. 자식 @AfterEach
         * 5. 부모 @AfterEach
         */
        @BeforeEach
        void nested_전용_setup() {
            log.add("Nested @BeforeEach");
        }

        @Test
        void 부모_BeforeEach도_실행됨() {
            // 부모의 @BeforeEach 먼저 실행되고
            // 이 클래스의 @BeforeEach 실행됨
            assertThat(true).isTrue();
        }
    }
}
