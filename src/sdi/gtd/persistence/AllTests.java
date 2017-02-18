package sdi.gtd.persistence;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	CategoryDaoTests.class, 
	TaskDaoTests.class, 
	TaskQueryTests.class, 
	UserDaoTests.class 
})
public class AllTests {

}
