import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LocalWiki {
	private static final String DIR = "localWiki/";
	private static final String URL = "https://en.wikipedia.org/w/index.php?search=";
	private static final String[] X = { ".mw-parser-output > p", ".mw-parser-output > ul li", ".infobox.vcard tr",
	".mw-parser-output .multicol > tbody li" };
	
	public LocalWiki()
	{
		new File(DIR).mkdirs();
	}

	public static String requestPage(String token)
	{
		String pageData= "";  
		if (LocalWiki.check(token))
		{
			pageData = LocalWiki.get(token);
		}
		else
		{
			try {
				Document document = Jsoup.connect(URL+URLEncoder.encode(token, "UTF8")).get();
				Elements set = document.select(X[0] + ", " + X[1] + ", " + X[2] + ", " + X[3]);
				

				for (Element e : set)
				{
					pageData += e.text() + "\n";
					//System.out.println(e.text() + "9999999999999999999999999999");
				}
				
				LocalWiki.add(token,pageData);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return pageData;
		
	}
	public static boolean check(String token)
	{
		return new File(DIR + token).exists();
	}
	public static String get(String token)
	{
		try {
			return FileUtils.readFileToString(new File(DIR + token), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static void add(String token, String pageData)
	{
		try {
			FileUtils.writeStringToFile(new File(DIR + token), pageData, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
