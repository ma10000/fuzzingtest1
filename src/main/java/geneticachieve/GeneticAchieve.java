package geneticachieve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import createfuzzing.GenerateFuzzing;
import creategene.StringChangeGene;
import execute.ExecuteFuzzing;
import geneticoperate.geneticKid;
import population.Individual;

/**
 * 
 * @author mace
 *用于实现遗传算法的类
 */
public class GeneticAchieve {
	
	//单字符特征字符串
	public static String[] singlechars = { ".*[hH].*", ".*[xX].*", ".*[mM].*", ".*[sS].*", ".*[lL].*", ".*[cC].*", ".*[tT].*", ".*[iI].*", ".*[nN].*", ".*[rR].*", ".*[dD].*", ".*[oO].*", ".*[uU].*", ".*[pP].*", ".*[bB].*", ".*[aA].*", ".*[wW].*",
			                         ".*_.*", ".* .*", ".*\\(.*"};
	//双字符特征字符串
	public static String[] doublechars = { ".*[sS][eE].*", ".*[iI][nN].*", ".*[dD][eE].*", ".*[cC][oO].*", ".*[dD][rR].*", ".*[uU][pP].*", ".*[tT][rR].*", 
			                        ".*[aA][sS].*", ".*[mM][iI].*", ".*[cC][hH].*", ".*[xX][pP].*", ".*[eE][xX].*", ".*[mM][aA].*", ".*[nN][eE].*", 
			                        ".*[aA][nN].*", ".*[oO][rR].*", ".*[wW][hH].*"};
	//三字符特征字符串
	public static String[] thirdchars = { ".*[sS][eE][lL].*", ".*[iI][nN][sS].*", ".*[dD][eE][lL].*", ".*[cC][oO][uU].*", 
            ".*[dD][rR][oO].*", ".*[uU][pP][dD].*", ".*[tT][rR][uU].*", ".*[aA][sS][cC].*", ".*[mM][iI][dD].*", 
            ".*[cC][hH][aA].*", ".*[xX][pP]_.*", ".*[eE][xX][eE].*", ".*[mM][aA][sS].*", ".* [nN][eE].*", ".* [aA][nN].*", ".* [oO][rR].*", ".*[wW][hH][eE].*"};

	
	
	private int populationnum = 1000;//种群数量
	private ArrayList<Individual> population = new ArrayList<Individual>();
	private int mutationnum = 0;//变异的字符数
	private double mutationrate = 0.001;//变异的概率
	private int iterationnum = 100;//种群迭代次数
	
	private int generation = 1;//当前迭代次数
	private int mutationtype = 0;//变异类型
	
	private double bestscore=0;
	private double totalscore=0;//总的得分
	private double averagescore=0;//平均得分
	private double lowestscore=0;//最低得分
	
	private Individual bestindividual=null;//最好的分的个体
	private ArrayList<String> path = new ArrayList<String>();//记录新的路径
	private HashMap<String,Individual> pathindividualmap = new HashMap<String,Individual>();//用于存储第一次发现新路径的个体
	private ArrayList<Individual> sqlinjectionindividual = new ArrayList<Individual>();
	
	public GeneticAchieve() {
		super();
	}
	public GeneticAchieve(int populationnum) {
		this.populationnum = populationnum;
	}
	public void init() {
		initialisePopulation();
		calculateRoute();
		calculateScore();
		calculateTotalScore();
		calculateAverageScore();
		calculateBestScore();
		calculateLowestScore();
	}
	/**
	 * 初始化种群
	 */
	public void initialisePopulation() {
		for(int i=0;i<populationnum;i++) {
			Individual indi = new Individual();
			boolean flage = true;
			String geneticStr = null;//可能会出现空指针错误
			while(flage) {
				geneticStr = GenerateFuzzing.generateCase();
				if(geneticStr.length()>=4) {
					flage = false;
				}
			}
			System.out.println(i+"测试用例"+geneticStr);
			String geneticAscii = StringChangeGene.stringChangeGene(geneticStr);
			String geneticGcode = StringChangeGene.geneChangeGraycode(geneticAscii);
			indi.setGene(geneticGcode);
			population.add(indi);
			
		}
//		calculateRoute();
//		calculateScore();
		
	}
	/**
	 * 计算每个测试用例（种群中的个体）的路径
	 */
	public void calculateRoute() {
		for(int i=0;i<population.size();i++) {
			String geneticGcode = population.get(i).getGene();
//			System.out.println(i+population.get(i).toString());
			String geneticAscii = StringChangeGene.graycodeChangeGene(geneticGcode);
			String geneticStr = StringChangeGene.geneChangeString(geneticAscii);
			StringBuffer sb = new StringBuffer(geneticStr);
			int length = sb.length();
			int n = 0;
			while(n<length) {
				char tem1 = (char)92;
				char tem2 = (char)34;
				char ch = sb.charAt(n);
				if(ch==tem1||ch==tem2) {
					sb.insert(n,tem1);
					n++;
					length++;
				}
				n++;
			}
			String geneticreal = sb.toString();
			String genestr = "\""+geneticreal+"\"";
			String[] list = ExecuteFuzzing.execute(genestr);
			System.out.println("在第"+generation+"遗传的种群中对第"+i+"个测试用例："+geneticStr+" "+geneticreal+"进行测试。");
			ArrayList<String> slist = new ArrayList<String>();
			if(list != null) {
				boolean flag = false;
				for(int m=0;m<list.length;m++) {
					if(list[m].contains("sql注入")) {
						flag = true;
					}
				}
				if(flag) {
					sqlinjectionindividual.add(population.get(i));
					System.out.println("在第"+(generation+1)+"遗传的种群中发现sql注入痕迹："+geneticStr+"。");
				}
				for(int m=0;m<list.length;m++) {
					if(list[m].matches("[1-9][0-9]*")) {
						slist.add(list[m]);
						if(!path.contains(list[m])) {
							path.add(list[m]);
							pathindividualmap.put(list[m],population.get(i));
						}
					}
				}
			}
			population.get(i).setRoute(slist);
				
		}
	}
	/**
	 * 计算每个测试用例（种群中的个体）的分数
	 */
	public void calculateScore() {
		int singlecharslength = singlechars.length;
		int doublecharslength = doublechars.length;
		int thirdcharslength = thirdchars.length;
		for(int i=0;i<population.size();i++) {
			int size = population.get(i).getRoute().size();
			String glgene = population.get(i).getGene();
			String agene = StringChangeGene.graycodeChangeGene(glgene);
			String charstr = StringChangeGene.geneChangeString(agene);
			if(size==0) {
				population.get(i).setScore(0);
			}else if(size==1) {
				double score = 1;
				int n = 0;
				for(int m=0;m<singlecharslength;m++) {
					System.out.println(singlechars[m]);
					Pattern pattern = Pattern.compile(singlechars[m]);
					Matcher matcher = pattern.matcher(charstr);
					if(matcher.matches()) {
						n++;
					}
				}
				score = score + (n-1)*0.001;
				population.get(i).setScore(score);
			}else if(size==2) {
				double score = 2;
				int n1 = 0;
				int n2 = 0;
				for(int m=0;m<singlecharslength;m++) {
					Pattern pattern = Pattern.compile(singlechars[m]);
					Matcher matcher = pattern.matcher(charstr);
					if(matcher.matches()) {
						n1++;
					}
				}
				for(int m=0;m<doublecharslength;m++) {
					Pattern pattern = Pattern.compile(doublechars[m]);
					Matcher matcher = pattern.matcher(charstr);
					if(matcher.matches()) {
						n2++;
					}
				}
				score = score+n1*0.001+(n2-1)*0.02;
				population.get(i).setScore(score);
			}else if(size==3) {
				double score = 3;
				int n1 = 0;
				int n2 = 0;
				int n3 = 0;;
				for(int m=0;m<singlecharslength;m++) {
					Pattern pattern = Pattern.compile(singlechars[m]);
					Matcher matcher = pattern.matcher(charstr);
					if(matcher.matches()) {
						n1++;
					}
				}
				for(int m=0;m<doublecharslength;m++) {
					Pattern pattern = Pattern.compile(doublechars[m]);
					Matcher matcher = pattern.matcher(charstr);
					if(matcher.matches()) {
						n2++;
					}
				}
				for(int m=0;m<thirdcharslength;m++) {
					Pattern pattern = Pattern.compile(thirdchars[m]);
					Matcher matcher = pattern.matcher(charstr);
					if(matcher.matches()) {
						n3++;
					}
				}
				score = score+n1*0.001+n2*0.02+(n3-1)*0.3;
				population.get(i).setScore(score);
			}
			
		}
	}
	/**
	 * 计算当前种群中的最低分数及对应得个体
	 */
	public void calculateLowestScore() {
		double lowest = population.get(0).getScore();
		for(int i=1;i<population.size();i++) {
			if(population.get(i).getScore()<lowest) {
				lowest = population.get(i).getScore();
			}
		}
		lowestscore = lowest;
	}
	/**
	 * 计算当前种群的总分数
	 */
	public void calculateTotalScore() {
		double sum = 0;
		for(int i=0;i<population.size();i++) {
			sum += population.get(i).getScore();
		}
		totalscore = sum;
	}
	/**
	 * 计算当前种群的平均分数
	 */
	public void calculateAverageScore() {
		averagescore = totalscore/populationnum;
	}
	/**
	 * 计算最好的成绩及对应的个体
	 */
	public void calculateBestScore() {
		double bests = 0;
		Individual indi1 = null;
		for(int i=0;i<population.size();i++) {
			if(population.get(i).getScore()>bests) {
				bests = population.get(i).getScore();
				indi1 = population.get(i);
			}
		}
		bestindividual = indi1;
		bestscore = bests;
	}
	/**
	 * 用于产生下一代种群的方法
	 */
	public void produceGeneration() {
		int usedlength = 0;
		ArrayList<Individual> childpopulation = new ArrayList<Individual>();
//		for(int i=0;i<population.size();i++) {
//			if(population.get(i).getScore()==bestscore) {
//				childpopulation.add(geneticKid.clone(population.get(i)));
//				usedlength++;
//			}
//		}
		while(usedlength<populationnum) {
			childpopulation.add(geneticKid.clone(bestindividual));
			usedlength++;
			childpopulation.add(pathindividualmap.get(path.get(path.size()-1)));
			usedlength++;
			System.out.println(averagescore);
			Individual p1 = getParentIndividual();
			while(p1==null) {
				p1 = getParentIndividual();
			}
			Individual p2 = getParentIndividual();
			while(p2==null) {
				p2 = getParentIndividual();
			}
			
			System.out.println(p1.toString());
			System.out.println(p2.toString());
			List<Individual> children = geneticKid.geneticChild(p1,p2);
			if(children != null) {
				for(int m=0;((m<children.size())&&(usedlength < populationnum));m++) {
					childpopulation.add(children.get(m));
					usedlength++;
				}
			}
		}
		//将下一代种群替换上一代种群
		List<Individual> t = population;
		population = childpopulation;
		t.clear();
		t=null;
		//基因突变
		
	
	}
	public void inherit() {
		produceGeneration();
		mutation();
		//计算当前个体的路径和分数
		calculateRoute();
		calculateScore();
		calculateTotalScore();
		calculateAverageScore();
		calculateBestScore();
		calculateLowestScore();
	}
	/**
	 * 用轮盘赌发选择用于交换基因产生下一代的个体
	 * @return 返回选择的父类个体
	 */
	public Individual getParentIndividual() {
//		System.out.println(totalscore);
		double part1 = Math.random()*totalscore;
		double part2 = Math.random()*totalscore;
//		System.out.println(part);
//		System.out.println(averagescore);
		Individual indi1 = new Individual();
		Individual indi2 = new Individual();
		double count1 =0;
		double count2 =0;
		for(Individual indi:population) {
			count1 += indi.getScore();
			if(count1>part1) {
				indi1=indi;
			}
		}
		for(Individual indi:population) {
			count2 += indi.getScore();
			if(count2>part2) {
				indi2=indi;
			}
		}
		
		
		return (indi1.getScore()>indi2.getScore())?indi1:indi2;
	}
	/**
	 * 对种群中的某些个体尽心变异
	 * 
	 */
	public void mutation() {
		for(Individual indi:population) {
			int length = indi.getGene().length();
			if(Math.random()<mutationrate) {//发生基因突变
				mutationtype = (int)((Math.random()*3)+1);
				if(mutationtype==1) {
					int num = (int)(Math.random()*(length/8-1))+1;
					indi.mutation(mutationtype,num);
				}else if(mutationtype==2) {
					int num = (int)(Math.random()*(length/8-1))+1;
					while(length/8<(num+4)) {
						num = (int)(Math.random()*(length/8-1))+1;
					}
					indi.mutation(mutationtype,num);
				}else if(mutationtype==3) {
					int num = (int)(Math.random()*(length/8-1))+1;
					while((num+length/8)>50) {
						num = (int)(Math.random()*(length/8-1))+1;
					}
					indi.mutation(mutationtype,num);
				}
			}
		}
	}
	public void startIteration() {
		generation = 1;
		init();
		
		
		while(generation<iterationnum) {
			generation++;
			inherit();
			
		}
	}
	public int getPopulationnum() {
		return populationnum;
	}
	public void setPopulationnum(int populationnum) {
		this.populationnum = populationnum;
	}
	public ArrayList<Individual> getPopulation() {
		return population;
	}
	public void setPopulation(ArrayList<Individual> population) {
		this.population = population;
	}
	public int getMutationnum() {
		return mutationnum;
	}
	public void setMutationnum(int mutationnum) {
		this.mutationnum = mutationnum;
	}
	public double getMutationrate() {
		return mutationrate;
	}
	public void setMutationrate(double mutationrate) {
		this.mutationrate = mutationrate;
	}
	public int getIterationnum() {
		return iterationnum;
	}
	public void setIterationnum(int iterationnum) {
		this.iterationnum = iterationnum;
	}
	public int getGeneration() {
		return generation;
	}
	public void setGeneration(int generation) {
		this.generation = generation;
	}
	public double getBestscore() {
		return bestscore;
	}
	public void setBestscore(double bestscore) {
		this.bestscore = bestscore;
	}
	public double getTotalscore() {
		return totalscore;
	}
	public void setTotalscore(double totalscore) {
		this.totalscore = totalscore;
	}
	public double getAveragescore() {
		return averagescore;
	}
	public void setAveragescore(double averagescore) {
		this.averagescore = averagescore;
	}
	public int getMutationtype() {
		return mutationtype;
	}
	public void setMutationtype(int mutationtype) {
		this.mutationtype = mutationtype;
	}
	public double getLowestscore() {
		return lowestscore;
	}
	public void setLowestscore(double lowestscore) {
		this.lowestscore = lowestscore;
	}
	public Individual getBestindividual() {
		return bestindividual;
	}
	public void setBestindividual(Individual bestindividual) {
		this.bestindividual = bestindividual;
	}
	public ArrayList<String> getPath() {
		return path;
	}
	public void setPath(ArrayList<String> path) {
		this.path = path;
	}
	public HashMap<String, Individual> getPathindividualmap() {
		return pathindividualmap;
	}
	public void setPathindividualmap(HashMap<String, Individual> pathindividualmap) {
		this.pathindividualmap = pathindividualmap;
	}
	public ArrayList<Individual> getSqlinjectionindividual() {
		return sqlinjectionindividual;
	}
	public void setSqlinjectionindividual(ArrayList<Individual> sqlinjectionindividual) {
		this.sqlinjectionindividual = sqlinjectionindividual;
	}

}
