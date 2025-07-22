public class MemoryRegister {
    int value;
    public MemoryRegister(int memoryValue) {
        value = memoryValue;
    }
    public MemoryRegister() {
        value = 0;
    }

    //get first 3 digits
    public int getOpCode() {
        return value / 1000;
    }

    //get last 3 digits
    public int getAddress() {
        return value % 1000;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
