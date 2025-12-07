package junit;

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
}
