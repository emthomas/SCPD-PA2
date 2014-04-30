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

	   // MyClass is tested
	   
	   // check if multiply(10,5) returns 50
	   String noisy = "quade quad cabche controller";
	   String clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.ins+"ab", String.valueOf(EmpiricalCostModel.editType(noisy,clean)));
	   System.out.println("noisy: "+noisy);
	   System.out.println("clean: "+clean);
	   System.out.println("P(ab|a) = COUNT(ins[a,c])/COUNT(c)");
	   System.out.println();
	   /*
	   noisy = "quade quad  cache controller";
	   clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.ins+"  ", EmpiricalCostModel.editType(noisy,clean));
	   
	   noisy = "quade quad cadche controller";
	   clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.ins+"ad", EmpiricalCostModel.editType(noisy,clean));
	   
	   noisy = "equade quad cache controller";
	   clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.ins+" e", EmpiricalCostModel.editType(noisy,clean));
	   
	*/
	 } 
	
	@Test
	public void testDel() throws Exception {

	   // MyClass is tested
	   
	   // check if multiply(10,5) returns 50
	   System.out.println();
	   String noisy = "quade quad cche controller";
	   String clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.del+"ca", String.valueOf(EmpiricalCostModel.editType(noisy,clean)));
	   System.out.println("noisy: "+noisy);
	   System.out.println("clean: "+clean);
	   System.out.println("P(c|ca) = COUNT(del[a,c])/COUNT(c)");
	   System.out.println();
	   /*
	   noisy = "quade quadcache controller";
	   clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.del+"d ", EmpiricalCostModel.editType(noisy,clean));
	   
	   noisy = "quade quad cache controlle";
	   clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.del+"er", EmpiricalCostModel.editType(noisy,clean));
	   
	   noisy = "uade quad cache controller";
	   clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.del+" q", EmpiricalCostModel.editType(noisy,clean)); 
	*/
	 } 
	 
	@Test
	public void testNoSub() throws Exception {

	   // MyClass is tested
	   
	   // check if multiply(10,5) returns 50
	   String noisy = "quade quad cache xontroller";
	   String clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.sub+"cx", String.valueOf(EmpiricalCostModel.editType(noisy,clean)));
	   System.out.println("noisy: "+noisy);
	   System.out.println("clean: "+clean);
	   System.out.println("P(x|c) = COUNT(sub[c,x])/COUNT(c)");
	   System.out.println();
	   /*noisy = "zuade quad cache controller";
	   clean = "quade quad cache controller";
	   assertEquals("3qz", EmpiricalCostModel.editType(noisy,clean));
	   
	   noisy = "quade quad cache controllel";
	   clean = "quade quad cache controller";
	   assertEquals("3rl", EmpiricalCostModel.editType(noisy,clean));*/
	 }
	
	@Test
	public void testNoTrans() throws Exception {

	   // MyClass is tested
	   
	   // check if multiply(10,5) returns 50
	   String noisy = "uqade quad cache controller";
	   String clean = "quade quad cache controller";
	   assertEquals(EmpiricalCostModel.trans+"qu",String.valueOf(EmpiricalCostModel.editType(noisy,clean)));
	   System.out.println("noisy: "+noisy);
	   System.out.println("clean: "+clean);
	   System.out.println("P(u|q) = COUNT(trans[q,u])/COUNT(qu)");
	   System.out.println();
	   /*
	   noisy = "quade quad cache controllre";
	   clean = "quade quad cache controller";
	   assertEquals("4er", EmpiricalCostModel.editType(noisy,clean));
	   
	   noisy = "quade quad cachec ontroller";
	   clean = "quade quad cache controller";
	   assertEquals("4 c", EmpiricalCostModel.editType(noisy,clean));
	   
	   System.out.println('0');
	   System.out.println(','-'a');
	   */
	 }
	
	 
	@Test
	public void testEdit() throws Exception {
		char[] test = "0ab".toCharArray();
		EmpiricalCostModel.recordEdit(test);
		}
	}


