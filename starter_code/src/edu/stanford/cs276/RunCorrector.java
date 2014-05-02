package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunCorrector {

	public static LanguageModel languageModel;
	public static NoisyChannelModel nsm;
	public static double epsilon = 0.25;
	
	public static HashMap<String, String> commonSpellingErrorMap = null;

	public static Set<String> prunning(Set<String> candidates, int num) {
		Set<String> result = new HashSet<String>();
		
		Pattern p = Pattern.compile("\\w+");
		for(String candidate : candidates) {
			String curr="";
			int nonWord = 0;
			Matcher m = p.matcher(candidate.toLowerCase());
			while(m.find()) 
				{
				//System.out.println("adding to unigrams: "+curr);
				curr = m.group();
				//if("vassal".equalsIgnoreCase(curr)){
					//System.out.println("Checking for: "+curr+"    P_MLE="+languageModel.P_MLE(curr));
				//}
				if(languageModel.P_MLE(curr)==0) {
					nonWord++;
				}
				if(nonWord==num) {
					break;
				}
				}
			if(nonWord<num) {
				result.add(candidate);
			}		
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		
		long startTime = System.currentTimeMillis();
		
		// Parse input arguments
		String uniformOrEmpirical = null;
		String queryFilePath = null;
		String goldFilePath = null;
		String extra = null;
		BufferedReader goldFileReader = null;
		BufferedReader commonSpellingErrorsFileReader = null;
		boolean commonSpellingErrorModel = false;
		if (args.length == 2) {
			// Run without extra and comparing to gold
			uniformOrEmpirical = args[0];
			queryFilePath = args[1];
		}
		else if (args.length == 3) {
			uniformOrEmpirical = args[0];
			queryFilePath = args[1];
			if (args[2].equals("extra")) {
				extra = args[2];
			} else {
				goldFilePath = args[2];
			}
		} 
		else if (args.length == 4) {
			uniformOrEmpirical = args[0];
			queryFilePath = args[1];
			extra = args[2];
			goldFilePath = args[3];
		}
		else {
			System.err.println(
					"Invalid arguments.  Argument count must be 2, 3 or 4" +
					"./runcorrector <uniform | empirical> <query file> \n" + 
					"./runcorrector <uniform | empirical> <query file> <gold file> \n" +
					"./runcorrector <uniform | empirical> <query file> <extra> \n" +
					"./runcorrector <uniform | empirical> <query file> <extra> <gold file> \n" +
					"SAMPLE: ./runcorrector empirical data/queries.txt \n" +
					"SAMPLE: ./runcorrector empirical data/queries.txt data/gold.txt \n" +
					"SAMPLE: ./runcorrector empirical data/queries.txt extra \n" +
					"SAMPLE: ./runcorrector empirical data/queries.txt extra data/gold.txt \n");
			return;
		}
		
		if (goldFilePath != null ){
			goldFileReader = new BufferedReader(new FileReader(new File(goldFilePath)));
		}
		
		if ("extra".equals(extra)) {
			System.out.println("Loading Expanded edit model......");
			commonSpellingErrorModel = true;
			//commonSpellingErrorsFileReader = new BufferedReader(new FileReader(new File(extra)));
			commonSpellingErrorsFileReader = new BufferedReader(new FileReader(new File("./data/commonspellingerrors.txt")));
			commonSpellingErrorMap = new HashMap<String, String>();
			String line = null;
			while ((line = commonSpellingErrorsFileReader.readLine()) != null) {
				String[] tokens = line.trim().split("\\s+");
				if(tokens.length == 2){
					commonSpellingErrorMap.put(tokens[0], tokens[1]);
				}
				
			}
			//System.out.println("Common Spelling file loaded......");
		}
		
		
		// Load models from disk
		languageModel = LanguageModel.load(); 
		nsm = NoisyChannelModel.load();
		nsm.setProbabilityType(uniformOrEmpirical);
		BufferedReader queriesFileReader = new BufferedReader(new FileReader(new File(queryFilePath)));
		//String output = "/Users/ethomas35/SCPD/PA2";
		String output = "/Users/gupsumit/dev/Stanford/cs276/pa/pa2_git/SCPD-PA2";
		//BufferedWriter edits2Writer = new BufferedWriter(new FileWriter(new File(output, "experiments.txt")));
		for(double x = 0.25; x<=0.25; x+=0.1)
		{
		epsilon = x;
		for(double lambda = 0.1; lambda<=0.1; lambda+=0.25)
		{
			languageModel.setLambda(lambda);	
		for(double uniProb = 0.1; uniProb<=0.1; uniProb+=0.25)
		{
			nsm.uniformCostModel.setUniformProb(uniProb);	
		//goldFileReader = new BufferedReader(new FileReader(new File(goldFilePath)));
		queriesFileReader = new BufferedReader(new FileReader(new File(queryFilePath)));
		int totalCount = 0;
		int yourCorrectCount = 0;
		String query = null;
		
		/*
		 * Each line in the file represents one query.  We loop over each query and find
		 * the most likely correction
		 */

		int i=0;
		double score = 0;
		while ((query = queriesFileReader.readLine()) != null) {
			Set<String> candidates = CandidateGenerator.getCandidates(query,commonSpellingErrorMap);
			//Set<String> candidates = CandidateGenerator.getCandidates(query);
			//System.out.println("Candidates in corrector: "+candidates.size());
			
			candidates = prunning(candidates,1);
			//System.out.println("Candidates after prunning: "+candidates.size());
			//Set<String> edits2 = CandidateGenerator.getCandidates(candidates,languageModel.getUnigrams());
			//System.out.println("edits2: "+edits2.size());
			//edits2 = prunning(edits2,1);
			//System.out.println("edits2 after prunning: "+edits2.size());
			String correctedQuery = query;
			score = 0;
			//System.out.println("Query: "+query);
			
			for(String candidate : candidates) {
				double curr = nsm.P(query, candidate, 1)*Math.pow(languageModel.P(candidate),epsilon);
				//System.out.println("[candidate, score]: \t"+candidate+"\t"+curr);
				// edits2Writer.write(curr+"\t"+candidate+"\n");
				//System.out.println(curr>score);
				//System.exit(0);
				if(curr>score) {
					score = curr;
					correctedQuery = candidate.replaceAll(" +", " ");
				//System.out.println("candidate: "+candidate+";\tLanguage Model: "+languageModel.P(candidate));
				//if(candidate.equalsIgnoreCase("quad quad cache controller")) {
				//	System.out.println("found");
				}
			}
			//System.out.println(score);
			//System.out.println("Candidate: "+correctedQuery);
			/*
			if(i==1)
			System.exit(0);
			i++;*/
			
			/*
			 * Generate Candidates
			 * Score using a function of noisy channel + language model
			 * Corrected Query = best query
			 */
			/*
			 * Your code here
			 */
			
			
			if ("extra".equals(extra)) {
				/*
				 * If you are going to implement something regarding to running the corrector, 
				 * you can add code here. Feel free to move this code block to wherever 
				 * you think is appropriate. But make sure if you add "extra" parameter, 
				 * it will run code for your extra credit and it will run you basic 
				 * implementations without the "extra" parameter.
				 */	
			}
			

			// If a gold file was provided, compare our correction to the gold correction
			// and output the running accuracy
			if (goldFileReader != null) {
				String goldQuery = goldFileReader.readLine();
				//System.out.println("Correct: "+goldQuery);
				if (goldQuery.equals(correctedQuery)) {
					yourCorrectCount++;
				}else{
					/*
					System.out.println("-----------------------");
					System.out.println(query);
					System.out.println(correctedQuery);
					System.out.println(goldQuery);
					System.out.println("-----------------------");
					*/
					//System.out.println();
				}
				totalCount++;
			}
		}
		queriesFileReader.close();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("RUNNING TIME: "+totalTime/1000+" seconds ");
		System.out.println("Total Query matched = "+yourCorrectCount);
		System.out.println("score: "+score+"\tepsilon: "+((double)Math.round(epsilon*100))/100+"\t"+"lambda: "+((double)Math.round(lambda*100))/100+"\t"+"uniProb: "+((double)Math.round(uniProb*100))/100+"\t"+(100*yourCorrectCount/totalCount)+"%");
		//edits2Writer.write("epsilon: "+((double)Math.round(epsilon*100))/100+"\t"+"lambda: "+((double)Math.round(lambda*100))/100+"\t"+"uniProb: "+((double)Math.round(uniProb*100))/100+"\t"+(100*yourCorrectCount/totalCount)+"%\n");
		}
	   }
	  }

    //edits2Writer.close();
	}
}
