package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.registers.EFlag;

public class Jz implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        // opcode=0x74 JZ Jb
        int len = 16;
        String logicalAddr = CPU_State.cs.read() + eip;
        String wholeInstr = new String(mmu.read(logicalAddr, len));
        String offset = wholeInstr.substring(8);
        if(((EFlag) CPU_State.eflag).getZF()){
            eip = alu.add(eip, offset);
            len = 0;
        }
        CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
        return len;
    }
}
