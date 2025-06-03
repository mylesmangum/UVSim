public class Main {
    public static void main(String[] args) {
        UVCpu cpu = new UVCpu("Test1.txt");
        cpu.run();
        System.out.println(UVConsole.userInputInt());
    }
}