package sdi.practica1.service.user;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.practica1.util.Builder;
import sdi.practica1.util.Fixture;
import uo.sdi.business.Services;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;

public class RegisterUserTests {

	private UserService userService;
	private User u1;

	@Before
	public void setUp() throws Exception {
		userService = Services.getUserService();
		u1 = Builder.newUser();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Register new non existent user
	 * @throws BusinessException 
	 */
	@Test
	public void testNonExistentUserIsRegistered() throws BusinessException {
		Long id = userService.registerUser( u1 );
		try {
			u1.setId( id );
			
			User user = Fixture.findUserById( id );
			assertTrue( u1.equals( user ) );
		}
		finally {
			Fixture.remove(u1);			
		}
	}
	
	/**
	 * Try to register a duplicated user
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testDuplicatedUserCannotBeRegistered() throws BusinessException {
		Fixture.registerUser( u1 );
		try {
			userService.registerUser( u1 );
		} 
		finally {
			Fixture.remove(u1);						
		}
	}
	
	/**
	 * Try to register a new user as admin
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testUserAsAdminCannotBeRegistered() throws BusinessException {
		u1.setIsAdmin( true );
		userService.registerUser( u1 );
	}
	
	/**
	 * Try to register a new user with invalid email (syntactically)
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testInvalidEmailCannotBeRegistered() throws BusinessException {
		u1.setEmail("thisIsNotValidEmail");
		userService.registerUser( u1 );
	}
	
	/**
	 * Try to register a new user with login too short (>=3 chars)
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testLoginTooShortCannotBeRegistered() throws BusinessException {
		u1.setLogin("ab");
		userService.registerUser( u1 );
	}
	
	/**
	 * Try to register a new user with password too short (>=6 chars)
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testPasswordTooShortCannotBeRegistered() throws BusinessException {
		u1.setPassword("abcde");
		userService.registerUser( u1 );
	}

}
