package cpu.instr.all_instrs;

import cpu.alu.ALU;

import transformer.*;

public interface Instruction {

    int exec(String eip, int opcode);

    ALU alu = new ALU();

    Transformer trans = new Transformer();
}
