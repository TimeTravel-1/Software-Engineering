package seng301.assn3.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.frontend3.hardware.SimulationException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;
import org.lsmr.vending.frontend3.hardware.WRAPPER;

import seng301.assn3.VendingMachineLogic;

@SuppressWarnings("javadoc")
public class BadVendingMachineFactoryTest extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);
    }

    /**
     * bad-script2
     */
    @Test(expected = SimulationException.class)
    public void testBadScript2() {
	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 0));
	fail();
	vm.loadCoins(1, 1, 2, 0);
	vm.loadPopCans(1, 1, 1);
	
	assertEquals(0, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopCanRacks(vm));
    }
    
    /**
     * U02
     */
    @Test(expected = SimulationException.class)
    public void testBadCostsList() {
	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 0));
	fail();
    }

    /**
     * U03
     */
    @Test(expected = SimulationException.class)
    public void testBadNamesList() {
	vm.configure(Arrays.asList("Coke", "water"), Arrays.asList(250, 250));
    }

    /**
     * U04
     */
    @Test(expected = SimulationException.class)
    public void testNonUniqueDenomination() {
	new VendingMachine(new int[] {1, 1}, 1, 10, 10, 10);
	fail();
    }

    /**
     * U05
     */
    @Test(expected = SimulationException.class)
    public void testBadCoinKind() {
	new VendingMachine(new int[] {0}, 1, 10, 10, 10);
	fail();
    }

    /**
     * U06
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadButton() {
	vm.getSelectionButton(3).press();
	fail();
    }

    /**
     * U07
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadButton2() {
	vm.getSelectionButton(-1).press();
	fail();
    }

    /**
     * U08
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadButton3() {
	vm.getSelectionButton(4).press();
	fail();
    }
}
