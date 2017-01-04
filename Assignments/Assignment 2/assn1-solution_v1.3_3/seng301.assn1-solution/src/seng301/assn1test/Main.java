package seng301.assn1test;

import java.io.IOException;

import org.lsmr.vending.frontend1.ScriptProcessor;
import org.lsmr.vending.frontend1.parser.ParseException;

import seng301.assn1.VendingMachineFactory;

/**
 * Runs the test scripts.
 * 
 * @author Robert J. Walker
 */
public class Main {
    /**
     * This is the method that is called to run your program.
     * 
     * @param args
     *            This is formal parameter is required, but is ignored. Don't
     *            use it.
     * @throws ParseException
     *             If a script is in the wrong syntax.
     * @throws IOException
     *             If a script file cannot be found or read.
     */
    public static void main(String[] args) throws ParseException, IOException {
	int count = 0, pass = 0, fail = 0;
	String[] goodScripts = {"T01-good-insert-and-press-exact-change", "T02-good-insert-and-press-change-expected",
				"T03-good-teardown-without-configure-or-load", "T04-good-press-without-insert", "T05-good-scrambled-coin-kinds",
				"T06-good-extract-before-sale", "T07-good-changing-configuration", "T08-good-approximate-change", "T09-good-hard-for-change",
				"T10-good-invalid-coin", "T11-good-extract-before-sale-complex", "T12-good-approximate-change-with-credit"};

	for(String script : goodScripts)
	    try {
		count++;
		new ScriptProcessor(script, new VendingMachineFactory(), true);
		pass++;
		System.err.println(script + ": CORRECT");
		System.err.println();
	    }
	    catch(RuntimeException | ParseException t) {
		fail++;
		t.printStackTrace();
		System.err.println(script + ": WRONG");
		System.err.println();
	    }

	String[] badScripts = {"U01-bad-configure-before-construct", "U02-bad-costs-list", "U03-bad-names-list", "U04-bad-non-unique-denomination",
			       "U05-bad-coin-kind", "U06-bad-button-number", "U07-bad-button-number-2", "U08-bad-button-number-3"};

	for(String script : badScripts)
	    try {
		count++;
		new ScriptProcessor(script, new VendingMachineFactory(), true);
		fail++;
		System.err.println(script + ": WRONG");
		System.err.println();
	    }
	    catch(RuntimeException | ParseException t) {
		pass++;
		t.printStackTrace();
		System.err.println(script + ": CORRECT");
		System.err.println();
	    }

	System.err.println(count + " scripts executed: " + pass + " passed, " + fail + " failed.");
    }
}
