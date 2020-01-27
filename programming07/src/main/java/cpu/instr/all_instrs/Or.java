package cpu.instr.all_instrs;

import cpu.CPU_State;

public class Or implements Instruction{
    public int exec(String eip, int opcode) {
        String a = CPU_State.eax.read();
        eip = eip.substring(0,32);
        CPU_State.eax.write(alu.or(eip, a));
        return 40;
    }
}