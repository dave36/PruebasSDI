package sdi.practica1.persistence;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
import sdi.practica1.util.Builder;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.persistence.CategoryDao;
import uo.sdi.persistence.PersistenceException;
import uo.sdi.persistence.Persistence;
import uo.sdi.persistence.TaskDao;
import uo.sdi.persistence.UserDao;

public class TaskDaoTests {

	private UserDao uDao;
	private CategoryDao cDao;
	private TaskDao tDao;
	private User userDto;
	private Long userId;
	private Category categoryDto;
	private Long catId;
	private Task taskDto;
	private Long taskId;

	@Before
	public void setUp() throws Exception {
		uDao = Persistence.getUserDao();
		cDao = Persistence.getCategoryDao();
		tDao = Persistence.getTaskDao();

		userDto = Builder.newUser();
		userId = uDao.save(userDto);
		userDto.setId(userId);

		categoryDto = Builder.newCategoryFor(userId);
		catId = cDao.save(categoryDto);
		categoryDto.setId(catId);

		taskDto = Builder.newTaskFor(userId, catId);
		taskId = tDao.save(taskDto);
		taskDto.setId(taskId);
	}

	@After
	public void tearDown() throws Exception {
		tDao.delete(taskId);
		cDao.delete(catId);
		uDao.delete(userId);
	}

	/**
	 * Tasks may have repeated title
	 */
	@Test
	public void testRepeatedTaskTitle() {
		Task other = Builder.newTaskFor(userId, catId);
		other.setTitle(taskDto.getTitle()); // Repeated title
		Long id = tDao.save(other);

		Task found = tDao.findById(id);

		try {
			assertTrue(found.getCategoryId().equals(taskDto.getCategoryId()));
			assertTrue(found.getCreated().equals(taskDto.getCreated()));
			assertTrue(found.getTitle().equals(taskDto.getTitle()));
			assertTrue(found.getUserId().equals(taskDto.getUserId()));

			assertTrue(found.getFinished() == null);
			assertTrue(taskDto.getFinished() == null);

			assertTrue(found.getPlanned() == null);
			assertTrue(taskDto.getPlanned() == null);

			assertTrue(found.getComments() == null);
			assertTrue(taskDto.getComments() == null);

			assertTrue(!found.getId().equals(taskDto.getId()));
		} finally {
			tDao.delete(id);
		}
	}

	/**
	 * Task may be saved without category (they are in the inbox)
	 */
	@Test
	public void testSaveWithNullCategoryId() {
		Task other = Builder.newTaskFor(userId, catId).setCategoryId(null);

		Long id = tDao.save(other);
		other.setId(id);
		try {
			Task persisted = tDao.findById(id);
			assertTrue(persisted.equals(other));
		} finally {
			tDao.delete(id);
		}
	}

	@Test(expected = PersistenceException.class)
	public void testSaveWithNullCreatedDate() {
		Task other = Builder.newTaskFor(userId, catId).setCreated(null);

		tDao.save(other);
	}

	@Test(expected = PersistenceException.class)
	public void testSaveWithNullTittle() {
		Task other = Builder.newTaskFor(userId, catId).setTitle(null);

		tDao.save(other);
	}

	@Test
	public void testFindById() {
		Task persisted = tDao.findById(taskId);
		assertTrue(persisted.equals(taskDto));
	}

	@Test
	public void testFindAll() {
		List<Task> tasks = tDao.findAll();
		assertTrue(tasks.contains(taskDto));
	}

	@Test
	public void testUpdate() {
		taskDto.setComments("blah, blah, blah")
				.setPlanned(DateUtil.today())
				.setFinished(DateUtil.today())
				.setTitle(taskDto.getTitle() + " - updated");

		int nr = tDao.update(taskDto);
		assertTrue(nr == 1);

		Task persisted = tDao.findById(taskId);
		assertTrue(persisted.equals(taskDto));
	}

	@Test
	public void testDelete() {
		int nr = tDao.delete(taskId);
		assertTrue(nr == 1);

		Task persisted = tDao.findById(taskId);
		assertTrue(persisted == null);
	}

	/**
	 * Delete all from user id
	 */
	@Test
	public void testDeleteFromUserId() {
		Task t1 = buildAndSaveTaskFor(userId, catId);
		Task t2 = buildAndSaveTaskFor(userId, catId);
		Task t3 = buildAndSaveTaskFor(userId, catId);

		try {
			int nr = tDao.deleteAllFromUserId(userId);
			assertTrue(nr == 4);

			List<Task> tasks = tDao.findByUserId(userId);
			assertTrue(tasks.size() == 0);
		} finally {
			tDao.delete(t1.getId());
			tDao.delete(t2.getId());
			tDao.delete(t3.getId());
		}
	}

	private Task buildAndSaveTaskFor(Long userId, Long catId) {
		Task t = Builder.newTaskFor(userId, catId);
		Long id = tDao.save(t);
		t.setId(id);
		return t;
	}

	/**
	 * Find all from user id
	 */
	@Test
	public void testFindAllFromUserId() {
		Task t1 = buildAndSaveTaskFor(userId, catId);
		Task t2 = buildAndSaveTaskFor(userId, catId);
		Task t3 = buildAndSaveTaskFor(userId, catId);

		try {
			List<Task> tasks = tDao.findByUserId(userId);
			assertTrue(tasks.size() == 4);
			assertTrue(tasks.contains(taskDto));
			assertTrue(tasks.contains(t1));
			assertTrue(tasks.contains(t2));
			assertTrue(tasks.contains(t3));
		} finally {
			tDao.delete(t1.getId());
			tDao.delete(t2.getId());
			tDao.delete(t3.getId());
		}
	}

}
