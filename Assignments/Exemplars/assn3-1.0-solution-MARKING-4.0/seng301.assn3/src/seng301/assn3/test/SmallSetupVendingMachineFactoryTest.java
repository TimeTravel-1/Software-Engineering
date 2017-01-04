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
public class SmallSetupVendingMachineFactoryTest extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 1, 10, 10, 10);
	new VendingMachineLogic(vm);
    }

    /**
     * T08
     */
    @Test
    public void testApproximateChange() throws DisabledException {
	vm.configure(Arrays.asList("stuff"), Arrays.asList(140));
	vm.getCoinRack(0).load();
	vm.getCoinRack(1).load(new Coin(10), new Coin(10), new Coin(10), new Coin(10), new Coin(10));
	vm.getCoinRack(2).load(new Coin(25));
	vm.getCoinRack(3).load(new Coin(100));
	vm.getPopCanRack(0).load(new PopCan("stuff"));

	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));

	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(155, "stuff"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(320, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
    }

    /**
     * T09
     */
    @Test
    public void testHardForChange() throws DisabledException {
	vm.configure(Arrays.asList("stuff"), Arrays.asList(140));
	vm.loadCoins(1, 6, 1, 1);
	vm.loadPopCans(1);
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(160, "stuff"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(330, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopCanRacks(vm));
    }

    /**
     * T10
     */
    @Test
    public void testInvalidCoins() throws DisabledException {
	vm.configure(Arrays.asList("stuff"), Arrays.asList(140));
	vm.loadCoins(1, 6, 1, 1);
	vm.loadPopCans(1);
	vm.getCoinSlot().addCoin(new Coin(1));
	vm.getCoinSlot().addCoin(new Coin(139));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(140), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(190, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff"), Utilities.extractAndSortFromPopCanRacks(vm));
    }

    /**
     * T12
     */
    @Test
    public void testApproximateChangeWithCredit() throws DisabledException {
	vm.configure(Arrays.asList("stuff"), Arrays.asList(140));
	vm.loadCoins(0, 5, 1, 1);
	vm.loadPopCans(1);

	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(155, "stuff"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(320, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopCanRacks(vm));

	vm.loadCoins(10, 10, 10, 10);
	vm.loadPopCans(1);

	vm.getCoinSlot().addCoin(new Coin(25));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(10));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0, "stuff"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(1400, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(135, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopCanRacks(vm));
    }

    /**
     * T13
     */
    @Test
    public void testNeedToStorePayment() throws DisabledException {
	vm.configure(Arrays.asList("stuff"), Arrays.asList(135));
	vm.loadCoins(10, 10, 10, 10);
	vm.loadPopCans(1);

	vm.getCoinSlot().addCoin(new Coin(25));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(10));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0, "stuff"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(1400, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(135, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopCanRacks(vm));
    }

    /**
     * T14
     */
    @Test
    public void testWeirdName() throws DisabledException {
	vm.configure(Arrays.asList("\""), Arrays.asList(135));
    }
}
