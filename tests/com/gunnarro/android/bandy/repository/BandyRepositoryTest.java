package com.gunnarro.android.bandy.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.activity.MatchEvent.MatchEventTypesEnum;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Status;
import com.gunnarro.android.bandy.domain.activity.Tournament;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.activity.Type.MatchTypesEnum;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl.PlayerLinkTableTypeEnum;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.impl.DataLoader;

@RunWith(RobolectricTestRunner.class)
public class BandyRepositoryTest {

	private static final String DB_PATH = "C:/code/git/bandy-master/database/sportsteam-1.0-SNAPSHOT.db";
	private BandyRepository bandyRepository;
	SQLiteDatabase db;

	@Before
	public void setUp() throws Exception {
		File dbFile = new File(DB_PATH);
		String dbPath = dbFile.getAbsolutePath();
		db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		bandyRepository = new BandyRepositoryImpl(db);
		// Must be set for each new db connection!
		db.execSQL("PRAGMA foreign_keys=\"ON\";");
		// bandyRepository.setForeignKeyConstraintsEnabled(true);
		// Cleanup after each test case
		bandyRepository.deleteAllTableData();
	}

	@After
	public void tearDown() throws Exception {
		// Perform any necessary clean-up operations...
	}

	@Ignore
	@Test
	public void rawSQLqueryTest() {
		// bandyRepository.deleteClub(7);
		// bandyRepository.deletePlayer(1);
		for (String name : bandyRepository.getClubNames()) {
			System.out.println("CLUB: " + name);
		}

		for (String name : bandyRepository.getTeamNames("%")) {
			System.out.println("TEAM: " + name);
		}

		for (Player player : bandyRepository.getPlayerList(1)) {
			System.out.println("PLAYER: " + player.getId() + " " + player.getFullName());
		}

		for (Contact contact : bandyRepository.getContactList(1)) {
			System.out.println("PLAYER: " + contact.getId() + " " + contact.getFullName());
		}
	}

	@Test
	public void databaseSettings() throws Exception {
		assertThat(bandyRepository.getSqliteVersion(), equalTo("3.7.2"));
		assertThat(bandyRepository.getDBUserVersion(), equalTo("1"));
		assertThat(bandyRepository.getDBFileName(), equalTo("0"));
		assertThat(bandyRepository.getDBEncoding(), equalTo("UTF-8"));
		assertThat(bandyRepository.getDBforeignkeys(), equalTo("1"));
	}

	@Test
	public void verifySettings() {
		assertThat(DataLoader.TEAM_XML_URL + "/uil.xml", equalTo(bandyRepository.getSetting(SettingsTable.DATA_FILE_URL_KEY)));
		// assertThat("0.1",
		// equalTo(bandyRepository.getSetting(SettingsTable.DATA_FILE_VERSION_KEY)));
		// assertThat("0",
		// equalTo(bandyRepository.getSetting(SettingsTable.DATA_FILE_LAST_UPDATED_KEY)));
		assertThat("na", equalTo(bandyRepository.getSetting(SettingsTable.MAIL_ACCOUNT_KEY)));
		assertThat("na", equalTo(bandyRepository.getSetting(SettingsTable.MAIL_ACCOUNT_PWD_KEY)));
	}

	@Test
	public void checkInitializedData() {
		assertArrayEquals(new Item[] { new Item(0, "CUP"), new Item(0, "LEAGUE"), new Item(0, "TOURNAMENT"), new Item(0, "TRAINING") },
				bandyRepository.getMatchTypes());
		assertArrayEquals(new Item[] { new Item(0, "CANCELLED"), new Item(0, "NOT PLAYED"), new Item(0, "ONGOING"), new Item(0, "PLAYED"),
				new Item(0, "POSTPONED") }, bandyRepository.getMatchStatusList());
		assertArrayEquals(new String[] { "Gutt", "Junior", "Knøtt", "Lillegutt", "Old boys", "Smågutt", "Veteran" }, bandyRepository.getLeagueNames());
		assertArrayEquals(new String[] { "ACTIVE", "INJURED", "PASSIVE", "QUIT" }, bandyRepository.getPlayerStatusTypes());
		assertArrayEquals(new String[] { "BOARD MEMBER", "CHAIRMAN", "COACH", "DEFAULT", "DEPUTY CHAIRMAN", "PARENT", "TEAMLEAD" },
				bandyRepository.getRoleTypeNames());
		assertArrayEquals(new String[] { "2013/2014", "2014/2015", "2015/2016", "2016/2017" }, bandyRepository.getSeasonPeriodes());
	}

	@Test
	public void verifyGettersNoMatch() {
		assertNull(bandyRepository.getClub(1));
		assertNull(bandyRepository.getTeam(1));
		assertNull(bandyRepository.getPlayer(1));
		assertNull(bandyRepository.getContact(1));
		assertNull(bandyRepository.getMatch(1));
		assertNull(bandyRepository.getLeague("invalid"));
		assertNull(bandyRepository.getReferee(1));
		assertNull(bandyRepository.getSeason(0));
		// assertNull(bandyRepository.getSetting("invalid"));
	}

	@Test
	public void verifyGettersNoMatchReturnEmptyList() {
		assertEquals(0, bandyRepository.getClubNames().length);
		assertEquals(0, bandyRepository.getContactList(1).size());
		assertEquals(0, bandyRepository.getCupList(1, 2014).size());
		assertEquals(0, bandyRepository.getMatchEventList(0).size());
		assertEquals(0, bandyRepository.getMatchList(1, 2014).size());
		assertEquals(0, bandyRepository.getMatchPlayerList(1, 1).size());
		assertEquals(0, bandyRepository.getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.MATCH, 1));
		assertEquals(0, bandyRepository.getPlayerList(1).size());
		assertEquals(0, bandyRepository.getPlayersAsItemList(1).size());
		assertEquals(0, bandyRepository.getRefereeNames().length);
		assertEquals(0, bandyRepository.getTeamList("unkown_club_name").size());
		assertEquals(0, bandyRepository.getTeamNames("unkown_club_name").length);
		assertEquals(0, bandyRepository.getTeamNames("unkown_club_name").length);
		assertEquals(0, bandyRepository.getTrainingList(1, 2014).size());
	}

	@Test
	public void newClubTeamPlayerAndReferee() {
		Long addressId = bandyRepository.createAddress(new Address("Stavangergt", "22", null, "9090", "oslo", "norge"));
		Address clubAddress = bandyRepository.getAddress(addressId.intValue());
		assertNotNull(clubAddress);
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", clubAddress, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		assertNotNull(club);
		assertEquals("newSportsClub", club.getName());
		assertEquals("newBandy", club.getDepartmentName());
		assertEquals("CK", club.getClubNameAbbreviation());
		assertEquals("bandyStadium", club.getStadiumName());
		assertEquals("http://club.homepage.org", club.getHomePageUrl());
		assertEquals("Stavangergt 22", club.getAddress().getFullStreetName());
		assertEquals("9090", club.getAddress().getPostalCode());
		assertEquals("Oslo", club.getAddress().getCity());
		assertEquals("Norge", club.getAddress().getCountry());

		// Create team
		int teamId = bandyRepository.createTeam(new Team("newTeam", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		assertNotNull(team);
		assertEquals("newTeam", team.getName());
		assertEquals("Male", team.getGender());
		assertEquals(2004, team.getTeamYearOfBirth().intValue());

		// Create player
		long dateOfBirth = System.currentTimeMillis();
		List<Contact> parents = new ArrayList<Contact>();
		Address playerAddress = new Address("streetname", "25", "c", "postalcode", "city", "country");
		int playerId = bandyRepository.createPlayer(new Player(team, "newPlayerFirstname", null, "newPlayerLastName", "M", PlayerStatusEnum.ACTIVE, parents,
				dateOfBirth, playerAddress));

		Player player = bandyRepository.getPlayer(playerId);
		assertNotNull(player);
		assertEquals("Newplayerfirstname Newplayerlastname", player.getFullName());
		assertEquals("M", player.getGender());
		assertFalse(player.hasParents());
		assertEquals(PlayerStatusEnum.ACTIVE, player.getStatus());
		assertEquals("City", player.getAddress().getCity());
		assertEquals("Country", player.getAddress().getCountry());
		assertEquals("Streetname 25C", player.getAddress().getFullStreetName());
		assertEquals("postalcode", player.getAddress().getPostalCode());
		assertTrue(player.getAddress().isAddressValid());

		// Create referee
		Referee newReferee = new Referee("refFirstname", "refMiddlename", "refLastname");
		newReferee.setClub(club);
		newReferee.setGender("M");
		newReferee.setMobileNumber("12345678");
		newReferee.setEmailAddress("r@mail.com");
		int refereeId = bandyRepository.createReferee(newReferee);
		Referee referee = bandyRepository.getReferee(refereeId);
		assertNotNull(referee);
		// Must cleanup, so the test case can be run multiple times
		// int deletedPlayerRows = bandyRepository.deletePlayer(playerId);
		// int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		// assertTrue(deletedPlayerRows == 1);
		// assertTrue(deletedTeamRows == 1);
		assertTrue(deletedClubRows == 1);
		Team deletedTeam = bandyRepository.getTeam(teamId);
		assertNull(deletedTeam);
		Player deletedPlayer = bandyRepository.getPlayer(playerId);
		assertNull(deletedPlayer);
		referee = bandyRepository.getReferee(refereeId);
		assertNull(referee);
	}

	@Test
	public void deleteClub() {
		// Create new club with address
		Address address = new Address("clubstreetname", "45", "b", "0889", "oslo", "Norway");
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", address, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		assertNotNull(club);
		int addressId = club.getAddress().getId();
		assertNotNull(addressId);

		// Check that when deleting address do not delete the club
		int deletedAddressRows = bandyRepository.deleteAddress(addressId);
		assertTrue(deletedAddressRows == 1);
		club = bandyRepository.getClub(clubId);
		assertNotNull(club);
		// Delete the club and check that the address is also deleted
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
		club = bandyRepository.getClub(clubId);
		assertNull(club);
		// verify that club address is deleted
		Address addressDeleted = bandyRepository.getAddress(addressId);
		assertNull(addressDeleted);
	}

	@Test
	public void newClubDuplicate() {
		int clubId = bandyRepository.createClub(new Club(null, "clubname", "duplicate", "CK", "bandyStadium", null, "http://club.homepage.org"));
		try {
			bandyRepository.createClub(new Club(null, "clubname", "duplicate", "CK", "bandyStadium", null, "http://club.homepage.org"));
		} catch (ApplicationException ae) {
			assertEquals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (columns club_name, club_department_name are not unique)", ae.getMessage());
		}
		// Clean up
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newTeamInvalidClub() {
		try {
			bandyRepository.createTeam(new Team("newTeam", new Club("name", "department"), 2004, "Male"));
		} catch (ApplicationException ae) {
			assertEquals("Club must be set for creating new Team!", ae.getMessage());
		}
	}

	@Test
	public void newTeamDuplicate() {
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("newTeam", club, 2004, "Male"));
		try {
			bandyRepository.createTeam(new Team("newTeam", club, 2004, "Male"));
		} catch (ApplicationException ae) {
			assertEquals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (column team_name is not unique)", ae.getMessage());
		}

		// Clean up
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newAddress() {
		Address newAddress = new Address("streetname", "45", "b", "0889", "oslo", "Norway");
		long addressId = bandyRepository.createAddress(newAddress);
		Address address = bandyRepository.getAddress(new Long(addressId).intValue());
		assertTrue(address.isAddressValid());
		assertEquals("Streetname 45B", address.getFullStreetName());
		assertEquals("0889", address.getPostalCode());
		assertEquals("Oslo", address.getCity());
		assertEquals("Norway", address.getCountry());

		// Clean up
		int deletedAddressRows = bandyRepository.deleteAddress(new Long(addressId).intValue());
		assertTrue(deletedAddressRows == 1);
	}

	@Test
	public void newContact() {
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		int contactId = bandyRepository.createContact(new Contact(new Team("team name"), null, "firstName", "middleName", "lastName", "M", "11111111",
				"p1@email.no", address));
		Contact contact = bandyRepository.getContact(contactId);
		assertNotNull(contact);
		assertNotNull(contact.getAddress());
		assertFalse(contact.hasTeamRoles());
		assertEquals("Firstname Middlename Lastname", contact.getFullName());
		assertEquals("M", contact.getGender());
		assertEquals("Country", contact.getAddress().getCountry());
		assertEquals("Streetname 25C", contact.getAddress().getFullStreetName());
		assertEquals("postalcode", contact.getAddress().getPostalCode());
		assertTrue(contact.getAddress().isAddressValid());

		// Must cleanup
		int deletedContactRows = bandyRepository.deleteContact(contactId);
		assertTrue(deletedContactRows == 1);
		Address addressDeleted = bandyRepository.getAddress(contact.getAddress().getId());
		assertNull(addressDeleted);
	}

	@Test
	public void newPlayer() {
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		int playerId = bandyRepository.createPlayer(new Player(team, "firstName", "middleName", "lastName", "M", PlayerStatusEnum.ACTIVE, null, System
				.currentTimeMillis(), address));

		Player player = bandyRepository.getPlayer(playerId);
		assertNotNull(player);
		assertEquals("Firstname Middlename Lastname", player.getFullName());
		assertEquals("M", player.getGender());
		assertEquals("Country", player.getAddress().getCountry());
		assertEquals("Streetname 25C", player.getAddress().getFullStreetName());
		assertEquals("postalcode", player.getAddress().getPostalCode());
		assertTrue(player.getAddress().isAddressValid());

		// Must cleanup
		int deletedPlayerRows = bandyRepository.deletePlayer(playerId);
		assertTrue(deletedPlayerRows == 1);
		Address addressDeleted = bandyRepository.getAddress(player.getAddress().getId());
		assertNull(addressDeleted);
		int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		assertTrue(deletedTeamRows == 1);
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newReferee() {
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		Referee newReferee = new Referee("firstName", "middleName", "lastName");
		newReferee.setAddress(address);
		newReferee.setMobileNumber("+4712345678");
		newReferee.setEmailAddress("new.referee@mail.com");
		newReferee.setGender("M");
		int refereeId = bandyRepository.createReferee(newReferee);
		Referee referee = bandyRepository.getReferee(refereeId);
		assertNotNull(referee);
		assertEquals("Firstname Middlename Lastname", referee.getFullName());
		assertEquals("M", referee.getGender());
		assertEquals("+4712345678", referee.getMobileNumber());
		assertEquals("new.referee@mail.com", referee.getEmailAddress());
		assertTrue(referee.getAddress().isAddressValid());
		assertNotNull(referee.getAddress().getId());
		assertEquals("Country", referee.getAddress().getCountry());
		assertEquals("Streetname 25C", referee.getAddress().getFullStreetName());
		assertEquals("postalcode", referee.getAddress().getPostalCode());

		// Must cleanup
		int addressId = referee.getAddress().getId();
		int deletedRefereeRows = bandyRepository.deleteReferee(refereeId);
		assertTrue(deletedRefereeRows == 1);
		Address addressDeleted = bandyRepository.getAddress(addressId);
		assertNull(addressDeleted);
	}

	@Test
	public void newTraining() {
		Season season = bandyRepository.getSeason("2014/2015");
		assertNotNull(season.getId());
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		long startTime = System.currentTimeMillis();
		Training newTraining = new Training(season, startTime, System.currentTimeMillis(), team, "place");
		int newTrainingId = bandyRepository.createTraining(newTraining);
		Training training = bandyRepository.getTrainingByDate(team.getId(), startTime);
		assertEquals("2014/2015", training.getSeason().getPeriod());
		assertEquals("place", training.getVenue());
		// assertEquals(startTime, training.getStartTime());
		assertEquals("team name", training.getTeam().getName());
		assertEquals(0, training.getNumberOfParticipatedPlayers().intValue());

		int playerId = bandyRepository.createPlayer(new Player(team, "firstName", "middleName", "lastName", "M", PlayerStatusEnum.ACTIVE, null, System
				.currentTimeMillis(), null));

		Player player = bandyRepository.getPlayer(playerId);
		assertNotNull(player);
		// register player for training
		this.bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.TRAINING, playerId, newTrainingId);
		training = bandyRepository.getTraining(newTrainingId);
		assertEquals(1, training.getNumberOfParticipatedPlayers().intValue());

		// unregister player for training
		this.bandyRepository.deletePlayerLink(PlayerLinkTableTypeEnum.TRAINING, playerId, newTrainingId);
		training = bandyRepository.getTraining(newTrainingId);
		assertEquals(0, training.getNumberOfParticipatedPlayers().intValue());

		// Reasign player
		this.bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.TRAINING, playerId, newTrainingId);
		assertEquals(1, bandyRepository.getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.TRAINING, newTrainingId));

		// delete training
		int deletedTrainingRows = bandyRepository.deleteTraining(newTrainingId);
		assertTrue(deletedTrainingRows == 1);

		// check that team and player are not deleted upon training delete
		assertNotNull(bandyRepository.getTeam(teamId));
		assertNotNull(bandyRepository.getPlayer(playerId));
		assertEquals(0, bandyRepository.getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.TRAINING, newTrainingId));

		// Clean up after test case
		int deleteClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deleteClubRows == 1);
		// int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		// assertTrue(deletedTeamRows == 1);
		// int deletedPlayerRows = bandyRepository.deletePlayer(playerId);
		// assertTrue(deletedPlayerRows == 1);
	}

	@Test
	public void newMatch() {
		int clubId = bandyRepository.createClub(new Club(null, "club1", "football", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team1", club, 2003, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Season season = bandyRepository.getSeason("2013/2014");
		Match newMatch = new Match(season, System.currentTimeMillis() - 60000, team, new Team("homeTeam"), new Team("awayTeam"), "venue", new Referee(
				"referee-firstname", null, "referee-lastname"));
		int matchId = bandyRepository.createMatch(newMatch);
		Match match = bandyRepository.getMatch(matchId);
		// assertEquals("teamName", match.getTeam().getName());
		assertEquals("homeTeam - awayTeam", match.getTeamVersus());
		assertEquals("NOT PLAYED", match.getMatchStatus().getName());
		assertEquals(MatchTypesEnum.LEAGUE, match.getMatchType());
		assertEquals("0 - 0", match.getResult());
		assertNull(match.getReferee());
		assertEquals(0, match.getNumberOfSignedPlayers().intValue());
		assertEquals(0, match.getNumberOfGoalsHome().intValue());
		assertEquals(0, match.getNumberOfGoalsAway().intValue());
		assertEquals("0 - 0", match.getResult());
		assertFalse(match.isPlayed());
		assertEquals(0, match.getNumberOfSignedPlayers().intValue());

		// Registrer referee
		Referee referee = new Referee(null, "rfirstName", "rmiddleName", "rlastName", "M");
		referee.setMobileNumber("+4711223344");
		referee.setEmailAddress("referee@mail.com");
		int refereeId = bandyRepository.createReferee(referee);
		bandyRepository.registrerRefereeForMatch(refereeId, matchId);
		match = bandyRepository.getMatch(matchId);
		assertEquals("Rfirstname Rmiddlename Rlastname", match.getReferee().getFullName());

		// Registrer player
		int playerId = bandyRepository.createPlayer(new Player(team, "pfirstName", "pmiddleName", "plastName", "M", PlayerStatusEnum.ACTIVE, null, System
				.currentTimeMillis(), null));
		bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.MATCH, playerId, matchId);
		match = bandyRepository.getMatch(matchId);
		assertEquals(1, match.getNumberOfSignedPlayers().intValue());

		// Unregistrer player
		int deletePlayerLinkRows = bandyRepository.deletePlayerLink(PlayerLinkTableTypeEnum.MATCH, playerId, matchId);
		assertEquals(1, deletePlayerLinkRows);
		match = bandyRepository.getMatch(matchId);
		assertEquals(0, match.getNumberOfSignedPlayers().intValue());

		// Then recreate player link in order to verify that it is removed upon
		// match delete at end of this test
		bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.MATCH, playerId, matchId);

		// Match update score
		bandyRepository.updateGoals(matchId, 2, true);
		bandyRepository.createMatchEvent(new MatchEvent(matchId, 23, "newTeam", "player hometeam", MatchEventTypesEnum.GOAL_HOME.toString(), "2"));
		bandyRepository.updateGoals(matchId, 3, false);
		bandyRepository.createMatchEvent(new MatchEvent(matchId, 43, "newTeam", "player awayteam", MatchEventTypesEnum.GOAL_AWAY.toString(), "3"));

		match = bandyRepository.getMatch(matchId);
		assertEquals("2 - 3", match.getResult());

		List<MatchEvent> matchEventList = bandyRepository.getMatchEventList(matchId);
		assertEquals(2, matchEventList.size());
		assertEquals(matchId, matchEventList.get(0).getMatchId());
		assertEquals("newTeam", matchEventList.get(0).getTeamName());
		assertEquals("player hometeam", matchEventList.get(0).getPlayerName());
		assertEquals(MatchEventTypesEnum.GOAL_HOME.name(), matchEventList.get(0).getEventTypeName());
		assertEquals(23, matchEventList.get(0).getPlayedMinutes().intValue());
		assertEquals("2", matchEventList.get(0).getValue());

		assertEquals(matchId, matchEventList.get(1).getMatchId());
		assertEquals("newTeam", matchEventList.get(1).getTeamName());
		assertEquals("player awayteam", matchEventList.get(1).getPlayerName());
		assertEquals(MatchEventTypesEnum.GOAL_AWAY.name(), matchEventList.get(1).getEventTypeName());
		assertEquals(43, matchEventList.get(1).getPlayedMinutes().intValue());
		assertEquals("3", matchEventList.get(1).getValue());

		Status matchStatus = bandyRepository.getMatchStatus(2);
		bandyRepository.updateMatchStatus(matchId, matchStatus.getId());
		match = bandyRepository.getMatch(matchId);
		assertEquals("PLAYED", match.getStatus().getName());

		// Clean up
		// int deletedMatchEventsRows =
		// bandyRepository.deleteMatchEvents(matchId);
		// assertTrue(deletedMatchEventsRows == 2);
		int deletedMatchRows = bandyRepository.deleteMatch(matchId);
		// Should delete, match, match events, and player link
		assertEquals(1, deletedMatchRows);

		// Check that team and player not are deleted upon match delete
		assertNotNull(bandyRepository.getTeam(teamId));
		assertNotNull(bandyRepository.getPlayer(playerId));

		// int deletedPlayerRows = bandyRepository.deletePlayer(playerId);
		// assertTrue(deletedPlayerRows == 1);
		// int deletedRefereeRows = bandyRepository.deleteReferee(refereeId);
		// assertTrue(deletedRefereeRows == 1);
		// int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		// assertTrue(deletedTeamRows == 1);
		// Everything will be deleted upon club delete
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newCup() {
		Season season = bandyRepository.getSeason("2014/2015");
		Cup newCup = new Cup(season, System.currentTimeMillis(), "Cup Name", "arranging club name", "cup-venue", System.currentTimeMillis());
		int cupId = bandyRepository.createCup(newCup);
		Cup cup = bandyRepository.getCup(cupId);
		assertEquals("Cup Name", cup.getCupName());
		assertEquals("arranging club name", cup.getClubName());
		assertEquals("cup-venue", cup.getVenue());
		assertEquals(0, cup.getNumberOfParticipatedPlayers().intValue());

		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Match cupMatch = Match.createCupMatch();
		cupMatch.setStartTime(System.currentTimeMillis());
		cupMatch.setTeam(team);
		cupMatch.setHomeTeam(new Team("home-team"));
		cupMatch.setAwayTeam(new Team("away-team"));
		cupMatch.setVenue("venue");
		int matchId = bandyRepository.createMatch(cupMatch);
		int createCupMatchLnk = bandyRepository.createCupMatchLnk(cupId, matchId);
		assertTrue(createCupMatchLnk > -1);
		int deletedCupRows = bandyRepository.deleteCup(cupId);
		assertTrue(deletedCupRows == 1);
	}

	@Test
	public void newTournament() {
		Season season = bandyRepository.getSeason("2014/2015");
		assertNotNull(season.getId());
		Tournament newTournament = new Tournament(season, System.currentTimeMillis(), "Tournament Name", "Organizer club name", "place",
				System.currentTimeMillis());
		int tournamentId = bandyRepository.createTournament(newTournament);
		Tournament tournament = bandyRepository.getTournament(tournamentId);
		assertEquals("Tournament Name", tournament.getTournamentName());
		assertEquals("Organizer club name", tournament.getOrganizerName());
		assertEquals("place", tournament.getVenue());

		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		Integer teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		assertTrue(teamId.intValue() > 0);

		List<Tournament> list = bandyRepository.getTournaments(teamId, 2014);
		assertEquals(0, list.size());
		bandyRepository.tournamentRegistration(tournamentId, teamId);
		List<Tournament> tournaments = bandyRepository.getTournaments(teamId, 2014);
		assertEquals(1, tournaments.size());
		assertEquals("Tournament Name", tournaments.get(0).getTournamentName());
		int tournamentUnRegistrationRows = bandyRepository.tournamentUnRegistration(tournamentId, teamId);
		assertEquals(1, tournamentUnRegistrationRows);
		tournaments = bandyRepository.getTournaments(teamId, 2014);
		assertEquals(0, tournaments.size());

		int deletedTournamentRows = bandyRepository.deleteTournament(tournamentId);
		assertTrue(deletedTournamentRows == 1);
	}

	@Test
	public void addToSeason() {
		Season season = bandyRepository.getSeason("2014/2015");
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Match newMatch = new Match(season, System.currentTimeMillis() - 60000, team, new Team("homeTeam"), new Team("awayTeam"), "venue", new Referee(
				"referee-firstname", null, "referee-lastname"));
		bandyRepository.createMatch(newMatch);
		Training newTraining = new Training(season, System.currentTimeMillis(), System.currentTimeMillis(), team, "place");
		bandyRepository.createTraining(newTraining);
		// FIXME
	}

	@Test
	public void teamStatistic() {
	}

	@Test
	public void playerStatistic() {
	}

	@Ignore
	@Test
	public void recreateDatabaseTabels() {
		try {
			// Use transaction, all or nothing
			// db.beginTransaction();
			// BandyDataBaseHjelper.dropAllTables(db);
			BandyDataBaseHjelper.createTables(db);
			BandyDataBaseHjelper.insertDefaultData(db);
			// db.isDatabaseIntegrityOk();
			// db.endTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
