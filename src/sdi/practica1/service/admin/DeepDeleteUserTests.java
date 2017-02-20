package sdi.practica1.service.admin;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.practica1.util.Fixture;
import uo.sdi.business.AdminService;
import uo.sdi.business.Services;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;

public class DeepDeleteUserTests {

	private AdminService service;

	@Before
	public void setUp() throws Exception {
		service = Services.getAdminService();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * A user without tasks and categories is deleted
	 * @throws BusinessException 
	 */
	@Test
	public void testUserWithoutTasksAndCategoriesIsDeleted() throws BusinessException {
		User u1 = Fixture.registerNewUser();
		Long id = u1.getId();
		
		service.deepDeleteUser( id );
		
		assertTrue( service.findUserById( id ) == null );
	}
	
	/**
	 * A user with categories is deleted along with its categories
	 * @throws BusinessException 
	 */
	@Test
	public void testUserAndCategoriesAreDeleted() throws BusinessException {
		User u1 = Fixture.registerNewUser();
		Fixture.registerNewCategoryForUser( u1 );
		Long id = u1.getId();
	
		service.deepDeleteUser( id );
		
		assertTrue( service.findUserById( id ) == null );
		assertTrue( Fixture.findCategoriesByUserId( id ).isEmpty() );
	}
	
	/**
	 * A user with tasks uncategorized is deleted along with its tasks
	 * @throws BusinessException 
	 */
	@Test
	public void testUserAndInboxTasksAreDeleted() throws BusinessException {
		User u1 = Fixture.registerNewUser();
		Fixture.registerNewTaskForUser( u1 );
		Long id = u1.getId();
	
		service.deepDeleteUser( id );
		
		assertTrue( service.findUserById( id ) == null );
		assertTrue( Fixture.findTasksByUserId( id ).isEmpty() );
	}
	
	/**
	 * A user with categories and tasks is deleted along categories and tasks
	 * @throws BusinessException 
	 */
	@Test
	public void testUserCategoriesAndTasksAreDeleted() throws BusinessException {
		User u1 = Fixture.registerNewUser();
		Fixture.registerNewCategoryForUser( u1 );
		Fixture.registerNewTaskForUser( u1 );
		Long id = u1.getId();
	
		service.deepDeleteUser( id );
		
		assertTrue( service.findUserById( id ) == null );
		assertTrue( Fixture.findCategoriesByUserId( id ).isEmpty() );
		assertTrue( Fixture.findTasksByUserId( id ).isEmpty() );
	}

}
