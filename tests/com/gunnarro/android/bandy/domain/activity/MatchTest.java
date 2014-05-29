package com.gunnarro.android.bandy.domain.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Type.MatchTypesEnum;
import com.gunnarro.android.bandy.domain.party.Referee;

public class MatchTest {

	@Test
	public void testConstructor() {
		Match match = new Match(new Season("2013/2014", 0, 0), System.currentTimeMillis() - 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				"venue", new Referee("firstname", "middlename", "lastname"));
		match.setStatus(new Status(null, "PLAYED"));
		assertNull(match.getNumberOfGoalsHome());
		assertNull(match.getNumberOfGoalsAway());
		assertNull(match.getResult());
		assertEquals("NOT PLAYED", match.getMatchStatus().getName());
		assertFalse(match.isPlayed());
		assertEquals(0, match.getNumberOfParticipatedPlayers().intValue());
	}

	@Test
	public void testConstructorPlayed() {
		Match match = new Match(1, new Season("2013/2014", 0, 0), System.currentTimeMillis() - 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				4, 5, "venue", new Referee("firstname", "middlename", "lastname"), MatchTypesEnum.CUP);
		match.setStatus(new Status(null, "PLAYED"));
		assertEquals("PLAYED", match.getStatus().getName());
		assertEquals(1, match.getId().intValue());
		assertEquals("homeTeam", match.getHomeTeam().getName());
		assertEquals("awayTeam", match.getAwayTeam().getName());
		assertEquals("homeTeam - awayTeam", match.getTeamVersus());
		assertEquals(4, match.getNumberOfGoalsHome().intValue());
		assertEquals(5, match.getNumberOfGoalsAway().intValue());
		assertEquals("4 - 5", match.getResult());
		assertEquals("CUP", match.getMatchType().getName());
		assertEquals(3, match.getMatchType().getId());
		assertEquals("Firstname Middlename Lastname", match.getReferee().getFullName());
		// assertTrue(match.isPlayed());
	}

	public void testConstructorNotPlayedDateBefore() {
		Match match = new Match(1, new Season("2013/2014", 0, 0), System.currentTimeMillis() - 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				null, null, "venue", new Referee("firstname", "middlename", "lastname"), MatchTypesEnum.CUP);
		match.setStatus(new Status(null, "PLAYED"));
		assertTrue(match.isFinished());
		assertNull(match.getNumberOfGoalsHome());
		assertNull(match.getNumberOfGoalsAway());
		assertNull(match.getResult());
		assertFalse(match.isPlayed());
	}

	public void testConstructorNotPlayedDateAfter() {
		Match match = new Match(1, new Season("2013/2014", 0, 0), System.currentTimeMillis() + 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				null, null, "venue", new Referee("firstname", "middlename", "lastname"), MatchTypesEnum.CUP);
		match.setStatus(new Status(null, "PLAYED"));
		assertFalse(match.isFinished());
		assertNull(match.getNumberOfGoalsHome());
		assertNull(match.getNumberOfGoalsAway());
		assertNull(match.getResult());
		assertFalse(match.isPlayed());
	}
}
