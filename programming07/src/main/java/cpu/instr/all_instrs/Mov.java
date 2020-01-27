package cpu.instr.all_instrs;

import cpu.CPU_State;

public class Mov implements Instruction{
    public int exec(String eip, int opcode) {
        eip = eip.substring(0,32);
        CPU_State.eax.write(eip);
        return 40;
    }
}