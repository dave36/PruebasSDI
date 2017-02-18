package sdi.gtd;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	sdi.gtd.persistence.AllTests.class,
	sdi.gtd.service.AllTests.class 
})
public class AllTests {

}
