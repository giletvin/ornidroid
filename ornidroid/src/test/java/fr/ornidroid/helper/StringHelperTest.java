package fr.ornidroid.helper;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import fr.ornidroid.tests.AbstractTest;

/**
 * The Class StringHelperTest.
 */
public class StringHelperTest extends AbstractTest {

	/**
	 * Test default string.
	 */
	@Test
	public void testDefaultString() {
		Assert.assertEquals("", StringHelper.defaultString(null));
		Assert.assertEquals("", StringHelper.defaultString(""));
		Assert.assertEquals("bat", StringHelper.defaultString("bat"));
	}

	/**
	 * Test equals.
	 */
	@Test
	public void testEquals() {
		Assert.assertTrue(StringHelper.equals(null, null));
		Assert.assertFalse(StringHelper.equals(null, "abc"));
		Assert.assertFalse(StringHelper.equals("abc", null));
		Assert.assertTrue(StringHelper.equals("abc", "abc"));
		Assert.assertFalse(StringHelper.equals("abc", "ABC"));
	}

	/**
	 * Test equals ignore case.
	 */
	@Test
	public void testEqualsIgnoreCase() {
		Assert.assertTrue(StringHelper.equalsIgnoreCase(null, null));
		Assert.assertFalse(StringHelper.equalsIgnoreCase(null, "abc"));
		Assert.assertFalse(StringHelper.equalsIgnoreCase("abc", null));
		Assert.assertTrue(StringHelper.equalsIgnoreCase("abc", "abc"));
		Assert.assertTrue(StringHelper.equalsIgnoreCase("abc", "ABC"));

	}

	/**
	 * Test is blank.
	 */
	@Test
	public void testIsBlank() {
		Assert.assertTrue(StringHelper.isBlank(null));
		Assert.assertTrue(StringHelper.isBlank(""));
		Assert.assertTrue(StringHelper.isBlank(" "));
		Assert.assertFalse(StringHelper.isBlank("bob"));
		Assert.assertFalse(StringHelper.isBlank("  bob  "));
	}

	/**
	 * Test is not blank.
	 */
	@Test
	public void testIsNotBlank() {
		Assert.assertFalse(StringHelper.isNotBlank(null));
		Assert.assertFalse(StringHelper.isNotBlank(""));
		Assert.assertFalse(StringHelper.isNotBlank(" "));
		Assert.assertTrue(StringHelper.isNotBlank("bob"));
		Assert.assertTrue(StringHelper.isNotBlank("  bob  "));
	}

	/**
	 * Test split.
	 */
	@Test
	public void testSplit() {
		Assert.assertEquals(StringHelper.split(null, "*"), null);
		String[] result = StringHelper.split("", "*");
		Assert.assertEquals(result.length, 0);
		result = StringHelper.split("abc def", null);
		Assert.assertEquals(result.length, 2);
		Assert.assertEquals(result[0], "abc");
		Assert.assertEquals(result[1], "def");
		result = StringHelper.split("abc def", " ");
		Assert.assertEquals(result.length, 2);
		Assert.assertEquals(result[0], "abc");
		Assert.assertEquals(result[1], "def");
		result = StringHelper.split("abc  def", " ");
		Assert.assertEquals(result.length, 2);
		Assert.assertEquals(result[0], "abc");
		Assert.assertEquals(result[1], "def");
		result = StringHelper.split("ab:cd:ef", ":");
		Assert.assertEquals(result.length, 3);
		Assert.assertEquals(result[0], "ab");
		Assert.assertEquals(result[1], "cd");
		Assert.assertEquals(result[2], "ef");

	}

	/**
	 * Test substring after last.
	 */
	@Test
	public void testSubstringAfterLast() {
		Assert.assertEquals(StringHelper.substringAfterLast(null, "*"), null);
		Assert.assertEquals(StringHelper.substringAfterLast("", "*"), "");
		Assert.assertEquals(StringHelper.substringAfterLast("*", ""), "");
		Assert.assertEquals(StringHelper.substringAfterLast("*", null), "");
		Assert.assertEquals(StringHelper.substringAfterLast("abc", "a"), "bc");
		Assert.assertEquals(StringHelper.substringAfterLast("abcba", "b"), "a");
		Assert.assertEquals(StringHelper.substringAfterLast("abc", "c"), "");
		Assert.assertEquals(StringHelper.substringAfterLast("a", "a"), "");
		Assert.assertEquals(StringHelper.substringAfterLast("a", "z"), "");

	}

	/**
	 * Test parse list preference value.
	 */
	@Test
	public void testParseListPreferenceValue() {
		List<String> values = StringHelper.parseListPreferenceValue(null, null);
		Assert.assertEquals(0, values.size());
		values = StringHelper.parseListPreferenceValue(null,
				BasicConstants.COMMA_STRING);
		Assert.assertEquals(0, values.size());
		values = StringHelper.parseListPreferenceValue("",
				BasicConstants.COMMA_STRING);
		Assert.assertEquals(0, values.size());
		values = StringHelper.parseListPreferenceValue("es",
				BasicConstants.COMMA_STRING);
		Assert.assertEquals(1, values.size());
		Assert.assertEquals("es", values.get(0));
		values = StringHelper.parseListPreferenceValue("es,de",
				BasicConstants.COMMA_STRING);
		Assert.assertEquals(2, values.size());
		Assert.assertEquals("es", values.get(0));
		Assert.assertEquals("de", values.get(1));
	}

	/**
	 * Test strip accents.
	 */
	@Test
	public void testStripAccents() {
		Assert.assertEquals("", StringHelper.stripAccents(null));
		Assert.assertEquals("", StringHelper.stripAccents(""));
		Assert.assertEquals("Eeeeecoui", StringHelper.stripAccents("Eeéêèçöùî"));
	}

	/**
	 * Test replace.
	 */
	@Test
	public void testReplace() {
		Assert.assertEquals("Bec*croise",
				StringHelper.replace("Bec-croise", "-", "*", 1));
		Assert.assertEquals("Bec*croise-zzz",
				StringHelper.replace("Bec-croise-zzz", "-", "*", 1));
		Assert.assertEquals("Bec*croise*zzz",
				StringHelper.replace("Bec-croise-zzz", "-", "*", -1));
		Assert.assertNull(StringHelper.replace(null, null, null, 0));

	}
}
