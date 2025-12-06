package assertj;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ExceptionAssertionTest {

    @Nested
    class assertThatThrownBy_예외_발생_검증 {

        @Test
        void 예외_타입_검증() {
            assertThatThrownBy(() -> {
                        throw new IllegalArgumentException();
                    })
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 예외_메시지_검증() {
            assertThatThrownBy(() -> {
                        throw new IllegalArgumentException("잘못된 값입니다");
                    })
                    .hasMessage("잘못된 값입니다");
        }

        @Test
        void 예외_메시지_포함_검증() {
            assertThatThrownBy(() -> {
                        throw new IllegalArgumentException("잘못된 값입니다: -1");
                    })
                    .hasMessageContaining("잘못된 값");
        }

        @Test
        void 예외_메시지_시작_검증() {
            assertThatThrownBy(() -> {
                        throw new IllegalArgumentException("잘못된 값입니다");
                    })
                    .hasMessageStartingWith("잘못된");
        }

        @Test
        void 예외_메시지_끝_검증() {
            assertThatThrownBy(() -> {
                        throw new IllegalArgumentException("잘못된 값입니다");
                    })
                    .hasMessageEndingWith("값입니다");
        }

        @Test
        void 체이닝으로_여러_조건_검증() {
            assertThatThrownBy(() -> {
                        throw new IllegalArgumentException("잘못된 값입니다");
                    })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("잘못된");
        }
    }

    @Nested
    class assertThatCode_예외_미발생_검증 {

        @Test
        void 예외가_발생하지_않으면_통과() {
            assertThatCode(() -> {
                        int result = 1 + 1;
                    })
                    .doesNotThrowAnyException();
        }
    }
}
