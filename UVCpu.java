 public class UVCpu {
        //TODO - finish cases, write junit tests
        //Part A area
        //while loop
        //split number into opcode and address
        public String testName = "";
        public Memory mem;
        private UVSimGUI gui;

        //Using for testing
        public UVCpu() {
            this.mem = new Memory(); // You'll need to add this constructor in Memory
        }


         public UVCpu(String fileName, UVSimGUI gui) {
             this.testName = fileName;
             this.gui = gui;
             this.mem = new Memory(fileName, gui); // ✅ pass GUI into memory
         }

         //changed to 249 bc changed array size
        private void checkIndex(int address) {
             if (address > 249 || address < 0) {
                 throw new IndexOutOfBoundsException();
             }
         }

        int pc = 0, acc = 0; // program counter, accumulator
        boolean halted = false;
        public static final int overflowLimit = 999999; //Used to mark limit -- changed to 999999

        public void run() {
            MemoryRegister instruction;
            int opcode;
            int address;
            while (!halted) {
                //checks for overflow
                if (Math.abs(acc) > overflowLimit) {
                    int sign;
                    if (acc < 0) {sign = -1;}
                    else {sign = 1;}
                    acc = sign * (Math.abs(acc) % 1000000); //added to 0's because i'm not sure what this is doing but we changed the max num size
                }
                instruction = mem.read(pc); //read memory address at pc value into instruction
                address = instruction.getAddress(); //modulo division to get last two digits
                opcode = instruction.getOpCode(); //int division to get first two digits
                //if division has problems just turn instruction into a string or char array instead

                int value = mem.read(address).getValue();
                switch (opcode) {
                    case 10: //read from keyboard, write to memory
                        mem.write(address, gui.userInputInt());
                        break;
                    case 11: //write a word from location in memory to screen
                        gui.displayOutput(String.valueOf(mem.read(address).getValue()));
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
