package sdi.gtd.persistence;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;
import uo.sdi.persistence.PersistenceException;
import uo.sdi.persistence.Persistence;
import uo.sdi.persistence.UserDao;

public class UserDaoTests {

	private User dto;
	private UserDao uDao;
	private Long userId;

	@Before
	public void setUp() throws Exception {
		dto = new User()
				.setLogin("userTest")
				.setEmail("userTest@mail.gtd")
				.setPassword("password")
				.setIsAdmin( true )
				.setStatus( UserStatus.DISABLED );
			
		uDao = Persistence.getUserDao();
		userId = uDao.save(dto);
		dto.setId( userId );
	}

	@After
	public void tearDown() throws Exception {
		uDao.delete(userId);
	}

	@Test(expected = PersistenceException.class)
	public void tesRepeatedLogin() {
		User other = new User()
				.setLogin("userTest")  // repeated login
				.setEmail("userTest@mail.gtd")
				.setPassword("password")
				.setIsAdmin( true )
				.setStatus( UserStatus.DISABLED );
		
		uDao.save( other );
	}
	
	@Test(expected = PersistenceException.class)
	public void tesNullValueSave() {
		User other = new User(); // Null values
		uDao.save( other );
	}

	@Test
	public void testFindByLogin() {
		User persisted = uDao.findByLogin("userTest");
		assertTrue( dto.equals( persisted ) );
	}

	/**
	 * When login and password matches the user is found
	 */
	@Test
	public void testFindByLoginAndPassword() {
		User persisted = uDao.findByLoginAndPassword("userTest", "password");
		assertTrue( dto.equals( persisted ) );
	}

	/**
	 * When either login or password don0t matches null is returned
	 */
	@Test
	public void testFindByLoginAndPasswordFailCases() {
		User persisted = uDao.findByLoginAndPassword("userTest", "wrong");
		assertTrue( persisted == null );

		persisted = uDao.findByLoginAndPassword("wrong", "password");
		assertTrue( persisted == null );

		persisted = uDao.findByLoginAndPassword("wrong", "wrong");
		assertTrue( persisted == null );
	}

	@Test
	public void testFindById() {
		User persisted = uDao.findById( userId );
		assertTrue( dto.equals( persisted ) );
	}

	@Test
	public void testFindAll() {
		List<User> dtos = uDao.findAll();
		assertTrue( dtos.contains( dto ) );
	}

	@Test
	public void testUpdate() {
		dto.setLogin( dto.getLogin() + " - updated" )
			.setPassword( "newPassword" )
			.setIsAdmin( false)
			.setEmail("no@mail.org")
			.setStatus( UserStatus.ENABLED );
		
		int nr = uDao.update(dto);
		assertTrue( nr == 1);
		
		User persisted = uDao.findById( userId );
		assertTrue( dto.equals( persisted ) );
	}

	@Test
	public void testDelete() {
		int nr = uDao.delete( userId );
		assertTrue( nr == 1);
		
		User persisted = uDao.findById( userId );
		assertTrue( persisted == null );
	}
}
