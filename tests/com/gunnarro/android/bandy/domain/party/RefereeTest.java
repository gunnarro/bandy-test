package com.gunnarro.android.bandy.domain.party;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RefereeTest {

	@Test
	public void testConstructorNew() {
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		Referee newReferee = new Referee("referee-firstname", "referee-middlename", "referee-lastname");
		newReferee.setMobileNumber("+471234567891");
		newReferee.setEmailAddress("referee@mail.com");
		newReferee.setAddress(address);
		assertTrue(newReferee.isNew());
		assertEquals("Referee-firstname Referee-middlename Referee-lastname", newReferee.getFullName());
		assertNull(newReferee.getGender());
		assertEquals("+471234567891", newReferee.getMobileNumber());
		assertEquals("referee@mail.com", newReferee.getEmailAddress());
		assertEquals("City", newReferee.getAddress().getCity());
		assertEquals("Country", newReferee.getAddress().getCountry());
		assertEquals("Streetname 25C", newReferee.getAddress().getFullStreetName());
		assertEquals("postalcode", newReferee.getAddress().getPostalCode());
		assertTrue(newReferee.getAddress().isAddressValid());
	}

	@Test
	public void testConstructorId() {
		 Referee referee = new Referee(123, "referee-firstname", "referee-middlename", "referee-lastname", "M");
		 assertFalse(referee.isNew());
		 assertEquals(123, referee.getId().intValue());
	}
}
