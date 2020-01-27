package cpu;

import cpu.instr.all_instrs.InstrFactory;
import cpu.instr.all_instrs.Instruction;
import transformer.Transformer;

public class CPU {

    Transformer transformer = new Transformer();
    MMU mmu = MMU.getMMU();


    /**
     * execInstr specific numbers of instructions
     *
     * @param number numbers of instructions
     */
    public int execInstr(long number) {
        // 执行过的指令的总长度
        int totalLen = 0;
        while (number > 0) {
            totalLen += execInstr();
            number --;
        }
        return totalLen;
    }

    /**
     * execInstr a single instruction according to eip value
     */
    private int execInstr() {
        String eip = CPU_State.eip.read();
        int len = decodeAndExecute(eip);
        return len;
    }

    private int decodeAndExecute(String eip) {
        int opcode = instrFetch(eip, 1);
        Instruction instruction = InstrFactory.getInstr(opcode);

        //這個用於判斷是否有了這個instruction類，也就是要自己寫的那些什麼add.java之類的
        assert instruction != null;

        //exec the target instruction
        int len = instruction.exec(eip, opcode);
        return len;
    }

    /**
     * @param eip
     * @param length opcode的字节数，本作业只使用单字节opcode
     * @return
     */

     // cs寄存器存的就是包含指令起始的那個段在mem中的段號
    private int instrFetch(String eip, int length) {
        // 没有前缀的话就是说前八个字就是opcode
        String logicalAddr = CPU_State.cs.read().substring(3) + "000" + eip; //段號 + 段內偏移量(eip)
        String possible_opcode = new String(mmu.read(logicalAddr, 8));
        return Integer.parseInt(possible_opcode, 2);
    }


    public void execUntilHlt(){
        while (execInstr() != -1) {
            continue;
        }
    }

}

