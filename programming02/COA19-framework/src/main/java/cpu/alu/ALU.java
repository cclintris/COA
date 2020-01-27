package cpu.alu;

import transformer.Transformer;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 * TODO: 加减乘除
 */
public class ALU {
    Transformer trans = new Transformer();

    // 模拟寄存器中的进位标志位
    private String CF = "0";

    // 模拟寄存器中的溢出标志位
    private String OF = "0";

    //add two integer
    String add(String src, String dest) {
        ALU alu = new ALU();

        String[] strarr1 = src.split("");
        String[] strarr2 = dest.split("");
        String[] strarr3 = new String[strarr1.length];

        CF="0";

        for(int i=strarr1.length-1; i>=1; i--) {
            strarr3[i] = alu.xor(alu.xor(strarr1[i], strarr2[i]), CF);
            if(Integer.parseInt(strarr1[i]) + Integer.parseInt(strarr2[i]) + Integer.parseInt(CF)>=2) {
                CF="1";
            }else {
                CF="0";
            }
        }
        strarr3[0] = alu.xor(alu.xor(strarr1[0], strarr2[0]), CF);
        if(Integer.parseInt(strarr1[0]) + Integer.parseInt(strarr2[0]) + Integer.parseInt(CF) >=2) {
            OF="1";
        }else {
            OF="0";
        }

        String result="";
        for(String s: strarr3) {
            result+=s;
        }

        return result;
    }

    // 取反加一
    public static String complement(String src) {
        ALU alu = new ALU();
        String[] strarr1 = src.split("");
        String[] strarr2 = src.split("");

        for(int i=0; i<src.length(); i++) {
            if(strarr1[i].equals("1")) {
                strarr2[i]="0";
            }else {
                strarr2[i]="1";
            }
        }

        String str1="";
        for(String s:strarr2) {
            str1+=s;
        }

        String adder="";
        for(int i=0; i<src.length()-1; i++) {
            adder+="0";
        }
        adder+="1";

        return alu.add(adder, str1);
    }

    //sub two integer
    // dest - src
    String sub(String src, String dest) {
        return add(complement(src), dest);
	}

    //signed integer mod
    String imod(String src, String dest) {
        int i1 = Integer.parseInt(trans.binaryToInt(src));
        int i2 = Integer.parseInt(trans.binaryToInt(dest));

        int i = i2%i1;
        String s = trans.intToBinary(String.valueOf(i));
		return s;
    }

    String and(String src, String dest) {
        char[] c1 = src.toCharArray();
        char[] c2 = dest.toCharArray();
        char[] c = new char[c1.length];

        for(int i=0; i< c1.length; i++) {
            if(c1[i]=='1' && c2[i]=='1') {
                c[i]='1';
            } else {
                c[i]='0';
            }
        }

        String s = new String(c);
        return s;
    }

    String or(String src, String dest) {
        char[] c1 = src.toCharArray();
        char[] c2 = dest.toCharArray();
        char[] c = new char[c1.length];

        for(int i=0; i< c1.length; i++) {
            if(c1[i]=='1' || c2[i]=='1') {
                c[i]='1';
            } else {
                c[i]='0';
            }
        }

        String s = new String(c);
        return s;
    }

    String xor(String src, String dest) {
        char[] c1 = src.toCharArray();
        char[] c2 = dest.toCharArray();
        char[] c = new char[c1.length];

        for(int i=0; i< c1.length; i++) {
            if(c1[i]!=c2[i]) {
                c[i]='1';
            } else if(c1[i] == c2[i]){
                c[i]='0';
            }
        }

        String s = new String(c);
        return s;
    }

    // 邏輯左移(src位)
    String shl(String src, String dest) {
        int num = Integer.parseInt(trans.binaryToInt(src));
        String[] strarr = dest.split("");

        while(num>0) {
            for(int i=0; i<dest.length()-1; i++) {
                strarr[i] = strarr[i+1];
            }
            strarr[dest.length()-1]="0";
            num--;
        }

        String str="";
        for(String s: strarr) {
            str+=s;
        }
        return str;
    }

    // 邏輯右移(src位)
    String shr(String src, String dest) {
        int num = Integer.parseInt(trans.binaryToInt(src));
        String[] strarr = dest.split("");

        while(num>0) {
            for(int i=dest.length()-1; i>0; i--) {
                strarr[i] = strarr[i-1];
            }
            strarr[0]="0";
            num--;
        }

        String str="";
        for(String s: strarr) {
            str+=s;
        }

        return str;
    }

    String sal(String src, String dest) {
        int biased = Integer.parseInt(trans.binaryToInt(src));

        if(biased>=32) {
            return "00000000000000000000000000000000";
        }
        
        String s = dest.substring(biased);

        while(s.length()<32) {
            s=s+'0';
        }

        return s;
    }

    String sar(String src, String dest) {
        int i1 = Integer.parseInt(trans.binaryToInt(src));
        int i2 = Integer.parseInt(trans.binaryToInt(dest));

        int i = i2/(i1*2);

        String s = trans.intToBinary(String.valueOf(i));

        return s;
    }

    String rol(String src, String dest) {
        int i1 = Integer.parseInt(trans.binaryToInt(src));

        String s1 = dest.substring(0, i1);
        String s2 = dest.substring(i1);
        String s = s2+s1;
		return s;
    }

    String ror(String src, String dest) {
        int i1 = Integer.parseInt(trans.binaryToInt(src));

        String s1 = dest.substring(dest.length()-i1);
        String s2 = dest.substring(0, dest.length()-i1);
        String s = s1+s2;
		return s;
    }

}
