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
public class ScrambledCoinKindsVendingMachineFactoryTests3 extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	vm = new VendingMachine(new int[] {100, 5, 25, 10}, 3, 2, 10, 10);
	new VendingMachineLogic(vm);
	
	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 205));
	vm.loadCoins(0, 1, 2, 1);
	vm.loadPopCans(1, 1, 1);
    }

    /**
     * T05
     */
    @Test
    public void testScrambledCoinKinds() throws DisabledException {
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(215, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(100, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopCanRacks(vm));
    }
}
