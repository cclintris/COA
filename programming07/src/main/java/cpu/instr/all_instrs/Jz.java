package cpu.instr.all_instrs;

import cpu.CPU_State;

// 如果ZF(0位)為1，則為偏移地址，將當前eip地址加上傳入immediate
// 如果ZF(1位)為, 則為小地偏移，將當前eip地址的值加上指令長度16

public class Jz implements Instruction{
    public int exec(String eip, int opcode) {
        eip = eip.substring(0,8); // 只有前8位有效
        if(CPU_State.eflag.read().charAt(6)=='1') {
            //從reg寄存器的第6位讀取出ZERO位是否開啟
            String base = CPU_State.eip.read();
            CPU_State.eip.write(alu.add(eip, base));
            return 0;
        }else {
            CPU_State.eip.write(alu.add(CPU_State.eip.read(), trans.intToBinary(String.valueOf(eip.length()+8))));
            return 16;
        }
    }
}