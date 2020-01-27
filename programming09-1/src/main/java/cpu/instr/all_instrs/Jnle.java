package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.registers.EFlag;
import program.Log;

public class Jnle implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        // 0x7f	JG/JNLE rel8
        int len = 16;
        String logicalAddr = CPU_State.cs.read() + eip;
        String wholeInstr = new String(mmu.read(logicalAddr, len));
        Log.write(wholeInstr); // 这个东西助教忘记加了
        String offset = wholeInstr.substring(8);
        if(((EFlag) CPU_State.eflag).getSF() == ((EFlag) CPU_State.eflag).getOF() && !((EFlag) CPU_State.eflag).getZF()){
            eip = alu.add(eip, offset);
            len = 0;
        }
        CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
        return len;
    }
}
