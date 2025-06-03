import org.junit.Test;
import static org.junit.Assert.*;

public class UVCpuTest {

    //Added 8 tests. 2 per case (30,31,32,33)
    @Test
    public void testAdditionPositive() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 3005);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 10);
        cpu.acc = 5;

        cpu.run();

        assertEquals(15, cpu.acc);
    }

    @Test
    public void testAdditionNegative() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 3005);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, -20);
        cpu.acc = 10;

        cpu.run();

        assertEquals(-10, cpu.acc);
    }

    @Test
    public void testSubtractionPositive() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 3105);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 4);
        cpu.acc = 10;

        cpu.run();

        assertEquals(6, cpu.acc);
    }

    @Test
    public void testSubtractionNegativeResult() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 3105);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 20);
        cpu.acc = 5;

        cpu.run();

        assertEquals(-15, cpu.acc);
    }

    @Test
    public void testDivisionResult() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 3205);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 2);
        cpu.acc = 10;

        cpu.run();

        assertEquals(5, cpu.acc);
    }

    @Test
    public void testDivisionByZeroThrows() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 3205);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 0);
        cpu.acc = 10;

        try {
            cpu.run();
            fail("Expected ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            //
        }
    }

    @Test
        public void testMultiplicationPositive() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 3305);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 4);
        cpu.acc = 3;

        cpu.run();

        assertEquals(12, cpu.acc);
    }

    @Test
    public void testMultiplicationByZero() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 3305);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 0);
        cpu.acc = 25;

        cpu.run();

        assertEquals(0, cpu.acc);
    }
}
