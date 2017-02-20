package sdi.practica1.service.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
import sdi.practica1.util.Builder;
import sdi.practica1.util.Fixture;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;
import uo.sdi.dto.util.Cloner;

public class CreateTaskServiceTests {

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
	 * A task for a user without category is created
	 * @throws BusinessException 
	 */
	@Test
	public void testCreateTaskForUserWithoutCategory() throws BusinessException {
		Task t = Builder.newTaskFor( u1.getId() );
		
		try {
			Long id = service.createTask(t);
			t.setId( id );

			Task persisted = Fixture.findTaskById(t.getId());
			assertTrue(t.equals(persisted));
		} finally {
			Fixture.remove(t);
		}
	}
	
	/**
	 * A task for a user and a category is created 
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskForUserAndCategoryIsCreated() throws BusinessException {
		Task t = Builder.newTaskFor( u1.getId(), ct1.getId() );
		
		try {
			Long id = service.createTask(t);
			t.setId( id );

			Task persisted = Fixture.findTaskById(t.getId());
			assertTrue(t.equals(persisted));
		} finally {
			Fixture.remove(t);
		}
	}
	
	/**
	 * A task for a disabled user cannot be created
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskForDisabledUserRaisesException() throws BusinessException {
		u1.setStatus( UserStatus.DISABLED );
		Fixture.update( u1 );
		Task t = Builder.newTaskFor( u1.getId(), ct1.getId() );
		
		service.createTask(t);
	}
	
	/**
	 * A task for an admin user cannot be created
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskForAdminUserRaisesException() throws BusinessException {
		u1.setIsAdmin( true );
		Fixture.update( u1 );
		Task t = Builder.newTaskFor( u1.getId(), ct1.getId() );
		
		service.createTask(t);
	}
	
	/**
	 * Try to create a task for a null user raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskForNullUserRaisesException() throws BusinessException {
		Task t = Builder.newTaskFor( null, ct1.getId() );
		service.createTask(t);
	}
	
	/**
	 * Try to create a task for a non existing user raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskForNonExistingUserRaisesException() throws BusinessException {
		Task t = Builder.newTaskFor( Fixture.NON_EXISTING_ID, ct1.getId() );
		service.createTask(t);
	}
	
	/**
	 * Try to create a task for a non existing category raises exception
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskForNonExistingCategoryRaisesException() throws BusinessException {
		Task t = Builder.newTaskFor( u1.getId(), Fixture.NON_EXISTING_ID );
		service.createTask(t);
	}
	
	/**
	 * The date of creation is established by the service and ignored the 
	 * one from the Dto, if any
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskCreationDateIsSetByService() throws BusinessException {
		Task t = Builder.newTaskFor( u1.getId(), ct1.getId() );
		t.setCreated( DateUtil.yesterday() );
		
		try {
			Long id = service.createTask(t);
			t.setId( id );

			Task persisted = Fixture.findTaskById(t.getId());
			assertTrue( persisted.getCreated().equals( DateUtil.today() ));
			assertTrue( persisted.getId().equals( t.getId() ));
		} finally {
			Fixture.remove(t);
		}
	}
	
	/**
	 * The finished date cannot be set at creation
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskFinisiedDateIgnoredAtCreation() throws BusinessException {
		Task t = Builder.newTaskFor( Fixture.NON_EXISTING_ID, ct1.getId() );
		t.setFinished( DateUtil.yesterday() );
		service.createTask(t);
	}

	/**
	 * The title of a task cannot be null
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskTitleCannotBeNull() throws BusinessException {
		Task t = Builder.newTaskFor( Fixture.NON_EXISTING_ID, ct1.getId() );
		t.setTitle( null );
		service.createTask(t);
	}
	
	/**
	 * The title of a task cannot be empty
	 * @throws BusinessException 
	 */
	@Test(expected = BusinessException.class)
	public void testTaskTitleCannotBeEmpty() throws BusinessException {
		Task t = Builder.newTaskFor( Fixture.NON_EXISTING_ID, ct1.getId() );
		t.setTitle( "" );
		service.createTask(t);
	}
	
	/**
	 * A task with same user, category and title can be created
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskWithRepeatedValuesCanBeCreated() throws BusinessException {
		Task t1 = Fixture.registerNewTaskFor(u1, ct1);
		Task t2 = Cloner.clone( t1 );
		
		try {
			Long id = service.createTask(t2);
			t2.setId( id );

			Task persisted = Fixture.findTaskById( t2.getId() );
			assertTrue( t2.equals(persisted) );
		} finally {
			Fixture.remove(t1);
			Fixture.remove(t2);
		}
	}

}
