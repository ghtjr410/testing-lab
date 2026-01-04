package s02_assertj;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
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
        void assertThatNoException_방식() {
            assertThatNoException().isThrownBy(() -> {
                int result = 1 + 1;
            });
        }

        @Test
        void assertThatCode_doesNotThrowAnyException_방식() {
            assertThatCode(() -> {
                        int result = 1 + 1;
                    })
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class 타입별_단축_메서드 {

        @Test
        void Exception_단축() {
            assertThatException()
                    .isThrownBy(() -> {
                        throw new Exception("에러 발생");
                    })
                    .withMessage("에러 발생");
        }

        @Test
        void RuntimeException_단축() {
            assertThatRuntimeException()
                    .isThrownBy(() -> {
                        throw new RuntimeException("런타임 에러");
                    })
                    .withMessage("런타임 에러");
        }

        @Test
        void NullPointerException_단축() {
            assertThatNullPointerException().isThrownBy(() -> {
                throw new NullPointerException("null 값");
            });
        }

        @Test
        void IllegalArgumentException_단축() {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> {
                        throw new IllegalArgumentException("잘못된 인자");
                    })
                    .withMessage("잘못된 인자");
        }

        @Test
        void IllegalStateException_단축() {
            assertThatIllegalStateException()
                    .isThrownBy(() -> {
                        throw new IllegalStateException("잘못된 상태");
                    })
                    .withMessageContaining("상태");
        }

        @Test
        void IOException_단축() {
            assertThatIOException().isThrownBy(() -> {
                throw new IOException("IO 에러");
            });
        }

        @Test
        void IndexOutOfBoundsException_단축() {
            assertThatIndexOutOfBoundsException()
                    .isThrownBy(() -> {
                        throw new IndexOutOfBoundsException("범위 에러");
                    })
                    .withMessageContaining("범위");
        }

        @Test
        void ReflectiveOperationException_단축() {
            assertThatReflectiveOperationException()
                    .isThrownBy(() -> {
                        Class.forName("존재하지.않는.클래스");
                    })
                    .withMessageContaining("존재하지.않는.클래스");
        }
    }
}
