/* Tests for TwitterBot class */
import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwitterBotTest {

	String simpleData = "files/simple_test_data.csv";
	String testData = "files/test_data.csv";
	@Test
	public void simpleTwitterBotTest(){

		List<Integer> indices = new ArrayList<Integer>(Collections.nCopies(100, 0));
		indices.set(0, 1);
		
		ListNumberGenerator lng = new ListNumberGenerator(indices);
		TwitterBot t = new TwitterBot(simpleData, 1, lng);
		t.mc.reset("this");
		
		String expected = "this comes from data with no duplicate words.";
		String actual = TweetParser.replacePunctuation(t.generateTweet(63));
		assertEquals(expected, actual);
	}
}
