package geneticoperate;

import java.util.ArrayList;

import creategene.StringChangeGene;
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

	/**
	 * 根据不同的方式生成下一代孩子
	 * @param type  方式编号
	 * @param father  父亲个体
	 * @param mother  母亲个体
	 * @return   父母个体诞生的孩子数组
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
				/* 获取父母中用于交换的基因 */
				String fchildgene = fathergene.substring(0,fgenehalf*8);
				String mchildgene = mothergene.substring(mgenehalf*8,mgenelength);
				/* 交换基因 */
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
				/* 获取父母中用于交换的基因 */
				String fchildgene = fathergene.substring(fgenehalf*8,fgenelength);
				String mchildgene = mothergene.substring(mgenehalf*8,mgenelength);
				/* 交换基因 */
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
			//生成随机减去的基因个数
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
			//生成随机增加的基因个数
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
	 * 随机生成可现实字符
	 * @return 返回生成字符的格雷码
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
