package edu.stanford.cs276;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.cs276.util.Dictionary;
import edu.stanford.cs276.util.Pair;

public class CandidateGenerator implements Serializable {


	private static CandidateGenerator cg_;
	
	// Don't use the constructor since this is a Singleton instance
	private CandidateGenerator() {}
	
	public static CandidateGenerator get() throws Exception{
		if (cg_ == null ){
			cg_ = new CandidateGenerator();
		}
		return cg_;
	}
	
	
	public static final Character[] alphabet = {
					'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
					'o','p','q','r','s','t','u','v','w','x','y','z',
					'0','1','2','3','4','5','6','7','8','9',
					' ',','};
	
	
	
	// Generate all candidates for the target query
	public static Set<String> getCandidates(String query, HashMap<String, String> commonSpellingErrorMap) throws Exception {
		Set<String> candidates = new HashSet<String>();	
		/*
		 * have 4 loop that to generate insertion, deleting, etc...
		*/
		//deletion
		for(int i=0; i < query.length(); ++i) {
		//	System.out.println(query.substring(0, i) + query.substring(i+1));
			//candidates.add(query.substring(0, i) + query.substring(i+1));
			String cd = query.substring(0, i) + query.substring(i+1);
			//candidates.add(cd);
			if(commonSpellingErrorMap != null){
				cd = commonSpellingCorrector(cd,commonSpellingErrorMap);
				//candidates.add(cd);
			}
			candidates.add(cd);
		}
		//transpose
		 for(int i=0; i < query.length()-1; ++i) {
		 //	System.out.println(query.substring(0, i) + query.substring(i+1, i+2) + query.substring(i, i+1) + query.substring(i+2));
			//candidates.add(query.substring(0, i) + query.substring(i+1, i+2) + query.substring(i, i+1) + query.substring(i+2));
			 
			 String cd = query.substring(0, i) + query.substring(i+1, i+2) + query.substring(i, i+1) + query.substring(i+2);
			 //candidates.add(cd);
			 if(commonSpellingErrorMap != null){
				 cd = commonSpellingCorrector(cd,commonSpellingErrorMap);
				 //candidates.add(cd);
			 }
			 candidates.add(cd);
				
			
		}
		
		//replace
		for(int i=0; i < query.length(); ++i) {
			for(char c : alphabet) {
				//	System.out.println(query.substring(0, i) + String.valueOf(c) + query.substring(i+1));
				//candidates.add(query.substring(0, i) + String.valueOf(c) + query.substring(i+1));
				
				String cd = query.substring(0, i) + String.valueOf(c) + query.substring(i+1);
				//candidates.add(cd);
				 if(commonSpellingErrorMap != null){
					 cd = commonSpellingCorrector(cd,commonSpellingErrorMap);
					 //candidates.add(cd);
				 }
				 candidates.add(cd);
				
			}
		}
		
		
		//insert
		for(int i=0; i <= query.length(); ++i) {
			for(char c : alphabet) {
				//	System.out.println(query.substring(0, i) + String.valueOf(c) + query.substring(i));
				//candidates.add(query.substring(0, i) + String.valueOf(c) + query.substring(i));
				
				String cd =query.substring(0, i) + String.valueOf(c) + query.substring(i);
				//candidates.add(cd);
				if(commonSpellingErrorMap != null){
					cd = commonSpellingCorrector(cd,commonSpellingErrorMap);
					//candidates.add(cd);
				}
				candidates.add(cd);
				
				
			}
		}
		
		/*
		 * Your code here
		 */
		//System.out.println("count: "+count);
		//System.out.println("Candidates: "+candidates.size());
		
		/*
		if(commonSpellingErrorMap != null){
			StringBuilder sbCorrectedCandidate = new StringBuilder();
			String curr="";
			Pattern p = Pattern.compile("\\w+");
			Matcher m = p.matcher(query.toLowerCase());
			while(m.find()){
				curr = m.group();
				
				if(commonSpellingErrorMap.containsKey(curr)){
					sbCorrectedCandidate.append(" "+commonSpellingErrorMap.get(curr));
					System.out.println("[Common]:\t"+curr+"\t"+commonSpellingErrorMap.get(curr));
				}else{
					sbCorrectedCandidate.append(" "+curr);
				}
			}
			System.out.println("Common Spelling Error Correction: "+sbCorrectedCandidate.toString().trim());
			candidates.add(sbCorrectedCandidate.toString().trim());
			
		}
		*/
		
		return candidates;
	}
	
	public static String commonSpellingCorrector(String candidate, HashMap<String, String> commonSpellingErrorMap){
		
		boolean corrected = false;
		StringBuilder sbCorrectedCandidate = new StringBuilder();
		String curr="";
		Pattern p = Pattern.compile("\\w+");
		Matcher m = p.matcher(candidate.toLowerCase());
		while(m.find()){
			curr = m.group();
			if(commonSpellingErrorMap.containsKey(curr)){
				sbCorrectedCandidate.append(" "+commonSpellingErrorMap.get(curr));
				corrected = true;
				//System.out.println("[Common]:\t"+curr+"\t"+commonSpellingErrorMap.get(curr));
			}else{
				sbCorrectedCandidate.append(" "+curr);
			}
		}
		
		if(corrected){
			return sbCorrectedCandidate.toString().trim();
		}else{
			return candidate;
		}
		
		
	}
	
	public static Set<String> getCandidates(Set<String> candidatesList, Dictionary dict) throws Exception {
		Set<String> candidates = new HashSet<String>();	
		for(String candidate : candidatesList) {
			for(String nextEdit : getCandidates(candidate, dict)) {
				candidates.add(nextEdit);
			}
		}
		return candidates;
	}
	
	public static boolean isValid(String candidate, Dictionary dict) {
		boolean answer = true;
		Pattern p = Pattern.compile("\\w+");
		String curr="";
			Matcher m = p.matcher(candidate.toLowerCase());
			while(m.find()) 
				{
				//System.out.println("adding to unigrams: "+curr);
				curr = m.group();
				if(dict.count(curr)==0) {
					answer = false;
					break;
				}
			}
		return answer;
	}

	public static Set<String> getCandidates(String query, Dictionary dict) throws Exception {
		Set<String> candidates = new HashSet<String>();	
		/*
		 * have 4 loop that to generate insertion, deleting, etc...
		*/
		//deletion
		String candidate = "";
		for(int i=0; i < query.length(); ++i) {
			candidate = query.substring(0, i) + query.substring(i+1);
			if(isValid(candidate,dict)) {
			candidates.add(candidate);
			}
		}
		//transpose
		 for(int i=0; i < query.length()-1; ++i) {
			 candidate = query.substring(0, i) + query.substring(i+1, i+2) + query.substring(i, i+1) + query.substring(i+2);
			 if(isValid(candidate,dict)) {
					candidates.add(candidate);
					}
		}
		
		//replace
		for(int i=0; i < query.length(); ++i) {
			for(char c : alphabet) {
			candidate = query.substring(0, i) + String.valueOf(c) + query.substring(i+1);
			if(isValid(candidate,dict)) {
				candidates.add(candidate);
				}
			}
		}
		
		
		//insert
		for(int i=0; i <= query.length(); ++i) {
			for(char c : alphabet) {
			candidate = query.substring(0, i) + String.valueOf(c) + query.substring(i);
			if(isValid(candidate,dict)) {
				candidates.add(candidate);
				}
			}
		}
		
		/*
		 * Your code here
		 */
		//System.out.println("count: "+count);
		//System.out.println("Candidates: "+candidates.size());
		
		return candidates;
	}
	
	public static Set<String> getCandidatesFromCommonSpellingErrors(String query, HashMap<String, String> commonSpellingErrorMap){
		Set<String> candidates = new HashSet<String>();	
		
		
		
		
		return candidates;
	}

}
