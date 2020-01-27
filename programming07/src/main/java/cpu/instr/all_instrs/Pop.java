package cpu.instr.all_instrs;

import cpu.CPU_State;
import memory.*;

public class Pop implements Instruction{
    public int exec(String eip, int opcode) {
        Memory mem = Memory.getMemory();
        String value = mem.topOfStack(CPU_State.esp.read());
        switch(opcode) {
            case 88:
                CPU_State.eax.write(value);
                break;
            case 89:
                CPU_State.ecx.write(value);
                break;
            case 90:
                CPU_State.edx.write(value);
                break;
        }
        return 8;
    }
}