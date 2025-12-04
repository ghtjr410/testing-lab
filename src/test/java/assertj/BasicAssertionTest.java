package assertj;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BasicAssertionTest {

    @Nested
    class isEqualTo_동등성_비교 {

        @Test
        void 문자열_값_비교() {
            assertThat("hello").isEqualTo("hello");
        }

        @Test
        void 숫자_값_비교() {
            assertThat(100).isEqualTo(100);
        }

        @Test
        void 다른_인스턴스지만_값이_같으면_통과() {
            String str1 = new String("test");
            String str2 = new String("test");

            assertThat(str1).isEqualTo(str2);
        }
    }
}
