package sdi.practica1.service.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
import sdi.practica1.util.Fixture;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.dto.util.Cloner;

public class UpdateTaskServiceTests {

	private TaskService service = Services.getTaskService();
	private User u1;
	private Category ct1;
	private Task t1;

	@Before
	public void setUp() throws Exception {
		u1 = Fixture.registerNewUser();
		ct1 = Fixture.registerNewCategoryForUser(u1);
		t1 = Fixture.registerNewTaskFor(u1, ct1);
	}

	@After
	public void tearDown() throws Exception {
		Fixture.remove( t1 );
		Fixture.remove( ct1 );
		Fixture.remove( u1 );
	}

	/**
	 * The category of a task may be set to null
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskCategoryMayBeUpdatedToNull() throws BusinessException {
		t1.setCategoryId( null );
		
		service.updateTask( t1 );
		
		Task persisted = Fixture.findTaskById( t1.getId() );
		assertTrue( persisted.equals( t1 ) );
	}
	
	/**
	 * The category of a task may be updated
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskCategoryMayBeUpdated() throws BusinessException {
		Category c = Fixture.registerNewCategoryForUser(u1);
		try {
			t1.setCategoryId( c.getId() );
			
			service.updateTask( t1 );
			
			Task persisted = Fixture.findTaskById( t1.getId() );
			assertTrue( persisted.equals( t1 ) );
		}
		finally {
			Fixture.remove( t1 );  // now t1 depends on c
			Fixture.remove( c );
		}
	}
	
	/**
	 * A task cannot be updated for another user
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskCannotBeUpdatedForAnotherUser() throws BusinessException {
		User u = Fixture.registerNewUser();
		t1.setUserId( u.getId() );
		
		try {
			service.updateTask( t1 );
		}
		finally {
			Fixture.remove( u );
		}
	}
	
	/**
	 * Update a task for a non existing category raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskForNonExistingCategoryRaisesException() throws BusinessException {
		t1.setCategoryId( Fixture.NON_EXISTING_ID );
		service.updateTask( t1 );
	}
	
	/**
	 * The task date of creation is ignored in updating
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskCreatedDateUpdatingIsIgnored() throws BusinessException {
		Task clon = Cloner.clone( t1 );
		t1.setCreated( DateUtil.yesterday() );
		
		service.updateTask( t1 );
		
		Task persisted = Fixture.findTaskById( t1.getId() );
		assertTrue( persisted.equals( clon ) );
	}
	
	/**
	 * The planned date can be null and the finished date be set
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskPlannedDateNullButFinishedDateSet() throws BusinessException {
		t1.setPlanned( null );
		t1.setFinished( DateUtil.today() );
		
		service.updateTask( t1 );
		
		Task persisted = Fixture.findTaskById( t1.getId() );
		assertTrue( persisted.equals( t1 ) );
	}

	/**
	 * The title of a task cannot be updated to null
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskTitleCannotBeUpdatedToNull() throws BusinessException {
		t1.setTitle( null );
		service.updateTask( t1 );
	}

	/**
	 * The finished date cannot be updated and raises an exception
	 * (must be done with markAsFinished)
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskFinishedDateUpdatingIsIgnored() throws BusinessException {
		t1.setFinished( DateUtil.today() );
		service.updateTask( t1 );
	}

}
