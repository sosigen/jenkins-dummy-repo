import static org.junit.Assert.*;
import org.junit.Test;

public class MyDemoTest {

    @Test
    public void testGreet() {
        assertEquals("Hello, World!", MyDemo.greet());
    }

    @Test
    public void testLengthOfGreet() {
        int expectedLength = 13;
        assertEquals(expectedLength, MyDemo.greet().length());
    }

    @Test
    public void testGreetContainsHello() {
        assertTrue(MyDemo.greet().contains("Hello"));
    }

    @Test
    public void testGreetStartsWithHello() {
        assertTrue(MyDemo.greet().startsWith("Hello"));
    }

    @Test
    public void testGreetEndsWithWorld() {
        assertTrue(MyDemo.greet().endsWith("World!"));
    }
}
