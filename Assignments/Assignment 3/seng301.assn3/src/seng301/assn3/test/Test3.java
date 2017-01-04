package seng301.assn3.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import seng301.assn3.test.VendingMachineLogic;
import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.hardware.DisabledException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

public class Test3  
{
  	private VendingMachine vm;

	  @Before
	  public void setup() 
	  {
				// construct(100, 5, 25, 10; 3; 2; 10; 10)
				// configure([0] "Coke", 250; "water", 250; "stuff", 205)
		  	// coin-load([0] 0; 100, 0)
		  	// coin-load([0] 1; 5, 1)
		  	// coin-load([0] 2; 25, 2)
		  	// coin-load([0] 3; 10, 1)
		  	// pop-load([0] 0; "Coke", 1)
		  	// pop-load([0] 1; "water", 1)
		  	// pop-load([0] 2; "stuff", 1)
	  	
				vm = new VendingMachine(new int[] {100, 5, 25, 10}, 3, 2, 10, 10);
				vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 205));
				
				new VendingMachineLogic(vm);
				
				vm.loadCoins(0, 1, 2, 1);
				vm.loadPopCans(1, 1, 1);
		}
			
		@Test
		public void T05goodscrambledcoinkinds() throws DisabledException 
		{
				// press([0] 0)
				// extract([0])
				// CHECK_DELIVERY(0)
				// insert([0] 100)
				// insert([0] 100)
				// insert([0] 100)
				// press([0] 0)
				// extract([0])
				// CHECK_DELIVERY(50, "Coke")
				// unload([0])
				// CHECK_TEARDOWN(215; 100; "water", "stuff")
			
				vm.getSelectionButton(0).press();
				
				assertEquals(Arrays.asList(0), Utility.extractDelivery(vm));
				
				vm.getCoinSlot().addCoin(new Coin(100));
				vm.getCoinSlot().addCoin(new Coin(100));
				vm.getCoinSlot().addCoin(new Coin(100));
				
				vm.getSelectionButton(0).press();
				
				assertEquals(Arrays.asList(50, "Coke"), Utility.extractDelivery(vm));
				assertEquals(215, Utility.extractCoin(vm));
				assertEquals(100, Utility.extractStorage(vm));
				assertEquals(Arrays.asList("stuff", "water"), Utility.extractPop(vm));
	  }
}