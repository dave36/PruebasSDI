package sdi.practica1.service.task;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CreateCategoryServiceTests.class, CreateTaskServiceTests.class,
		DeleteCategoryServiceTests.class, DeleteTaskServiceTests.class,
		DuplicateCategoryServiceTests.class,
		MarkTaskAsFinishedServiceTests.class, TaskQueryServicesTests.class,
		UpdateCategoryServiceTests.class, UpdateTaskServiceTests.class })
public class AllTests {

}
