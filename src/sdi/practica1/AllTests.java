package sdi.practica1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	sdi.practica1.persistence.AllTests.class,
	sdi.practica1.service.AllTests.class 
})
public class AllTests {

}
