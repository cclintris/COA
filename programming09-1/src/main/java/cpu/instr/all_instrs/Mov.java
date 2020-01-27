package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.registers.EFlag;
import memory.Memory;
import program.Log;

import java.lang.reflect.Array;

public class Mov implements Instruction{

    @Override
    public int exec(String eip, int opcode) {
        // 0xc7 MOV Ev, Iv
        if(opcode == 0xc7){
            int len = 80;
            String logicalAddr = CPU_State.cs.read() + eip;
            String wholeInstr = new String(mmu.read(logicalAddr, len));
            Log.write(wholeInstr); // 这个东西助教忘记加了
            String modRM = wholeInstr.substring(8, 16);
            String displacement = wholeInstr.substring(16, 16 + 32);
            String Iv = wholeInstr.substring(16 + 32);
            String registerData = modRM.substring(5).equals("011") ? CPU_State.ebx.read():  CPU_State.eax.read();
            String EvAddr = alu.add(registerData, displacement);
            Memory.getMemory().write(EvAddr, 32, Iv.toCharArray());
            CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
            return len;
        }
        // 0x8b MOV Gv, Ev
        else if(opcode == 0x8b){
            int len = 48;
            String logicalAddr = CPU_State.cs.read() + eip;
            String wholeInstr = new String(mmu.read(logicalAddr, len));
            Log.write(wholeInstr); // 这个东西助教忘记加了
            String modRM = wholeInstr.substring(8, 16);
            String displacement = wholeInstr.substring(16);
            String choosedRegisterNum = modRM.substring(2, 5);
            String registerData = modRM.substring(5).equals("011") ? CPU_State.ebx.read():  CPU_State.eax.read();
            String EvAddr = alu.add(registerData, displacement);
            String dataRegister = CPU_State.ds.read();
            switch (choosedRegisterNum) {
                case "000":
                    CPU_State.eax.write(new String(mmu.read(dataRegister + EvAddr, 32)));
                    break;
                case "001":
                    CPU_State.ecx.write(new String(mmu.read(dataRegister + EvAddr, 32)));
                    break;
                case "010":
                    CPU_State.edx.write(new String(mmu.read(dataRegister + EvAddr, 32)));
                    break;
                case "011":
                    CPU_State.ebx.write(new String(mmu.read(dataRegister + EvAddr, 32)));
                    break;
            }
            CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
            return len;
        }
        // 0x89 MOV Ev, Gv
        else if (opcode == 0x89){
            int len = 48;
            String logicalAddr = CPU_State.cs.read() + eip;
            String wholeInstr = new String(mmu.read(logicalAddr, len));
            Log.write(wholeInstr); // 这个东西助教忘记加了
            String modRM = wholeInstr.substring(8, 16);
            String displacement = wholeInstr.substring(16);
            String choosedRegisterNum = modRM.substring(2, 5);
            String registerData = modRM.substring(5).equals("011") ? CPU_State.ebx.read():  CPU_State.eax.read();
            String EvAddr = alu.add(registerData, displacement);
//            String dataRegister = CPU_State.ds.read();
            switch (choosedRegisterNum) {
                case "000":
                    Memory.getMemory().write(EvAddr, 32, CPU_State.eax.read().toCharArray());
                    break;
                case "001":
                    Memory.getMemory().write(EvAddr, 32, CPU_State.ecx.read().toCharArray());
                    break;
                case "010":
                    Memory.getMemory().write(EvAddr, 32, CPU_State.edx.read().toCharArray());
                    break;
                case "011":
                    Memory.getMemory().write(EvAddr, 32, CPU_State.ebx.read().toCharArray());
                    break;
            }
            CPU_State.eip.write(alu.add(eip, t.intToBinary(String.valueOf(len))));
            return len;
        }
        return 0;
    }
}
