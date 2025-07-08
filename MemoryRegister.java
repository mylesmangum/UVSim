public class MemoryRegister {
    int value;
    public MemoryRegister(int memoryValue) {
        value = memoryValue;
    }
    public MemoryRegister() {
        value = 0;
    }

    public int getOpCode() {
        return value / 100;
    }

    public int getAddress() {
        return value % 100;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
