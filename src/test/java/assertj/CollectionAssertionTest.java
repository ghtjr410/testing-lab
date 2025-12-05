package assertj;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;
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

    @Nested
    class contains_포함_검증 {

        @Test
        void contains_특정_요소_포함() {
            List<String> list = List.of("apple", "banana", "cherry");
            assertThat(list).contains("banana");
        }

        @Test
        void contains_여러_요소_포함_순서_무관() {
            List<String> list = List.of("apple", "banana", "cherry");
            assertThat(list).contains("cherry", "apple");
        }

        @Test
        void containsOnly_해당_요소들만_순서_무관() {
            List<String> list = List.of("a", "b", "c");
            assertThat(list).containsOnly("c", "a", "b");
        }

        @Test
        void containsExactly_정확한_요소_정확한_순서() {
            List<String> list = List.of("a", "b", "c");
            assertThat(list).containsExactly("a", "b", "c");
        }

        @Test
        void containsExactlyInAnyOrder_정확한_요소_순서_무관() {
            List<String> list = List.of("a", "b", "c");
            assertThat(list).containsExactlyInAnyOrder("c", "a", "b");
        }

        @Test
        void doesNotContain_포함하지_않음() {
            List<String> list = List.of("a", "b", "c");
            assertThat(list).doesNotContain("d", "e");
        }

        @Test
        void containsNull_null_포함() {
            List<String> list = java.util.Arrays.asList("a", null, "b");
            assertThat(list).containsNull();
        }

        @Test
        void doesNotContainNull_null_미포함() {
            List<String> list = List.of("a", "b");
            assertThat(list).doesNotContainNull();
        }
    }

    @Nested
    class 첫번째_마지막_요소 {

        @Test
        void first_첫번째_요소() {
            List<String> list = List.of("a", "b", "c");
            assertThat(list).first().isEqualTo("a");
        }

        @Test
        void last_마지막_요소() {
            List<String> list = List.of("a", "b", "c");
            assertThat(list).last().isEqualTo("c");
        }

        @Test
        void element_특정_인덱스_요소() {
            List<String> list = List.of("a", "b", "c");
            assertThat(list).element(1).isEqualTo("b");
        }
    }

    @Nested
    class extracting_필드_추출 {

        record User(String name, int age) {}

        @Test
        void 단일_필드_추출() {
            List<User> users = List.of(new User("철수", 20), new User("영희", 25));

            assertThat(users).extracting(User::name).containsExactly("철수", "영희");
        }

        @Test
        void 여러_필드_추출() {
            List<User> users = List.of(new User("철수", 20), new User("영희", 25));

            assertThat(users).extracting(User::name, User::age).containsExactly(tuple("철수", 20), tuple("영희", 25));
        }
    }

    @Nested
    class filteredOn_필터링 {

        record User(String name, int age) {}

        @Test
        void 조건으로_필터링_후_검증() {
            List<User> users = List.of(new User("철수", 20), new User("영희", 25), new User("민수", 30));

            assertThat(users)
                    .filteredOn(user -> user.age() >= 25)
                    .extracting(User::name)
                    .containsExactly("영희", "민수");
        }
    }

    @Nested
    class Set_검증 {

        @Test
        void Set도_동일하게_사용() {
            Set<String> set = Set.of("a", "b", "c");

            assertThat(set).hasSize(3).contains("a", "b");
        }
    }
}
