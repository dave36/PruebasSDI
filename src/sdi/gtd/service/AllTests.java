package sdi.gtd.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	sdi.gtd.service.admin.AllTests.class,
	sdi.gtd.service.task.AllTests.class,
	sdi.gtd.service.user.AllTests.class
})
public class AllTests {

}
