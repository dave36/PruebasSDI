package sdi.practica1.service.task;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.practica1.util.Builder;
import sdi.practica1.util.Fixture;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;

public class CreateCategoryServiceTests {

	private TaskService service; 
	private User u1;

	@Before
	public void setUp() throws Exception {
		service = Services.getTaskService();
		u1 = Fixture.registerNewUser();
	}

	@After
	public void tearDown() throws Exception {
		Fixture.remove(u1);
	}

	/**
	 * Try to create a category without name raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryWithoutNameCannotBeCreated() throws BusinessException {
		Category c = Builder.newCategoryFor( u1.getId() );
		c.setName( null );
		service.createCategory( c );
	}
	
	/**
	 * Try to create a category with an empty name raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryWithEmptyNameCannotBeCreated() throws BusinessException {
		Category c = Builder.newCategoryFor( u1.getId() );
		c.setName( "" );
		service.createCategory( c );
	}
	
	/**
	 * Try to create a category with a repeated name raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryWithRepeatedNameCannotBeCreated() throws BusinessException {
		Category c1 = Fixture.registerNewCategoryForUser(u1);
		try {
			Category c = Builder.newCategoryFor( u1.getId() );
			c.setName( c1.getName() );
			service.createCategory( c );			
		}
		finally {
			Fixture.remove(c1);
		}
	}
	
	/**
	 * Try to create a valid category for a disabled user raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryForDisabledUserCannotBeCreated() throws BusinessException {
		u1.setStatus( UserStatus.DISABLED );
		Fixture.update(u1);

		Category c = Builder.newCategoryFor( u1.getId() );
		service.createCategory( c );
	}
	
	/**
	 * Try to create a category for an admin user raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryForAdminUserCannotBeCreated() throws BusinessException {
		u1.setIsAdmin( true );
		Fixture.update(u1);

		Category c = Builder.newCategoryFor( u1.getId() );
		service.createCategory( c );
	}
	
	/**
	 * Try to create a valid category for a null user raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryForNullUserRaisesException() throws BusinessException {
		Category c = Builder.newCategoryFor( u1.getId() );
		c.setUserId( null );
		service.createCategory( c );
	}
	
	/**
	 * Try to create a category for a non existing user raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryForNonExisitingUserRaisesException() throws BusinessException {
		Category c = Builder.newCategoryFor( u1.getId() );
		c.setUserId( Fixture.NON_EXISTING_ID );
		service.createCategory( c );
	}
	
	/**
	 * Create a category with a unique name for an existing enabled user
	 * @throws BusinessException 
	 */
	@Test
	public void testCategoryWithUniqueNameForExistingUser() throws BusinessException {
		Category c = Builder.newCategoryFor( u1.getId() );
		try {
			Long id = service.createCategory( c );
			c.setId(id);
			
			Category persisted = Fixture.findCategoryById( id );
			assertTrue( c.equals( persisted ) );
		}
		finally {
			Fixture.remove( c );
		}
	}

}
