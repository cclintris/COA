package cpu.instr.all_instrs;

import cpu.CPU_State;
import memory.*;

public class Push implements Instruction{
    public int exec(String eip, int opcode) {
        Memory mem = Memory.getMemory();
        mem.pushStack(CPU_State.esp.read(), CPU_State.ebx.read());
        return 8;
    }
}