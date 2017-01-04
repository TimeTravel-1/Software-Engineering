package seng301.assn3.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import seng301.assn3.test.VendingMachineLogic;
import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.hardware.DisabledException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

public class Test4 
{
		 private VendingMachine vm;
	
	   @Before
	   public void setup() 
	   {
					// construct(100, 5, 25, 10; 3; 10; 10; 10)
	  	 		// configure([0] "Coke", 250; "water", 250; "stuff", 205)
	  	 		// coin-load([0] 0; 100, 0)
	  	 		// coin-load([0] 1; 5, 1)
	  	 		// coin-load([0] 2; 25, 2)
	  	 		// coin-load([0] 3; 10, 1)
	  	 		// pop-load([0] 0; "Coke", 1)
	  	 		// pop-load([0] 1; "water", 1)
	  	 		// pop-load([0] 2; "stuff", 1)
	  	 
					vm = new VendingMachine(new int[] {100, 5, 25, 10}, 3, 10, 10, 10);
					vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 205));
					
					new VendingMachineLogic(vm);
				
					vm.loadCoins(0, 1, 2, 1);
					vm.loadPopCans(1, 1, 1);
		}
				
	   @Test
	   public void T06goodextractbeforesale() throws DisabledException 
	   {
					// press([0] 0)
					// extract([0])
					// CHECK_DELIVERY(0)
					// insert([0] 100)
					// insert([0] 100)
					// insert([0] 100)
					// extract([0])
					// CHECK_DELIVERY(0)
					// unload([0])
					// CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
								
					vm.getSelectionButton(0).press();
								
					assertEquals(Arrays.asList(0), Utility.extractDelivery(vm));
								
					vm.getCoinSlot().addCoin(new Coin(100));
					vm.getCoinSlot().addCoin(new Coin(100));
					vm.getCoinSlot().addCoin(new Coin(100));
							
					assertEquals(Arrays.asList(0), Utility.extractDelivery(vm));
					assertEquals(65, Utility.extractCoin(vm));
					assertEquals(0, Utility.extractStorage(vm));
					assertEquals(Arrays.asList("Coke", "stuff", "water"), Utility.extractPop(vm));
				}
				
				@Test
				public void T11goodextractbeforesalecomplex() throws DisabledException 
				{
					// press([0] 0)
					// extract([0])
					// CHECK_DELIVERY(0)
					// insert([0] 100)
					// insert([0] 100)
					// insert([0] 100)
					// extract([0])
					// CHECK_DELIVERY(0)
					// unload([0])
					// CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
					// coin-load([0] 0; 100, 0)
					// coin-load([0] 1; 5, 1)
					// coin-load([0] 2; 25, 2)
					// coin-load([0] 3; 10, 1)
					// pop-load([0] 0; "Coke", 1)
					// pop-load([0] 1; "water", 1)
					// pop-load([0] 2; "stuff", 1)
					// press([0] 0)
					// extract([0])
					// CHECK_DELIVERY(50, "Coke")
					// unload([0])
					// CHECK_TEARDOWN(315; 0; "water", "stuff")
					// construct(100, 5, 25, 10; 3; 10; 10; 10)
					// configure([1] "Coke", 250; "water", 250; "stuff", 205)
					// configure([1] "A", 5; "B", 10; "C", 25)
					// unload([1])
					// CHECK_TEARDOWN(0; 0)
					// coin-load([1] 0; 100, 0)
					// coin-load([1] 1; 5, 1)
					// coin-load([1] 2; 25, 2)
					// coin-load([1] 3; 10, 1)
					// pop-load([1] 0; "A", 1)
					// pop-load([1] 1; "B", 1)
					// pop-load([1] 2; "C", 1)
					// insert([1] 10)
					// insert([1] 5)
					// insert([1] 10)
					// press([1] 2)
					// extract([1])
					// CHECK_DELIVERY(0, "C")
					// unload([1])
					// CHECK_TEARDOWN(90; 0; "A", "B")
				
					vm.getSelectionButton(0).press();
					
					assertEquals(Arrays.asList(0), Utility.extractDelivery(vm));
					
					vm.getCoinSlot().addCoin(new Coin(100));
					vm.getCoinSlot().addCoin(new Coin(100));
					vm.getCoinSlot().addCoin(new Coin(100));
					
					assertEquals(Arrays.asList(0), Utility.extractDelivery(vm));
					assertEquals(65, Utility.extractCoin(vm));
					assertEquals(0, Utility.extractStorage(vm));
					assertEquals(Arrays.asList("Coke", "stuff", "water"), Utility.extractPop(vm));
				
					vm.loadCoins(0, 1, 2, 1);
					vm.loadPopCans(1, 1, 1);
				
					vm.getSelectionButton(0).press();
					
					assertEquals(Arrays.asList(50, "Coke"), Utility.extractDelivery(vm));
					assertEquals(315, Utility.extractCoin(vm));
					assertEquals(0, Utility.extractStorage(vm));
					assertEquals(Arrays.asList("stuff", "water"), Utility.extractPop(vm));
				
					VendingMachine vm2 = new VendingMachine(new int[] {100, 5, 25, 10}, 3, 10, 10, 10);
					vm2.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.<Integer> asList(250, 250, 205));
					vm2.configure(Arrays.asList("A", "B", "C"), Arrays.<Integer> asList(5, 10, 25));
					
					new VendingMachineLogic(vm2);
					
					assertEquals(0, Utility.extractCoin(vm2));
					assertEquals(0, Utility.extractStorage(vm2));
					assertEquals(Arrays.asList(), Utility.extractPop(vm2));
				
					vm2.loadCoins(0, 1, 2, 1);
					vm2.loadPopCans(1, 1, 1);
					
					vm2.getCoinSlot().addCoin(new Coin(10));
					vm2.getCoinSlot().addCoin(new Coin(5));
					vm2.getCoinSlot().addCoin(new Coin(10));
					
					vm2.getSelectionButton(2).press();
					
					assertEquals(Arrays.asList(0, "C"), Utility.extractDelivery(vm2));
					assertEquals(90, Utility.extractCoin(vm2));
					assertEquals(0, Utility.extractStorage(vm2));
					assertEquals(Arrays.asList("A", "B"), Utility.extractPop(vm2));
	   }
}