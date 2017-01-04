package seng301.assn3.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import seng301.assn3.test.VendingMachineLogic;
import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.hardware.DisabledException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

public class Test5 
{
	   private VendingMachine vm;

	    @Before
	    public void setup() 
	    {
				// construct(5, 10, 25, 100; 3; 10; 10; 10)
				// configure([0] "A", 5; "B", 10; "C", 25)
				// coin-load([0] 0; 5, 1)
	    	// coin-load([0] 1; 10, 1)
	    	// coin-load([0] 2; 25, 2)
	    	// coin-load([0] 3; 100, 0)
	    	// pop-load([0] 0; "A", 1)
	    	// pop-load([0] 1; "B", 1)
	    	// pop-load([0] 2; "C", 1)
		
				vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
				vm.configure(Arrays.asList("A",  "B", "C"), Arrays.asList(5, 10, 25));
				
				new VendingMachineLogic(vm);
				
				vm.loadCoins(1, 1, 2, 0);
				vm.loadPopCans(1, 1, 1);
			}
		
			  @Test
			  public void T07goodchangingconfiguration() throws DisabledException 
			  {
						// configure([0] "Coke", 250; "water", 250; "stuff", 205)
			  		// press([0] 0)
						// extract([0])
						// CHECK_DELIVERY(0)
						// insert([0] 100)
						// insert([0] 100)
						// insert([0] 100)
						// press([0] 0)
						// extract([0])
						// CHECK_DELIVERY(50, "A")
						// unload([0])
						// CHECK_TEARDOWN(315; 0; "B", "C")
						// coin-load([0] 0; 5, 1)
			  		// coin-load([0] 1; 10, 1)
			  		// coin-load([0] 2; 25, 2)
			  		// coin-load([0] 3; 100, 0)
			  		// pop-load([0] 0; "Coke", 1)
			  		// pop-load([0] 1; "water", 1)
			  		// pop-load([0] 2; "stuff", 1)
						// insert([0] 100)
						// insert([0] 100)
						// insert([0] 100)
						// press([0] 0)
						// extract([0])
						// CHECK_DELIVERY(50, "Coke")
						// unload([0])
						// CHECK_TEARDOWN(315; 0; "water", "stuff")
				
						vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 205));
						
						vm.getSelectionButton(0).press();
						
						assertEquals(Arrays.asList(0), Utility.extractDelivery(vm));
				
						vm.getCoinSlot().addCoin(new Coin(100));
						vm.getCoinSlot().addCoin(new Coin(100));
						vm.getCoinSlot().addCoin(new Coin(100));
						
						vm.getSelectionButton(0).press();
				
						assertEquals(Arrays.asList(50, "A"), Utility.extractDelivery(vm));
						assertEquals(315, Utility.extractCoin(vm));
						assertEquals(0, Utility.extractStorage(vm));
						assertEquals(Arrays.asList("B", "C"), Utility.extractPop(vm));
						
						vm.loadCoins(1, 1, 2, 0);
						vm.loadPopCans(1, 1, 1);
				
						vm.getCoinSlot().addCoin(new Coin(100));
						vm.getCoinSlot().addCoin(new Coin(100));
						vm.getCoinSlot().addCoin(new Coin(100));
						vm.getSelectionButton(0).press();
						
						assertEquals(Arrays.asList(50, "Coke"), Utility.extractDelivery(vm));
						assertEquals(315, Utility.extractCoin(vm));
						assertEquals(0, Utility.extractStorage(vm));
						assertEquals(Arrays.asList("stuff", "water"), Utility.extractPop(vm));
	    }
}