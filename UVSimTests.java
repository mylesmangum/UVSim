import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.*;

public class UVSimTests {

    private Memory testMemory;
    private UVSimGUI gui;

    @Before
    public void setUp() {
        try {
            File testFile = new File("Test1.txt");
            FileWriter writer = new FileWriter(testFile);
            writer.write("1009\n");
            writer.write("2015\n");
            writer.write("4300\n");
            writer.close();

            testMemory = new Memory("Test1.txt", gui);

            testFile.delete();
        } catch (IOException e) {
            testMemory = new Memory();
            testMemory.write(0, 1009);
            testMemory.write(1, 2015);
            testMemory.write(2, 4300);
        }
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // //
    // // // // // // // // // // General  Tests // // // // // // // // // // // //
    // // // // // // // // // // // // // // // // // // // // // // // // // // //

    // Tests for Load/Store Operations (20, 21)
    @Test
    public void testLoadOperation() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 2005);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 1234);

        cpu.run();

        assertEquals(1234, cpu.acc);
    }

    @Test
    public void testLoadNegativeValue() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 2005);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, -5678);

        cpu.run();

        assertEquals(-5678, cpu.acc);
    }

    @Test
    public void testStoreOperation() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 2105);
        cpu.mem.write(1, 4300);
        cpu.acc = 9876;

        cpu.run();

        assertEquals(9876, cpu.mem.read(5));
    }

    @Test
    public void testStoreNegativeValue() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 2105);
        cpu.mem.write(1, 4300);
        cpu.acc = -4321;

        cpu.run();

        assertEquals(-4321, cpu.mem.read(5));
    }

    // Tests for I/O Operations (10, 11)
    @Test
    public void testWriteOperation() {
        UVCpu cpu = new UVCpu();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            cpu.mem.write(0, 1105);
            cpu.mem.write(1, 4300);
            cpu.mem.write(5, 1234);

            cpu.run();

            assertEquals("1234" + System.lineSeparator(), outputStream.toString());
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testWriteNegativeValue() {
        UVCpu cpu = new UVCpu();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            cpu.mem.write(0, 1105);
            cpu.mem.write(1, 4300);
            cpu.mem.write(5, -9876);

            cpu.run();

            assertEquals("-9876" + System.lineSeparator(), outputStream.toString());
        } finally {
            System.setOut(originalOut);
        }
    }

    // Tests for Control Operations (40, 41, 42, 43)
    @Test
    public void testBranchOperation() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 4005);
        cpu.mem.write(1, 2099);
        cpu.mem.write(5, 4300);

        cpu.run();

        assertEquals(6, cpu.pc);
        assertTrue(cpu.halted);
    }

    @Test
    public void testBranchNegativeWhenAccumulatorNegative() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 4105);
        cpu.mem.write(1, 2099);
        cpu.mem.write(5, 4300);
        cpu.acc = -100;

        cpu.run();

        assertEquals(6, cpu.pc);
        assertTrue(cpu.halted);
    }

    @Test
    public void testBranchNegativeWhenAccumulatorPositive() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 4105);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 2099);
        cpu.acc = 100;

        cpu.run();

        assertEquals(2, cpu.pc);
        assertTrue(cpu.halted);
    }

    @Test
    public void testBranchZeroWhenAccumulatorZero() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 4205);
        cpu.mem.write(1, 2099);
        cpu.mem.write(5, 4300);
        cpu.acc = 0;

        cpu.run();

        assertEquals(6, cpu.pc);
        assertTrue(cpu.halted);
    }

    @Test
    public void testBranchZeroWhenAccumulatorNonZero() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 4205);
        cpu.mem.write(1, 4300);
        cpu.mem.write(5, 2099);
        cpu.acc = 42;

        cpu.run();

        assertEquals(2, cpu.pc);
        assertTrue(cpu.halted);
    }

    @Test
    public void testHaltOperation() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 4300);
        cpu.mem.write(1, 2099);

        cpu.run();

        assertTrue(cpu.halted);
        assertEquals(1, cpu.pc);
    }

    @Test
    public void testInvalidMemoryAccess() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 2099);
        cpu.mem.write(1, 4300);

        cpu.run();

        assertEquals(0, cpu.acc);
    }

    @Test
    public void testProgramCounterIncrement() {
        UVCpu cpu = new UVCpu();
        cpu.mem.write(0, 2005);
        cpu.mem.write(1, 3006);
        cpu.mem.write(2, 4300);
        cpu.mem.write(5, 100);
        cpu.mem.write(6, 200);

        cpu.run();

        assertEquals(3, cpu.pc);
        assertEquals(300, cpu.acc);
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // //
    // // // // // // // // // // UVCpu Tests // // // // // // // // // // // // //
    // // // // // // // // // // // // // // // // // // // // // // // // // // //

    // Arithmetic Operations Tests (30, 31, 32, 33)
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

    // // // // // // // // // // // // // // // // // // // // // // // // // // //
    // // // // // // // // // //  Memory Tests  // // // // // // // // // // // //
    // // // // // // // // // // // // // // // // // // // // // // // // // // //

    @Test
    public void read() {
        assertEquals(1009, testMemory.read(0));
        assertEquals(0, testMemory.read(99));
    }

    @Test
    public void write() {
        testMemory.write(0, 5555);
        assertEquals(5555, testMemory.read(0));
    }

    @Test
    public void readTextFail() {
        Memory badMem = new Memory("Test0.txt", gui);
        MemoryRegister[] emptyArray = new MemoryRegister[100];
        assertArrayEquals(emptyArray, badMem.memoryArray);
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // //
    // // // // // // // // // //  Memory Tests  // // // // // // // // // // // //
    // // // // // // // // // // // // // // // // // // // // // // // // // // //
    @Test
    public void testUserInt() {
        provideInput("123");
        int input = gui.userInputInt();
        provideInput("-456");
        assertEquals(123, input);
        input = gui.userInputInt();
        assertEquals(-456, input);
    }

    public void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
}