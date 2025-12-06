package assertj;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AdvancedAssertionTest {

    @Nested
    class SoftAssertions_모든_검증_실행 {

        @Test
        void 일반_assertion은_첫_실패에서_멈춤() {
            // 첫 번째 실패하면 두 번째는 실행 안 됨
            // assertThat(1).isEqualTo(2);  // 여기서 멈춤
            // assertThat(3).isEqualTo(4);  // 실행 안 됨
        }

        @Test
        void soft_assertion은_모든_검증_실행_후_한번에_실패() {
            var softly = new org.assertj.core.api.SoftAssertions();

            softly.assertThat(1).isEqualTo(1);
            softly.assertThat("hello").startsWith("h");
            softly.assertThat(List.of(1, 2, 3)).hasSize(3);

            softly.assertAll(); // 여기서 모든 실패 한번에 보고
        }

        @Test
        void assertSoftly_람다_버전() {
            assertSoftly(softly -> {
                softly.assertThat(1).isEqualTo(1);
                softly.assertThat("hello").isNotEmpty();
                softly.assertThat(List.of("a", "b")).contains("a");
            });
        }
    }
}
