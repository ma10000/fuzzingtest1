package creategene;

public class StringChangeGene {
	/* 将测试用例变换为01形式的ascii码表示每个字符 */
	public static String stringChangeGene(String example) {
		String result = "";
		
		char[] strchar = example.toCharArray();
		for(int i = 0;i<strchar.length;i++) {
			String bstr = Integer.toBinaryString(strchar[i]);
			int d = 8 - (int)(bstr.length());
			StringBuffer sb = new StringBuffer(bstr);
			for(int s = 0;s<d;s++) {
				sb.insert(0,'0');
			}
			result += sb.toString()+" ";
		}
		
		return result;
	}

	/* 将生成的ascii码变为格雷码 */
	public static String geneChangeGraycode(String geneascii) {
		String result = "";
		
		String[] reslist = geneascii.split(" ");
		StringBuffer bs2 = new StringBuffer();
		for(int i=0;i<reslist.length;i++) {
			
			bs2.append(reslist[i].substring(0,1));
			for(int m=1;m<=7;m++) {
				String temp1 = reslist[i].substring(m-1,m);
				String temp2 = reslist[i].substring(m,m+1);
				if(temp1.equals(temp2)) {
					bs2.append("0");
				}else {
					bs2.append("1");
				}
			}
		}
		result = bs2.toString();
		
		return result;
		
	}

	/* 将格雷码变为ascii码 */
	public static String graycodeChangeGene(String graycode) {
		String result = "";
		
		StringBuffer bs3 = new StringBuffer();
		String[] strlist = new String[graycode.length()/8];
		for(int i =0;i<strlist.length;i++) {
			strlist[i]=""+graycode.substring(i*8,i*8+8);
		}
		for(int i=0;i<strlist.length;i++) {
			bs3.append(strlist[i].substring(0,1));
			for(int m=1;m<=7;m++) {
				String temp1 = bs3.substring(i*8+i+m-1,i*8+i+m);
				String temp2 = strlist[i].substring(m,m+1);
				if(temp1.equals(temp2)) {
					bs3.append("0");
				}else {
					bs3.append("1");
				}
			}
			bs3.append(" ");
		}
		result = bs3.toString();
		
		return result;
		
	}

	/* 将基因ascii码转换为对应字符 */
	public static String geneChangeString(String gene) {
		String result = "";
		
		String[] reslist = gene.split(" ");
		char[] charlist = new char[reslist.length];
		for(int i=0;i<reslist.length;i++) {
			char[] tchar = reslist[i].toCharArray();
			int tnum = 0;
			for(int m=0;m<tchar.length;m++) {
				tnum += (int)(tchar[m]-48)*Math.pow(2,tchar.length-1-m);
			}
//			System.out.println(tnum);
			charlist[i]=(char)tnum;
		}
		result = String.valueOf(charlist);
//		System.out.println(result);
		
		return result;
	}

}
