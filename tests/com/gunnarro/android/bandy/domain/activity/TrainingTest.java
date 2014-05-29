package com.gunnarro.android.bandy.domain.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Activity.ActivityTypesEnum;
import com.gunnarro.android.bandy.utility.Utility;

public class TrainingTest {

	@Test
	public void testConstructor() {
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.YEAR, 2014);
		startTime.set(Calendar.MONTH, 1);
		startTime.set(Calendar.DAY_OF_MONTH, 1);
		startTime.set(Calendar.HOUR_OF_DAY, 16);
		startTime.set(Calendar.MILLISECOND, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MINUTE, 0);
		long endTime = startTime.getTimeInMillis() + 2 * 60 * 60 * 1000;
		Training training = new Training(new Season("2013/2014", 0, 0), startTime.getTimeInMillis(), endTime, new Team("team name"), "venue");
		assertEquals(ActivityTypesEnum.Training.name(), training.getName());
		assertEquals(ActivityTypesEnum.Training.name(), training.getType());
		assertEquals("2013/2014", training.getSeason().getPeriod());
		assertEquals("01.02.2014 04:00", Utility.formatTime(training.getStartTime(), "dd.MM.yyyy hh:mm"));
		assertEquals("01.02.2014 06:00", Utility.formatTime(training.getEndTime(), "dd.MM.yyyy hh:mm"));
		assertNull(training.getStatus());
		assertEquals("team name", training.getTeam().getName());
		assertEquals("venue", training.getVenue());
		assertTrue(training.isFinished());
		assertEquals(0, training.getNumberOfParticipatedPlayers().intValue());
	}

	@Test
	public void notFinished() {
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.YEAR, 2016);
		startTime.set(Calendar.MONTH, 1);
		startTime.set(Calendar.DAY_OF_MONTH, 1);
		startTime.set(Calendar.HOUR_OF_DAY, 16);
		startTime.set(Calendar.MILLISECOND, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MINUTE, 0);
		long endTime = startTime.getTimeInMillis() + 2 * 60 * 60 * 1000;
		Training training = new Training(new Season("2015/2016", 0, 0), startTime.getTimeInMillis(), endTime, new Team("team name"), "venue");
		assertFalse(training.isFinished());
	}

	@Test
	public void numberOfPlayers() {
		Training training = new Training(new Season("2015/2016", 0, 0), System.currentTimeMillis(), System.currentTimeMillis(), new Team("team name"), "venue");
		assertFalse(training.isFinished());
		assertEquals(0, training.getNumberOfParticipatedPlayers().intValue());
		training.setNumberOfParticipatedPlayers(11);
		assertEquals(11, training.getNumberOfParticipatedPlayers().intValue());
	}
}
