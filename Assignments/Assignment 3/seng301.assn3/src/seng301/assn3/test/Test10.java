package seng301.assn3.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seng301.assn3.test.VendingMachineLogic;
import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.hardware.DisabledException;
import org.lsmr.vending.frontend3.hardware.SimulationException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

public class Test10 
{
	   private VendingMachine vm;

	   @After
	   public void teardown() 
	   {
	  	 		vm = null;
	   }

	    // U01 and bad-script1 do not make sense. If we do not construct the
	    // VendingMachine, then it will fail.

	    @Test(expected = SimulationException.class)
	    public void U02badcostslist() 
	    {
					// construct(5, 10, 25, 100; 3; 10; 10; 10)
					// configure([0] "Coke", 250; "water", 250; "stuff", 0) 
					// coin-load([0] 0; 5, 1)
	    		// coin-load([0] 1; 10, 1)
	    		// coin-load([0] 2; 25, 2)
	    		// coin-load([0] 3; 100, 0)
	    		// pop-load([0] 0; "Coke", 1)
	    		// pop-load([0] 1; "water", 1)
	    		// pop-load([0] 2; "stuff", 1)
	    		// unload([0])
	    		// CHECK_TEARDOWN(0; 0) 
					
	    		vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
					vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 0));
					
					new VendingMachineLogic(vm);
					
					vm.loadCoins(1, 1, 2, 0);
					vm.loadPopCans(1, 1, 1);
	    }
	    
	    @Test(expected = SimulationException.class)
	    public void badscript2() 
	    {
					// construct(5, 10, 25, 100; 3; 10; 10; 10)
					// configure([0] "Coke", 250; "water", 250; "stuff", 0) 
					// coin-load([0] 0; 5, 1)
	    		// coin-load([0] 1; 10, 1)
	    		// coin-load([0] 2; 25, 2)
	    		// pop-load([0] 0; "Coke", 1)
	    		// pop-load([0] 1; "water", 1)
	    		// pop-load([0] 2; "stuff", 1)
	    		// unload([0])
	    		// CHECK_TEARDOWN(0; 0) 
					
	    		vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
					vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 0));
					
					new VendingMachineLogic(vm);
					
					vm.loadCoins(1, 1, 2, 0);
					vm.loadPopCans(1, 1, 1);
	    }

	    @Test(expected = SimulationException.class)
	    public void U03badnameslist() 
	    {
					// construct(5, 10, 25, 100; 3; 10; 10; 10)
					// configure([0] "Coke", 250; "water", 250)
					
	    		vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
					vm.configure(Arrays.asList("Coke", "water"), Arrays.asList(250, 250));
					
					new VendingMachineLogic(vm);
	    }

	    @Test(expected = SimulationException.class)
	    public void U04badnonuniquedenomination() 
	    {
					// construct(1, 1; 1; 10; 10; 10)
	    	
					new VendingMachine(new int[] {1, 1}, 1, 10, 10, 10);
	    }

	    @Test(expected = SimulationException.class)
	    public void U05badcoinkind() 
	    {
					// construct(0; 1; 10; 10; 10)
	    	
					new VendingMachine(new int[] {0}, 1, 10, 10, 10);
	    }

	    @Test(expected = IndexOutOfBoundsException.class)
	    public void U06badbuttonnumber() 
	    {
					// construct(5, 10, 25, 100; 3; 10; 10; 10)
					// press([0] 3)
	    	
					vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
					
					new VendingMachineLogic(vm);
					
					vm.getSelectionButton(3).press();
	    }

	    @Test(expected = IndexOutOfBoundsException.class)
	    public void U07badbuttonnumber2() 
	    {
					// construct(5, 10, 25, 100; 3; 10; 10; 10)
					// press([0] -1)
	    	
					vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
					
					new VendingMachineLogic(vm);
					
					vm.getSelectionButton(-1).press();			
			}
	    
	    @Test(expected = IndexOutOfBoundsException.class)
	    public void U08badbuttonnumber3() 
	    {
					// construct(5, 10, 25, 100; 3; 10; 10; 10)
					// press([0] 4)
	    	
					vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
					
					new VendingMachineLogic(vm);
					
					vm.getSelectionButton(4).press();
	    }
}