package cpu.alu;

import java.util.ArrayList;

public class NBCDU {

	// 模拟寄存器中的进位标志位
	private String CF = "0";

	// 模拟寄存器中的溢出标志位
	private String OF = "0";

	/**
	 *
	 * @param a A 32-bits NBCD String
	 * @param b A 32-bits NBCD String
	 * @return a + b 
	 */
	String add(String a, String b) {
		String aposORneg = a.substring(0,4);
		String bposORneg = b.substring(0,4);
		StringBuilder result = new StringBuilder();

		ArrayList<String> a1 = new ArrayList<>();
		ArrayList<String> b1 = new ArrayList<>();

		for(int i=4; i<=28 ;i=i+4) {
			a1.add(a.substring(i, i+4));
		}

		for(int i=4; i<=28; i=i+4) {
			b1.add(b.substring(i, i+4));
		}

		int atempi=0;
		for(int i=0; i<a1.size(); i++) {
			for(int j=0; j<4; j++) {
			    atempi += Integer.parseInt(String.valueOf(a1.get(i).charAt(j))) * (int)Math.pow(2, 3-j);
			}
			a1.set(i, String.valueOf(atempi));
			atempi=0;
		}

		int btempi=0;
		for(int i=0; i<b1.size(); i++) {
			for(int j=0; j<4; j++) {
			    btempi += Integer.parseInt(String.valueOf(b1.get(i).charAt(j))) * (int)Math.pow(2, 3-j);
			}
			b1.set(i, String.valueOf(btempi));
			btempi=0;
		}

		String deciastr="";
		for(int i=0; i<a1.size(); i++) {
			deciastr+=a1.get(i);
		}

		String decibstr="";
		for(int i=0; i<b1.size(); i++) {
			decibstr+=b1.get(i);
		}

		int deciaint = Integer.parseInt(deciastr);
		int decibint = Integer.parseInt(decibstr);

		int decisum = 0;
		if(aposORneg.equals(bposORneg)) {
			if(aposORneg.equals("1100") && bposORneg.equals("1100")) {
				result.append("1100");
			}else if(aposORneg.equals("1101") &&bposORneg.equals("1101")) {
				result.append("1101");
			}

			decisum = deciaint+decibint;

			String decisumstr = String.valueOf(decisum);
			char[] decisumch = decisumstr.toCharArray();

			String valuestr="";
			for(int i=0; i<decisumch.length; i++) {
				String tempbi = Integer.toBinaryString(Integer.parseInt(String.valueOf(decisumch[i])));
				while(tempbi.length()<4) {
					tempbi="0"+tempbi;
				}
				valuestr+=tempbi;
			}

			for(int i=0; i<28-valuestr.length(); i++) {
				result.append("0");
			}
			result.append(valuestr);
		}else if(!aposORneg.equals(bposORneg)) {
			if(aposORneg.equals("1100") && bposORneg.equals("1101")) {
				decisum = deciaint-decibint;
			}else if(aposORneg.equals("1101") && bposORneg.equals("1100")) {
				decisum = -deciaint+decibint;
			}

			if(decisum<0) {
				result.append("1101");
			}else if(decisum>0) {
				result.append("1100");
			}

			if(decisum<0) {
				decisum=-decisum;
			}

			String decisumstr = String.valueOf(decisum);
			char[] decisumch = decisumstr.toCharArray();

			String valuestr="";
			for(int i=0; i<decisumch.length; i++) {
				String tempbi = Integer.toBinaryString(Integer.parseInt(String.valueOf(decisumch[i])));
				while(tempbi.length()<4) {
					tempbi="0"+tempbi;
				}
				valuestr+=tempbi;
			}

			for(int i=0; i<28-valuestr.length(); i++) {
				result.append("0");
			}
			result.append(valuestr);

		}
		
		if(result.length()!=32) {
			result.delete(4, 8);
		}

		return result.toString();
	}

	/***
	 *
	 * @param a A 32-bits NBCD String
	 * @param b A 32-bits NBCD String
	 * @return b - a
	 */
	String sub(String a, String b) {
		if(a.equals(b)) {
			return "11000000000000000000000000000000";
		}

		StringBuilder complea = new StringBuilder(a);
		complea.delete(0, 4);
		complea.insert(0, "1101");
		String result = add(complea.toString(), b);
		return result;
	}

}
