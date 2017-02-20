package sdi.practica1.util;

import java.util.List;

import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.persistence.CategoryDao;
import uo.sdi.persistence.Persistence;
import uo.sdi.persistence.PersistenceException;
import uo.sdi.persistence.TaskDao;
import uo.sdi.persistence.Transaction;
import uo.sdi.persistence.UserDao;

public class Fixture {

	public static final Long NON_EXISTING_ID = -1111L;

	public static User registerNewUser() {
		User u = Builder.newUser();
		return registerUser( u );
	}

	public static User registerUser(User user) {
		UserDao uDao = Persistence.getUserDao();
		Long id = uDao.save( user );
		user.setId( id );
		return user;
	}

	public static void remove(User u1) {
		Persistence.getUserDao().delete( u1.getId() );
	}

	public static void remove(Category c) {
		Persistence.getCategoryDao().delete( c.getId() );
	}
	
	public static void remove(Task t) {
		Persistence.getTaskDao().delete( t.getId() );
	}

	public static List<Category> findCategoriesByUserId(Long id) {
		return Persistence.getCategoryDao().findByUserId( id );
	}

	public static Category registerNewCategoryForUser(User u1) {
		Category c = Builder.newCategoryFor( u1.getId() );
		CategoryDao cDao = Persistence.getCategoryDao();
		Long id = cDao.save( c );
		c.setId( id );
		return c;
	}

	public static Task registerNewTaskForUser(User u1) {
		Task t = Builder.newTaskFor( u1.getId() );
		TaskDao tDao = Persistence.getTaskDao();
		Long id = tDao.save( t );
		t.setId( id );
		return t;
	}

	public static Task registerNewTaskFor(User u1, Category ct1) {
		Task t = Builder.newTaskFor( u1.getId(), ct1.getId() );
		TaskDao tDao = Persistence.getTaskDao();
		Long id = tDao.save( t );
		t.setId( id );
		return t;
	}

	public static List<Task> findTasksByUserId(Long id) {
		return Persistence.getTaskDao().findByUserId( id );
	}

	public static void update(User u) {
		Persistence.getUserDao().update( u );
	}

	public static void update(Task t) {
		Persistence.getTaskDao().update( t );
	}

	public static User findUserById(Long id) {
		return Persistence.getUserDao().findById( id );
	}

	public static Category findCategoryById(Long id) {
		return Persistence.getCategoryDao().findById( id );
	}

	public static Task findTaskById(Long id) {
		return Persistence.getTaskDao().findById( id );
	}

	public static List<Task> findTasksByCategoryId(Long id) {
		return Persistence.getTaskDao().findTasksByCategoryId( id );
	}

	public static void register(Task[] tasks) {
		TaskDao tDao = Persistence.getTaskDao();
		Transaction trx = Persistence.newTransaction();
		trx.begin();
		try {
			
			for (Task t : tasks) {
				Long id = tDao.save(t);
				t.setId(id);
			}
			trx.commit();
			
		} catch (PersistenceException e) {
			trx.rollback();
		}
	}

	public static void remove(Task[] tasks) {
		TaskDao tDao = Persistence.getTaskDao();
		Transaction trx = Persistence.newTransaction();
		trx.begin();
		try {

			for (Task t : tasks) {
				tDao.delete(t.getId());
			}
			trx.commit();

		} catch (PersistenceException e) {
			trx.rollback();
		}
	}

}
