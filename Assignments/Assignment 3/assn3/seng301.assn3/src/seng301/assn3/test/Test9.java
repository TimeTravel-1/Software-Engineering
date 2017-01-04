package seng301.assn3.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import seng301.assn3.test.VendingMachineLogic;
import org.lsmr.vending.frontend3.hardware.DisabledException;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

public class Test9 
{
		 
	   private VendingMachine vm;

	    @Test
	    public void T14weirdname() throws DisabledException 
	    {
					// construct(5, 10, 25, 100; 1; 10; 10; 10)
	    		// configure([0] "\"", 135)
	    		
	    		vm = new VendingMachine(new int[] {5, 10, 25, 100}, 1, 10, 10, 10);
	    		vm.configure(Arrays.asList("\""), Arrays.asList(135));
	    		
	    		new VendingMachineLogic(vm);
		
	    		assertTrue(vm.getNumberOfPopCanRacks() > 0);
	    }
}