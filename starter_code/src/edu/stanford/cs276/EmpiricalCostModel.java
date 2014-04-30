package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import edu.stanford.cs276.util.Dictionary;

public class EmpiricalCostModel implements EditCostModel{
	final public static char noEdit = '0';
	final public static char ins = '1';
	final public static char del = '2'; 
	final public static char sub = '3';
	final public static char trans = '4';
	
	public static Dictionary INS = new Dictionary();
	public static Dictionary DEL = new Dictionary();
	public static Dictionary SUB = new Dictionary();
	public static Dictionary TRANS = new Dictionary();
	public static Dictionary COUNT = new Dictionary();
	
	public static void recordEdit(char[] editType) {
		String x = String.valueOf(editType[1]);
		String y = String.valueOf(editType[2]);
		char edit = editType[0];
			if(edit==ins) {
				INS.add(x+y);
				//INS[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
			else if(edit==del) {
				DEL.add(x+y);
				//DEL[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
			else if(edit==sub) {
				SUB.add(x+y);
				//SUB[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
			else if(edit==trans) {
				TRANS.add(x+y);
				//TRANS[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
	}
	
	public static char[] editType(String noisy, String clean) {
		//System.out.println("noisy: "+noisy+"\tclean: "+clean);
		//count the number of letters and char bigrams
		for(int i=0; i<clean.length(); i++) { //TODO
		COUNT.add(String.valueOf(clean.charAt(i)));	
		if(i==0) {
			COUNT.add(" "+String.valueOf(clean.charAt(i)));
		}
		else {
			COUNT.add(clean.substring(i-1, i+1));
		}
		}
		
		char[] answer = new char[3];
		if(noisy.equalsIgnoreCase(clean))
		{
			answer[0] = noEdit;
			answer[1] = '#';
			answer[2] = '#';
			return answer;
		}
		
		char editType = noEdit;
		char x = ' ';
		char y = ' ';
		int firstDiffIndex = 0;
		
		while(firstDiffIndex<clean.length() && firstDiffIndex<noisy.length()) {
			if(clean.charAt(firstDiffIndex)!=noisy.charAt(firstDiffIndex)) {break;}
			firstDiffIndex++;	
			}
		
		if(noisy.length()>clean.length()) {
			editType = ins;
			y=noisy.charAt(firstDiffIndex);
			if(firstDiffIndex>0) {
				x=clean.charAt(firstDiffIndex-1);
			}
		//	System.out.println("ins["+x+","+y+"] ("+x+" as "+x+""+y+")");	
		}
		else if(noisy.length()<clean.length()) {
			editType = del;
			y=clean.charAt(firstDiffIndex);
			if(firstDiffIndex>0) {
				x=clean.charAt(firstDiffIndex-1);
			}
		//	System.out.println("del["+x+","+y+"] ("+x+""+y+" as "+x+")");
		}
		else if(firstDiffIndex==noisy.length()-1) {
			editType = sub;
			y=noisy.charAt(firstDiffIndex);
			x=clean.charAt(firstDiffIndex);
		//	System.out.println("sub["+x+","+y+"] ("+x+" as "+y+")");
		}
		else {
			if(noisy.charAt(firstDiffIndex+1)==clean.charAt(firstDiffIndex+1)) {
				editType = sub;
				y=noisy.charAt(firstDiffIndex);
				x=clean.charAt(firstDiffIndex);
			//	System.out.println("sub["+x+","+y+"] ("+x+" as "+y+")");	
			}
			else {
				editType = trans;
				y=noisy.charAt(firstDiffIndex);
				x=clean.charAt(firstDiffIndex);
			//	System.out.println("stran["+x+","+y+"] ("+x+""+y+" as "+y+""+x+")");
			}
		}
		
		answer[0] = editType;
		answer[1] = x;
		answer[2] = y;
		
		return answer;
	}
	
	public EmpiricalCostModel(String editsFile) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(editsFile));
		System.out.println("Constructing edit distance map...");
		String line = null;
		while ((line = input.readLine()) != null) {
			Scanner lineSc = new Scanner(line);
			lineSc.useDelimiter("\t");
			String noisy = lineSc.next();
			String clean = lineSc.next();
			char[] editType = editType(noisy,clean);
			
			recordEdit(editType);
			
			
			// Determine type of error and record probability
			/*
			 * Your code here
			 */
		}

		input.close();
		System.out.println("Done.");
	}

	// You need to update this to calculate the proper empirical cost
	@Override
	public double editProbability(String original, String R, int distance) {
		double answer = 0.95;
		
		char[] editType = editType(original, R);
		String x = String.valueOf(editType[1]);
		String y = String.valueOf(editType[2]);
		char edit = editType[0];
			if(edit==ins) {
				answer = (double)(INS.count(x+y)+1)/(COUNT.count(x)+26);
			} 
			else if(edit==del) {
				answer = (double)(DEL.count(x+y)+1)/(COUNT.count(x+y)+26);
				//DEL[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
			else if(edit==sub) {
				answer = (double)(SUB.count(x+y)+1)/(COUNT.count(y)+26);
				//SUB[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
			else if(edit==trans) {
				answer = (double)(TRANS.count(x+y)+1)/(COUNT.count(x+y)+26);
				//TRANS[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
		System.out.println(answer+"\t"+original+"\t"+R+"\t"+edit+"\t");
		return answer;
		/*
		 * Your code here
		 */
	}
}
