package com.gunnarro.android.bandy.domain.activity;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Match.MatchStatus;
import com.gunnarro.android.bandy.domain.activity.Match.MatchTypesEnum;
import com.gunnarro.android.bandy.domain.party.Referee;

public class MatchTest {

	@Test
	public void testConstructor() {
		Match match = new Match(new Season("2013/2014", 0, 0), System.currentTimeMillis() - 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				"venue", new Referee("firstname", "middlename", "lastname"));
		assertNull(match.getNumberOfGoalsHome());
		assertNull(match.getNumberOfGoalsAway());
		assertNull(match.getResult());
		assertEquals("NOT STARTED", match.getStatus());
		assertFalse(match.isPlayed());
	}

	@Test
	public void testConstructorPlayed() {
		Match match = new Match(1, new Season("2013/2014", 0, 0), System.currentTimeMillis() - 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				4, 5, "venue", new Referee("firstname", "middlename", "lastname"), MatchTypesEnum.CUP, MatchStatus.PLAYED.name());
		assertEquals(1, match.getId().intValue());
		assertEquals("homeTeam", match.getHomeTeam().getName());
		assertEquals("awayTeam", match.getAwayTeam().getName());
		assertEquals("homeTeam - awayTeam", match.getTeamVersus());
		assertEquals(4, match.getNumberOfGoalsHome().intValue());
		assertEquals(5, match.getNumberOfGoalsAway().intValue());
		assertEquals("4 - 5", match.getResult());
		assertEquals(3, match.getMatchType().getCode());
		assertEquals("CUP", match.getMatchType().name());
		assertEquals("Firstname Middlename Lastname", match.getReferee().getFullName());
		assertTrue(match.isPlayed());
	}

	public void testConstructorNotPlayedDateBefore() {
		Match match = new Match(1, new Season("2013/2014", 0, 0), System.currentTimeMillis() - 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				null, null, "venue", new Referee("firstname", "middlename", "lastname"), MatchTypesEnum.CUP, MatchStatus.PLAYED.name());
		assertTrue(match.isFinished());
		assertNull(match.getNumberOfGoalsHome());
		assertNull(match.getNumberOfGoalsAway());
		assertNull(match.getResult());
		assertFalse(match.isPlayed());
	}

	public void testConstructorNotPlayedDateAfter() {
		Match match = new Match(1, new Season("2013/2014", 0, 0), System.currentTimeMillis() + 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				null, null, "venue", new Referee("firstname", "middlename", "lastname"), MatchTypesEnum.CUP, MatchStatus.PLAYED.name());
		assertFalse(match.isFinished());
		assertNull(match.getNumberOfGoalsHome());
		assertNull(match.getNumberOfGoalsAway());
		assertNull(match.getResult());
		assertFalse(match.isPlayed());
	}
}
