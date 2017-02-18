package sdi.gtd.service.task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.gtd.util.Builder;
import sdi.gtd.util.Fixture;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.User;

public class UpdateCategoryServiceTests {

	private TaskService service;
	private User u1;
	private Category ct1;

	@Before
	public void setUp() throws Exception {
		service = Services.getTaskService();
		u1 = Fixture.registerNewUser();
		ct1 = Fixture.registerNewCategoryForUser(u1);
	}

	@After
	public void tearDown() throws Exception {
		Fixture.remove(ct1);
		Fixture.remove(u1);
	}

	/**
	 * A category name cannot be updated to null
	 * 
	 * @throws BusinessException
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryNameCannotBeUpdatedToNull()
			throws BusinessException {
		ct1.setName( null );
		service.updateCategory(ct1);
	}

	/**
	 * A category name cannot be updated to empty string
	 * 
	 * @throws BusinessException
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryNameCannotBeUpdatedToEmpty()
			throws BusinessException {
		ct1.setName( "" );
		service.updateCategory(ct1);
	}

	/**
	 * A category name cannot be updated to an existing value
	 * 
	 * @throws BusinessException
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryNameCannotBeUpdatedToRepeatedValue()
			throws BusinessException {
		Category otherCategory = Fixture.registerNewCategoryForUser(u1);
		try {
			ct1.setName( otherCategory.getName() );
			service.updateCategory(ct1);
		} finally {
			Fixture.remove(otherCategory);
		}
	}

	/**
	 * A category cannot be set to other user
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryNameCannotBeUpdatedToOtherUser() throws BusinessException {
		User otherUser = Fixture.registerNewUser();
		try {
			ct1.setUserId( otherUser.getId() );
			service.updateCategory(ct1);
		} finally {
			Fixture.remove( otherUser );
		}
	}

	/**
	 * A category cannot be set to null user
	 * 
	 * @throws BusinessException
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryNameCannotBeUpdatedToNullUser()
			throws BusinessException {
		ct1.setUserId(null);
		service.updateCategory(ct1);
	}

	/**
	 * Try to update a non existent category raises an exception
	 * 
	 * @throws BusinessException
	 */
	@Test(expected = BusinessException.class)
	public void testNonExistentCategoryCannotBeUpdated()
			throws BusinessException {
		Category clon = Builder.clone( ct1 );
		clon.setId( Fixture.NON_EXISTING_ID );
		service.updateCategory( clon );
	}

	/**
	 * A category name can be updated to a valid, unique, value for the user
	 * @throws BusinessException 
	 */
	@Test
	public void testCategoryNameCanBeUpdatedToUniqueValue() throws BusinessException {
		ct1.setName( "otherNonExistngName" );
		service.updateCategory(ct1);
	}

}
