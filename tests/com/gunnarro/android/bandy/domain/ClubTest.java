package com.gunnarro.android.bandy.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gunnarro.android.bandy.domain.party.Address;

public class ClubTest {

	@Test
	public void testConstructorNew() {
		Address address = new Address("Stavangergt", "22", null, "9090", "oslo", "norge");
		Club club = new Club(null, "SportsClub", "Bandy", "CK", "Stadium", address, "http://club.homepage.org");
		assertTrue(club.isNew());
		assertEquals("SportsClub", club.getName());
		assertEquals("Bandy", club.getDepartmentName());
		assertEquals("CK", club.getClubNameAbbreviation());
		assertEquals("Stadium", club.getStadiumName());
		assertEquals("http://club.homepage.org", club.getHomePageUrl());
		assertEquals("Stavangergt 22", club.getAddress().getFullStreetName());
		assertEquals("9090", club.getAddress().getPostalCode());
		assertEquals("Oslo", club.getAddress().getCity());
		assertEquals("Norge", club.getAddress().getCountry());
	}

	@Test
	public void testConstructorId() {
		Club club = new Club(345, "name", "departmentName", "clubNameAbbreviation", "stadiumName", null, "homepageUrl");
		assertFalse(club.isNew());
		assertEquals(345, club.getId().intValue());
	}
}
