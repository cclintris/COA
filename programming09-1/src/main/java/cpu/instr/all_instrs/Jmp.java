package cpu.instr.all_instrs;

import cpu.CPU_State;
import program.Log;

public class Jmp implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        // opcode=0xeb JMP rel8
        int len = 16;
        String logicalAddr = CPU_State.cs.read() + eip;
        String wholeInstr = new String(mmu.read(logicalAddr, len));
        Log.write(wholeInstr); // 这个东西助教忘记加了
        String offset = wholeInstr.substring(8);
        eip = alu.add(eip, offset);
        CPU_State.eip.write(eip);
        return 0;
    }
}
