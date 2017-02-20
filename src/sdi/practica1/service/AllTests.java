package sdi.practica1.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	sdi.practica1.service.admin.AllTests.class,
	sdi.practica1.service.task.AllTests.class,
	sdi.practica1.service.user.AllTests.class
})
public class AllTests {

}
