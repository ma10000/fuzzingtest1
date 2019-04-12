package population;

import java.util.ArrayList;
import java.util.Arrays;

import creategene.StringChangeGene;

public class Individual {
	private String gene;//基因序列
	private double score;//对应的函数得分
	private ArrayList<String> route;//该测试用例所经历的路径
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public Individual() {
		super();
		// TODO Auto-generated constructor stub
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public ArrayList<String> getRoute() {
		return route;
	}
	public void setRoute(ArrayList<String> route) {
		this.route = route;
	}
	
	
	public void mutation(int type,int num) {
		int size = gene.length();
//		System.out.println(type==3);
		StringBuffer sb = new StringBuffer(gene);
		if((type==1)&&((size/8)>num)) {
			for(int i=0;i<num;i++) {
				int at = ((int)(Math.random()*(size/8)))%(size/8);
				String grgene = charGene();
				String grgene1 = sb.substring(at*8,at*8+8);
				while(grgene.equals(grgene1)) {
					grgene = charGene();
				}
				sb.replace(at*8,at*8+8,grgene1);
			}
		}else if((type==2)&&((size/8)>(num+4))) {
			int temp = 0;
			while(temp<num) {
				int index = (int)(Math.random()*(size/8));
				sb.delete(index*8,index*8+8);
				size = size-8;
				temp++;
			}
		}else if(type==3&&(((size/8)+(num+4))<50)){
			int temp =0;
			while(temp<num) {
				int index = (int)(Math.random()*(size/8));
				String chargene = charGene();
				String glgene = StringChangeGene.geneChangeGraycode(chargene);
				sb.insert(index*8,glgene);
				size = size + 8;
				temp++;
			}
		}
		gene = sb.toString();
	}
	private String charGene() {
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
		return sb.toString();
	}
	@Override
	public String toString() {
		return "Individual [gene=" + gene + ", score=" + score + ", route=" + route + "]";
	}
 
}
