import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class FactChecker 
{
	private static final String URL1 = "<http://swc2017.aksw.org/task2/dataset/";
	private static final String URL2 = "<http://swc2017.aksw.org/hasTruthValue>";
	private static final String URL3 = "^^<http://www.w3.org/2001/XMLSchema#double>";
	
	
	public FactChecker(String trainFile, String trainResultFile, LocalWiki LW)
	{
		
		HashMap<String,String> facts = readFile(trainFile);
		String result = "";
		for (Map.Entry<String, String> e : facts.entrySet())
		{
			
			String fact = e.getValue();
			boolean truth ;
			List<String> facttokens = generateTokens(fact);  
			truth = checkTruth(facttokens,fact);
			
			if (truth)
				result += URL1 + e.getKey() + "> " + URL2 + " \"" + "1.0" + "\"" + URL3 + " .\n";
			else	
				result += URL1 + e.getKey() + "> " + URL2 + " \"" + "0.0" + "\"" + URL3 + " .\n";
			
		}
		
		try {
			FileUtils.writeStringToFile(new File(trainResultFile), result, "UTF-8");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("An error occured while saving result file");
		}
		
		
		
		
				
		
	}
	
	public void checkFacts(String testFile, String testResultFile, LocalWiki LW)
	{
		HashMap<String,String> facts = readFile(testFile);
		String result = "";
		for (Map.Entry<String, String> e : facts.entrySet())
		{
			
			String fact = e.getValue();
			boolean truth ;
			List<String> facttokens = generateTokens(fact);  
			truth = checkTruth(facttokens,fact);
			
			if (truth)
				result += URL1 + e.getKey() + "> " + URL2 + " \"" + "1.0" + "\"" + URL3 + " .\n";
			else	
				result += URL1 + e.getKey() + "> " + URL2 + " \"" + "0.0" + "\"" + URL3 + " .\n";
			
		}
		
		try {
			FileUtils.writeStringToFile(new File(testResultFile), result, "UTF-8");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("An error occured while saving result file");
		}
		
	}
	
	public HashMap<String,String> readFile(String fileName)
	{
		HashMap<String, String> factMap = new HashMap<String,String>();
 		try {
			String File = FileUtils.readFileToString(new File ( fileName), "UTF-8");
			String Facts[] = File.split("\n");
			for ( String line : Facts)
			{
				if (line.split("\t")[0].matches("\\d+"))
				factMap.put(line.split("\t")[0],line.split("\t")[1]);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("An error occured while reading facts file");
		}
 		
 		return factMap;
	}
	
	private boolean checkTruth (List<String> facttokens, String fact) {
		String delim = "(, )|(\\sin\\s)|(\\s[A-Z]\\.)|(d')";
		
		
		
		
		for (String pagetoken : facttokens )
		{
			String pagedata = LocalWiki.requestPage(pagetoken);
						
			for (String token : facttokens)
			{
				boolean truth=true;
				
				
				if(pagetoken!=token)
				{
					for ( String temp : token.split("(, )|(\\sin\\s)|(\\s[A-Z]\\.)|(d')"))
						{
						
						String predicate = getPredicate(fact);
							temp = temp.replaceAll(".\\(.*\\)", "");
							int length=temp.length();
							if(fact.contains("place"))
							{
								length=Math.min(temp.length(), 6);
							}
							
							predicate += temp.substring(0,length);
							if(Pattern.compile(predicate).matcher(pagedata).find()!=true )
							{
								truth=false;
								break;
							}
						}
					if(truth)
					{
						return true;
					}
				
				}

			}
		}
		

		return false;
	}

	private List<String> generateTokens(String fact)
	{
		List<String> tokens = new ArrayList<String>();
		String token = "";
		String[] words = fact.split(" ");
		for ( int i =0;i< words.length;i++)
		{
			words[i]=words[i].trim().replaceAll("(\\?|('s)|\\.|')+$", "");
			
			if(isToken(words[i])||((token!= "")&&ispreposition(words[i])))
			{
				token += words[i]+" ";
			}
			else if (token!= "")
			{
				tokens.add(token);
				token = "";
			}
			
			
		}
		
		if (token!= "")
			tokens.add(token);
		
		
		
		return tokens;
	}
	
	private boolean isToken(String word)
	{
		boolean truth = false;
		if(Character.isUpperCase(word.charAt(0))||(word.charAt(0)+"").matches("\\d"))
			truth = true;
		return truth;
	}
	private boolean ispreposition(String word)
	{
		
		
		String[]  prepositions	 =	{  "den",  "for", "del",  "in", "of", "the","das", "las",
				"de", "van", "von", "der","du", "a", "and", "or", "with", "&", "at", "on",	"to", "into", "from"};
		List<String> prepoList = Arrays.asList(prepositions);
		//System.out.println(prepoList.contains(word));
		
		return prepoList.contains(word);
	}
	
	private String getPredicate(String fact)
	{
		String filter = "";
		String[] born = {"birth place","nascence place"};
		String[] death = {"death place","last place"};
		String[] foundation = {"innovation","foundation"};
		String[] author = {"author"};
		String[] award = {"award"};
		String[] spouse = {"spouse","better half"};
		String[] team = {"subordinate","subsidiary","office","team","squad"};
		String[] star = {"stars","role"};
		
		for (String b : born)
			if(fact.contains(b))
				filter = "(B|b)orn.{0,150}";
		for (String d : death)
			if(fact.contains(d))
				filter = "(Died).{0,150}";
		for (String f : foundation)
			if(fact.contains(f))
				filter = "(((R|r)esidence)|((H|h)eadquarters)|((F|f)ounded)|((O|o)ffice)).{0,150}";
		for (String a : author)
			if(fact.contains(a))
				filter = "(A|a)uthor.{0,150}";
		for (String aw : award)
			if(fact.contains(aw))
				filter = "(A|a)ward.{0,150}";
		for (String s : spouse)
			if(fact.contains(s))
				filter = "(S|s)pouse.{0,150}";
		for (String t : team)
			if(fact.contains(t))
				filter = "\\b(?<!((((B|b)orn)|((D|d)ied)|((A|a)ward)|((S|s)pouse)|((A|a)uthor)|((S|s)tarring)|((F|f)ounded)|((H|h)eadquarters)|star|act).{0,150}))";
		for (String s : star)
			if(fact.contains(s))
				filter = "\\b(?<!((((B|b)orn)|((D|d)ied)|((A|a)ward)|((S|s)pouse)|((A|a)uthor)|((F|f)ounded)|((H|h)eadquarters)).{0,150}))";

		return filter + "(?i)";
	}
	
}
