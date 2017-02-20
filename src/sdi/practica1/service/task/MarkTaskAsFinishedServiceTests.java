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

public class MarkTaskAsFinishedServiceTests {

	private TaskService service = Services.getTaskService();
	private User u1;
	private Category ct1;
	private Task t1; 

	@Before
	public void setUp() throws Exception {
		u1 = Fixture.registerNewUser();
		ct1 = Fixture.registerNewCategoryForUser(u1);
		t1 = Fixture.registerNewTaskFor( u1, ct1 );
	}

	@After
	public void tearDown() throws Exception {
		Fixture.remove( t1 );
		Fixture.remove( ct1 );
		Fixture.remove( u1 );
	}

	/**
	 * Try to mark as finished a non existing task raises an exception
	 */
	@Test(expected = BusinessException.class)
	public void testNonExisitngTask() throws BusinessException {
		service.markTaskAsFinished( Fixture.NON_EXISTING_ID );
	}
	
	/**
	 * A task without planned date can be marked as finished
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskWithoutPlannedDateCanBeMarkedAsFinished() throws BusinessException {
		service.markTaskAsFinished( t1.getId() );
		
		Task persisted = Fixture.findTaskById( t1.getId() );
		assertTimeDiffMinute(persisted);
	}

	private void assertTimeDiffMinute(Task persisted) {
		assertTrue( 
			DateUtil.diffMinutes( persisted.getFinished(), DateUtil.today() ) == 0 
		);
	}
	
	/**
	 * A task without category can be marked as finished
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskWithoutCategoryMarkedAsFinished() throws BusinessException {
		t1.setCategoryId( null );
		Fixture.update( t1 );

		service.markTaskAsFinished( t1.getId() );
			
		Task persisted = Fixture.findTaskById( t1.getId() );
		assertTimeDiffMinute(persisted);
	}
	
	/**
	 * A task with planned date and category can be marked as finished
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskWithPlannedDateAndCategoryMarkedAsFinished() throws BusinessException {
		t1.setPlanned( DateUtil.today() );
		Fixture.update( t1 );

		service.markTaskAsFinished( t1.getId() );
			
		Task persisted = Fixture.findTaskById( t1.getId() );
		assertTimeDiffMinute(persisted);
	}

	/**
	 * Mark as finished an already finished task changes the finished
	 * date to the current date
	 * @throws BusinessException 
	 */
	@Test
	public void testMarkAsFinishedUpdatesFinishedDateOfAlreadyFinished() throws BusinessException {
		t1.setPlanned( DateUtil.yesterday() );
		t1.setFinished( DateUtil.yesterday() );
		Fixture.update( t1 );

		service.markTaskAsFinished( t1.getId() );
			
		Task persisted = Fixture.findTaskById( t1.getId() );
		assertTimeDiffMinute(persisted);
	}
	
}
