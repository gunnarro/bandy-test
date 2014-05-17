package com.gunnarro.android.bandy.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class UtilityTest {

	@Test
	public void formatTime() {
		assertEquals("22:23", Utility.formatTime(22, 23));
		assertEquals(23, Utility.getHour("23:45"));
		assertEquals(45, Utility.getMinute("23:45"));
	}

	@Test
	public void testToDate() {
		Date date = Utility.timeToDate("22.02.2014 22:23", Utility.DATE_TIME_PATTERN);
		assertEquals("22.02.2014 22:23", Utility.formatTime(date.getTime(), Utility.DATE_TIME_PATTERN));
	}
	
	@Test
	public void capitalzation() {
		assertEquals("Lastname Firstname", Utility.capitalizationWord("lastName fiRStName"));
		assertEquals("Streetname", Utility.capitalizationWord("StreetName "));
		assertEquals("A", Utility.capitalizationWord("a"));
	}

}
