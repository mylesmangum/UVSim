public class UVCpu {

    //Part A area
    //TODO - finish cases, write junit tests

    //Part A area
    //while loop
    //split number into opcode and address

    Memory mem = new Memory(UVConsole.getFile());

    //Console con = new Console();

    int pc = 0, acc = 0; // program counter, accumulator
    boolean halted = false;


    public void run(){
        int instruction = 0;
        int opcode = 0;
        int address = 0;
        while(!halted){
            instruction = mem.read(pc); //read memory address at pc value into instruction
            address = instruction % 100; //modulo division to get last two digits
            opcode = instruction / 100; //int division to get first two digits
            //if division has problems just turn instruction into a string or char array instead
            int value = mem.read(address);
            switch(opcode){
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
                case 30: //30-33 math ops, hook up with function below
                    acc = acc + value;
                    break;
                case 31:
                    acc = acc - value;
                    break;
                case 32:
                    acc = acc / value;
                    break;
                case 33:
                    acc = acc * value;
                    break;
                case 40: //branch
                    pc = address;
                    continue;

                case 41: //branch if acc < 0
                    if(acc < 0) {
                        pc = address;
                        continue;
                    }
                    break;
                case 42: //branch if acc == 0
                    if(acc == 0){
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

    //might want to just integrate math functions into run loop

    public static final int overflowLimit = 9999; //Used to mark limit


}