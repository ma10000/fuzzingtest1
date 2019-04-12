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
	 * 执行命令行命令，并获得返回的结果。
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
	 * 执行模糊测试
	 */
	public void executeFuzzing() {
		int nm = 1;
		while(true) {
			String temp = GenerateFuzzing.generateCase();
			while(temp.length()<4) {
				temp = GenerateFuzzing.generateCase();
			}
			System.out.println("第"+nm+"个!");
			System.out.println("测试用例："+temp+"。");
			String[] strlist = execute(temp);
			if(strlist != null) {
				for(String ss:strlist) {
					System.out.println(ss);
					if(ss.contains("sql注入")) {
						sqlstring = temp;
						System.out.println("发现SQL注入痕迹！");
					}
				}
			}
			nm++;
		}
		
	}

}
