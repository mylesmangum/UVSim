public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.write(0, 100);
        System.out.println(memory.read(0));
    }
}