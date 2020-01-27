package cpu.alu;

import transformer.Transformer;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * TODO: 浮点数运算
 */
public class FPU {
    static String P_ZERO = "00000000000000000000000000000000";
    static String P_INF = "01111111100000000000000000000000";
    static String P_NAN = "01111111110000000000000000000000";
    static String N_NAN = "11111111110000000000000000000000";

    /**
     * compute the float add of (a + b)
     **/
    String add(String a,String b){
        if(a.equals(N_NAN) || b.equals(N_NAN)) {
            return N_NAN;
        }
        if(a.equals(P_NAN) || b.equals(P_NAN)) {
            return P_NAN;
        }
        
        return decimalNormalize(decimalAdd(a, b));
    }

    /**
     * compute the float add of (a - b)
     **/
    String sub(String a,String b){
        FPU fpu = new FPU();
        if(a.equals(b)) {
            return P_ZERO;
        }

        String[] strarr = new String[2];
        if(b.charAt(0)=='1') {
            strarr[0]="0";
        }else {
            strarr[0]="1";
        }

        strarr[1] = b.substring(1,32);

        String str="";
        for(String s: strarr) {
            str+=s;
        }


        return fpu.add(a, str);
    }

    public static String judgeLargerEx(String a, String b) {
        Transformer trans = new Transformer();

        if(a.substring(1, 9).equals(b.substring(1, 9))) {
            return "ab";
        }else {
            return Integer.parseInt(trans.binaryToInt(a.substring(1, 9)))>Integer.parseInt(trans.binaryToInt(b.substring(1, 9)))?a:b;
        }
    }

    public static String exponentNormalize(String a, String b) { //右移較小的數
        ALU alu = new ALU();
        Transformer trans = new Transformer();

        String operatorstr="";
        int refernum = Integer.parseInt(trans.binaryToInt(judgeLargerEx(a, b).substring(1, 9)));
        int operatornum=0;

        if(judgeLargerEx(a, b).equals(a)) {
            operatorstr = b;
            operatornum = Integer.parseInt(trans.binaryToInt(b.substring(1, 9)));
        }else {
            operatorstr = b;
            operatornum = Integer.parseInt(trans.binaryToInt(a.substring(1, 9)));
        }

        String exponentstr = operatorstr.substring(1, 9);
        String decimalstr = getDecimal(operatorstr, "1");

        while(operatornum<refernum) {
            decimalstr = alu.shr("00000000000000000000000000000001", decimalstr);
            exponentstr = alu.add(exponentstr, "00000001");
            operatornum++;
        }

        String[] resultarr = new String[3];
        resultarr[0] = String.valueOf(operatorstr.charAt(0));
        resultarr[1] = judgeLargerEx(a, b).substring(1, 9);
        resultarr[2] = decimalstr.substring(9, 36);

        String result="";
        for(String s:resultarr) {
            result+=s;
        }

        return result;
    }

    public static String getDecimal(String a, String mode) {
        Transformer trans = new Transformer();

        String[] decimalarr = new String[36]; //無須判斷符號位，若為負數，後面取補碼就好

        for(int i=0; i<8; i++) {
            decimalarr[i] = "0"; //設置前面的數
        }

        if(a.length()==32) {
            for(int i=32; i<36; i++) {
                decimalarr[i] = "0"; //設置保護位
            }
        }else {
            for(int i=32; i<36; i++) {
                decimalarr[i] = String.valueOf(a.charAt(i));
            }
        }
        
        if(mode.equals("1")) {
            if(Integer.parseInt(trans.binaryToInt(a.substring(1, 9)))==0) {
                decimalarr[8] = "0";
            }else {
                decimalarr[8] = "1"; //小數點前一位
            }
        }else {
            decimalarr[8] = "0";
        }

        for(int i=9; i<32; i++) {
            decimalarr[i] = String.valueOf(a.charAt(i));
        }

        String result="";
        for(String s: decimalarr) {
            result+=s;
        }

        return result;
    }

    public static String[] decimalAdd(String a, String b) {
        ALU alu = new ALU();

        String deciadder1 = "";
        String deciadder2 = "";
        String resultdecistr = "";
        String exponent = "";
        String sign = "0";

        if(judgeLargerEx(a, b).equals(a)) { //得到小數位
            deciadder1 = getDecimal(exponentNormalize(a, b), "2");
            deciadder2 = getDecimal(a, "1");
            exponent = a.substring(1, 9);
        }else if(judgeLargerEx(a, b).equals(b)){
            deciadder1 = getDecimal(exponentNormalize(a, b), "2");
            deciadder2 = getDecimal(b, "1");
            exponent = b.substring(1, 9);
        }else {
            deciadder1 = getDecimal(a, "1");
            deciadder2 = getDecimal(b, "1");
            exponent = a.substring(1, 9);
        } 

        if(a.charAt(0)==b.charAt(0) && a.charAt(0)=='0') { //都為正數
            resultdecistr = alu.add(deciadder1, deciadder2);
            sign = "0";
        }else if(a.charAt(0)==b.charAt(0) && a.charAt(0)=='1') { //都為負數
            resultdecistr = alu.add(deciadder1, deciadder2);
            sign = "1";
        }else {
            if(a.charAt(0)=='1' && judgeLargerEx(a, b).equals(a)) {
                deciadder2 = complement(deciadder2);
            }else if(a.charAt(0)=='1' && judgeLargerEx(a, b).equals(b)) {
                deciadder1 = complement(deciadder1);
            }else if(b.charAt(0)=='1' && judgeLargerEx(a, b).equals(a)) {
                deciadder1 = complement(deciadder1);
            }else {
                deciadder2 = complement(deciadder2);
            }

            resultdecistr = alu.add(deciadder1, deciadder2);
            sign = String.valueOf(resultdecistr.charAt(0));
        }

        String[] resultarr = new String[3];
        if(sign.equals("1")) {
            resultdecistr = complement(resultdecistr);
        }

        resultarr[0] = resultdecistr;
        resultarr[1] = sign;
        resultarr[2] = exponent;

        return resultarr;
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

    public static String decimalNormalize(String[] deciarr) {
        ALU alu = new ALU();
        Transformer trans = new Transformer();

        if(deciarr[0].charAt(7)=='1') {
            deciarr[0] = alu.shr("00000001", deciarr[0]);
            deciarr[2] = alu.add("00000001", deciarr[2]);
        }else if(deciarr[0].charAt(8)=='1') {

        }else {
            if(deciarr[0].substring(8, 36).equals("00000000000000000000000000000")) {

            }else {
                while(deciarr[0].charAt(8)!='1') {
                    deciarr[0] = alu.shl("00000001", deciarr[0]);
                    deciarr[2] = alu.sub("00000001", deciarr[2]);
                }
            }
        }

        String[] resultarr = new String[3];
        resultarr[0] = deciarr[1];
        if(Integer.parseInt(trans.binaryToInt(deciarr[2]))>=255) {
            return P_INF;
        }

        resultarr[1] = deciarr[2];
        resultarr[2] = deciarr[0].substring(9, 32);

        String result = "";
        for(String s:resultarr) {
            result+=s;
        }

        return result;
    }

}
