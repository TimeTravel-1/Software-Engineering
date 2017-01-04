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
public class ChangingConfigurationVendingMachineFactoryTest extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);

	vm.configure(Arrays.asList(new String[] {"A", "B", "C"}), Arrays.asList(new Integer[] {5, 10, 25}));
	vm.getCoinRack(0).load(new Coin(5));
	vm.getCoinRack(1).load(new Coin(10));
	vm.getCoinRack(2).load(new Coin(25), new Coin(25));
	vm.getCoinRack(3).load();
	vm.getPopCanRack(0).load(new PopCan("A"));
	vm.getPopCanRack(1).load(new PopCan("B"));
	vm.getPopCanRack(2).load(new PopCan("C"));

	vm.configure(Arrays.asList(new String[] {"Coke", "water", "stuff"}), Arrays.asList(new Integer[] {250, 250, 205}));
    }

    /**
     * T07
     */
    @Test
    public void testChangingConfiguration() throws DisabledException {
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));

	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "A"), Utilities.extractAndSortFromDeliveryChute(vm));

	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("B", "C"), Utilities.extractAndSortFromPopCanRacks(vm));

	vm.getCoinRack(0).load(new Coin(5));
	vm.getCoinRack(1).load(new Coin(10));
	vm.getCoinRack(2).load(new Coin(25), new Coin(25));
	vm.getCoinRack(3).load();
	vm.getPopCanRack(0).load(new PopCan("Coke"));
	vm.getPopCanRack(1).load(new PopCan("water"));
	vm.getPopCanRack(2).load(new PopCan("stuff"));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopCanRacks(vm));
    }
}
