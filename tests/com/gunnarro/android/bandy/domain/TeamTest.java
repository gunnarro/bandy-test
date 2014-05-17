package com.gunnarro.android.bandy.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TeamTest {

	@Test
	public void testConstructorNew() {
		Team team = new Team("teamName", new Club("name", "department"), 2004, "Male");
		assertTrue(team.isNew());
		assertEquals("teamName", team.getName());
		assertEquals("Male", team.getGender());
		assertEquals(2004, team.getTeamYearOfBirth().intValue());
		assertEquals("name department", team.getClub().getFullName());
	}

	@Test
	public void testConstructorId() {
		Team team = new Team(23, "TeamName");
		assertFalse(team.isNew());
		assertEquals(23, team.getId().intValue());
	}
}
