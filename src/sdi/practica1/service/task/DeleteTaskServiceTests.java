package sdi.practica1.service.task;

import static org.junit.Assert.assertTrue;

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

public class DeleteTaskServiceTests {
	
	private TaskService service = Services.getTaskService();
	private User u1;
	private Category ct1;
	private Task t; 

	@Before
	public void setUp() throws Exception {
		u1 = Fixture.registerNewUser();
		ct1 = Fixture.registerNewCategoryForUser(u1);
		t = Fixture.registerNewTaskFor( u1, ct1 );
	}

	@After
	public void tearDown() throws Exception {
		Fixture.remove( t );
		Fixture.remove( ct1 );
		Fixture.remove( u1 );
	}

	/**
	 * Deletion of a non existing task is ignored
	 */
	@Test
	public void testDeleteNonExistingTaskRaisesException() throws BusinessException {
		service.deleteTask( Fixture.NON_EXISTING_ID );
		assertTrue( true );
	}
	
	/**
	 * Delete an existing task
	 * @throws BusinessException 
	 */
	@Test
	public void testDeleteTask() throws BusinessException {
		service.deleteTask( t.getId() );
		
		Task found = Fixture.findTaskById( t.getId() );
		assertTrue( found == null );
	}
}
