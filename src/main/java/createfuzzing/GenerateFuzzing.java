package createfuzzing;
import java.util.*;
public class GenerateFuzzing {
	public  static int number =0;
	public static String generateCase() {
		StringBuffer br = new StringBuffer();
		StringBuffer br1 = new StringBuffer();
		
		int num=(int)(Math.random()*49)+1;
		//System.out.println(num);
		for(int i=0;i<num;i++) {
			int charnum=(int)(Math.random()*94)+32;
			char a = (char)charnum;
			char tem1 = (char)92;
			char tem2 = (char)34;
			while(a==tem1||a==tem2) {
				int cnum=(int)(Math.random()*94)+32;
				a = (char)cnum;
			}
			
			//System.out.println(charnum);
			
			
			br.append(String.valueOf(a));
			
			//System.out.println(a);
			
		}
		number++;
		//br1 = translationChar(br);
		//System.out.println(br1.toString());
		return br.toString();
		
	}
	
	public static String translationChar(String br) {
		StringBuffer result = new StringBuffer();
		StringBuffer sb = new StringBuffer(br);
		int length = sb.length();
		System.out.println(length);
		int temp=0;
		char[] charstr = {77,92,34,55};
		for(int i=0;i<charstr.length;i++) {
			char a = charstr[i];
			System.out.println(a);
			char tem1 = (char)92;
			char tem2 = (char)34;
			
			if(a==tem1) {
				result.append(String.valueOf(tem1));
//				result.append(String.valueOf(tem1));
				result.append(String.valueOf(a));
				
			}else if(a==tem2) {
				result.append(String.valueOf(tem1));
				result.append(String.valueOf(tem1));
				result.append(String.valueOf(a));
				
			}
			else {
				result.append(String.valueOf(a));
			}
			System.out.println(result.toString());
			//System.out.println(a);
			
		}
		
		return result.toString();
	}



}
