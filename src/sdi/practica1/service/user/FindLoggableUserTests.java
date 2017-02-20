package sdi.practica1.service.user;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.practica1.util.Fixture;
import uo.sdi.business.Services;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;

public class FindLoggableUserTests {

	private UserService userService;
	private User u1;

	@Before
	public void setUp() throws Exception {
		userService = Services.getUserService();
		u1 = Fixture.registerNewUser();
	}

	@After
	public void tearDown() throws Exception {
		Fixture.remove(u1);
	}

	/**
	 * An existing and enabled user is returned
	 * @throws BusinessException 
	 */
	@Test
	public void testFindLoggableValidUser() throws BusinessException {
		String login = u1.getLogin();
		String password = u1.getPassword();
		
		User u = userService.findLoggableUser(login, password);
		assertTrue( u1.equals( u ) );
	}
	
	/**
	 * An existing but disabled user is not returned
	 * @throws BusinessException 
	 */
	@Test
	public void testExistingButDisabledIsNotReturned() throws BusinessException {
		u1.setStatus( UserStatus.DISABLED );
		Fixture.update( u1 );
		
		String login = u1.getLogin();
		String password = u1.getPassword();
		
		User u = userService.findLoggableUser(login, password);
		assertTrue( u == null );
	}
	
	/**
	 * A non existing user login returns null
	 * @throws BusinessException 
	 */
	@Test
	public void testNonExistingLoginReturnsNull() throws BusinessException {
		String password = u1.getPassword();
		
		User u = userService.findLoggableUser("wrong", password);
		assertTrue( u == null );
	}

	/**
	 * An existing user login with wrong password returns null 
	 * @throws BusinessException 
	 */
	@Test
	public void testExistingWithWrongPasswordReturnsNull() throws BusinessException {
		String login = u1.getLogin();
		
		User u = userService.findLoggableUser(login, "wrong");
		assertTrue( u == null );
	}

	/**
	 * The password is case sensitive
	 * @throws BusinessException 
	 */
	@Test
	public void testPasswordCheckIsCaseSensitive() throws BusinessException {
		String login = u1.getLogin();
		
		User u = userService.findLoggableUser(login, "pAsSwOrD");
		assertTrue( u == null );
	}

}
