
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTest {

    @Test
    public void test1() {
        System.out.println("Test 1 works");
    }

    @Test
    public void test2() {
        System.out.println("Test 1 should fail");
        assertEquals(1, 2);
    }
}