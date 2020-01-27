package cpu.instr.all_instrs;

import cpu.CPU_State;

// 計算當前immediate - eax寄存器中的數

public class Cmp implements Instruction{
    public int exec(String eip, int opcode) {
        String a = CPU_State.eax.read();
        eip = eip.substring(0,32);
        CPU_State.eax.write(alu.sub(a, eip));
        return 40;
    }
}