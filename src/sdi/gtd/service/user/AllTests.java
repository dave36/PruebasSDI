package sdi.gtd.service.user;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FindLoggableUserTests.class, RegisterUserTests.class,
		UpdateUserServiceTests.class })
public class AllTests {

}
