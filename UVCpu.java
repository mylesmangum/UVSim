 public class UVCpu {
        //TODO - finish cases, write junit tests
        //Part A area
        //while loop
        //split number into opcode and address
        public String testName = "";
        public Memory mem;

        //Using for testing
        public UVCpu() {
            this.mem = new Memory(); // You'll need to add this constructor in Memory
        }


     public UVCpu(String fileName) {
            this.testName = fileName;
            this.mem = new Memory(testName);
        }

        //Console con = new Console();

        int pc = 0, acc = 0; // program counter, accumulator
        boolean halted = false;
        public static final int overflowLimit = 9999; //Used to mark limit

        public void run() {
            int instruction = 0;
            int opcode = 0;
            int address = 0;
            while (!halted) {
                //checks for overflow
                if (Math.abs(acc) > overflowLimit) {
                    int sign;
                    if (acc < 0) {sign = -1;}
                    else {sign = 1;}
                    acc = sign * (Math.abs(acc) % 10000);
                }
                instruction = mem.read(pc); //read memory address at pc value into instruction
                address = instruction % 100; //modulo division to get last two digits
                opcode = instruction / 100; //int division to get first two digits
                //if division has problems just turn instruction into a string or char array instead

                int value = mem.read(address);
                switch (opcode) {
                    case 10: //read from keyboard, write to memory
                        mem.write(address, UVConsole.userInputInt());
                        break;
                    case 11: //write a word from location in memory to screen
                        System.out.println(mem.read(address));
                        break;
                    case 20: //load from memory into accumulator
                        acc = value;
                        break;
                    case 21: //store word from accumulator into location in memory
                        mem.write(address, acc);
                        break;
                    case 30: //Add case
                        acc = acc + value;
                        break;
                    case 31: //Subtract
                        acc = acc - value;
                        break;
                    case 32: //Divide
                        acc = acc / value;
                        break;
                    case 33: //Multiply
                        acc = acc * value;
                        break;
                    case 40: //branch
                        pc = address;
                        continue;
                    case 41: //branch if acc < 0
                        if (acc < 0) {
                            pc = address;
                            continue;
                        }
                        break;
                    case 42: //branch if acc == 0
                        if (acc == 0) {
                            pc = address;
                            continue;
                        }
                        break;
                    case 43: // Halt code, stop program
                        halted = true;
                        break;
                } //end sc

                pc++; //iterate the memory up by 1
            }
        }

}
