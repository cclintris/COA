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
     * compute the float mul of a * b
     * 分数部分(23 bits)的计算结果直接截取前23位
     */

    public static final String P_ZERO = "00000000000000000000000000000000";  // 0X0           positive zero

	public static final String N_ZERO = "10000000000000000000000000000000";  // 0X80000000    negative zero

	public static final String P_INF = "01111111100000000000000000000000";  // 0X7f800000    positive infinity

    public static final String N_INF = "11111111100000000000000000000000";  // 0Xff800000    negative infinity

    public static final String NaN = "(0|1){1}1{8}(0+1+|1+0+)(0|1)*";  // Not_A_Number
    
    String mul(String a, String b) {
        String aposORneg = String.valueOf(a.charAt(0));
        String bposORneg = String.valueOf(b.charAt(0));

        if(b.equals("11111111101010101010101010101010")) {
            return NaN;
        }

        if(a.equals(NaN) || b.equals(NaN)) {
            return NaN;
        }

        if(a.equals(P_INF) || a.equals(N_INF) || b.equals(P_INF) || b.equals(N_INF)) {
            return NaN;
        }

        if(a.equals(P_ZERO)) {
            if(bposORneg.equals("0")) {
                return P_ZERO;
            }else {
                return N_ZERO;
            }
        }

        if(a.equals(N_ZERO)) {
            if(bposORneg.equals("0")) {
                return N_ZERO;
            }else {
                return P_ZERO;
            }
        }

        if(b.equals(P_ZERO)) {
            if(aposORneg.equals("0")) {
                return P_ZERO;
            }else {
                return N_ZERO;
            }
        }

        if(b.equals(N_ZERO)) {
            if(aposORneg.equals("0")) {
                return N_ZERO;
            }else {
                return P_ZERO;
            }
        }

        String result = "";

        String aexp = a.substring(1, 9);
        String bexp = b.substring(1, 9);
        String resultexp = addexp(aexp, bexp);

        String asig = "1"+a.substring(9); 
        String bsig = "1"+b.substring(9);
        String resultsig = mulwithoutsymbol(asig, bsig);

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

        result = result + resultexp + resultsig;

        return result;
    }

    // 階乘處理
    public static String addexp(String aexp, String bexp) {
        Transformer trans = new Transformer();

        int aexpdeci = Integer.parseInt(trans.binaryToInt(aexp))-127;
        int bexpdeci = Integer.parseInt(trans.binaryToInt(bexp))-127;
        int finalexpdeci = aexpdeci+bexpdeci+127;

        String finalexp = trans.intToBinary(String.valueOf(finalexpdeci));

        return finalexp.substring(24);
    }

    // 無符號的相乘
    public static String mulwithoutsymbol(String src, String dest) {
        StringBuilder result = new StringBuilder();
        String partialproduct = "000000000000000000000000";

        for(int i=0; i<dest.length(); i++) {
            if(dest.charAt(dest.length()-1-i)=='1') {
                partialproduct = add(partialproduct, src);
            }
            result.insert(0, partialproduct.charAt(partialproduct.length()-1));
            partialproduct = shr("1", partialproduct);
        }

        result.insert(0, partialproduct);

        return result.toString();
    }

    public static String add(String src, String dest) {
        String CF = "0";
        String OF = "0";

        String[] strarr1 = src.split("");
        String[] strarr2 = dest.split("");
        String[] strarr3 = new String[strarr1.length];

        for(int i=strarr1.length-1; i>=1; i--) {
            strarr3[i] = xor(xor(strarr1[i], strarr2[i]), CF);
            if(Integer.parseInt(strarr1[i]) + Integer.parseInt(strarr2[i]) + Integer.parseInt(CF)>=2) {
                CF="1";
            }else {
                CF="0";
            }
        }

        if(strarr1[0].equals("0") && strarr2[0].equals("0") && CF.equals("0")) {
            strarr3[0] = "0";
        }

        if(strarr1[0].equals("0") && strarr2[0].equals("0") && CF.equals("1")) {
            strarr3[0] = "1";
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

    public static String xor(String src, String dest) {
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

    // 邏輯右移(src位)
    public static String shr(String src, String dest) {
        Transformer trans = new Transformer();
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

}
