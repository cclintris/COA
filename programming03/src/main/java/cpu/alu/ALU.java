package cpu.alu;

import transformer.Transformer;

/**
 * Arithmetic Logic Unit
 * ALU封装类
 * TODO: 加减乘除
 */
public class ALU {

	/**
	 * 返回两个二进制整数的乘积(结果直接截取后32位)
     * 要求使用布斯乘法计算
	 * @param src 32-bits
	 * @param dest 32-bits
	 * @return 32-bits
	 */

	String boothdigit="0";

	String mul (String src, String dest){
		String multiplicand = src; //被乘數
		String multiplier = dest;  //乘數
		String partialproduct = "00000000000000000000000000000000"; //部分積
		int count = multiplicand.length();
		Boolean flag;

		while(count>0) {
			flag = check(multiplier, boothdigit);
			if(flag==true) {
				arithShift(partialproduct, multiplier, boothdigit);
			}else {
				if(returncheck(multiplier, boothdigit).equals("01")) {
					partialproduct = add(partialproduct, multiplicand);
				}else if(returncheck(multiplier, boothdigit).equals("10")) {
					partialproduct = sub(partialproduct, multiplicand);
				}
			}
			String[] aftershiftarr = new String[3];
			aftershiftarr = arithShift(partialproduct, multiplier, boothdigit);
			partialproduct = aftershiftarr[0];
			multiplier = aftershiftarr[1];
			boothdigit = aftershiftarr[2];

			count--;
		}

        return multiplier;
	}

	public Boolean check(String multiplier, String boothdigit) {
		String boothstr = multiplier.charAt(multiplier.length()-1)+boothdigit;

		if(boothstr.equals("00") || boothstr.equals("11")) {
			return true;
		}else {
			return false;
		}
	}

	public static String returncheck(String multiplier, String boothdigit) {
		return multiplier.charAt(multiplier.length()-1)+boothdigit;
	}

	public static String add(String partialproduct, String multiplicand) {
		Transformer trans = new Transformer();

		partialproduct = trans.intToBinary(String.valueOf(Integer.parseInt(trans.binaryToInt(partialproduct)) + Integer.parseInt(trans.binaryToInt(multiplicand))));
		return partialproduct;
	}
	
	public static String sub(String partialproduct, String multiplicand) {
		Transformer trans = new Transformer();

		partialproduct = trans.intToBinary(String.valueOf(Integer.parseInt(trans.binaryToInt(partialproduct)) - Integer.parseInt(trans.binaryToInt(multiplicand))));
		return partialproduct;
	}

	public static String[] arithShift(String partialproduct, String multiplier, String boothdigit) {
		String[] result = new String[3];
		String combo = partialproduct+multiplier+boothdigit;

		String temp1 = String.valueOf(partialproduct.charAt(0));

		String temp2="";
		for(int i=0; i<combo.length()-1; i++) {
			temp2+=combo.charAt(i);
		}

		String aftershift = temp1+temp2;
		String newpartialproduct = aftershift.substring(0, 32);
		String newmultiplier = aftershift.substring(32, 64);
		String newboothdigit = aftershift.substring(64);

		result[0] = newpartialproduct;
		result[1] = newmultiplier;
		result[2] = newboothdigit;

		return result;
	}

}
