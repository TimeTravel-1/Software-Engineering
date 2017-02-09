package org.lsmr.vending.frontend3.hardware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Each test class has to extend this one.
 * 
 * @author Robert J. Walker
 */
@SuppressWarnings("javadoc")
public class WRAPPER {
    private static List<String> expecteds =
		new ArrayList<String>(Arrays.asList("construct(1, 1; 1; 10; 10; 10)\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\n",
	    "construct(0; 1; 10; 10; 10)\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\n",
	    "construct(100, 5, 25, 10; 3; 2; 10; 10)\npress(0)\nextract()\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
	    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(25)\ninsert(25)\npress(0)\nextract()\n",
            "construct(100, 5, 25, 10; 3; 10; 10; 10)\npress(0)\nextract()\ninsert(100)\ninsert(100)\ninsert(100)\nextract()\n",    
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\npress(0)\nextract()\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
	    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(1)\ninsert(139)\npress(0)\nextract()\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\nextract()\n",
	    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\ninsert(25)\ninsert(100)\ninsert(10)\npress(0)\nextract()\n",
	    "construct(5, 10, 25, 100; 1; 10; 10; 10)\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\npress(0)\nextract()\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
	    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
	    "construct(5, 10, 25, 100; 3; 10; 10; 10)\npress(0)\nextract()\n",
	    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(25)\ninsert(100)\ninsert(10)\npress(0)\nextract()\n",
	    "construct(100, 5, 25, 10; 3; 10; 10; 10)\ninsert(10)\ninsert(5)\ninsert(10)\npress(2)\nextract()\n"));

    private static int expectedCount = expecteds.size(), testCount, totalTestCount = 0, totalMissing = 0, totalExtra = 0, totalFound = 0;
    public static StringBuilder actual;
    public static ArrayList<String> actuals;

    @BeforeClass
    public static void WRAPPERinit() {
	testCount = 0;
	actuals = new ArrayList<>();
    }

    @Rule
    public TestWatcher watcher = new TestWatcher() {
	protected void starting(Description description) {
	    System.err.print("TEST (" + description.getClassName() + "." + description.getMethodName() + "): ");
	}
    };

    @Before
    public void WRAPPERsetup() {
	actual = null;
	testCount++;
    }

    @After
    public void WRAPPERteardown() {
	actuals.add(actual.toString());

	int index = expecteds.indexOf(actual.toString());
	if(index < 0) {
	    System.err.println("EXTRA");
	    System.err.println();
	    System.err.println(actual.toString());
	    System.err.println();
	}
	else {
	    System.err.println("MATCH");
	}
    }

    @AfterClass
    public static void WRAPPERcheck() {
	System.err.print("SUMMARY. ");

	int extra = 0, found = 0;
	for(String actual : actuals) {
	    int index = expecteds.indexOf(actual);
	    if(index < 0) {
		extra++;
	    }
	    else {
		found++;
		expecteds.remove(index);
	    }
	}

	totalFound += found;
	totalMissing = expecteds.size();

	if(totalMissing == 1)
	    System.err.println(totalMissing + " expected test output is MISSING.");
	else
	    System.err.println(totalMissing + " expected test outputs are MISSING.");
	totalExtra += extra;
	totalTestCount += testCount;
	System.err.println("Expected: " + expectedCount + "; test count: " + totalTestCount + "; matching: " + totalFound + "; missing: " + totalMissing
			   + "; extra: " + totalExtra);

	actuals = null;
    }
}
