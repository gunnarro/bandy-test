package com.gunnarro.android.bandy.domain.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.gunnarro.android.bandy.domain.activity.Activity.ActivityTypesEnum;

public class CupTest {

	@Test
	public void testConstructor() {
		Cup cup = new Cup(new Season("2013/2014", 0, 0), System.currentTimeMillis(), "cup name", "club name", "venue", System.currentTimeMillis());
		assertEquals(ActivityTypesEnum.Cup.name(), cup.getName());
		assertEquals(ActivityTypesEnum.Cup.name(), cup.getType());
		assertEquals("2013/2014", cup.getSeason().getPeriod());
		assertEquals("club name", cup.getClubName());
		assertEquals("cup name", cup.getCupName());
		assertEquals("venue", cup.getVenue());
		assertEquals(0, cup.getNumberOfParticipatedPlayers().intValue());
		// assertEquals(ActivityStatusEnum.BEGIN, cup.getStatus());
		assertFalse(cup.isFinished());
	}
}
