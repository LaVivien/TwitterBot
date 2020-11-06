import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//read tweets and remove unwanted characters before put into Markov chain
public class TweetParser {
	
	private static final String BADWORD_REGEX = ".*[\\W&&[^']].*";
	private static final String URL_REGEX = "\\bhttp\\S*";

	public static String removeURLs(String s) {
		return s.replaceAll(URL_REGEX, "");
	}

	public static String cleanWord(String word) {
		String cleaned = word.trim().toLowerCase();
		if (cleaned.matches(BADWORD_REGEX) || cleaned.isEmpty())
			return null;
		return cleaned;
	}

	private static final char[] puncs = new char[] { '.', '?', '!', ';' };

	public static char[] getPunctuation() {
		return puncs.clone();
	}


	public static String replacePunctuation(String tweet) {
		for (char c : puncs) {
			tweet = tweet.replace(c, '.');
		}
		return tweet;
	}

	public static List<String> sentenceSplit(String tweet) {
		List<String> sentences = new LinkedList<String>();
		for (String sentence : replacePunctuation(tweet).split("\\.")) {
			sentence = sentence.trim();
			if (!sentence.equals("")) {
				sentences.add(sentence);
			}
		}
		return sentences;
	}

	public static String extractColumn(String csvLine, int csvColumn) {
		String[] strs = csvLine.split(",");
		if(strs.length >= csvColumn)
			return strs[csvColumn];
		return null; 
	}
	
	public static List<String> parseAndCleanSentence(String sentence) {
		List<String> res = new ArrayList<>();
		String[] strs = sentence.split(" ");
		if(strs.length > 0) {
			for(String s: strs) {
				s=replacePunctuation(s);
				s = s.replaceAll("\\.", "");
				String cleanWord = cleanWord(s);
				if(cleanWord!=null)
					res.add(cleanWord);
			}
			return res;
		}
		return null;
	}
	
	public static List<List<String>> parseAndCleanTweet(String tweet) {
		String cleanTweet = removeURLs(tweet);
		List<String> sentences =  sentenceSplit (cleanTweet);
		List<List<String>> res = new ArrayList<>();
		for(String str: sentences) {
			List<String> cleanSent = parseAndCleanSentence(str);
			res.add(cleanSent);
		}
		if (res.size()>0 )
			return res;
		return null; 
	}

	public static List<String> csvFileToTweets(String pathToCSVFile, int tweetColumn)  {
		List<String> res = new ArrayList<>();
		try{
			FileLineIterator iter = new FileLineIterator(pathToCSVFile);
			while(iter.hasNext()) {
				String nextLine = iter.next();
				String[] strs=nextLine.split(",");
				res.add(strs[tweetColumn]);
			}
			return res;
		} catch(IllegalArgumentException e) {
			throw new IllegalArgumentException( e);	
		}
	}

	public static List<List<String>> csvFileToTrainingData(String pathToCSVFile, int tweetColumn) {
		List<List<String>> res = new ArrayList<>();
		try{
			FileLineIterator iter = new FileLineIterator(pathToCSVFile);
			while(iter.hasNext()) {
				String nextLine = iter.next();		
				if(nextLine.length()==0 ||! nextLine.contains(","))
					continue;
				String[] strs=nextLine.split(",");
				String sentence = strs[tweetColumn];
				List<String> sen = parseAndCleanSentence(sentence);
				res.add(sen);
			}
			return res;
		} catch(IllegalArgumentException e) {
			throw new IllegalArgumentException( e);	
		}
	}
	
	 public static void main(String[] args) {		
	    
	    System.out.println("removeURL: "+TweetParser.removeURLs("https:// abc https``\":ala34?#? def"));
	    System.out.println("cleanword:"+TweetParser.cleanWord("@abc"));
	    
	    System.out.println("extracColumn:"+TweetParser.extractColumn("wrongColumn, wrong column, wrong column!, This is a tweet.", 3));
	    
	    List<String> list=TweetParser.parseAndCleanSentence("abc #@#F");
	    System.out.println("parseAndCleanSentence:"+list.size());		    
	    System.out.println(list);
	    	
	    List<List<String>> list1=TweetParser.parseAndCleanTweet("abc http://www.cis.upenn.edu");
	    System.out.println("parseAndCleanTweet:"+list1.size());
	    System.out.println(list1);
	    
	    List<String> tweets = TweetParser.csvFileToTweets("files/simple_test_data.csv", 1);
	    System.out.println("csvFileToTweets:"+ tweets.size());
	    System.out.println(tweets);
	    
	    List<List<String>> tweets1 = TweetParser.csvFileToTrainingData("files/simple_test_data.csv", 1);
	    System.out.println("csvFileToTrainingData:"+ tweets1.size());
	    System.out.println(tweets1);
	 }
}
