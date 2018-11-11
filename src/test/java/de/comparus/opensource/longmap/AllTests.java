package de.comparus.opensource.longmap;

import de.comparus.opensource.longmap.blackBox.LongMapImplTestsBB;
import de.comparus.opensource.longmap.whiteBox.LongMapImplTestsWB;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({LongMapImplTestsWB.class, LongMapImplTestsBB.class})
@RunWith(Suite.class)
public class AllTests {
}
