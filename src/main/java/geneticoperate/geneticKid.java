package geneticoperate;

import java.util.ArrayList;

import population.Individual;
/**
 * ������һ���� ��
 * @author mace
 *
 */
public class geneticKid {
	/**
	 * ��¡����
	 * @param parent
	 * @return Individual
	 */
	public static Individual clone(Individual parent) {
		Individual idkid = new Individual();
		/* ���ϴ��Ļ��򡢷�����·�������Ƹ��´� */
		idkid.setGene(parent.getGene());
		idkid.setScore(parent.getScore());
		idkid.setRoute(parent.getRoute());
		
		return idkid;
	}
	/**
	 * ͨ����ĸ�Ļ��򽻻��ĵ�����
	 * @param father
	 * @param mother
	 * @return ArrayList<Individual>
	 */
	public static ArrayList<Individual> geneticChild(Individual father,Individual mother){
		ArrayList<Individual> list = new ArrayList<Individual>();
		/* ��ȡ��ĸ�Ļ������� */
		String fathergene = father.getGene();
		String mothergene = mother.getGene();
		/* ��ȡ�϶̵Ļ������еĳ��� */
		int len = (fathergene.length()>mothergene.length())?mothergene.length():fathergene.length();
		/* �õ���ĸ�л������н϶̵Ķ�Ӧ���ַ��ĳ��� */
		int charnum = len/8;
		/* �����������������ȥ����֮��Ļ�����н����� */
		int a = ((int) (Math.random()*(charnum)))%charnum;
		int b = ((int) (Math.random()*(charnum)))%charnum;
		/* ʹaΪ��ֵ */
		if(a<b) {
			int temp = a;
			a = b;
			b = temp;
		}
		/* ��ȡ��ĸ�����ڽ����Ļ��� */
		String fchildgene = fathergene.substring(b*8,a*8);
		String mchildgene = mothergene.substring(b*8,a*8);
		/* ����ĸ����תΪStringBuffer���� */
		StringBuffer sbfgene = new StringBuffer(fathergene);
		StringBuffer sbmgene = new StringBuffer(mothergene);
		/* �������� */
		sbfgene.replace(b*8,a*8,mchildgene);
		sbmgene.replace(b*8,a*8,fchildgene);
		/* ���ɺ��� */
		Individual kid1 = new Individual();
		Individual kid2 = new Individual();
		kid1.setGene(String.valueOf(sbfgene));
		kid2.setGene(String.valueOf(sbmgene));
		
		list.add(kid1);
		list.add(kid2);
		
		return list;
	}
	

}
