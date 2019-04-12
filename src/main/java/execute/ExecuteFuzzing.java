package execute;

import createfuzzing.GenerateFuzzing;
/**
 * 
 * @author mace
 *
 *
 */
public class ExecuteFuzzing {
	private String sqlstring = null;
	/**
	 * ִ���������������÷��صĽ����
	 * @param example
	 * @return String[]
	 */
	public static String[] execute(String example) {
		
		String str = example;
		String[] strlist = null;

		String searchname = "\""+str+"\""; 
		String commandstr ="java -jar C:\\Users\\mace\\Desktop\\sqltest.jar "+searchname;
		String tmp= Executecmd.Execmd(commandstr);
		strlist = tmp.split("\n");
	
		return strlist;
	}
	/**
	 * ִ��ģ������
	 */
	public void executeFuzzing() {
		int nm = 1;
		while(true) {
			String temp = GenerateFuzzing.generateCase();
			while(temp.length()<4) {
				temp = GenerateFuzzing.generateCase();
			}
			System.out.println("��"+nm+"��!");
			System.out.println("����������"+temp+"��");
			String[] strlist = execute(temp);
			if(strlist != null) {
				for(String ss:strlist) {
					System.out.println(ss);
					if(ss.contains("sqlע��")) {
						sqlstring = temp;
						System.out.println("����SQLע��ۼ���");
					}
				}
			}
			nm++;
		}
		
	}

}
