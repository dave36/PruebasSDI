package sdi.practica1.service.admin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DeepDeleteUserTests.class, DisableEnableUserTests.class,
		FindUserByIdTests.class })
public class AllTests {

}
