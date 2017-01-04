package seng301.assn3.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import seng301.assn3.test.VendingMachineLogic;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

public class Test2 
{
    private VendingMachine vm;

    @Before
    public void setup() 
    {
			// construct(5, 10, 25, 100; 3; 10; 10; 10)
   
			vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
			
			new VendingMachineLogic(vm);
    }

    @Test
    public void T03goodteardownwithoutconfigureorload() 
    {
			// extract([0])
			// CHECK_DELIVERY(0)
			// unload([0])
			// CHECK_TEARDOWN(0; 0)
    	
			assertEquals(Arrays.asList(0), Utility.extractDelivery(vm));
			assertEquals(0, Utility.extractCoin(vm));
			assertEquals(0, Utility.extractStorage(vm));
			assertEquals(Arrays.asList(), Utility.extractPop(vm));
    }
}