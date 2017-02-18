package sdi.gtd.service.task;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.gtd.util.Builder;
import sdi.gtd.util.Fixture;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import alb.util.date.DateUtil;

/**
 * A table of different configured tasks is created. Every task has a unique 
 * configuration for the:
 * 		- planned date
 * 		- finished date
 * 		- category
 * 		- user
 * 
 * The table has 48 tasks
 * 
 * T	Planned		Finished	user		category	T1	T2	T3	T4	T5	T6	T7
 * t0							userId					yes	no	no	no	no	no	yes
 * t1	yesterday				userId					yes	yes	yes	no	no	no	yes
 * t2	yesterday	yesterday	userId					no	no	no	no	no	yes	yes
 * t3	yesterday	today		userId					no	no	no	no	no	yes	yes
 * t4	today					userId					yes	yes	yes	no	no	no	yes
 * t5	today		today		userId					no	no	no	no	no	yes	yes
 * t6	tomorrow				userId					yes	no	yes	no	no	no	yes
 * t7	tomorrow	tomorrow	userId					no	no	no	no	no	yes	yes
 * t8	today + 7				userId					yes	no	yes	no	no	no	yes
 * t9	today + 7	today + 7	userId					no	no	no	no	no	yes	yes
 * t10	today + 8				userId					yes	no	no	no	no	no	yes
 * t11	today + 8	today + 8	userId					no	no	no	no	no	yes	yes
 * t12							otherUserId				no	no	no	no	no	no	no
 * t13	yesterday				otherUserId				no	no	no	no	no	no	no
 * t14	yesterday	yesterday	otherUserId				no	no	no	no	no	no	no
 * t15	yesterday	today		otherUserId				no	no	no	no	no	no	no
 * t16	today					otherUserId				no	no	no	no	no	no	no
 * t17	today		today		otherUserId				no	no	no	no	no	no	no
 * t18	tomorrow				otherUserId				no	no	no	no	no	no	no
 * t19	tomorrow	tomorrow	otherUserId				no	no	no	no	no	no	no
 * t20	today + 7				otherUserId				no	no	no	no	no	no	no
 * t21	today + 7	today + 7	otherUserId				no	no	no	no	no	no	no
 * t22	today + 8				otherUserId				no	no	no	no	no	no	no
 * t23	today + 8	today + 8	otherUserId				no	no	no	no	no	no	no
 * t24							userId		catId		no	no	no	yes	no	no	yes
 * t25	yesterday				userId		catId		no	yes	yes	yes	no	no	yes
 * t26	yesterday	yesterday	userId		catId		no	no	no	no	yes	no	yes
 * t27	yesterday	today		userId		catId		no	no	no	no	yes	no	yes
 * t28	today					userId		catId		no	yes	yes	yes	no	no	yes
 * t29	today		today		userId		catId		no	no	no	no	yes	no	yes
 * t30	tomorrow				userId		catId		no	no	yes	yes	no	no	yes
 * t31	tomorrow	tomorrow	userId		catId		no	no	no	no	yes	no	yes
 * t32	today + 7				userId		catId		no	no	yes	yes	no	no	yes
 * t33	today + 7	today + 7	userId		catId		no	no	no	no	yes	no	yes
 * t34	today + 8				userId		catId		no	no	no	yes	no	no	yes
 * t35	today + 8	today + 8	userId		catId		no	no	no	no	yes	no	yes
 * t36							otherUserId	otherCatId	no	no	no	no	no	no	no
 * t37	yesterday				otherUserId	otherCatId	no	no	no	no	no	no	no
 * t38	yesterday	yesterday	otherUserId	otherCatId	no	no	no	no	no	no	no
 * t39	yesterday	today		otherUserId	otherCatId	no	no	no	no	no	no	no
 * t40	today					otherUserId	otherCatId	no	no	no	no	no	no	no
 * t41	today		today		otherUserId	otherCatId	no	no	no	no	no	no	no
 * t42	tomorrow				otherUserId	otherCatId	no	no	no	no	no	no	no
 * t43	tomorrow	tomorrow	otherUserId	otherCatId	no	no	no	no	no	no	no
 * t44	today + 7				otherUserId	otherCatId	no	no	no	no	no	no	no
 * t45	today + 7	today + 7	otherUserId	otherCatId	no	no	no	no	no	no	no
 * t46	today + 8				otherUserId	otherCatId	no	no	no	no	no	no	no
 * t47	today + 8	today + 8	otherUserId	otherCatId	no	no	no	no	no	no	no
 *  
 * @author alb
 */
public class TaskQueryServicesTests {

	private TaskService service;

	private Task[] tasks = new Task[48];
	private User user;
	private User otherUser;
	private Category cat;
	private Category otherCat;

	@Before
	public void setUp() throws Exception {
		service = Services.getTaskService();
		
		user = Fixture.registerNewUser();
		otherUser = Fixture.registerNewUser();
		cat = Fixture.registerNewCategoryForUser( user );
		otherCat = Fixture.registerNewCategoryForUser( otherUser );

		buildTasks();
		setCategories();
		setPlannedDates();
		setFinishedDates();
		
		Fixture.register( tasks );
	}

	@After
	public void tearDown() throws Exception {
		Fixture.remove( tasks);
		Fixture.remove( cat );
		Fixture.remove( otherCat );
		Fixture.remove( user );
		Fixture.remove( otherUser );
	}

	private void setFinishedDates() {
		setFinishedForToday();
		setFinishedForYesterday();
		setFinishedForTomorrow();
		setFinishedForTodayPlusSeven();
		setFinishedForTodayPlusEight();
	}

	private void setFinishedForTodayPlusEight() {
		int[] taskIdxs = {11, 23, 35, 47};
		Date date = DateUtil.addDays(DateUtil.today(), 8); 
		setFinishedDateForTasks( date, taskIdxs);
	}

	private void setFinishedForTodayPlusSeven() {
		int[] taskIdxs = {9, 21, 33, 45};
		Date date = DateUtil.addDays(DateUtil.today(), 7); 
		setFinishedDateForTasks( date, taskIdxs);
	}

	private void setFinishedForTomorrow() {
		int[] taskIdxs = {7, 19, 31, 43};
		Date date = DateUtil.tomorrow(); 
		setFinishedDateForTasks( date, taskIdxs);
	}

	private void setFinishedForYesterday() {
		int[] taskIdxs = {2, 14, 26, 38};
		Date date = DateUtil.yesterday(); 
		setFinishedDateForTasks( date, taskIdxs);
	}

	private void setFinishedForToday() {
		int[] taskIdxs = {3, 5, 15, 17, 27, 29, 39, 41};
		Date date = DateUtil.today(); 
		setFinishedDateForTasks( date, taskIdxs);
	}

	private void setPlannedDates() {
		setPlannedForToday();
		setPlannedForYesterday();
		setPlannedForTomorrow();
		setPlannedForTodayPlusSeven();
		setPlannedForTodayPlusEight();
	}

	private void setPlannedForTodayPlusEight() {
		int[] taskIdxs = {10, 11, 22, 23, 34, 35, 46, 47};
		Date date = DateUtil.addDays(DateUtil.today(), 8); 
		setPlannedDateForTasks( date, taskIdxs);
	}

	private void setPlannedForTodayPlusSeven() {
		int[] taskIdxs = {8, 9, 20, 21, 32, 33, 44, 45};
		Date date = DateUtil.addDays(DateUtil.today(), 7); 
		setPlannedDateForTasks( date, taskIdxs);
	}

	private void setPlannedForTomorrow() {
		int[] taskIdxs = {6, 7, 18, 19, 30, 31, 42, 43};
		Date date = DateUtil.tomorrow(); 
		setPlannedDateForTasks( date, taskIdxs);
	}

	private void setPlannedForYesterday() {
		int[] taskIdxs = {1, 2, 3, 13, 14, 15, 25, 26, 27, 37, 38, 39};
		Date date = DateUtil.yesterday(); 
		setPlannedDateForTasks( date, taskIdxs);
	}

	private void setPlannedForToday() {
		int[] taskIdxs = {4, 5, 16, 17, 28, 29, 40, 41};
		Date date = DateUtil.today(); 
		setPlannedDateForTasks( date, taskIdxs);
	}

	private void setPlannedDateForTasks(Date date, int[] taskIdxs) {
		for(int i = 0; i < taskIdxs.length; i++) {
			tasks[ taskIdxs[i] ].setPlanned( date );
		}
	}

	private void setFinishedDateForTasks(Date date, int[] taskIdxs) {
		for(int i = 0; i < taskIdxs.length; i++) {
			tasks[ taskIdxs[i] ].setFinished( date );
		}
	}

	private void setCategories() {
		int[] forCatIdTasks = {
				24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
		};
		setCategoryForTasks(cat.getId(), forCatIdTasks);
		
		int[] forOtherCatIdTasks = {
				36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47
		};
		setCategoryForTasks(otherCat.getId(), forOtherCatIdTasks);
	}

	private void setCategoryForTasks(Long catId, int[] taskIdxs) {
		for(int i = 0; i < taskIdxs.length; i++) {
			tasks[ taskIdxs[i] ].setCategoryId( catId );
		}
	}

	private void buildTasks() {
		int[] forUserIdtasks = {
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 
				24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
		};
		buildTasksForUser(user.getId(), forUserIdtasks);

		int[] forOtherUserTasks = {
				12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 
				36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47
		};
		buildTasksForUser(otherUser.getId(), forOtherUserTasks);
	}

	private void buildTasksForUser(Long userId, int[] taskIdxs) {
		for(int i = 0; i < taskIdxs.length; i++) {
			Task t = Builder.newTaskFor( userId );
			t.setTitle( "t" + taskIdxs[i] );
			tasks[ taskIdxs[i] ] = t;
		}
	}

	/**
	 * (T1) All not finished tasks in the user's inbox (without category)
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskForInboxQuery() throws BusinessException {

		List<Task> found = service.findInboxTasksByUserId( user.getId() );
		
		assertTrue( found.size() == 6 );
		assertTrue( found.contains( tasks[ 0 ] ));
		assertTrue( found.contains( tasks[ 1 ] ));
		assertTrue( found.contains( tasks[ 4 ] ));
		assertTrue( found.contains( tasks[ 6 ] ));
		assertTrue( found.contains( tasks[ 8 ] ));
		assertTrue( found.contains( tasks[ 10 ] ));
	}

	/**
	 * (T2) All not finished tasks of the user for today, or delayed, and for all 
	 * categories (inbox included)
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskForTodayQuery() throws BusinessException {
		List<Task> found = service.findTodayTasksByUserId( user.getId() );

		assertTrue( found.size() == 4 );
		assertTrue( found.contains( tasks[ 1 ] ));
		assertTrue( found.contains( tasks[ 4 ] ));
		assertTrue( found.contains( tasks[ 25 ] ));
		assertTrue( found.contains( tasks[ 28 ] ));
	}

	/**
	 * (T3) All not finished tasks of the user for the week (today + 7 days), 
	 * or delayed, and for all categories (inbox included)
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskForWeekQuery() throws BusinessException {
		List<Task> found = service.findWeekTasksByUserId( user.getId() );
		
		assertTrue( found.size() == 8 );
		assertTrue( found.contains( tasks[ 1 ] ));
		assertTrue( found.contains( tasks[ 4 ] ));
		assertTrue( found.contains( tasks[ 6 ] ));
		assertTrue( found.contains( tasks[ 8 ] ));
		assertTrue( found.contains( tasks[ 25 ] ));
		assertTrue( found.contains( tasks[ 28 ] ));
		assertTrue( found.contains( tasks[ 30 ] ));
		assertTrue( found.contains( tasks[ 32 ] ));
	}
	
	
	/**
	 * (T4) All not finished tasks for a category
	 * @throws BusinessException 
	 */
	@Test
	public void testTaskForCategoryQuery() throws BusinessException {
		List<Task> found = service.findTasksByCategoryId( cat.getId() );

		assertTrue( found.size() == 6 );
		assertTrue( found.contains( tasks[ 24 ] ));
		assertTrue( found.contains( tasks[ 25 ] ));
		assertTrue( found.contains( tasks[ 28 ] ));
		assertTrue( found.contains( tasks[ 30 ] ));
		assertTrue( found.contains( tasks[ 32 ] ));
		assertTrue( found.contains( tasks[ 34 ] ));
	}

	/**
	 *(T5) It must return all the finished tasks for a user and a category.
	 * The user must be specified as category may be null (inbox)
	 * @throws BusinessException 
	 */
	@Test
	public void testFinishedTasksForCategoryQuery() throws BusinessException {
		List<Task> found = service.findFinishedTasksByCategoryId( cat.getId() );

		assertTrue( found.size() == 6 );
		assertTrue( found.contains( tasks[ 26 ] ));
		assertTrue( found.contains( tasks[ 27 ] ));
		assertTrue( found.contains( tasks[ 29 ] ));
		assertTrue( found.contains( tasks[ 31 ] ));
		assertTrue( found.contains( tasks[ 33 ] ));
		assertTrue( found.contains( tasks[ 35 ] ));
	}

	/**
	 *(T6) It must return all the finished tasks for a user in the inbox
	 * @throws BusinessException 
	 */
	@Test
	public void testFinishedInboxTasksQuery() throws BusinessException {
		List<Task> found = service.findFinishedInboxTasksByUserId( user.getId() );

		assertTrue( found.size() == 6 );
		assertTrue( found.contains( tasks[ 2 ] ));
		assertTrue( found.contains( tasks[ 3 ] ));
		assertTrue( found.contains( tasks[ 5 ] ));
		assertTrue( found.contains( tasks[ 7 ] ));
		assertTrue( found.contains( tasks[ 9 ] ));
		assertTrue( found.contains( tasks[ 11 ] ));
	}

}
