package sdi.practica1.util;

import alb.util.date.DateUtil;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;

public class Builder {
	
	private static int users = 0;
	private static int categories = 0;
	private static int tasks = 0;

	public static User newUser() {
		return new User()
				.setLogin("dummy user - " + users++)
				.setEmail("userTest@mail.gtd")
				.setPassword("password123");
	}

	public static Category newCategoryFor(Long userId) {
		return new Category()
				.setName("dummy category - " + categories++)
				.setUserId(userId);	
	}

	public static Task newTaskFor(Long userId, Long catId) {
		return newTaskFor(userId).setCategoryId( catId );
	}

	public static Task newTaskFor(Long userId) {
		return new Task()
				.setTitle("Task " + tasks++ + " for user " + userId)
				.setCreated( DateUtil.today() )
				.setUserId( userId );
	}

	public static User clone(User u) {
		return new User()
			.setId( u.getId() )
			.setEmail( u.getEmail() )
			.setIsAdmin( u.getIsAdmin() )
			.setLogin( u.getLogin() )
			.setPassword( u.getPassword() )
			.setStatus( u.getStatus() );
	}

	public static Category clone(Category c) {
		return new Category()
			.setId( c.getId() )
			.setName( c.getName() )
			.setUserId( c.getUserId() );
	}

}
