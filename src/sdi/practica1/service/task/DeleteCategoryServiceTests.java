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

public class DeleteCategoryServiceTests {

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
	 * A category with tasks is removed along its tasks
	 * @throws BusinessException 
	 */
	@Test
	public void testCategoryWithTasksCannotBeRemoved() throws BusinessException {
		Task t1 = Fixture.registerNewTaskFor(u1, ct1);
		Task t2 = Fixture.registerNewTaskFor(u1, ct1);
		
		try {
			service.deleteCategory( ct1.getId() );
			
			Category deleted = Fixture.findCategoryById( ct1.getId() );
			assertTrue( deleted == null );
			
			List<Task> ts = Fixture.findTasksByUserId( u1.getId() );
			assertTrue( ts.size() == 0 );
		}
		finally {
			Fixture.remove( t1 );
			Fixture.remove( t2 );
		}
	}
	
	/**
	 * An empty category can be removed
	 * @throws BusinessException 
	 */
	@Test
	public void testEmptyCategoryCanBeRemoved() throws BusinessException {
		service.deleteCategory( ct1.getId() );
		
		Category c = Fixture.findCategoryById( ct1.getId() );
		assertTrue( c == null );
	}
	
	/**
	 * Deleting a non existing category is ignored 
	 * @throws BusinessException 
	 */
	@Test
	public void testNonExistingCategoryIsIgnored() throws BusinessException {
		service.deleteCategory( Fixture.NON_EXISTING_ID );
		assertTrue( true );
	}

}
