package cpu.instr.all_instrs;

import cpu.CPU_State;
import memory.Memory;

public class Pop implements Instruction{
    Memory memory = Memory.getMemory();
    @Override
    public int exec(String eip, int opcode) {
        int len = 8;
        String logicalAddr = CPU_State.cs.read() + eip;
        String wholeInstr = new String(mmu.read(logicalAddr, len));
        // opcode=0x58 POP eAX
        if(Integer.parseInt(wholeInstr, 2) == 0x58){
            CPU_State.eax.write(memory.topOfStack(CPU_State.esp.read()));
        }
        // opcode=0x59 POP eCX
        else if(Integer.parseInt(wholeInstr, 2) == 0x59){
            CPU_State.ecx.write(memory.topOfStack(CPU_State.esp.read()));
        }
        // opcode=0x5a POP eDX
        else if(Integer.parseInt(wholeInstr, 2) == 0x5a){
            CPU_State.edx.write(memory.topOfStack(CPU_State.esp.read()));
        }
        CPU_State.esp.write(alu.sub("01", CPU_State.esp.read()));
        CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
        return len;
    }
}
