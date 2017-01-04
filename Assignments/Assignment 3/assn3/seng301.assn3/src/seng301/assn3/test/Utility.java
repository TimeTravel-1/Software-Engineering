package seng301.assn3.test;

import java.util.ArrayList;
import java.util.List;

import org.lsmr.vending.frontend3.Coin;
import org.lsmr.vending.frontend3.PopCan;
import org.lsmr.vending.frontend3.hardware.CoinRack;
import org.lsmr.vending.frontend3.hardware.VendingMachine;

public class Utility 
{
		// Method to get retrieve coins
		public static int extractCoin(VendingMachine vm) 
		{
			// Counter of money
			int money = 0;
		
			// Loop over coin racks
			for (int i = 0; i < vm.getNumberOfCoinRacks(); i++) 
			{
			    CoinRack rack = vm.getCoinRack(i);
			    List<Coin> listC = rack.unload();
			    for (Coin coin : listC)
			    {
			    		// Add total value of coins to money
			    		money += coin.getValue();
			    }
			}
			
			//Return total money in machine
			return money;
    }
		
		// Method to retrieve pops 
		public static List<String> extractPop(VendingMachine vm) 
    {
			// Add pops to list
			List<PopCan> listP = new ArrayList<PopCan>();
			for(int i = 0; i < vm.getNumberOfPopCanRacks(); i++)
			{
				listP.addAll(vm.getPopCanRack(i).unload());
			}
		
			// Find pop names
			List<String> extracted = new ArrayList<>();
			for(PopCan popCan : listP)
			{
				extracted.add(popCan.getName());
			}
		
			extracted.sort(null);
		
			// Return names of pops
			return extracted;
    }
		
		// Method to retrieve objects in delivery chute
		public static List<Object> extractDelivery(VendingMachine vm) 
    {
			// Retrieve objects in delivery chute
			Object[] objs = vm.getDeliveryChute().removeItems();
			
			// Variable to determine total cost
			int totalC = 0;
			
			// Extracted items to send back
			List<Object> extracted = new ArrayList<>();
		
			for(Object items : objs) 
			{
					// If item is a pop can, determine name
			    if(items instanceof PopCan) 
			    {
			    	PopCan can = (PopCan) items;
			    	String nameP = can.getName();
			    	extracted.add(nameP);
			    }
			    else
			    {
			    	// Otherwise add total value of coin
			    	totalC += ((Coin)items).getValue();
			    }
			}

			extracted.sort(null);
			extracted.add(0, totalC);
			return extracted;
			
		 }

		// Method to retrieve objects from 
    public static int extractStorage(VendingMachine vm) 
    {
    	// Count total value
			int total = 0;
		
			// Get coins from storage bin
			List<Coin> listC = vm.getStorageBin().unload();
			
			// Add value of coins in 
			for(Coin value : listC)
			{
				total += value.getValue();
			}
		
			// Return total
			return total;
    }
}
