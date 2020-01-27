package cpu.instr.all_instrs;

import cpu.CPU_State;
import util.BinaryIntegers;

// Sbb ax,dx  --->   ax = ax - dx - carry

public class Sbb implements Instruction{
    public int exec(String eip, int opcode) {
        String a = CPU_State.eax.read();
        eip = eip.substring(0,32);
        CPU_State.eax.write(alu.sub(BinaryIntegers.ONE, alu.sub(eip, a)));
        return 40;
    }
}