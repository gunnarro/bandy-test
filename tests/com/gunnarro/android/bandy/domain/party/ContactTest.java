package com.gunnarro.android.bandy.domain.party;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gunnarro.android.bandy.domain.Team;

public class ContactTest {

	@Test
	public void testConstructorNew() {
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		Contact newContact = new Contact(new Team("team name"), null, "firstName", "middleName", "lastName", "M", "11111111", "p1@email.no", address);
		assertTrue(newContact.isNew());
		assertFalse(newContact.hasTeamRoles());
		assertEquals("Firstname Middlename Lastname", newContact.getFullName());
		assertEquals("M", newContact.getGender());
		assertEquals("Country", newContact.getAddress().getCountry());
		assertEquals("Streetname 25C", newContact.getAddress().getFullStreetName());
		assertEquals("postalcode", newContact.getAddress().getPostalCode());
		assertEquals("Streetname 25C\npostalcode City\nCountry", newContact.getAddress().getFullAddress());
		assertTrue(newContact.getAddress().isAddressValid());

		Contact c = new Contact(new Team("teamName"), "firstName", "middleName", "lastName", "F", null);
		assertTrue(c.isNew());
		assertNull(c.getId());
	}

	@Test
	public void testConstructorId() {
		Contact contact = new Contact(new Integer(12), new Team("team name"), "firstName", "middleName", "lastName", "M");
		assertFalse(contact.isNew());
		assertEquals(12, contact.getId().intValue());
	}

}
