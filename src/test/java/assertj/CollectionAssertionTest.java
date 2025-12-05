package assertj;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CollectionAssertionTest {

    @Nested
    class 크기_검증 {

        @Test
        void hasSize_정확한_크기() {
            List<String> list = List.of("a", "b", "c");
            assertThat(list).hasSize(3);
        }

        @Test
        void isEmpty_빈_컬렉션() {
            List<String> list = List.of();
            assertThat(list).isEmpty();
        }

        @Test
        void isNotEmpty_비어있지_않음() {
            List<String> list = List.of("a");
            assertThat(list).isNotEmpty();
        }

        @Test
        void hasSizeBetween_크기_범위() {
            List<Integer> list = List.of(1, 2, 3, 4, 5);
            assertThat(list).hasSizeBetween(3, 10);
        }
    }
}
