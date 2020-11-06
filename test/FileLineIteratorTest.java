/* Tests for FileLineIterator */
import static org.junit.Assert.*;

import org.junit.Test;

public class FileLineIteratorTest {

	@Test
	public void testHasNextAndNext() {
		FileLineIterator li = new FileLineIterator("files/simple_test_data.csv");
		assertTrue(li.hasNext());
		assertEquals("0, The end should come here.", li.next());
		assertTrue(li.hasNext());
		assertEquals("1, This comes from data with no duplicate words!", li.next());
		assertFalse(li.hasNext());
	}	
}

