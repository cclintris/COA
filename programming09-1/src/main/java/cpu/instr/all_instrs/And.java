package cpu.instr.all_instrs;

import cpu.CPU_State;

public class And implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        // opcode=0x25 AND eAX,Iv
        int len = 40;
        String logicalAddr = CPU_State.cs.read().substring(3) + "000" + eip;
        String wholeInstr = new String(mmu.read(logicalAddr, len));
        String Iv = wholeInstr.substring(8);
        CPU_State.eax.write(alu.and(Iv,  CPU_State.eax.read()));
        CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
        return len;
    }
}
