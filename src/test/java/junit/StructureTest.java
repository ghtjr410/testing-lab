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
}
