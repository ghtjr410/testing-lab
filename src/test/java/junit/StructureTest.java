package junit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class StructureTest {

    @Nested
    class DisplayName_테스트_이름 {

        @Test
        @DisplayName("한글로 테스트 이름 작성 가능")
        void 메서드명_대신_DisplayName_사용() {
            assertThat(true).isTrue();
        }

        @Test
        @DisplayName("특수문자, 공백, 이모지 🎉 모두 가능")
        void 특수문자_포함_가능() {
            assertThat(true).isTrue();
        }
    }

    @Nested
    class DisplayNameGeneration_자동_이름_생성 {

        /**
         * DisplayNameGenerator 종류:
         * - Standard: 메서드명 그대로 (기본값)
         * - Simple: 괄호 제거
         * - ReplaceUnderscores: 언더스코어를 공백으로
         * - IndicativeSentences: 클래스명 + 메서드명 조합
         */
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class ReplaceUnderscores_적용시 {

            @Test
            void 언더스코어가_공백으로_변환된다() {
                // 표시: "언더스코어가 공백으로 변환된다"
                assertThat(true).isTrue();
            }
        }
    }

    @Nested
    class Nested_테스트_그룹화 {

        /**
         * @Nested 장점:
         * - 관련 테스트 논리적 그룹화
         * - 계층 구조로 가독성 향상
         * - 각 Nested 클래스별 @BeforeEach 가능
         */
        @Nested
        class 성공_케이스 {

            @Test
            void 정상_입력시_성공() {
                assertThat(true).isTrue();
            }
        }

        @Nested
        class 실패_케이스 {

            @Test
            void null_입력시_예외() {
                assertThat(true).isTrue();
            }

            @Test
            void 빈값_입력시_예외() {
                assertThat(true).isTrue();
            }
        }
    }

    @Nested
    class Disabled_테스트_비활성화 {

        @Test
        @Disabled("버그 수정 전까지 비활성화")
        void 비활성화된_테스트() {
            // 실행되지 않음
        }

        @Test
        @Disabled
        void 이유_생략_가능() {
            // 실행되지 않음
        }
    }

    @Nested
    class Tag_테스트_분류 {

        /**
         * @Tag로 테스트 분류 후 선택 실행 가능
         *
         * Gradle: test { useJUnitPlatform { includeTags 'fast' } }
         * Maven: -Dgroups="fast"
         */
        @Test
        @Tag("fast")
        void 빠른_테스트() {
            assertThat(true).isTrue();
        }

        @Test
        @Tag("slow")
        void 느린_테스트() {
            assertThat(true).isTrue();
        }

        @Test
        @Tag("fast")
        @Tag("unit")
        void 여러_태그_가능() {
            assertThat(true).isTrue();
        }
    }

    @Nested
    class Order_테스트_순서 {

        /**
         * 기본적으로 테스트 순서는 보장되지 않음
         * @TestMethodOrder로 순서 지정 가능
         *
         * 주의: 테스트 간 의존성은 안티패턴
         * 순서 지정은 통합 테스트 시나리오에서만 사용
         */
        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        class OrderAnnotation_순서_지정 {

            @Test
            @Order(1)
            void 첫번째() {
                assertThat(true).isTrue();
            }

            @Test
            @Order(2)
            void 두번째() {
                assertThat(true).isTrue();
            }

            @Test
            @Order(3)
            void 세번째() {
                assertThat(true).isTrue();
            }
        }

        @Nested
        @TestMethodOrder(MethodOrderer.DisplayName.class)
        class DisplayName_알파벳_순서 {

            @Test
            @DisplayName("A 테스트")
            void z_메서드명과_무관하게_DisplayName_순서() {
                assertThat(true).isTrue();
            }

            @Test
            @DisplayName("B 테스트")
            void a_메서드명과_무관하게_DisplayName_순서() {
                assertThat(true).isTrue();
            }
        }
    }
}
