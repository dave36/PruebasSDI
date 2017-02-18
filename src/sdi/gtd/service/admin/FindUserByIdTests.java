package sdi.gtd.service.admin;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.gtd.util.Fixture;
import uo.sdi.business.AdminService;
import uo.sdi.business.Services;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;
import uo.sdi.persistence.Persistence;

public class FindUserByIdTests {

	private AdminService adminService;
	private User u1;
	
	@Before
	public void setUp() throws Exception {
		adminService = Services.getAdminService();
		u1 = Fixture.registerNewUser();
	}

	@After
	public void tearDown() throws Exception {
		Fixture.remove(u1);
	}

	/**
	 * A non existing user returns null
	 * @throws BusinessException 
	 */
	@Test
	public void testNonExistingUserReturnsNull() throws BusinessException {
		User u = adminService.findUserById( Fixture.NON_EXISTING_ID );
		
		assertTrue( u == null );
	}
	
	/**
	 * An existing enabled user is returned 
	 * @throws BusinessException 
	 */
	@Test
	public void testAnEnabledUserCanBeFound() throws BusinessException {
		User u = adminService.findUserById( u1.getId() );
		
		assertTrue( u1.equals( u ) );
	}
	
	/**
	 * An existing disabled user is returned
	 * @throws BusinessException 
	 */
	@Test
	public void testDisabledUserCanBeFound() throws BusinessException {
		u1.setStatus( UserStatus.DISABLED );
		Persistence.getUserDao().update( u1 );
		
		User u = adminService.findUserById( u1.getId() );
		
		assertTrue( u1.equals( u ) );
	}

}
