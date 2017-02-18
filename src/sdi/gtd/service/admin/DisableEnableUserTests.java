package sdi.gtd.service.admin;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.gtd.util.Builder;
import sdi.gtd.util.Fixture;
import uo.sdi.business.AdminService;
import uo.sdi.business.Services;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;

public class DisableEnableUserTests {
	
	private User u1;
	private User u2;

	private AdminService adminService;
	private UserService userService;
	
	@Before
	public void setUp() {
		u1 = Fixture.registerNewUser();
		u2 = registerNewDisabledUser();
		adminService = Services.getAdminService();
		userService = Services.getUserService();
	}
	
	@After
	public void tearDown() throws BusinessException {
		Fixture.remove(u1);
		Fixture.remove(u2);
	}

	private User registerNewDisabledUser() {
		User user = Builder.newUser().setStatus( UserStatus.DISABLED );
		return Fixture.registerUser(user);
	}

	/**
	 * A user is disabled and then cannot log-in
	 * @throws BusinessException
	 */
	@Test
	public void testLockUser() throws BusinessException {
		adminService.disableUser( u1.getId() );
		
		User u = adminService.findUserById( u1.getId() );
		assertTrue( u.getStatus().equals( UserStatus.DISABLED ) );
		
		String login = u.getLogin();
		String password = u.getPassword();
		User userLoggedIn = userService.findLoggableUser(login, password);
		assertTrue( userLoggedIn == null );
	}

	/**
	 * A disabled user is enabled and then can log-in
	 * @throws BusinessException
	 */
	@Test
	public void testUnlockUser() throws BusinessException {
		adminService.enableUser( u2.getId() );
		
		User u = adminService.findUserById( u2.getId() );
		assertTrue( u.getStatus().equals( UserStatus.ENABLED ) );
		
		String login = u.getLogin();
		String password = u.getPassword();
		User userLoggedIn = userService.findLoggableUser(login, password);
		assertTrue( userLoggedIn.equals( u ) );
	}

}
