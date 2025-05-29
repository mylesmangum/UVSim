public class UVCpu {

    //Part A area












    public static final int overflowLimit = 9999; //Used to mark limit

    public Integer opcodeMath(int opcode, int address, Memory memory, int accumulator){

        int value = memory.read(address); //obtains value from the used address
        switch(opcode){ //switch to check through each opcode for correct one
            case 30: //Add case
                accumulator = accumulator + value;
                break;
            case 31: //Subtract
                accumulator = accumulator - value;
                break;
            case 32: //Divide
                accumulator = accumulator / value;
                break;
            case 33: //Multiply
                accumulator = accumulator * value;
                break;
            default: //If it is invalid
                System.out.println("Invalid opcode");
                return null;
        }

        //checks for overflow
        if(accumulator > overflowLimit || accumulator < -overflowLimit){
            throw new Overflow("Overflow limit exceeded. Value: " + accumulator);
        }

        return accumulator; //returns the value
    }

}
