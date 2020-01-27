package cpu.instr.all_instrs;

import cpu.CPU_State;
import program.Log;

public class Hlt implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        // opcode=0xf4 hlt
        int len = 8;
        String logicalAddr = CPU_State.cs.read() + eip;
        String wholeInstr = new String(mmu.read(logicalAddr, len));
        Log.write(wholeInstr); // 这个东西助教忘记加了
        //CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
        return -1;
    }
}
