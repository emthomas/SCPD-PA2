package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.cs276.LanguageModel;
import edu.stanford.cs276.NoisyChannelModel;
import edu.stanford.cs276.EmpiricalCostModel;

public class LanguageModelTest {
	 LanguageModel tester = null;
	 NoisyChannelModel nsm = null;
			 
	
	/*@Before
	public void setup() throws Exception {
		String query = "quade quad cache xontro2ler";
//		tester = LanguageModel.load();	
//		nsm = NoisyChannelModel.load();
		System.out.println("Log(0): "+Math.log((double)28362812));
	}*/
	/*
	@Test
	public void testNoEdit() throws Exception {

	   // MyClass is tested
	   
	   // check if multiply(10,5) returns 50
	   String noisy = "quade quad cache controller";
	   String clean = "quade quad cache controller";
	   assertEquals("0##", EmpiricalCostModel.editType(noisy,clean));
	   
	   noisy = "quade quad cache controller";
	   clean = "quade quad cache controller";
	   assertEquals("0##", EmpiricalCostModel.editType(noisy,clean));
	   
	   noisy = "quade quad cache controller";
	   clean = "quade quad cache controller";
	   assertEquals("0##", EmpiricalCostModel.editType(noisy,clean));
	 } 
	*/
	@Test
	public void testIns() throws Exception {

	   String noisy = "quade quad acress cache controller";
	   String clean = "quade quad cress cache controller";
	   String answer = EmpiricalCostModel.editType(noisy,clean)[0]+
			   EmpiricalCostModel.editType(noisy,clean)[1]+
			   EmpiricalCostModel.editType(noisy,clean)[2];
	   String x = EmpiricalCostModel.editType(noisy,clean)[1];
	   String w = EmpiricalCostModel.editType(noisy,clean)[2];
	   assertEquals(EmpiricalCostModel.ins+"a ", answer);
	   System.out.println("noisy: "+noisy);
	   System.out.println("clean: "+clean);
	   System.out.println("P("+x+"|"+w+") = COUNT(ins["+w.charAt(0)+","+x+"])/COUNT("+w.charAt(0)+")");
	   System.out.println();
	 } 
	
	@Test
	public void testDel() throws Exception {

	   String noisy = "quade quad cche controller";
	   String clean = "quade quad cache controller";
	   String answer = EmpiricalCostModel.editType(noisy,clean)[0]+
			   EmpiricalCostModel.editType(noisy,clean)[1]+
			   EmpiricalCostModel.editType(noisy,clean)[2];
	   String x = EmpiricalCostModel.editType(noisy,clean)[1];
	   String w = EmpiricalCostModel.editType(noisy,clean)[2];
	   assertEquals(EmpiricalCostModel.del+"cca", answer);
	   System.out.println("noisy: "+noisy);
	   System.out.println("clean: "+clean);
	   System.out.println("P("+x+"|"+w+") = COUNT(del["+w.charAt(0)+","+w.charAt(1)+"])/COUNT("+w+")");
	   System.out.println();
	 } 
	 
	@Test
	public void testSub() throws Exception {

	   // MyClass is tested
	   
	   // check if multiply(10,5) returns 50
	   String noisy = "quade quad cache xontroller";
	   String clean = "quade quad cache controller";
	   String answer = EmpiricalCostModel.editType(noisy,clean)[0]+
			   EmpiricalCostModel.editType(noisy,clean)[1]+
			   EmpiricalCostModel.editType(noisy,clean)[2];
	   String x = EmpiricalCostModel.editType(noisy,clean)[1];
	   String w = EmpiricalCostModel.editType(noisy,clean)[2];
	   assertEquals(EmpiricalCostModel.sub+"xc", answer);
	   System.out.println("noisy: "+noisy);
	   System.out.println("clean: "+clean);
	   System.out.println("P("+x+"|"+w+") = COUNT(sub["+x+","+w+"])/COUNT("+w+")");
	   System.out.println();
	 }
	
	@Test
	public void testTrans() throws Exception {

	   // MyClass is tested
	   
	   // check if multiply(10,5) returns 50
	   String noisy = "uqade quad cache controller";
	   String clean = "quade quad cache controller";
	   String answer = EmpiricalCostModel.editType(noisy,clean)[0]+
			   EmpiricalCostModel.editType(noisy,clean)[1]+
			   EmpiricalCostModel.editType(noisy,clean)[2];
	   String x = EmpiricalCostModel.editType(noisy,clean)[1];
	   String w = EmpiricalCostModel.editType(noisy,clean)[2];
	   assertEquals(EmpiricalCostModel.trans+"uqqu",answer);
	   System.out.println("noisy: "+noisy);
	   System.out.println("P("+x+"|"+w+") = COUNT(trans["+w.charAt(0)+","+w.charAt(1)+"])/COUNT("+w+")");
	   System.out.println();
	 }
	
	 
	@Test
	public void testEdit() throws Exception {
		//char[] test = "0ab".toCharArray();
		//EmpiricalCostModel.recordEdit(test);
		}
	}


