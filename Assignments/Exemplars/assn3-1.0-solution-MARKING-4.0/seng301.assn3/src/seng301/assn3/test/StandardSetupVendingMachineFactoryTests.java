package seng301.assn3.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.hardware.DisabledException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;
import org.lsmr.vending.frontend3.hardware.WRAPPER;

import seng301.assn3.VendingMachineLogic;

@SuppressWarnings("javadoc")
public class StandardSetupVendingMachineFactoryTests extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);

	vm.configure(Arrays.asList(new String[] {"Coke", "water", "stuff"}), Arrays.asList(new Integer[] {250, 250, 205}));
	vm.loadCoins(1, 1, 2, 0);
	vm.loadPopCans(1, 1, 1);
    }

    /**
     * good-script
     */
    @Test
    public void testGoodScript() throws DisabledException {
	vm.getSelectionButton(0).press();

	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();
	
	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopCanRacks(vm));
    }
    
    /**
     * T01
     */
    @Test
    public void testInsertAndPressWithExactChange() throws DisabledException {
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(25));
	vm.getCoinSlot().addCoin(new Coin(25));

	vm.getSelectionButton(0).press();

	assertEquals(Arrays.asList(0, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopCanRacks(vm));
    }

    /**
     * T02
     */
    @Test
    public void testInsertAndPressExpectingChange() throws DisabledException {
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));

	vm.getSelectionButton(0).press();

	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopCanRacks(vm));
    }

    /**
     * T04
     */
    @Test
    public void testPressWithoutInsert() throws DisabledException {
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(65, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("Coke", "stuff", "water"), Utilities.extractAndSortFromPopCanRacks(vm));
    }
}
