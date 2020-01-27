package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.registers.EFlag;
import program.Log;

public class Cmp implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        // opcode=0x3D CMP eAX,Iv
        if(opcode == 0x3D) {
            int len = 40;
            String logicalAddr = CPU_State.cs.read() + eip;
            String wholeInstr = new String(mmu.read(logicalAddr, len));
            String Iv = wholeInstr.substring(8);
            String temp = alu.sub(Iv, CPU_State.eax.read());
            CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
            if (alu.isZero(temp)) ((EFlag) CPU_State.eflag).setZF(true);
            else ((EFlag) CPU_State.eflag).setZF(false);
            if (temp.charAt(0) == '1') ((EFlag) CPU_State.eflag).setSF(true);
            else ((EFlag) CPU_State.eflag).setSF(false);
            return len;
        }
        // 0x39 CMP Ev, Gv
        else if(opcode == 0x39){
            // 这里好像不加displacement？？
            int len = 16;
            String logicalAddr = CPU_State.cs.read().substring(3) + "000" + eip;
            String wholeInstr = new String(mmu.read(logicalAddr, len));
            Log.write(wholeInstr); // 这个东西助教忘记加了
            String modRM = wholeInstr.substring(8, 16);
//            String displacement = wholeInstr.substring(16);
            String choosedRegisterNum = modRM.substring(2, 5);
            String registerData = CPU_State.ebx.read();
//            String EvAddr = alu.add(registerData, displacement);
            String EvAddr = registerData;
            String dataRegister = CPU_State.cs.read();
            String Gv = "";
            String Ev = new String(mmu.read(dataRegister + EvAddr, 32));
            switch (choosedRegisterNum) {
                case "000":
                    Gv = CPU_State.eax.read();
                    break;
                case "001":
                    Gv = CPU_State.ecx.read();
                    break;
                case "010":
                    Gv = CPU_State.edx.read();
                    break;
                case "011":
                    Gv = CPU_State.ecx.read();
                    break;
            }
            CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
            String temp = alu.sub(Gv, Ev);
            if (alu.isZero(temp)) ((EFlag) CPU_State.eflag).setZF(true);
            else ((EFlag) CPU_State.eflag).setZF(false);
            if (temp.charAt(0) == '1') ((EFlag) CPU_State.eflag).setSF(true);
            else ((EFlag) CPU_State.eflag).setSF(false);
            return len;
        }
        return 0;
    }
}
