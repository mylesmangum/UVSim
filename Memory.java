public class Memory {
    int[] memoryArray = new int[100];

    public int read(int address) {
        checkIndex(address);
        return memoryArray[address];
    }
    public void write(int address, int value) {
        checkIndex(address);
        memoryArray[address] = value;
    }

    private void checkIndex(int address) {
        if (address > 99 || address < 0) {
            throw new IndexOutOfBoundsException();
        }
    }
}
