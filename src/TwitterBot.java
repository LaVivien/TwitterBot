
import java.util.*;

//read from tweets, use markov chain to train the data, generate tweets from the data
public class TwitterBot {

	static final int MAX_TWEET_LENGTH = 180;
	static final String pathToTweets = "files/dog_feelings_tweets.csv";
	static final int tweetColumn = 2;
	static final String pathToOutputTweets = "files/generated_tweets.txt";
	static final String[] poolOfStartWords ={"i", "the", "when", "sometimes", "we", "today", "it","however", "but"};

	MarkovChain mc;
	NumberGenerator ng;
	ProbabilityDistribution<String> startWords;

	public TwitterBot(String csvFile, int tweetColumn) {
		this(csvFile, tweetColumn, new RandomNumberGenerator());
	}

	//constructor, read tweets file, train the data with Markov chain
	public TwitterBot(String csvFile, int tweetColumn, NumberGenerator ng) {
		mc = new MarkovChain(ng);
		this.ng = ng;
		generateStartWords();
		
		List<List<String>> tweets = TweetParser.csvFileToTrainingData(csvFile, tweetColumn);		
	    for(List<String> t : tweets) {
	    	 if(t.size()==0)
	    		 continue;
	    	 Iterator<String> s = t.iterator();
	    	 mc.train(s);
	    }
	}
	
	private void generateStartWords() {
		startWords=new ProbabilityDistribution<String>();
		for (String s: poolOfStartWords) {
			startWords.record(s);
		}	
	}

	//generate tweets based on the trained data
	public String generateTweet(int length) {		
	   mc.reset(startWords.pick(ng));
       int i = 0;
       StringBuilder sb = new StringBuilder();
	   while(mc.hasNext()) {
		   if(i == length)
			   break;
        	sb.append(mc.next() + " ");
        	i++;	        	
       }	
	   sb.setCharAt(sb.length()-1,randomPunctuation().charAt(0));
	   return sb.toString(); 
	}

	public List<String> generateTweets(int numTweets, int tweetLength) {
		List<String> tweets = new ArrayList<String>();
		while (numTweets > 0) {
			String tw = generateTweet(tweetLength);
			if(tw.length() > 10 && tw.length() < MAX_TWEET_LENGTH) {
				tweets.add(tw);
				numTweets--;
			}
		}
		return tweets;
	}

	public String randomPunctuation() {
		char[] puncs = { ';', '?', '!' };
		int m = ng.next(10);
		if (m < puncs.length)
			return String.valueOf(puncs[m]);
		return ".";
	}

	public static boolean isPunctuated(String s) {
		if (s == null || s.equals("")) {
			return false;
		}
		char[] puncs = TweetParser.getPunctuation();
		for (char c : puncs) {
			if (s.charAt(s.length() - 1) == c) {
				return true;
			}
		}
		return false;
	}

	public static void main(String args[]) {
		//generate 10 tweets
		TwitterBot t = new TwitterBot(pathToTweets,2);
		List<String> tweets = t.generateTweets(10, MAX_TWEET_LENGTH);
		for (String tweet : tweets) {
			System.out.println(tweet);
		}		
	}
}
