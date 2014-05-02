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
	
	public static void recordEdit(String[] editType) {
		String x = editType[1];
		String y = editType[2];
		char edit = editType[0].charAt(0);
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
	
	public static String[] editType(String noisy, String clean) {
		//System.out.println("noisy: "+noisy+"\tclean: "+clean);
		//count the number of letters and char bigrams
		String[] answer = new String[3];
		if(noisy.equalsIgnoreCase(clean))
		{
			answer[0] = String.valueOf(noEdit);
			return answer;
		}
		
		String edit = "";
		String x = "";
		String w = "";
		
		int firstDiffIndex = 0;
		
		while(firstDiffIndex<clean.length() && firstDiffIndex<noisy.length()) {
			if(clean.charAt(firstDiffIndex)!=noisy.charAt(firstDiffIndex)) {break;}
			firstDiffIndex++;	
			}
		
		if(noisy.length()>clean.length()) {
			edit = String.valueOf(ins);
			x=String.valueOf(noisy.charAt(firstDiffIndex));
			if(firstDiffIndex>0) {
				w=String.valueOf(clean.charAt(firstDiffIndex-1));
			}
			else { w = " ";}
		//	System.out.println("ins["+x+","+y+"] ("+x+" as "+x+""+y+")");	
		}
		else if(noisy.length()<clean.length()) {
			edit = String.valueOf(del);
			if(firstDiffIndex>0) {
				x=String.valueOf(noisy.charAt(firstDiffIndex-1));
				w=String.valueOf(clean.charAt(firstDiffIndex-1))+String.valueOf(clean.charAt(firstDiffIndex));
			}
			else {
				x=" ";
				w=" "+String.valueOf(clean.charAt(firstDiffIndex));
			}
		//	System.out.println("del["+x+","+y+"] ("+x+""+y+" as "+x+")");
		}
		else {
			if(firstDiffIndex==noisy.length()-1 || noisy.charAt(firstDiffIndex+1)==clean.charAt(firstDiffIndex+1)) {
				edit = String.valueOf(sub);
				x=String.valueOf(noisy.charAt(firstDiffIndex));
				w=String.valueOf(clean.charAt(firstDiffIndex));
			//	System.out.println("sub["+x+","+y+"] ("+x+" as "+y+")");	
			}
			else {
				edit = String.valueOf(trans);
				x=String.valueOf(noisy.charAt(firstDiffIndex))+String.valueOf(noisy.charAt(firstDiffIndex+1));
				w=String.valueOf(clean.charAt(firstDiffIndex))+String.valueOf(clean.charAt(firstDiffIndex+1));
			//	System.out.println("stran["+x+","+y+"] ("+x+""+y+" as "+y+""+x+")");
			}
		}
		
		answer[0] = edit;
		answer[1] = x;
		answer[2] = w;
		
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
			String[] editType = editType(noisy,clean);
			
			recordEdit(editType);
			
			for(int i=0; i<clean.length(); i++) { //TODO
				COUNT.add(String.valueOf(clean.charAt(i)));	
				if(i==0) {
					COUNT.add(" "+String.valueOf(clean.charAt(i)));
				}
				else {
					COUNT.add(clean.substring(i-1, i+1));
				}
				}
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
		
		int A = CandidateGenerator.alphabet.length;
		String[] editType = editType(original, R);
		String x = editType[1];
		String w = editType[2];
		char edit = editType[0].charAt(0);
			if(edit==ins) {
				answer = (double)(INS.count(x.charAt(0)+x)+1)/(COUNT.count(String.valueOf(x.charAt(0)))+A);
			} 
			else if(edit==del) {
				answer = (double)(DEL.count(w)+1)/(COUNT.count(w)+A);
				//DEL[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
			else if(edit==sub) {
				answer = (double)(SUB.count(x+w)+1)/(COUNT.count(w)+A);
				//SUB[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
			else if(edit==trans) {
				answer = (double)(TRANS.count(w)+1)/(COUNT.count(w)+A);
				//TRANS[alphabet.indexOf(x)][alphabet.indexOf(y)]++;
			} 
		//System.out.println(answer+"\t"+original+"\t"+R+"\t"+edit+"\t");
		return answer;
		/*
		 * Your code here
		 */
	}
}
