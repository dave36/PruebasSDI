package sdi.practica1.service.user;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.practica1.util.Builder;
import sdi.practica1.util.Fixture;
import uo.sdi.business.Services;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;

public class UpdateUserServiceTests {

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
	 * Update a non existent user (id) raises an exception
	 */
	@Test(expected = BusinessException.class)
	public void testUpdateNonExistingUser() throws BusinessException {
		User u = Builder.clone( u1 );
		u.setId( Fixture.NON_EXISTING_ID );		
		userService.updateUserDetails( u );
	}

	/**
	 * Update all valid fields of a user returns 1
	 * @throws BusinessException 
	 */
	@Test
	public void testValidUpdate() throws BusinessException {
		u1.setEmail("another@email.org");
		u1.setLogin("newUniqueLogin");
		u1.setPassword("newPassword");
		
		userService.updateUserDetails( u1 );
		
		User u = Fixture.findUserById( u1.getId() );
		assertTrue( u1.getEmail().equals( u.getEmail() ) );
		assertTrue( u1.getLogin().equals( u.getLogin() ) );
		assertTrue( u1.getPassword().equals( u.getPassword() ) );
	}

	/**
	 * Try to promote a user to admin raises an exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testUpdateIsAdminRaisesException() throws BusinessException {
		u1.setIsAdmin( true );		
		userService.updateUserDetails( u1 );
	}
	
	/**
	 * Try to degrade an admin to user raises an exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testDegradeAdminRaisesException() throws BusinessException {
		u1.setIsAdmin( true );
		Fixture.update(u1);
		
		u1.setIsAdmin( false );
		userService.updateUserDetails( u1 );
	}
	
	/**
	 * Updating the login of a user to a repeated value raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testUpdateLoginToRepeatedRaisesException() throws BusinessException {
		User other = Fixture.registerNewUser();
		try {
			u1.setLogin( other.getLogin() );
			userService.updateUserDetails( u1 );
		}
		finally {
			Fixture.remove(other);
		}
	}
	
	/**
	 * Try to update the login of a user to be too short raises an exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testUpdateLoginTooShortRaisesException() throws BusinessException {
		u1.setLogin( "ab" );
		userService.updateUserDetails( u1 );
	}
	
	/**
	 * Try to update a user with a password too short (6 chars at least)
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testUpdatePasswordToShortRaisesException() throws BusinessException {
		u1.setPassword( "abcde" );
		userService.updateUserDetails( u1 );
	}
	
	/**
	 * Try to update the email with an string not syntactically valid
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testUpdateInvalidEmailRaisesException() throws BusinessException {
		u1.setEmail( "abcde@" );
		userService.updateUserDetails( u1 );
	}
	
	/** 
	 * The status cannot be changed using this service
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testChangeStatusRaisesException() throws BusinessException {
		u1.setStatus( UserStatus.DISABLED );
		userService.updateUserDetails( u1 );
	}
	
}
