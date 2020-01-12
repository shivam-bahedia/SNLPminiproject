
public class MainClass {
	
	public static void main(String Args[])
	{
		
		LocalWiki LW = new LocalWiki(); 
		
		String trainFile = "train.tsv";
		String testFile =  "SNLP2019_test.tsv";
		String trainResultFile = "trainresult.ttl";
		String testResultFile = "testresult.ttl";
		
		FactChecker	fc = new FactChecker(testFile,testResultFile,LW);
		//fc.checkFacts(testFile,testResultFile,LW);
		//fc.checkFacts(testFile, testResultFile, LW);
				
	}

}
