package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.registers.EFlag;

// 帶進位的add
public class Adc implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        // opcode=0x15 ADC eAX,Iv
        int len = 40;
        String logicalAddr = CPU_State.cs.read() + eip;
        String wholeInstr = new String(mmu.read(logicalAddr, len));
        String Iv = wholeInstr.substring(8);
        String carry = ((EFlag) CPU_State.eflag).getCF() ? "01" : "00";
        CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
        CPU_State.eax.write(alu.add(alu.add(Iv,  CPU_State.eax.read()), carry));
        if(alu.isZero(CPU_State.eax.read())) ((EFlag) CPU_State.eflag).setZF(true);
        else ((EFlag) CPU_State.eflag).setZF(false);
        return len;
    }
}
