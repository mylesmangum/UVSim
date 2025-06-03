import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
class MemoryTest {
    private static Memory goodMem;
    @BeforeEach
    void setUp() {
        goodMem = new Memory("Test1.txt");
    }

    @Test
    void read() {
        assertEquals(1009, goodMem.memoryArray[0]);
        assertEquals(0, goodMem.memoryArray[99]);
    }

    @Test
    void write() {
        goodMem.write(0, 0);
        assertEquals(0, goodMem.memoryArray[0]);
    }

    @Test
    void readTextFail() {
        Memory badMem = new Memory("Test0.txt");
        int[] emptyArray = new int[100];
        assertArrayEquals(emptyArray, badMem.memoryArray);
    }

    @Test
    void isWord() {
        assertTrue(Memory.isWord(1000));
        assertFalse(Memory.isWord(100));
    }
}