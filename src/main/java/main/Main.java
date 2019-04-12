package main;

import java.util.ArrayList;

import createfuzzing.GenerateFuzzing;
import creategene.StringChangeGene;
import execute.ExecuteFuzzing;
import execute.Executecmd;
import geneticachieve.GeneticAchieve;
import geneticoperate.geneticKid;
import population.Individual;

public class Main {
	public static void main(String[] args) {
		
		

		
		ExecuteFuzzing ef = new ExecuteFuzzing();
		ef.executeFuzzing();
	//	GeneticAchieve ga = new GeneticAchieve();
//		ga.startIteration();
//		for(int m=1;m<257;m++) {
//			char tem = (char)m;			String tstr = "asdfd"+tem+"najhk";
//			System.out.println("m:"+m+" "+tstr);
//			String[] list = ExecuteFuzzing.execute(tstr);
//			for(int i=0;i<list.length;i++) {
//				System.out.println(list[i]);
//			}
//		}
//		String tem = "a";
//		for(int m=1;m<250;m++) {
//			
//			String ch = "c";
//			System.out.println("m:"+m+" "+tem);
//			String[] list = ExecuteFuzzing.execute(tem);
//			for(int i=0;i<list.length;i++) {
//				System.out.println(list[i]);
//			}
//			StringBuffer sb = new StringBuffer(tem);
//			sb.insert(0,ch);
//			tem = sb.toString();
//		}
		
		

	}


}
