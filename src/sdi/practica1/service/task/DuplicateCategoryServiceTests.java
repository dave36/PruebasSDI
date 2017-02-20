package sdi.practica1.service.task;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.practica1.util.Fixture;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;

public class DuplicateCategoryServiceTests {

	private TaskService service = Services.getTaskService();
	private User u1;
	private Category ct1;

	@Before
	public void setUp() throws Exception {
		u1 = Fixture.registerNewUser();
		ct1 = Fixture.registerNewCategoryForUser(u1);
	}

	@After
	public void tearDown() throws Exception {
		Fixture.remove( ct1 );
		Fixture.remove( u1 );
	}

	/**
	 * Try to duplicate a non existing category raises exception 
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testNonExisitingCategoryDuplicationRaisesException() throws BusinessException {
		service.duplicateCategory( Fixture.NON_EXISTING_ID );
	}

	/**
	 * A category of a disabled user cannot be duplicated 
	 */
	@Test(expected = BusinessException.class)
	public void testCategoryForDisabledUserRaisesException() throws BusinessException {
		u1.setStatus( UserStatus.DISABLED );
		Fixture.update( u1 );
		
		service.duplicateCategory( ct1.getId() );
	}
	
	/**
	 * Duplication of an empty category
	 * @throws BusinessException 
	 */
	@Test
	public void testEmptyCategoryDuplication() throws BusinessException {
		Long newId = service.duplicateCategory( ct1.getId() );
		Category copy = null;
		
		try {
			Category original = Fixture.findCategoryById( ct1.getId() );
			copy = Fixture.findCategoryById( newId );
			
			assertCategoryIsDuplicated(original, copy);
		}
		finally {
			Fixture.remove( copy );
		}
	}
	
	/**
	 * Duplication of a category with tasks also duplicates the tasks
	 * @throws BusinessException 
	 */
	@Test
	public void testCategoryDuplicationWithTasks() throws BusinessException {
		Task t1 = Fixture.registerNewTaskFor(u1, ct1);
		Task t2 = Fixture.registerNewTaskFor(u1, ct1);
		Category copy = null;
		List<Task> copyTasks = null;
		
		try {
			Long newId = service.duplicateCategory( ct1.getId() );
			
			Category original = Fixture.findCategoryById( ct1.getId() );
			copy = Fixture.findCategoryById( newId );
			
			List<Task> originalTasks = Fixture.findTasksByCategoryId( original.getId() );
			copyTasks = Fixture.findTasksByCategoryId( copy.getId() );

			assertCategoryIsDuplicated(original, copy);
			assertTaksAreDuplicated(copyTasks, originalTasks);
		}
		finally {
			Fixture.remove( t1 );
			Fixture.remove( t2 );
			for(Task t: copyTasks) {
				Fixture.remove( t );
			}
			Fixture.remove( copy );
		}
	}

	private void assertTaksAreDuplicated(
			List<Task> copyTasks,
			List<Task> originalTasks) {
		
		assertTrue( originalTasks.size() == copyTasks.size() );
		assertTrue( ! originalTasks.equals( copyTasks ) ); // not the same tasks
		assertResemblance( originalTasks, copyTasks );
	}

	private void assertResemblance(List<Task> origTasks, List<Task> copyTasks) {
		for(Task t: copyTasks) {
			assertTrue( ! t.getCategoryId().equals( ct1.getId() ) );
			assertTrue( t.getUserId().equals( ct1.getUserId() ) );
		}
	}

	private void assertCategoryIsDuplicated(Category original, Category copy) {
		assertTrue( copy.getName().equals( original.getName() + " - copy" ));
		assertTrue( original.getUserId().equals( copy.getUserId() ) );
		assertTrue( ! original.getId().equals( copy.getId() ) );
	}
	
}
