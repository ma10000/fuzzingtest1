package geneticoperate;

import java.util.ArrayList;

import creategene.StringChangeGene;
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

	/**
	 * ���ݲ�ͬ�ķ�ʽ������һ������
	 * @param type  ��ʽ���
	 * @param father  ���׸���
	 * @param mother  ĸ�׸���
	 * @return   ��ĸ���嵮���ĺ�������
	 */
	public static ArrayList<Individual> geneticChild(int type,Individual father,Individual mother){
		ArrayList<Individual> childlist = new ArrayList<Individual>();
		String fathergene = father.getGene();
		String mothergene = mother.getGene();
		if(type==0){
			childlist =geneticChild(father,mother);
		}else if(type==1){
			int fgenelength = fathergene.length();
			int mgenelength = mothergene.length();
			int fgenehalf = (int)(fgenelength/8/2);
			int mgenehalf = (int)(mgenelength/8/2);
			int swapnum = (int)(Math.random()*2);
			if(swapnum==0){
				StringBuffer fsb = new StringBuffer(fathergene);
				StringBuffer msb = new StringBuffer(mothergene);
				/* ��ȡ��ĸ�����ڽ����Ļ��� */
				String fchildgene = fathergene.substring(0,fgenehalf*8);
				String mchildgene = mothergene.substring(mgenehalf*8,mgenelength);
				/* �������� */
				fsb.replace(0,fgenehalf*8,mchildgene);
				msb.replace(mgenehalf*8,mgenelength,fchildgene);

				Individual kid1 = new Individual();
				Individual kid2 = new Individual();
				kid1.setGene(String.valueOf(fsb));
				kid2.setGene(String.valueOf(msb));

				childlist.add(kid1);
				childlist.add(kid2);
			}else if(swapnum==1){
				StringBuffer fsb = new StringBuffer(fathergene);
				StringBuffer msb = new StringBuffer(mothergene);
				/* ��ȡ��ĸ�����ڽ����Ļ��� */
				String fchildgene = fathergene.substring(fgenehalf*8,fgenelength);
				String mchildgene = mothergene.substring(mgenehalf*8,mgenelength);
				/* �������� */
				fsb.replace(fgenehalf*8,fgenelength,mchildgene);
				msb.replace(mgenehalf*8,mgenelength,fchildgene);

				Individual kid1 = new Individual();
				Individual kid2 = new Individual();
				kid1.setGene(String.valueOf(fsb));
				kid2.setGene(String.valueOf(msb));

				childlist.add(kid1);
				childlist.add(kid2);
			}
		}else if(type==2){
			int fgenelength = fathergene.length()/8;
			int mgenelength = mothergene.length()/8;
			StringBuffer fsb = new StringBuffer(fathergene);
			StringBuffer msb = new StringBuffer(mothergene);
			//���������ȥ�Ļ������
			int fsubgenenum = (int)(Math.random()*(fgenelength-4));
			int msubgenenum = (int)(Math.random()*(mgenelength-4));
            int n = 0;
            int n1 = 0;
            while(n<fsubgenenum){
            	int at = (int)(Math.random()*fgenelength);
            	fsb.delete(at*8,at*8+8);
            	fgenelength--;
            	n++;
			}
			while(n1<msubgenenum){
				int at = (int)(Math.random()*mgenelength);
				msb.delete(at*8,at*8+8);
				mgenelength--;
				n1++;
			}
			Individual kid1 = new Individual();
			Individual kid2 = new Individual();
			kid1.setGene(String.valueOf(fsb));
			kid2.setGene(String.valueOf(msb));

			childlist.add(kid1);
			childlist.add(kid2);
		}else if(type==3){
			int fgenelength = fathergene.length()/8;
			int mgenelength = mothergene.length()/8;
			StringBuffer fsb = new StringBuffer(fathergene);
			StringBuffer msb = new StringBuffer(mothergene);
			//����������ӵĻ������
			int fraisegenenum = (int)(Math.random()*46);
			while((fraisegenenum+fgenelength)>50){
				fraisegenenum = (int)(Math.random()*46);
			}
			int mraisegenenum = (int)(Math.random()*46);
			while((mraisegenenum+mgenelength)>50){
				mraisegenenum = (int)(Math.random()*46);
			}
			int n = 0;
			int n1 = 0;
			while(n<fraisegenenum){
				String raisestr = charGene();
				int at = (int)(Math.random()*fgenelength);
				fsb.insert(at*8,raisestr);
				fgenelength++;
				n++;
			}
			while(n1<mraisegenenum){
				String raisestr = charGene();
				int at = (int)(Math.random()*mgenelength);
				msb.insert(at*8,raisestr);
				mgenelength++;
				n1++;
			}
			Individual kid1 = new Individual();
			Individual kid2 = new Individual();
			kid1.setGene(String.valueOf(fsb));
			kid2.setGene(String.valueOf(msb));

			childlist.add(kid1);
			childlist.add(kid2);
		}else if(type==4){
			int fgenelength = fathergene.length()/8;
			int mgenelength = mothergene.length()/8;
			StringBuffer fsb = new StringBuffer(fathergene);
			StringBuffer msb = new StringBuffer(mothergene);

			int f1 = 0,f2=0;
			int m1 = 0,m2=0;
			f1 = (int)(Math.random()*fgenelength);
			f2 = (int)(Math.random()*fgenelength);
			while (f1==f2){
				f2 = (int)(Math.random()*fgenelength);
			}
			if(f1<f2){
				int temp = f1;
				f1=f2;
				f2=temp;
			}

			m1 = (int)(Math.random()*mgenelength);
			m2 = (int)(Math.random()*mgenelength);
			while (m1==m2){
				m2 = (int)(Math.random()*mgenelength);
			}
			if(m1<m2){
				int temp = m1;
				m1=m2;
				m2=temp;
			}

			String fchildgene = fathergene.substring(f2*8,f1*8);
			String mchildgene = mothergene.substring(m2*8,m1*8);

			fsb.replace(f2*8,f1*8,mchildgene);
			msb.replace(m2*8,m1*8,fchildgene);

			Individual kid1 = new Individual();
			Individual kid2 = new Individual();
			kid1.setGene(String.valueOf(fsb));
			kid2.setGene(String.valueOf(msb));

			childlist.add(kid1);
			childlist.add(kid2);
		}

		return childlist;
	}

	/**
	 * ������ɿ���ʵ�ַ�
	 * @return ���������ַ��ĸ�����
	 */
	private static String charGene() {
		int charnum=(int)(Math.random()*94)+32;
		char a = (char)charnum;
		char tem1 = (char)92;
		char tem2 = (char)34;
		while(a==tem1||a==tem2) {
			int cnum=(int)(Math.random()*94)+32;
			a = (char)cnum;
		}
//		System.out.println(a);
		String bstr = Integer.toBinaryString(a);
		int d = 8 - (int)(bstr.length());
		StringBuffer sb = new StringBuffer(bstr);
		for(int s = 0;s<d;s++) {
			sb.insert(0,'0');
		}
		String result = StringChangeGene.geneChangeGraycode(sb.toString());
		return result;
	}
	

}
