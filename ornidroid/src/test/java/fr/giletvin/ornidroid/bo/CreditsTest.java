package fr.giletvin.ornidroid.bo;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class CreditsTest.
 */
public class CreditsTest {

	/**
	 * Test get list credits.
	 */
	@Test
	public void testGetListCredits() {
		String[] creditsArray = new String[] { "title1|author1|url1|license1",
				"title2|author2|url2|" };

		Credits credits = new Credits(creditsArray);
		List<Map<String, String>> creditsList = credits.getListCredits();
		Assert.assertEquals(2, creditsList.size());
		Map<String, String> credit1 = creditsList.get(0);
		Assert.assertEquals("title1", credit1.get(Credits.CREDIT_TITLE));
		Assert.assertEquals("author1", credit1.get(Credits.CREDIT_AUTHOR));
		Assert.assertEquals("url1", credit1.get(Credits.CREDIT_URL));
		Assert.assertEquals("license1", credit1.get(Credits.CREDIT_LICENSE));
		Map<String, String> credit2 = creditsList.get(1);
		Assert.assertEquals("title2", credit2.get(Credits.CREDIT_TITLE));
		Assert.assertEquals("author2", credit2.get(Credits.CREDIT_AUTHOR));
		Assert.assertEquals("url2", credit2.get(Credits.CREDIT_URL));
		Assert.assertNull(credit2.get(Credits.CREDIT_LICENSE));

	}

}
