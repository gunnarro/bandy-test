package com.gunnarro.android.bandy.domain.party;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.domain.party.Role.RoleTypesEnum;

public class PlayerTest {

	@Test
	public void testConstructorNew() {
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		List<Contact> parents = new ArrayList<Contact>();
		List<RoleTypesEnum> p1roles = new ArrayList<RoleTypesEnum>();
		p1roles.add(RoleTypesEnum.COACH);
		List<RoleTypesEnum> p2roles = new ArrayList<RoleTypesEnum>();
		p2roles.add(RoleTypesEnum.TEAMLEAD);
		parents.add(new Contact(new Team("team name"), p1roles, "p1firstname", "p1middleName", "p1lastName", "M", "11111111", "p1@email.no", address));
		parents.add(new Contact(new Team("team name"), p2roles, "p2firstname", "p2middleName", "p2lastName", "M", "22222222", "p2@email.no", address));
		long dateOfBirth = System.currentTimeMillis();
		Player newPlayer = new Player(new Team("team name"), "firstname", null, "lastname", "M", PlayerStatusEnum.ACTIVE, parents, dateOfBirth, address);
		assertTrue(newPlayer.isNew());
		assertEquals("Firstname Lastname", newPlayer.getFullName());
		assertEquals("M", newPlayer.getGender());
		assertEquals(PlayerStatusEnum.ACTIVE, newPlayer.getStatus());
		assertTrue(newPlayer.hasParents());
		assertEquals("COACH", newPlayer.getParents().get(0).getRoles().get(0).name());
		assertEquals("P1firstname P1middlename P1lastname", newPlayer.getParents().get(0).getFullName());
		assertEquals("p1@email.no", newPlayer.getParents().get(0).getEmailAddress());
		assertEquals("11111111", newPlayer.getParents().get(0).getMobileNumber());
		assertEquals("City", newPlayer.getAddress().getCity());
		assertEquals("Country", newPlayer.getAddress().getCountry());
		assertEquals("Streetname 25C", newPlayer.getAddress().getFullStreetName());
		assertEquals("postalcode", newPlayer.getAddress().getPostalCode());
		assertTrue(newPlayer.getAddress().isAddressValid());
	}

	@Test
	public void testConstructorId() {
		Player player = new Player(221, new Team("team name"), "firstname", "middlename", "lastname", "M", PlayerStatusEnum.ACTIVE, new ArrayList<Contact>(),
				0, null);
		assertFalse(player.isNew());
		assertEquals(221, player.getId().intValue());
	}
}
