
public class MainClass {
	
	public static void main(String Args[])
	{
		
		LocalWiki LW = new LocalWiki(); 
		
		String trainFile = "train.tsv";
		String testFile =  "SNLP2019_test.tsv";
		String trainResultFile = "trainresult.tsv";
		String testResultFile = "testresult.tsv";
		
		FactChecker	fc = new FactChecker(trainFile,trainResultFile,LW);
		//fc.checkFacts(testFile,testResultFile,LW);
		fc.checkFacts(testFile, testResultFile, LW);
				
	}

}
