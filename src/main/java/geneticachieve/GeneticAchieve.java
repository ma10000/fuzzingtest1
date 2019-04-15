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
	
	private int populationnum = 1000;//种群数量
	private ArrayList<Individual> population = new ArrayList<Individual>();//初始种群数组
	private int parentsnum = 400;//生成的父母个体的数量
	private double[] parentratefitness = {1,1.5,2,2.5};//根据值将适应度分为五个区间
	private double[] parentselectrate = {0.1,0.3,0.5,0.7,0.9};//分别对应分数为0、1、2的选择概率
	private ArrayList<Individual> parentslist = new ArrayList<Individual>();//父母个体存放数组

	private int iterationnum = 100;//种群迭代次数
	private int generation = 1;//当前迭代次数

	private double[] birthselecttyperate = {0.2,0.2,0.2,0.2,0.2};//选择不同的基因交换原则的初始概率
    private ArrayList<Individual> swaptypelist0 =new ArrayList<Individual>();//用于存储基因种类交换0的下一代个体
	private ArrayList<Individual> swaptypelist1 =new ArrayList<Individual>();//用于存储基因种类交换1的下一代个体
	private ArrayList<Individual> swaptypelist2 =new ArrayList<Individual>();//用于存储基因种类交换2的下一代个体
	private ArrayList<Individual> swaptypelist3 =new ArrayList<Individual>();//用于存储基因种类交换3的下一代个体
	private ArrayList<Individual> swaptypelist4 =new ArrayList<Individual>();//用于存储基因种类交换4的下一代个体

	private int mutationtype = 0;//变异类型
	private int mutationnum = 2;//变异的字符数
	private double mutationrate = 0.001;//变异的概率
	
	private double bestfitness=0;//最好适应度
	private double totalfitness=0;//总的适应度
	private double averagefitness=0;//平均适应度
	private double lowestfitness=0;//最低适应度
	
	private Individual bestindividual=null;//最好的分的个体
	private ArrayList<String> path = new ArrayList<String>();//记录新的路径
	private HashMap<String,Individual> pathindividualmap = new HashMap<String,Individual>();//用于存储第一次发现新路径的个体
	private ArrayList<Individual> sqlinjectionindividual = new ArrayList<Individual>();//用于记录sql注入的个体
	
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
		calculateTotalFitness();
		calculateAverageFitness();
		calculateBestFitness();
		calculateLowestFitness();
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
		
	}

	/**
	 * 计算次代之后的个体路径
	 */
	public void calculatelaterRoute(){
		int listsizetype0 = swaptypelist0.size();
		int listsizetype1 = swaptypelist1.size();
		int listsizetype2 = swaptypelist2.size();
		int listsizetype3 = swaptypelist3.size();
		int listsizetype4 = swaptypelist4.size();
         //计算第一种基因交换方式的路径
		for(int i=0;i<listsizetype0;i++){
			Individual genetindividual = swaptypelist0.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist0.get(i).setRoute(list);
		}
        //计算第二种基因交换方式的路径
		for(int i=0;i<listsizetype1;i++){
			Individual genetindividual = swaptypelist1.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist1.get(i).setRoute(list);
		}
		//计算第三种基因交换方式的路径
		for(int i=0;i<listsizetype2;i++){
			Individual genetindividual = swaptypelist2.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist2.get(i).setRoute(list);
		}
		//计算第四种基因交换方式的路径
		for(int i=0;i<listsizetype3;i++){
			Individual genetindividual = swaptypelist3.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist3.get(i).setRoute(list);
		}
		//计算第五种基因交换方式的路径
		for(int i=0;i<listsizetype4;i++){
			Individual genetindividual = swaptypelist4.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist4.get(i).setRoute(list);
		}
	}

	/**
	 * 计算对应个体的路径
	 * @param indi 传入函数的个体
	 * @return  返回对应个体的路径
	 */
	public ArrayList<String> calculateSomeRoute(Individual indi){
		ArrayList<String> slist = new ArrayList<String>();

		String geneticGcode = indi.getGene();
		String geneticAscii = StringChangeGene.graycodeChangeGene(geneticGcode);
		String geneticStr = StringChangeGene.geneChangeString(geneticAscii);
		String genestr = "\""+geneticStr+"\"";
		String[] list = ExecuteFuzzing.execute(genestr);
		System.out.println("在第"+generation+"遗传的种群中对测试用例："+geneticStr+" 进行测试。");

		if(list != null) {
			boolean flag = false;
			for (int m = 0; m < list.length; m++) {
				if (list[m].contains("sql注入")) {
					flag = true;
				}
			}
			if (flag) {
				sqlinjectionindividual.add(indi);
				System.out.println("在第" + (generation + 1) + "遗传的种群中发现sql注入痕迹：" + geneticStr + "。");
			}
			for (int m = 0; m < list.length; m++) {
				if (list[m].matches("[1-9][0-9]*")) {
					slist.add(list[m]);
					if (!path.contains(list[m])) {
						path.add(list[m]);
						pathindividualmap.put(list[m],indi);
					}
				}
			}
		}


		return slist;
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
	 * 计算次一代测试用例的分数
	 */
	public void calculateLaterScore(){
		int listsizetype0 = swaptypelist0.size();
		int listsizetype1 = swaptypelist1.size();
		int listsizetype2 = swaptypelist2.size();
		int listsizetype3 = swaptypelist3.size();
		int listsizetype4 = swaptypelist4.size();
		//计算第一种基因交换方式的路径
		for(int i=0;i<listsizetype0;i++){
			Individual genetindividual = swaptypelist0.get(i);
			int score = calculateSomeScore(genetindividual);
			swaptypelist0.get(i).setScore(score);
		}
		for(int i=0;i<listsizetype1;i++){
			Individual genetindividual = swaptypelist1.get(i);
			int score = calculateSomeScore(genetindividual);
			swaptypelist1.get(i).setScore(score);
		}
		for(int i=0;i<listsizetype2;i++){
			Individual genetindividual = swaptypelist2.get(i);
			int score = calculateSomeScore(genetindividual);
			swaptypelist2.get(i).setScore(score);
		}
		for(int i=0;i<listsizetype3;i++){
			Individual genetindividual = swaptypelist3.get(i);
			int score = calculateSomeScore(genetindividual);
			swaptypelist3.get(i).setScore(score);
		}
		for(int i=0;i<listsizetype4;i++){
			Individual genetindividual = swaptypelist4.get(i);
			int score = calculateSomeScore(genetindividual);
			swaptypelist4.get(i).setScore(score);
		}

	}
	public int calculateSomeScore(Individual indi){
		int result = 0;

		int tem = indi.getRoute().size();
		if(tem==0){
			result=0;
		}else if(tem==1){
			result=1;
		}else if(tem==2){
			result=2;
		}

		return result;
	}
	/**
	 * 计算每个测试用例（种群中的个体）的分数
	 */
	public void calculateScore() {
		for(int i=0;i<population.size();i++) {
		    int tem = population.get(i).getRoute().size();
		    if(tem==0){
		    	population.get(i).setScore(0);
			}else if(tem==1){
				population.get(i).setScore(1);
			}else if(tem==2){
				population.get(i).setScore(2);
			}
		}

	}
	/**
	 * 计算初始种群的适应度
	 */
	public void calculateFitness(){
		for(int i=0;i<population.size();i++) {
		    population.get(i).calculateFitness();
		}
	}

	/**
	 * 计算初代之后种群的适应度
	 */
	public void calculateLaterFitness(){
		int listsizetype0 = swaptypelist0.size();
		int listsizetype1 = swaptypelist1.size();
		int listsizetype2 = swaptypelist2.size();
		int listsizetype3 = swaptypelist3.size();
		int listsizetype4 = swaptypelist4.size();

		for(int i=0;i<listsizetype0;i++){
			swaptypelist0.get(i).calculateFitness();
		}
		for(int i=0;i<listsizetype1;i++){
			swaptypelist1.get(i).calculateFitness();
		}
		for(int i=0;i<listsizetype2;i++){
			swaptypelist2.get(i).calculateFitness();
		}
		for(int i=0;i<listsizetype3;i++){
			swaptypelist3.get(i).calculateFitness();
		}
		for(int i=0;i<listsizetype4;i++){
			swaptypelist4.get(i).calculateFitness();
		}
	}
	/**
	 * 计算当前种群中的最低分数及对应得个体
	 */
	public void calculateLowestFitness() {
		double lowest = population.get(0).getFitness();
		for(int i=1;i<population.size();i++) {
			if(population.get(i).getFitness()<lowest) {
				lowest = population.get(i).getFitness();
			}
		}
		lowestfitness = lowest;
	}

	/**
	 * 计算次代之后的最低得分
	 */
	public void calculateLaterLowestFitness(){
		double lowestscore = 0;
		if(swaptypelist0.size()!=0){
			lowestscore = swaptypelist0.get(0).getFitness();
		}else if(swaptypelist1.size()!=0){
			lowestscore = swaptypelist1.get(0).getFitness();
		}else if(swaptypelist2.size()!=0){
			lowestscore = swaptypelist2.get(0).getFitness();
		}else if(swaptypelist3.size()!=0){
			lowestscore = swaptypelist3.get(0).getFitness();
		}else if(swaptypelist4.size()!=0){
			lowestscore = swaptypelist4.get(0).getFitness();
		}

		for(int i=0;i<swaptypelist0.size();i++){
			double tempscore = swaptypelist0.get(i).getFitness();
			if(tempscore<lowestscore){
				lowestscore=tempscore;
			}
		}
		for(int i=0;i<swaptypelist1.size();i++){
			double tempscore = swaptypelist1.get(i).getFitness();
			if(tempscore<lowestscore){
				lowestscore=tempscore;
			}
		}
		for(int i=0;i<swaptypelist2.size();i++){
			double tempscore = swaptypelist2.get(i).getFitness();
			if(tempscore<lowestscore){
				lowestscore=tempscore;
			}
		}
		for(int i=0;i<swaptypelist3.size();i++){
			double tempscore = swaptypelist3.get(i).getFitness();
			if(tempscore<lowestscore){
				lowestscore=tempscore;
			}
		}
		for(int i=0;i<swaptypelist4.size();i++){
			double tempscore = swaptypelist4.get(i).getFitness();
			if(tempscore<lowestscore){
				lowestscore=tempscore;
			}
		}

		lowestfitness = lowestscore;

	}
	/**
	 * 计算当前种群的总分数
	 */
	public void calculateTotalFitness() {
		double sum = 0;
		for(int i=0;i<population.size();i++) {
			sum += population.get(i).getFitness();
		}
		totalfitness = sum;
	}

	/**
	 * 计算次下一代的总适应度
	 */
	public void calculateLaterTotalFitness(){
		double sumfitness = 0;
		sumfitness = bestindividual.getFitness();
		for(int i=0;i<swaptypelist0.size();i++){
			double tempscore = swaptypelist0.get(i).getFitness();
			sumfitness += tempscore;
		}
		for(int i=0;i<swaptypelist1.size();i++){
			double tempscore = swaptypelist1.get(i).getFitness();
			sumfitness += tempscore;
		}
		for(int i=0;i<swaptypelist2.size();i++){
			double tempscore = swaptypelist2.get(i).getFitness();
			sumfitness += tempscore;
		}
		for(int i=0;i<swaptypelist3.size();i++){
			double tempscore = swaptypelist3.get(i).getFitness();
			sumfitness += tempscore;
		}
		for(int i=0;i<swaptypelist4.size();i++){
			double tempscore = swaptypelist4.get(i).getFitness();
			sumfitness += tempscore;
		}
		totalfitness = sumfitness;

	}
	/**
	 * 计算当前种群的平均分数
	 */
	public void calculateAverageFitness() {
		averagefitness = totalfitness/populationnum;
	}
	/**
	 * 计算最好的成绩及对应的个体
	 */
	public void calculateBestFitness() {
		double bests = 0;
		Individual indi1 = null;
		for(int i=0;i<population.size();i++) {
			if(population.get(i).getFitness()>bests) {
				bests = population.get(i).getFitness();
				indi1 = population.get(i);
			}
		}
		bestindividual = indi1;
		bestfitness = bests;
	}

	/**
	 * 计算次代之后的最好适应度和对应的个体
	 */
	public void calculateLaterBestFitness(){
		double bestfit = bestfitness;
		Individual bestindi=bestindividual;
		for(int i=0;i<swaptypelist0.size();i++){
			double tempscore = swaptypelist0.get(i).getFitness();
			if(tempscore>bestfit){
				bestfit=tempscore;
				bestindi=swaptypelist0.get(i);
			}
		}
		for(int i=0;i<swaptypelist1.size();i++){
			double tempscore = swaptypelist1.get(i).getFitness();
			if(tempscore>bestfit){
				bestfit=tempscore;
				bestindi=swaptypelist1.get(i);
			}
		}
		for(int i=0;i<swaptypelist2.size();i++){
			double tempscore = swaptypelist2.get(i).getFitness();
			if(tempscore>bestfit){
				bestfit=tempscore;
				bestindi=swaptypelist2.get(i);
			}
		}
		for(int i=0;i<swaptypelist3.size();i++){
			double tempscore = swaptypelist3.get(i).getFitness();
			if(tempscore>bestfit){
				bestfit=tempscore;
				bestindi=swaptypelist3.get(i);
			}
		}
		for(int i=0;i<swaptypelist4.size();i++){
			double tempscore = swaptypelist4.get(i).getFitness();
			if(tempscore>bestfit){
				bestfit=tempscore;
				bestindi=swaptypelist4.get(i);
			}
		}
		bestfitness=bestfit;
		bestindividual=bestindi;


	}

	/**
	 *计算次代之后的下一代种群的方法
	 */
	public void produceLaterGeneration(){
		int newpopulationnum = 999;
		int usedlength = 0;
		ArrayList<Individual> newswaptypelist0 = new ArrayList<Individual>();
		ArrayList<Individual> newswaptypelist1 = new ArrayList<Individual>();
		ArrayList<Individual> newswaptypelist2 = new ArrayList<Individual>();
		ArrayList<Individual> newswaptypelist3 = new ArrayList<Individual>();
		ArrayList<Individual> newswaptypelist4 = new ArrayList<Individual>();

		while (usedlength<newpopulationnum){
			Individual p1 = getParentIndividual();
			while(p1==null) {
				p1 = getParentIndividual();
			}
			Individual p2 = getParentIndividual();
			while(p2==null||p1.getGene().equals(p2.getGene())) {
				p2 = getParentIndividual();
			}
			double sumselectrate = birthselecttyperate[0]+birthselecttyperate[1]+birthselecttyperate[2]+
					birthselecttyperate[3]+birthselecttyperate[4];

			double temprate = sumselectrate*(Math.random());
			if(birthselecttyperate[0]>=temprate){
				ArrayList<Individual> list = geneticKid.geneticChild(0,p1,p2);
				if(list != null) {
					for(int m=0;((m<list.size())&&(usedlength < newpopulationnum));m++) {
						newswaptypelist0.add(list.get(m));
						usedlength++;
					}
				}
			}
			else if((birthselecttyperate[0]+birthselecttyperate[1])>=temprate){
				ArrayList<Individual> list = geneticKid.geneticChild(1,p1,p2);
				if(list != null) {
					for(int m=0;((m<list.size())&&(usedlength < newpopulationnum));m++) {
						newswaptypelist1.add(list.get(m));
						usedlength++;
					}
				}
			}
			if((birthselecttyperate[0]+birthselecttyperate[1]+birthselecttyperate[2])>=temprate){
				ArrayList<Individual> list = geneticKid.geneticChild(2,p1,p2);
				if(list != null) {
					for(int m=0;((m<list.size())&&(usedlength < newpopulationnum));m++) {
						newswaptypelist2.add(list.get(m));
						usedlength++;
					}
				}
			}
			if((birthselecttyperate[0]+birthselecttyperate[1]+birthselecttyperate[2]+birthselecttyperate[3])>=temprate){
				ArrayList<Individual> list = geneticKid.geneticChild(3,p1,p2);
				if(list != null) {
					for(int m=0;((m<list.size())&&(usedlength < newpopulationnum));m++) {
						newswaptypelist3.add(list.get(m));
						usedlength++;
					}
				}
			}
			if((birthselecttyperate[0]+birthselecttyperate[1]+birthselecttyperate[2]+birthselecttyperate[3]+birthselecttyperate[4])>=temprate){
				ArrayList<Individual> list = geneticKid.geneticChild(4,p1,p2);
				if(list != null) {
					for(int m=0;((m<list.size())&&(usedlength < newpopulationnum));m++) {
						newswaptypelist4.add(list.get(m));
						usedlength++;
					}
				}
			}
		}
		List<Individual> t0 = swaptypelist0;
		swaptypelist0 = newswaptypelist0;
		t0.clear();
		t0=null;
		List<Individual> t1 = swaptypelist1;
		swaptypelist1 = newswaptypelist1;
		t1.clear();
		t1=null;
		List<Individual> t2 = swaptypelist2;
		swaptypelist2 = newswaptypelist2;
		t2.clear();
		t2=null;
		List<Individual> t3 = swaptypelist3;
		swaptypelist3 = newswaptypelist3;
		t3.clear();
		t3=null;
		List<Individual> t4 = swaptypelist4;
		swaptypelist4 = newswaptypelist4;
		t4.clear();
		t4=null;

	}
	/**
	 * 用于产生下一代种群的方法
	 */
	public void produceGeneration() {
		int usedlength = 0;
		ArrayList<Individual> childpopulation = new ArrayList<Individual>();

		while(usedlength<populationnum) {
			childpopulation.add(geneticKid.clone(bestindividual));
			usedlength++;
			childpopulation.add(pathindividualmap.get(path.get(path.size()-1)));
			usedlength++;
			System.out.println(averagefitness);
			Individual p1 = getParentIndividual();
			while(p1==null) {
				p1 = getParentIndividual();
			}
			Individual p2 = getParentIndividual();
			while(p2==null||p1.getGene().equals(p2.getGene())) {
				p2 = getParentIndividual();
			}
			

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
	public void inheritLater(int tempgeneration){
		if(tempgeneration==1) {
			selectParentsList();
			produceLaterGeneration();
			mutationLater();
			calculatelaterRoute();
			calculateLaterScore();
			calculateLaterTotalFitness();
			calculateAverageFitness();
			calculateLaterBestFitness();
			calculateLaterLowestFitness();
		}else {
			clalculateBirthSelectRate();
			selectParentsList();
			produceLaterGeneration();
			mutationLater();
			calculatelaterRoute();
			calculateLaterScore();
			calculateLaterTotalFitness();
			calculateAverageFitness();
			calculateLaterBestFitness();
			calculateLaterLowestFitness();

		}
	}
	/**
	 * 进行遗传操作
	 */
	public void inherit() {
		selectParentsList();
		produceGeneration();
		mutation();
		//计算当前个体的路径和分数
		calculateRoute();
		calculateScore();
		calculateTotalFitness();
		calculateAverageFitness();
		calculateBestFitness();
		calculateLowestFitness();
	}
	/**
	 * 更具不同的适应度
	 * @return 返回选择的父类个体
	 */
	public Individual getParentIndividual() {
		int tt = (int)(Math.random()*400);
		return parentslist.get(tt);
	}

	/**
	 * 计算新的birthselecttyperate代替旧的birthselecttyperate
	 */
	public void clalculateBirthSelectRate(){
		double count0=0,count1=0,count2=0,count3=0,count4=0;
		count0 = calculateSwapTypeCount(swaptypelist0);
		count1 = calculateSwapTypeCount(swaptypelist0);
		count2 = calculateSwapTypeCount(swaptypelist0);
		count3 = calculateSwapTypeCount(swaptypelist0);
		count4 = calculateSwapTypeCount(swaptypelist0);
		birthselecttyperate[0] = count0/(count0+count1+count2+count3+count4);
		birthselecttyperate[1] = count1/(count0+count1+count2+count3+count4);
		birthselecttyperate[2] = count2/(count0+count1+count2+count3+count4);
		birthselecttyperate[3] = count3/(count0+count1+count2+count3+count4);
		birthselecttyperate[4] = count4/(count0+count1+count2+count3+count4);

	}

	/**
	 * 计算每类交叉数组中适应度大于等于2的个数
	 * @param list 交叉种类的数组
	 * @return  数组中适应度大鱼等于2 的个数
	 */
	public double calculateSwapTypeCount(ArrayList<Individual> list){
		double count = 0;
		for(Individual indi:list){
			if(indi.getFitness()>=2){
				count++;
			}
		}
		return count;
	}
	/**
	 * 选择次代之后的父母的个体
	 */
	public void selectLaterParentsList(){
        int n0=0,n1=0,n2=0,n3=0,n4=0;
		int m0=0,m1=0,m2=0,m3=0,m4=0;
		int count0=0,count1=0,count2=0,count3=0,count4=0;
        ArrayList<Individual> list = new ArrayList<Individual>();
        int num = 0;
        double bigcount = birthselecttyperate[0];
        for(int i=1;i<birthselecttyperate.length;i++){

        	if(bigcount<birthselecttyperate[i]){
        		num=i;
			}
		}
		count0 = (int)(birthselecttyperate[0]*400);
		count1 = (int)(birthselecttyperate[1]*400);
		count2 = (int)(birthselecttyperate[2]*400);
		count3 = (int)(birthselecttyperate[3]*400);
		count4 = (int)(birthselecttyperate[4]*400);
		int lang = 400-(count0+count1+count2+count3+count4);
		if(num==0){
			count0 += lang;
		}else if(num==1){
			count1 += lang;
		}else if(num==2){
			count2 += lang;
		}else if(num==3){
			count3 += lang;
		}else if(num==4){
			count4 += lang;
		}
		while(n0<count0){
			Individual indi = swaptypelist0.get(m0%(swaptypelist0.size()));
			boolean flag = judgeFitness(indi.getFitness());
			if(flag){
				list.add(indi);
				n0++;
				m0++;
			}
			m0++;
		}
		while(n1<count1){
			Individual indi = swaptypelist1.get(m1%(swaptypelist1.size()));
			boolean flag = judgeFitness(indi.getFitness());
			if(flag){
				list.add(indi);
				n1++;
				m1++;
			}
			m1++;
		}
		while(n2<count2){
			Individual indi = swaptypelist2.get(m2%(swaptypelist2.size()));
			boolean flag = judgeFitness(indi.getFitness());
			if(flag){
				list.add(indi);
				n2++;
				m2++;
			}
			m2++;
		}
		while(n3<count3){
			Individual indi = swaptypelist3.get(m3%(swaptypelist3.size()));
			boolean flag = judgeFitness(indi.getFitness());
			if(flag){
				list.add(indi);
				n3++;
				m3++;
			}
			m3++;
		}
		while(n4<count4){
			Individual indi = swaptypelist4.get(m4%(swaptypelist4.size()));
			boolean flag = judgeFitness(indi.getFitness());
			if(flag){
				list.add(indi);
				n4++;
				m4++;
			}
			m4++;
		}
		List<Individual> t = parentslist;
		parentslist = list;
		t.clear();
		t=null;

	}

	/**
	 * 判断该适应度对应的个体能否被选到父母数组中
	 * @param fitness 适应度
	 * @return  能否被选取的标志
	 */
	public boolean judgeFitness(double fitness){
        boolean flag = false;
        double temp = fitness;

		if(temp>=0&&temp<parentratefitness[0]){
			double tt = Math.random();
			if(tt<parentselectrate[0]) {
				flag = true;
			}

		}else if(temp>=parentratefitness[0]&&temp<parentratefitness[1]){
			double tt = Math.random();
			if(tt<parentselectrate[1]) {
				flag = true;
			}
		}
		else if(temp>=parentratefitness[1]&&temp<parentratefitness[2]){
			double tt = Math.random();
			if(tt<parentselectrate[2]) {
				flag = true;
			}
		}else if(temp>=parentratefitness[2]&&temp<parentratefitness[3]){
			double tt = Math.random();
			if(tt<parentselectrate[3]) {
				flag = true;
			}
		}else if(temp>=parentratefitness[3]){
			double tt = Math.random();
			if(tt<parentselectrate[4]) {
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 选择出400个父母个体一共遗传选择
	 */
	public void selectParentsList(){
		int i = 0;
		int num = 0;
		while(i<parentsnum){
			Individual indi = population.get(num%populationnum);
			double temp = indi.getFitness();
			if(temp>=0&&temp<parentratefitness[0]){
				double tt = Math.random();
				if(tt<parentselectrate[0]) {
					parentslist.add(indi);
					i++;
					num++;
				}

			}else if(temp>=parentratefitness[0]&&temp<parentratefitness[1]){
				double tt = Math.random();
				if(tt<parentselectrate[1]) {
					parentslist.add(indi);
					i++;
					num++;
				}
			}
			else if(temp>=parentratefitness[1]&&temp<parentratefitness[2]){
				double tt = Math.random();
				if(tt<parentselectrate[2]) {
					parentslist.add(indi);
					i++;
					num++;
				}
			}else if(temp>=parentratefitness[2]&&temp<parentratefitness[3]){
				double tt = Math.random();
				if(tt<parentselectrate[3]) {
					parentslist.add(indi);
					i++;
					num++;
				}
			}else if(temp>=parentratefitness[3]){
				double tt = Math.random();
				if(tt<parentselectrate[4]) {
					parentslist.add(indi);
					i++;
					num++;
				}
			}
			num++;

		}
	}

	/**
	 * 对次代种群的某些个体进行变异
	 */
	public void mutationLater(){
		mutationSelectType0();
		mutationSelectType1();
		mutationSelectType2();
		mutationSelectType3();
		mutationSelectType4();

	}
	public void mutationSelectType0(){
		for(Individual indi:swaptypelist0){
			int length = indi.getGene().length();
			if(Math.random()<mutationrate) {//发生基因突变
				mutationtype = (int)((Math.random()*3)+1);
				if(mutationtype==1) {
					indi.mutation(mutationtype,mutationnum);
				}else if(mutationtype==2) {
					int temp = mutationnum;
					while(length/8<(temp+4)) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}else if(mutationtype==3) {
					int temp = mutationnum;
					while((length/8+temp)>50) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}
			}
		}
	}
	public void mutationSelectType1(){
		for(Individual indi:swaptypelist1){
			int length = indi.getGene().length();
			if(Math.random()<mutationrate) {//发生基因突变
				mutationtype = (int)((Math.random()*3)+1);
				if(mutationtype==1) {
					indi.mutation(mutationtype,mutationnum);
				}else if(mutationtype==2) {
					int temp = mutationnum;
					while(length/8<(temp+4)) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}else if(mutationtype==3) {
					int temp = mutationnum;
					while((length/8+temp)>50) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}
			}
		}
	}
	public void mutationSelectType2(){
		for(Individual indi:swaptypelist2){
			int length = indi.getGene().length();
			if(Math.random()<mutationrate) {//发生基因突变
				mutationtype = (int)((Math.random()*3)+1);
				if(mutationtype==1) {
					indi.mutation(mutationtype,mutationnum);
				}else if(mutationtype==2) {
					int temp = mutationnum;
					while(length/8<(temp+4)) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}else if(mutationtype==3) {
					int temp = mutationnum;
					while((length/8+temp)>50) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}
			}
		}
	}
	public void mutationSelectType3(){
		for(Individual indi:swaptypelist3){
			int length = indi.getGene().length();
			if(Math.random()<mutationrate) {//发生基因突变
				mutationtype = (int)((Math.random()*3)+1);
				if(mutationtype==1) {
					indi.mutation(mutationtype,mutationnum);
				}else if(mutationtype==2) {
					int temp = mutationnum;
					while(length/8<(temp+4)) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}else if(mutationtype==3) {
					int temp = mutationnum;
					while((length/8+temp)>50) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}
			}
		}
	}
	public void mutationSelectType4(){
		for(Individual indi:swaptypelist4){
			int length = indi.getGene().length();
			if(Math.random()<mutationrate) {//发生基因突变
				mutationtype = (int)((Math.random()*3)+1);
				if(mutationtype==1) {
					indi.mutation(mutationtype,mutationnum);
				}else if(mutationtype==2) {
					int temp = mutationnum;
					while(length/8<(temp+4)) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}else if(mutationtype==3) {
					int temp = mutationnum;
					while((length/8+temp)>50) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}
			}
		}
	}
	/**
	 * 对种群中的某些个体进行变异
	 * 
	 */
	public void mutation() {
		for(Individual indi:population) {
			int length = indi.getGene().length();
			if(Math.random()<mutationrate) {//发生基因突变
				mutationtype = (int)((Math.random()*3)+1);
				if(mutationtype==1) {
					indi.mutation(mutationtype,mutationnum);
				}else if(mutationtype==2) {
					int temp = mutationnum;
					while(length/8<(temp+4)) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}else if(mutationtype==3) {
					int temp = mutationnum;
					while((length/8+temp)>50) {
						temp--;
					}
					indi.mutation(mutationtype,temp);
				}
			}
		}
	}
	public void printResult(){
		if(sqlinjectionindividual.isEmpty()){
			System.out.println("没有得到sql注入特征！");
		}else {
			System.out.println("sql注入特征测试用例：");
			for (Individual indi:sqlinjectionindividual){
				String gene = indi.getGene();
				String grgene = StringChangeGene.graycodeChangeGene(gene);
				String str = StringChangeGene.geneChangeString(grgene);
				System.out.println(str);
			}
		}
	}
	/**
	 * 计算对交叉算法改进的遗传操作
	 */
	public void startLaterIteration(){
		generation = 1;
		init();
		while(generation<iterationnum){
			inheritLater(generation);
			generation++;
		}
		printResult();
	 }
	/**
	 * 开始迭代函数
	 */
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
	public double getBestfitness() {
		return bestfitness;
	}
	public void setBestfitness(double bestscore) {
		this.bestfitness = bestscore;
	}
	public double getTotalfitness() {
		return totalfitness;
	}
	public void setTotalfitness(double totalscore) {
		this.totalfitness = totalscore;
	}
	public double getAveragefitness() {
		return averagefitness;
	}
	public void setAveragefitness(double averagescore) {
		this.averagefitness = averagescore;
	}
	public int getMutationtype() {
		return mutationtype;
	}
	public void setMutationtype(int mutationtype) {
		this.mutationtype = mutationtype;
	}
	public double getLowestfitness() {
		return lowestfitness;
	}
	public void setLowestfitness(double lowestscore) {
		this.lowestfitness = lowestscore;
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
