package cpu.instr.all_instrs;

import cpu.CPU_State;
import memory.Memory;

public class Push implements Instruction{
    Memory memory = Memory.getMemory();
    //opcode=0x53 PUSH eBX
    @Override
    public int exec(String eip, int opcode) {
        int len = 8;
        CPU_State.esp.write(alu.add("01", CPU_State.esp.read()));
//        String logicalAddr = CPU_State.cs.read() + eip;
//        String wholeInstr = new String(mmu.read(logicalAddr, len));
        memory.pushStack(CPU_State.esp.read(), CPU_State.ebx.read());
        CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
        return len;
    }
}
