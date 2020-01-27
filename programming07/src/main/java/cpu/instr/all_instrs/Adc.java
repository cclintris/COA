package cpu.instr.all_instrs;

import cpu.CPU_State;
import util.BinaryIntegers;

public class Adc implements Instruction{
    public int exec(String eip, int opcode) {
        String a = CPU_State.eax.read();
        eip = eip.substring(0,32);

        if(CPU_State.eflag.read().charAt(0)!='1') {
            // 判斷是否進位
            CPU_State.eax.write(alu.add(a, eip));
        }else {
            CPU_State.eax.write(alu.add(BinaryIntegers.ONE, alu.add(a, eip)));
        }
        return 40;
    }
}