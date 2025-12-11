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
}
