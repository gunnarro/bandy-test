package com.gunnarro.android.bandy.domain.statistic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gunnarro.android.bandy.domain.activity.Type.MatchTypesEnum;

public class MatchStatisticTest {

	@Test
	public void testConstructor() {
		MatchStatistic matchStatistic = new MatchStatistic(10, "Team Name", MatchTypesEnum.LEAGUE, 1, 2, 3, 4, 5, 6);
		assertEquals(10, matchStatistic.getSeasonId());
		assertEquals("Team Name", matchStatistic.getTeamName());
		assertEquals(MatchTypesEnum.LEAGUE.name(), matchStatistic.getName());
		assertEquals(1, matchStatistic.getPlayed().intValue());
		assertEquals(2, matchStatistic.getWon().intValue());
		assertEquals(3, matchStatistic.getDraw().intValue());
		assertEquals(4, matchStatistic.getLoss().intValue());
		assertEquals(5, matchStatistic.getGoalsScored().intValue());
		assertEquals(6, matchStatistic.getGoalsAgainst().intValue());

		matchStatistic.incrementPlayed(1);
		matchStatistic.incrementWon(2);
		matchStatistic.incrementDraw(3);
		matchStatistic.incrementLoss(4);
		matchStatistic.incrementGoalsScored(5);
		matchStatistic.incrementGoalsAgainst(6);

		assertEquals(2, matchStatistic.getPlayed().intValue());
		assertEquals(4, matchStatistic.getWon().intValue());
		assertEquals(6, matchStatistic.getDraw().intValue());
		assertEquals(8, matchStatistic.getLoss().intValue());
		assertEquals(10, matchStatistic.getGoalsScored().intValue());
		assertEquals(12, matchStatistic.getGoalsAgainst().intValue());
	}

}
