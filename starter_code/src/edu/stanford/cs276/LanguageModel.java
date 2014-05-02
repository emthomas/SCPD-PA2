package edu.stanford.cs276;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.cs276.util.Dictionary;


public class LanguageModel implements Serializable {

	private static LanguageModel lm_;
	private Dictionary dictUnigrams = new Dictionary();
	private Dictionary dictBigrams = new Dictionary();
	private double lambda = 0.1;
	/* Feel free to add more members here.
	 * You need to implement more methods here as needed.
	 * 
	 * Your code here ...
	 */
	
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	public Dictionary getUnigrams() {
		return this.dictUnigrams;
	}
	
	public double P_MLE(String w1) {
		return (double)dictUnigrams.count(w1)/dictUnigrams.termCount();
	}
	
	public double P_MLE(String w2, String w1) {
		if(dictUnigrams.count(w1)==0)
			return 0;
		return (double)dictBigrams.count(w1+"_"+w2)/dictUnigrams.count(w1);
	}
	
	public double P_int(String w2, String w1) {
		return lambda*P_MLE(w2)+(1-lambda)*P_MLE(w2,w1);
	}
	
	public double P(String query) {
		double result = 0;
		Pattern p = Pattern.compile("\\w+");
		Matcher m = p.matcher(query.toLowerCase());
		String curr="";
		String prev="";
		while(m.find()) 
		{
			curr = m.group();
			if(prev.equals(""))
				result = P_MLE(curr);
			else
			result *= P_int(curr,prev);
			prev=curr;
		}
		//result *= P_int(curr,"");
		return result;
	}
	// Do not call constructor directly since this is a Singleton
	private LanguageModel(String corpusFilePath) throws Exception {
		constructDictionaries(corpusFilePath);
	}

	public void loadCommonSpeelingErros(String commonErrorsFilePath) throws Exception{
		System.out.println("Adding comon spelling error dictionary...");
		BufferedReader commonSpellingErrorsFileReader = new BufferedReader(new FileReader(new File(commonErrorsFilePath)));
		String line = null;
		while ((line = commonSpellingErrorsFileReader.readLine()) != null) {
			String[] tokens = line.trim().split("\\s+");
			if(tokens.length == 2){
				dictUnigrams.add(tokens[1]);
			}
		}
		System.out.println("Unigrams: "+dictUnigrams.termCount());
	}

	public void constructDictionaries(String corpusFilePath)
			throws Exception {

		System.out.println("Constructing dictionaries...");
		File dir = new File(corpusFilePath);
		
		Pattern p = Pattern.compile("\\w+");
		for (File file : dir.listFiles()) {
			if (".".equals(file.getName()) || "..".equals(file.getName()) || file.getName().contains(".DS_Store")) {
				continue; // Ignore the self and parent aliases.
			}
			System.out.printf("Reading data file %s ...\n", file.getName());
			//System.out.printf("Testing %s ...\n", file.getName());
			BufferedReader input = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = input.readLine()) != null) {
				String curr="";
				String prev="";
				Matcher m = p.matcher(line.toLowerCase());
				while(m.find()) 
					{
					//System.out.println("adding to unigrams: "+curr);
					curr = m.group();
					dictUnigrams.add(curr);
					dictBigrams.add(prev+"_"+curr);
					prev=curr;
					}
				//dictBigrams.add(curr+"_");
			}
			input.close();
			//break;
		}
		System.out.println("Done.");
		System.out.println("Unigrams: "+dictUnigrams.termCount());
		System.out.println("Bigrams: "+dictBigrams.termCount());
	}
	
	// Loads the object (and all associated data) from disk
	public static LanguageModel load() throws Exception {
		try {
			if (lm_==null){
				FileInputStream fiA = new FileInputStream(Config.languageModelFile);
				ObjectInputStream oisA = new ObjectInputStream(fiA);
				lm_ = (LanguageModel) oisA.readObject();
				oisA.close();
			}
		} catch (Exception e){
			throw new Exception("Unable to load language model.  You may have not run build corrector");
		}
		return lm_;
	}
	
	// Saves the object (and all associated data) to disk
	public void save() throws Exception{
		FileOutputStream saveFile = new FileOutputStream(Config.languageModelFile);
		ObjectOutputStream save = new ObjectOutputStream(saveFile);
		save.writeObject(this);
		save.close();
	}
	
	// Creates a new lm object from a corpus
	public static LanguageModel create(String corpusFilePath) throws Exception {
		if(lm_ == null ){
			lm_ = new LanguageModel(corpusFilePath);
		}
		return lm_;
	}
}
