package seng301.assn3.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.PopCan;
import org.lsmr.vending.frontend3.hardware.CoinRack;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

/**
 * A set of simple utility methods for use by test cases.
 * 
 * @author Robert J. Walker
 */
public class Utilities {
    /**
     * Extracts all items in the delivery chute of the indicated vending
     * machine, sorting them.
     * 
     * @param vm
     *            The vending machine from which to extract.
     * @return A list of the extracted items.
     */
    public static List<Object> extractAndSortFromDeliveryChute(VendingMachine vm) {
	Object[] actualItems = vm.getDeliveryChute().removeItems();
	int actualValue = 0;
	List<Object> actualList = new ArrayList<>();

	for(Object obj : actualItems) {
	    if(obj instanceof PopCan) {
		PopCan pc = (PopCan)obj;
		String name = pc.getName();
		actualList.add(name);
	    }
	    else
		actualValue += ((Coin)obj).getValue();
	}

	Collections.sort(actualList, null);

	actualList.add(0, actualValue);
	return actualList;
    }

    /**
     * All coins in the coin racks of the indicated vending machine are
     * extracted.
     *
     * @param vm
     *            The vending machine to extract from.
     * @return The sum of the values of the extracted coins.
     */
    public static int extractAndSumFromCoinRacks(VendingMachine vm) {
	int total = 0;

	for(int i = 0, max = vm.getNumberOfCoinRacks(); i < max; i++) {
	    CoinRack cr = vm.getCoinRack(i);
	    List<Coin> coins = cr.unload();
	    for(Coin coin : coins)
		total += coin.getValue();
	}

	return total;
    }

    /**
     * All coins in the storage bin of the indicated vending machine are
     * extracted.
     *
     * @param vm
     *            The vending machine to extract from.
     * @return The sum of the values of the extracted coins.
     */
    public static int extractAndSumFromStorageBin(VendingMachine vm) {
	int total = 0;

	List<Coin> coins = vm.getStorageBin().unload();
	for(Coin coin : coins)
	    total += coin.getValue();

	return total;
    }

    /**
     * Extracts all pop cans in the pop can racks of the indicated vending
     * machine, sorting them.
     * 
     * @param vm
     *            The vending machine from which to extract.
     * @return A list of the extracted items.
     */
    public static List<String> extractAndSortFromPopCanRacks(VendingMachine vm) {
	List<PopCan> actualPopCans = new ArrayList<>();
	for(int i = 0, max = vm.getNumberOfPopCanRacks(); i < max; i++)
	    actualPopCans.addAll(vm.getPopCanRack(i).unload());

	List<String> actualList = new ArrayList<>();
	for(PopCan popCan : actualPopCans)
	    actualList.add(popCan.getName());

	Collections.sort(actualList, null);

	return actualList;
    }

}
