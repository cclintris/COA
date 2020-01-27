package cpu.alu;

import transformer.Transformer;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用4位保护位进行计算，计算完毕直接舍去保护位
 * TODO: 浮点数运算
 */
public class FPU {

    /**
     * compute the float mul of a / b
     */
    public static final String P_ZERO = "00000000000000000000000000000000";  // 0X0           positive zero

	public static final String N_ZERO = "10000000000000000000000000000000";  // 0X80000000    negative zero

	public static final String P_INF = "01111111100000000000000000000000";  // 0X7f800000    positive infinity

	public static final String N_INF = "11111111100000000000000000000000";  // 0Xff800000    negative infinity

    public static final String NaN = "(0|1){1}1{8}(0+1+|1+0+)(0|1)*";  // Not_A_Number

    Transformer trans = new Transformer();
    
    String div(String a, String b) {
        String aposORneg = String.valueOf(a.charAt(0));
        String bposORneg = String.valueOf(b.charAt(0));
        String result="";

        if((a.equals(P_ZERO) && b.equals(P_ZERO)) || a.equals(P_ZERO) && b.equals(N_ZERO) || a.equals(N_ZERO) && b.equals(P_ZERO) || a.equals(N_ZERO) && b.equals(N_ZERO)) {
            return NaN;
        }

        if(b.equals(P_ZERO) || b.equals(N_ZERO)) {
            throw new ArithmeticException();
        }

        if(a.equals(P_ZERO)) {
            return P_ZERO;
        }
        if(a.equals(N_ZERO)) {
            return N_ZERO;
        }

        if(a.equals(P_INF) && b.charAt(0)=='0') {
            return P_INF;
        }else if(a.equals(P_INF) && b.charAt(0)=='1') {
            return N_INF;
        }

        if(a.equals(N_INF) && b.charAt(0)=='0') {
            return N_INF;
        }else if(a.equals(N_INF) && b.charAt(0)=='1') {
            return P_INF;
        }

        String aexp = a.substring(1, 9);
        String bexp = b.substring(1, 9);
        String resultexp = subexp(aexp, bexp);

        String asig = "1"+a.substring(9);
        String bsig = "1"+b.substring(9);
        String resultsig = divwithoutsymbol(asig, bsig);

        // 取第一個找到的'1'緊接的23位
        for(int i=0; i<resultsig.length(); i++) {
            if(resultsig.charAt(i)=='1') {
                resultsig = resultsig.substring(i+1, i+24);
                break;
            }
        }

        if((aposORneg.equals("0") && bposORneg.equals("1")) || (aposORneg.equals("1") &&bposORneg.equals("0"))) {
            result="1";
        }else {
            result="0";
        }

        result = result+resultexp+resultsig;

        return result;
    }

    String subexp(String aexp, String bexp) {
        int aexpdeci = Integer.parseInt(trans.binaryToInt(aexp))-127;
        int bexpdeci = Integer.parseInt(trans.binaryToInt(bexp))-127;

        int tempexpdeci = aexpdeci-bexpdeci+127;
        String finalexp = trans.intToBinary(String.valueOf(tempexpdeci));

        return finalexp.substring(24);
    }

    // 無符號整數的除法
    String divwithoutsymbol(String asig, String bsig) {
        StringBuilder finalsig = new StringBuilder();
        String tempsigA = asig+"0000"; // 四位保護位
        String temp="";
        String asigforward="";
        String asigbehind="";

        for(int i=0; i<asig.length(); i++) {
            asigforward = tempsigA.substring(0, 24);
            asigbehind = tempsigA.substring(24);
            temp = sub(bsig, asigforward);   //asigforward - bsig
            if(temp.charAt(0)=='1') {
                finalsig.append("0");
            }else {
                finalsig.append("1");
                tempsigA = temp+asigbehind;
            }
            tempsigA = shl("1", tempsigA);
        }

        return finalsig.toString();
    }

    //sub two integer
    // dest - src
    String sub(String src, String dest) {

        return add(complement(src), dest);
	}

    //add two integer
    String add(String src, String dest) {

        String[] strarr1 = src.split("");
        String[] strarr2 = dest.split("");
        String[] strarr3 = new String[strarr1.length];

        String CF="0";
        String OF="0";

        for(int i=strarr1.length-1; i>=1; i--) {
            strarr3[i] = xor(xor(strarr1[i], strarr2[i]), CF);
            if(Integer.parseInt(strarr1[i]) + Integer.parseInt(strarr2[i]) + Integer.parseInt(CF)>=2) {
                CF="1";
            }else {
                CF="0";
            }
        }
        strarr3[0] = xor(xor(strarr1[0], strarr2[0]), CF);
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
    String complement(String src) {
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

        return add(adder, str1);
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
        int num = Integer.parseInt(src);
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

}
