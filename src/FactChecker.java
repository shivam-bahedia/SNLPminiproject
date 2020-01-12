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
			e1.printStackTrace();
		}
		
		
		
		
				
		
	}
	
	public void checkFacts(String File, String resultFile, LocalWiki LW)
	{
		
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
			e.printStackTrace();
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
		
		
		String[]  prepositions	 =	{ "van", "von", "der", "den", "das", "las", "for", "del", 
				"de", "du", "the", "a", "at", "on",	"to", "in", "of", "and", "or", "with", "&", "into", "from"};
		List<String> prepoList = Arrays.asList(prepositions);
		//System.out.println(prepoList.contains(word));
		
		return prepoList.contains(word);
	}
	
	private String getPredicate(String fact)
	{
		String filter = "";

		if (fact.contains("birth place") || fact.contains("nascence place"))
			filter = "(B|b)orn.{0,150}";

		if (fact.contains("death place") || fact.contains("last place"))
			filter = "(Died).{0,150}";

		if (fact.contains("innovation") || fact.contains("foundation"))
			filter = "(((R|r)esidence)|((H|h)eadquarters)|((F|f)ounded)|((O|o)ffice)).{0,150}";

		if (fact.contains("author"))
			filter = "(A|a)uthor.{0,150}";

		if (fact.contains("award"))
			filter = "(A|a)ward.{0,150}";

		if (fact.contains("spouse") || fact.contains("better half"))
			filter = "(S|s)pouse.{0,150}";

		if (fact.contains("subordinate") || fact.contains("subsidiary") || fact.contains("office")
				|| fact.contains("team") || fact.contains("squad")) {
			filter = "\\b(?<!((((B|b)orn)|((D|d)ied)|((A|a)ward)|((S|s)pouse)|((A|a)uthor)|((S|s)tarring)|((F|f)ounded)|((H|h)eadquarters)|star|act).{0,150}))";
		}

		if (fact.contains("stars") || fact.contains("role")) {
			filter = "\\b(?<!((((B|b)orn)|((D|d)ied)|((A|a)ward)|((S|s)pouse)|((A|a)uthor)|((F|f)ounded)|((H|h)eadquarters)).{0,150}))";
		}

		return filter + "(?i)";
	}
	
}
