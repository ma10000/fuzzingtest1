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
 *����ʵ���Ŵ��㷨����
 */
public class GeneticAchieve {
	
	private int populationnum = 1000;//��Ⱥ����
	private ArrayList<Individual> population = new ArrayList<Individual>();//��ʼ��Ⱥ����
	private int parentsnum = 400;//���ɵĸ�ĸ���������
	private double[] parentratefitness = {1,1.5,2,2.5};//����ֵ����Ӧ�ȷ�Ϊ�������
	private double[] parentselectrate = {0.1,0.3,0.5,0.7,0.9};//�ֱ��Ӧ����Ϊ0��1��2��ѡ�����
	private ArrayList<Individual> parentslist = new ArrayList<Individual>();//��ĸ����������

	private int iterationnum = 100;//��Ⱥ��������
	private int generation = 1;//��ǰ��������

	private double[] birthselecttyperate = {0.2,0.2,0.2,0.2,0.2};//ѡ��ͬ�Ļ��򽻻�ԭ��ĳ�ʼ����
    private ArrayList<Individual> swaptypelist0 =new ArrayList<Individual>();//���ڴ洢�������ཻ��0����һ������
	private ArrayList<Individual> swaptypelist1 =new ArrayList<Individual>();//���ڴ洢�������ཻ��1����һ������
	private ArrayList<Individual> swaptypelist2 =new ArrayList<Individual>();//���ڴ洢�������ཻ��2����һ������
	private ArrayList<Individual> swaptypelist3 =new ArrayList<Individual>();//���ڴ洢�������ཻ��3����һ������
	private ArrayList<Individual> swaptypelist4 =new ArrayList<Individual>();//���ڴ洢�������ཻ��4����һ������

	private int mutationtype = 0;//��������
	private int mutationnum = 2;//������ַ���
	private double mutationrate = 0.001;//����ĸ���
	
	private double bestfitness=0;//�����Ӧ��
	private double totalfitness=0;//�ܵ���Ӧ��
	private double averagefitness=0;//ƽ����Ӧ��
	private double lowestfitness=0;//�����Ӧ��
	
	private Individual bestindividual=null;//��õķֵĸ���
	private ArrayList<String> path = new ArrayList<String>();//��¼�µ�·��
	private HashMap<String,Individual> pathindividualmap = new HashMap<String,Individual>();//���ڴ洢��һ�η�����·���ĸ���
	private ArrayList<Individual> sqlinjectionindividual = new ArrayList<Individual>();//���ڼ�¼sqlע��ĸ���
	
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
	 * ��ʼ����Ⱥ
	 */

	public void initialisePopulation() {
		for(int i=0;i<populationnum;i++) {
			Individual indi = new Individual();
			boolean flage = true;
			String geneticStr = null;//���ܻ���ֿ�ָ�����
			while(flage) {
				geneticStr = GenerateFuzzing.generateCase();
				if(geneticStr.length()>=4) {
					flage = false;
				}
			}
			System.out.println(i+"��������"+geneticStr);
			String geneticAscii = StringChangeGene.stringChangeGene(geneticStr);
			String geneticGcode = StringChangeGene.geneChangeGraycode(geneticAscii);
			indi.setGene(geneticGcode);
			population.add(indi);
			
		}
		
	}

	/**
	 * ����δ�֮��ĸ���·��
	 */
	public void calculatelaterRoute(){
		int listsizetype0 = swaptypelist0.size();
		int listsizetype1 = swaptypelist1.size();
		int listsizetype2 = swaptypelist2.size();
		int listsizetype3 = swaptypelist3.size();
		int listsizetype4 = swaptypelist4.size();
         //�����һ�ֻ��򽻻���ʽ��·��
		for(int i=0;i<listsizetype0;i++){
			Individual genetindividual = swaptypelist0.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist0.get(i).setRoute(list);
		}
        //����ڶ��ֻ��򽻻���ʽ��·��
		for(int i=0;i<listsizetype1;i++){
			Individual genetindividual = swaptypelist1.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist1.get(i).setRoute(list);
		}
		//��������ֻ��򽻻���ʽ��·��
		for(int i=0;i<listsizetype2;i++){
			Individual genetindividual = swaptypelist2.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist2.get(i).setRoute(list);
		}
		//��������ֻ��򽻻���ʽ��·��
		for(int i=0;i<listsizetype3;i++){
			Individual genetindividual = swaptypelist3.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist3.get(i).setRoute(list);
		}
		//��������ֻ��򽻻���ʽ��·��
		for(int i=0;i<listsizetype4;i++){
			Individual genetindividual = swaptypelist4.get(i);
			ArrayList<String> list = calculateSomeRoute(genetindividual);
			swaptypelist4.get(i).setRoute(list);
		}
	}

	/**
	 * �����Ӧ�����·��
	 * @param indi ���뺯���ĸ���
	 * @return  ���ض�Ӧ�����·��
	 */
	public ArrayList<String> calculateSomeRoute(Individual indi){
		ArrayList<String> slist = new ArrayList<String>();

		String geneticGcode = indi.getGene();
		String geneticAscii = StringChangeGene.graycodeChangeGene(geneticGcode);
		String geneticStr = StringChangeGene.geneChangeString(geneticAscii);
		String genestr = "\""+geneticStr+"\"";
		String[] list = ExecuteFuzzing.execute(genestr);
		System.out.println("�ڵ�"+generation+"�Ŵ�����Ⱥ�жԲ���������"+geneticStr+" ���в��ԡ�");

		if(list != null) {
			boolean flag = false;
			for (int m = 0; m < list.length; m++) {
				if (list[m].contains("sqlע��")) {
					flag = true;
				}
			}
			if (flag) {
				sqlinjectionindividual.add(indi);
				System.out.println("�ڵ�" + (generation + 1) + "�Ŵ�����Ⱥ�з���sqlע��ۼ���" + geneticStr + "��");
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
	 * ����ÿ��������������Ⱥ�еĸ��壩��·��
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
			System.out.println("�ڵ�"+generation+"�Ŵ�����Ⱥ�жԵ�"+i+"������������"+geneticStr+" "+geneticreal+"���в��ԡ�");
			ArrayList<String> slist = new ArrayList<String>();
			if(list != null) {
				boolean flag = false;
				for(int m=0;m<list.length;m++) {
					if(list[m].contains("sqlע��")) {
						flag = true;
					}
				}
				if(flag) {
					sqlinjectionindividual.add(population.get(i));
					System.out.println("�ڵ�"+(generation+1)+"�Ŵ�����Ⱥ�з���sqlע��ۼ���"+geneticStr+"��");
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
	 * �����һ�����������ķ���
	 */
	public void calculateLaterScore(){
		int listsizetype0 = swaptypelist0.size();
		int listsizetype1 = swaptypelist1.size();
		int listsizetype2 = swaptypelist2.size();
		int listsizetype3 = swaptypelist3.size();
		int listsizetype4 = swaptypelist4.size();
		//�����һ�ֻ��򽻻���ʽ��·��
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
	 * ����ÿ��������������Ⱥ�еĸ��壩�ķ���
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
	 * �����ʼ��Ⱥ����Ӧ��
	 */
	public void calculateFitness(){
		for(int i=0;i<population.size();i++) {
		    population.get(i).calculateFitness();
		}
	}

	/**
	 * �������֮����Ⱥ����Ӧ��
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
	 * ���㵱ǰ��Ⱥ�е���ͷ�������Ӧ�ø���
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
	 * ����δ�֮�����͵÷�
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
	 * ���㵱ǰ��Ⱥ���ܷ���
	 */
	public void calculateTotalFitness() {
		double sum = 0;
		for(int i=0;i<population.size();i++) {
			sum += population.get(i).getFitness();
		}
		totalfitness = sum;
	}

	/**
	 * �������һ��������Ӧ��
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
	 * ���㵱ǰ��Ⱥ��ƽ������
	 */
	public void calculateAverageFitness() {
		averagefitness = totalfitness/populationnum;
	}
	/**
	 * ������õĳɼ�����Ӧ�ĸ���
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
	 * ����δ�֮��������Ӧ�ȺͶ�Ӧ�ĸ���
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
	 *����δ�֮�����һ����Ⱥ�ķ���
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
	 * ���ڲ�����һ����Ⱥ�ķ���
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
		//����һ����Ⱥ�滻��һ����Ⱥ
		List<Individual> t = population;
		population = childpopulation;
		t.clear();
		t=null;
		//����ͻ��
		
	
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
	 * �����Ŵ�����
	 */
	public void inherit() {
		selectParentsList();
		produceGeneration();
		mutation();
		//���㵱ǰ�����·���ͷ���
		calculateRoute();
		calculateScore();
		calculateTotalFitness();
		calculateAverageFitness();
		calculateBestFitness();
		calculateLowestFitness();
	}
	/**
	 * ���߲�ͬ����Ӧ��
	 * @return ����ѡ��ĸ������
	 */
	public Individual getParentIndividual() {
		int tt = (int)(Math.random()*400);
		return parentslist.get(tt);
	}

	/**
	 * �����µ�birthselecttyperate����ɵ�birthselecttyperate
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
	 * ����ÿ�ཻ����������Ӧ�ȴ��ڵ���2�ĸ���
	 * @param list �������������
	 * @return  ��������Ӧ�ȴ������2 �ĸ���
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
	 * ѡ��δ�֮��ĸ�ĸ�ĸ���
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
	 * �жϸ���Ӧ�ȶ�Ӧ�ĸ����ܷ�ѡ����ĸ������
	 * @param fitness ��Ӧ��
	 * @return  �ܷ�ѡȡ�ı�־
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
	 * ѡ���400����ĸ����һ���Ŵ�ѡ��
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
	 * �Դδ���Ⱥ��ĳЩ������б���
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
			if(Math.random()<mutationrate) {//��������ͻ��
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
			if(Math.random()<mutationrate) {//��������ͻ��
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
			if(Math.random()<mutationrate) {//��������ͻ��
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
			if(Math.random()<mutationrate) {//��������ͻ��
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
			if(Math.random()<mutationrate) {//��������ͻ��
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
	 * ����Ⱥ�е�ĳЩ������б���
	 * 
	 */
	public void mutation() {
		for(Individual indi:population) {
			int length = indi.getGene().length();
			if(Math.random()<mutationrate) {//��������ͻ��
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
			System.out.println("û�еõ�sqlע��������");
		}else {
			System.out.println("sqlע����������������");
			for (Individual indi:sqlinjectionindividual){
				String gene = indi.getGene();
				String grgene = StringChangeGene.graycodeChangeGene(gene);
				String str = StringChangeGene.geneChangeString(grgene);
				System.out.println(str);
			}
		}
	}
	/**
	 * ����Խ����㷨�Ľ����Ŵ�����
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
	 * ��ʼ��������
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
