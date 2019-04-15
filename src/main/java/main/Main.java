package main;

import java.util.ArrayList;
import java.util.List;


import createfuzzing.GenerateFuzzing;
import creategene.StringChangeGene;
import execute.ExecuteFuzzing;
import execute.Executecmd;
import geneticachieve.GeneticAchieve;
import geneticoperate.geneticKid;
import org.apache.log4j.Logger;
import population.Individual;

public class Main {



	public static void main(String[] args) {

		 String[] singlechars = { ".*[hH].*", ".*[xX].*", ".*[mM].*", ".*[sS].*", ".*[lL].*", ".*[cC].*", ".*[tT].*", ".*[iI].*", ".*[nN].*", ".*[rR].*", ".*[dD].*", ".*[oO].*", ".*[uU].*", ".*[pP].*", ".*[bB].*", ".*[aA].*", ".*[wW].*",
				".*_.*", ".* .*", ".*\\(.*"};

		String[] nosuchchars = {"!", "\"","#", "$", "%", "&", "'",")", "*", "+", ",", "-",".", "/", "0",
		"1", "2", "3", "4", "5", "6", "7", "8", "9",":",";", "<", "=", ">", "?", "@", "E", "F", "G", "J",
				"K", "Q", "V", "Y", "Z","[","\\","]", "^", "`", "e", "f", "g", "j", "k", "q", "v", "y", "z", "{", "|","}", "~"};
//		ExecuteFuzzing ef = new ExecuteFuzzing();
//		ef.executeFuzzing();
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
//		ArrayList<String> temp = new ArrayList<String>();
//		 for(int i=32;i<127;i++){
//		 	boolean flag = true;
//		 	for(int m=0;m<singlechars.length;m++) {
//				if (String.valueOf((char) i).matches(singlechars[m])){
//					flag = false;
//				}
//			}
//		 	if(flag){
//		 		temp.add(String.valueOf((char) i));
//			}
//		 }
//		System.out.println(singlechars.length);
//		 for(int i=0;i<temp.size();i++){
//			 System.out.println(temp.get(i));
//



		

	}


}
