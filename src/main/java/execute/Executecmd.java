package execute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Executecmd {
	/**
	 * ÷¥––√¸¡Ó
	 * @param commandstr
	 * @return String 
	 */
	public static String Execmd(String commandstr) {
		String outline = null;
		BufferedReader br = null;
		try {
			
			Process p =Runtime.getRuntime().exec(commandstr);
			br = new BufferedReader(new InputStreamReader(p.getInputStream(),Charset.forName("GBK")));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line=br.readLine())!=null) {
				sb.append(line+"\n");
			}
			outline = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return outline;
	}

}
