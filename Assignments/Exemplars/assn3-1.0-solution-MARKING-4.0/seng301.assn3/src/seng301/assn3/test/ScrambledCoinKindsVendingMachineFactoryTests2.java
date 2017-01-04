package seng301.assn3.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.PopCan;
import org.lsmr.vending.frontend3.hardware.DisabledException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;
import org.lsmr.vending.frontend3.hardware.WRAPPER;

import seng301.assn3.VendingMachineLogic;

@SuppressWarnings("javadoc")
public class ScrambledCoinKindsVendingMachineFactoryTests2 extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	vm = new VendingMachine(new int[] {100, 5, 25, 10}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);

	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 205));
	vm.getCoinRack(0).load();
	vm.getCoinRack(1).load(new Coin(5));
	vm.getCoinRack(2).load(new Coin(25), new Coin(25));
	vm.getCoinRack(3).load(new Coin(10));
	
	vm.loadPopCans(1, 1, 1);
    }

    /**
     * T11
     */
    @Test
    public void testExtractBeforeSaleComplex() throws DisabledException {
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));

	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	
	assertEquals(65, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("Coke", "stuff", "water"), Utilities.extractAndSortFromPopCanRacks(vm));

	vm.loadCoins(0, 1, 2, 1);
	vm.loadPopCans(1, 1, 1);

	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopCanRacks(vm));

	vm = new VendingMachine(new int[] {100, 5, 25, 10}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);
	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.<Integer> asList(250, 250, 205));
	vm.configure(Arrays.asList("A", "B", "C"), Arrays.<Integer> asList(5, 10, 25));
	assertEquals(0, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopCanRacks(vm));

	vm.getCoinRack(0).load();
	vm.getCoinRack(1).load(new Coin(5));
	vm.getCoinRack(2).load(new Coin(25), new Coin(25));
	vm.getCoinRack(3).load(new Coin(10));
	vm.getPopCanRack(0).load(new PopCan("A"));
	vm.getPopCanRack(1).load(new PopCan("B"));
	vm.getPopCanRack(2).load(new PopCan("C"));
	
	
	vm.getCoinSlot().addCoin(new Coin(10));
	vm.getCoinSlot().addCoin(new Coin(5));
	vm.getCoinSlot().addCoin(new Coin(10));
	vm.getSelectionButton(2).press();
	assertEquals(Arrays.asList(0, "C"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(90, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("A", "B"), Utilities.extractAndSortFromPopCanRacks(vm));
    }
}
