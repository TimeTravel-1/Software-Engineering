package seng301.assn3.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import seng301.assn3.test.VendingMachineLogic;
import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.hardware.DisabledException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

public class Test7 
{
		 
	   private VendingMachine vm;

	   @Before
	   public void setup() 
	   {
						// construct(5, 10, 25, 100; 1; 10; 10; 10)
						// configure([0] "stuff", 140)
						// coin-load([0] 0; 5, 1)
	  	 			// coin-load([0] 1; 10, 6)
	  	 			// coin-load([0] 2; 25, 1)
	  	 			// coin-load([0] 3; 100, 1)
	  	 			// pop-load([0] 0; "stuff", 1)
				
						vm = new VendingMachine(new int[] {5, 10, 25, 100}, 1, 10, 10, 10);
						vm.configure(Arrays.asList("stuff"), Arrays.asList(140));
						
						new VendingMachineLogic(vm);
				
						vm.loadCoins(1, 6, 1, 1);
						vm.loadPopCans(1);
	    }

	    @Test
	    public void T09goodhardforchange() throws DisabledException 
	    {
						// insert([0] 100)
						// insert([0] 100)
						// insert([0] 100)
						// press([0] 0)
						// extract([0] )
						// CHECK_DELIVERY(160, "stuff")
						// unload([0])
						// CHECK_TEARDOWN(330; 0)
						
						vm.getCoinSlot().addCoin(new Coin(100));
						vm.getCoinSlot().addCoin(new Coin(100));
						vm.getCoinSlot().addCoin(new Coin(100));
						
						vm.getSelectionButton(0).press();
						
						assertEquals(Arrays.asList(160, "stuff"), Utility.extractDelivery(vm));
						assertEquals(330, Utility.extractCoin(vm));
						assertEquals(0, Utility.extractStorage(vm));
						assertEquals(Arrays.asList(), Utility.extractPop(vm));
	    }

	    @Test
	    public void T10goodinvalidcoin() throws DisabledException 
	    {
						// insert([0] 1)
						// insert([0] 139)
						// press([0] 0)
						// extract([0])
						// CHECK_DELIVERY(140)
						// unload([0])
						// CHECK_TEARDOWN(190; 0; "stuff")
	    	
						vm.getCoinSlot().addCoin(new Coin(1));
						vm.getCoinSlot().addCoin(new Coin(139));
						
						vm.getSelectionButton(0).press();
						
						assertEquals(Arrays.asList(140), Utility.extractDelivery(vm));
						assertEquals(190, Utility.extractCoin(vm));
						assertEquals(0, Utility.extractStorage(vm));
						assertEquals(Arrays.asList("stuff"), Utility.extractPop(vm));
	    }
}