package seng301.assn3.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import seng301.assn3.test.VendingMachineLogic;
import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.hardware.DisabledException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

public class Test8 
{
		 
	   private VendingMachine vm;

	   @Before
	   public void setup() 
	   {
					// construct(5, 10, 25, 100; 1; 10; 10; 10)
					// configure([0] "stuff", 135)
					// coin-load([0] 0; 5, 10)
	  	 		// coin-load([0] 1; 10, 10)
	  	 		// coin-load([0] 2; 25, 10)
	  	 		// coin-load([0] 3; 100, 10)
	  	 		// pop-load([0] 0; "stuff", 1)
			
					vm = new VendingMachine(new int[] {5, 10, 25, 100}, 1, 10, 10, 10);
					vm.configure(Arrays.asList("stuff"), Arrays.asList(135));
					
					new VendingMachineLogic(vm);
			
					vm.loadCoins(10, 10, 10, 10);
					vm.loadPopCans(1);
	    }
	    
	    @Test
	    public void T13goodneedtostorepayment() throws DisabledException 
	    {
					// insert([0] 25)
					// insert([0] 100)
					// insert([0] 10)
					// press([0] 0)
					// extract([0] )
					// CHECK_DELIVERY(0, "stuff")
					// unload([0])
					// CHECK_TEARDOWN(1400; 135)
			
					vm.getCoinSlot().addCoin(new Coin(25));
					vm.getCoinSlot().addCoin(new Coin(100));
					vm.getCoinSlot().addCoin(new Coin(10));
					
					vm.getSelectionButton(0).press();
					
					assertEquals(Arrays.asList(0, "stuff"), Utility.extractDelivery(vm));
					assertEquals(1400, Utility.extractCoin(vm));
					assertEquals(135, Utility.extractStorage(vm));
					assertEquals(Arrays.asList(), Utility.extractPop(vm));
	    }
}