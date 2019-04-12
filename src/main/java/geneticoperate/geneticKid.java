package geneticoperate;

import java.util.ArrayList;

import population.Individual;
/**
 * 生成下一代的 类
 * @author mace
 *
 */
public class geneticKid {
	/**
	 * 克隆方法
	 * @param parent
	 * @return Individual
	 */
	public static Individual clone(Individual parent) {
		Individual idkid = new Individual();
		/* 将上代的基因、分数和路径都复制给下代 */
		idkid.setGene(parent.getGene());
		idkid.setScore(parent.getScore());
		idkid.setRoute(parent.getRoute());
		
		return idkid;
	}
	/**
	 * 通过父母的基因交换的到子类
	 * @param father
	 * @param mother
	 * @return ArrayList<Individual>
	 */
	public static ArrayList<Individual> geneticChild(Individual father,Individual mother){
		ArrayList<Individual> list = new ArrayList<Individual>();
		/* 获取父母的基因序列 */
		String fathergene = father.getGene();
		String mothergene = mother.getGene();
		/* 获取较短的基因序列的长度 */
		int len = (fathergene.length()>mothergene.length())?mothergene.length():fathergene.length();
		/* 得到父母中基因序列较短的对应的字符的长度 */
		int charnum = len/8;
		/* 生成随机的两个数，去两数之间的基因进行交换。 */
		int a = ((int) (Math.random()*(charnum)))%charnum;
		int b = ((int) (Math.random()*(charnum)))%charnum;
		/* 使a为大值 */
		if(a<b) {
			int temp = a;
			a = b;
			b = temp;
		}
		/* 获取父母中用于交换的基因 */
		String fchildgene = fathergene.substring(b*8,a*8);
		String mchildgene = mothergene.substring(b*8,a*8);
		/* 将父母基因转为StringBuffer类型 */
		StringBuffer sbfgene = new StringBuffer(fathergene);
		StringBuffer sbmgene = new StringBuffer(mothergene);
		/* 交换基因 */
		sbfgene.replace(b*8,a*8,mchildgene);
		sbmgene.replace(b*8,a*8,fchildgene);
		/* 生成孩子 */
		Individual kid1 = new Individual();
		Individual kid2 = new Individual();
		kid1.setGene(String.valueOf(sbfgene));
		kid2.setGene(String.valueOf(sbmgene));
		
		list.add(kid1);
		list.add(kid2);
		
		return list;
	}
	

}
