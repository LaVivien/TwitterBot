
import static org.junit.Assert.*;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

public class TweetParserTest {

	private static List<String> singleton(String word) {
		List<String> l = new LinkedList<String>();
		l.add(word);
		return l;
	}

	private static List<String> listOfArray(String[] words) {
		List<String> l = new LinkedList<String>();
		for (String s : words) {
			l.add(s);
		}
		return l;
	}

	@Test
	public void removeURLsTest() {
		assertEquals("abc", TweetParser.removeURLs("abc"));
		assertEquals("abc ", TweetParser.removeURLs("abc http://www.cis.upenn.edu"));
		assertEquals(" abc ", TweetParser.removeURLs("http:// abc http:ala34?#?"));
		assertEquals(" abc  def", TweetParser.removeURLs("http:// abc http:ala34?#? def"));
		assertEquals(" abc  def", TweetParser.removeURLs("https:// abc https``\":ala34?#? def"));
		assertEquals("abchttp", TweetParser.removeURLs("abchttp"));
	}

	@Test
	public void testCleanWord() {
		assertEquals("abc", TweetParser.cleanWord("abc"));
		assertEquals("abc", TweetParser.cleanWord("ABC"));
		assertEquals(null, TweetParser.cleanWord("@abc"));
	}

	@Test
	public void testExtractColumnGetsCorrectColumn() {
		assertEquals(" This is a tweet.",
				TweetParser.extractColumn("wrongColumn, wrong column, wrong column!, This is a tweet.", 3));
	}
	
	@Test
	public void parseAndCleanSentenceNonEmptyFiltered() {
		List<String> sentence = TweetParser.parseAndCleanSentence("abc #@#F");
		List<String> expected = new LinkedList<String>();
		expected.add("abc");
		assertEquals(expected, sentence);
	}
	
	@Test
	public void testParseAndCleanTweetRemovesURLS1() {
		List<List<String>> sentences = TweetParser.parseAndCleanTweet("abc http://www.cis.upenn.edu");
		List<List<String>> expected = new LinkedList<List<String>>();
		expected.add(singleton("abc"));
		assertEquals(expected, sentences);
	}
	
	@Test
	public void testCsvFileToTrainingDataSimpleCSV() {
		List<List<String>> tweets = TweetParser.csvFileToTrainingData("files/simple_test_data.csv", 1);
		List<List<String>> expected = new LinkedList<List<String>>();
		expected.add(listOfArray("the end should come here".split(" ")));
		expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
		assertEquals(expected, tweets);
	}
	
	@Test
	public void testCsvFileToTweetsSimpleCSV() {
		List<String> tweets = TweetParser.csvFileToTweets("files/simple_test_data.csv", 1);
		List<String> expected = new LinkedList<String>();
		expected.add(" The end should come here.");
		expected.add(" This comes from data with no duplicate words!");		
		assertEquals(expected, tweets);
	}	
}
