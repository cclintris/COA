package cpu.instr.all_instrs;

import cpu.MMU;
import cpu.alu.ALU;
import program.Log;
import transformer.Transformer;

public interface Instruction {
    ALU alu = new ALU();
    MMU mmu = MMU.getMMU();
    Transformer t = new Transformer();
    int exec(String eip, int opcode);
    default void toBinaryStr(String s){
        Log.write(s);
    }
}
