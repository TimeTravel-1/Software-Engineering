package ca.ucalgary.seng301.myvendingmachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.PopCan;
import ca.ucalgary.seng301.vendingmachine.VendingMachineStoredContents;
import ca.ucalgary.seng301.vendingmachine.hardware.CoinSlot;
import ca.ucalgary.seng301.vendingmachine.hardware.DeliveryChute;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class VendingMachineLogic {
		
	private VendingMachine vm;
    private VendingMachineStoredContents vmStoredContents;
    

    	//sets the corresponding vending machine this class' methods will act upon
	public VendingMachineLogic(VendingMachine vm){
		this.vm = vm;
	}
    
    public void press(int value){
    		//error checking
    	if (value < 0)
		    throw new IllegalArgumentException("Value cannot be negative or null");
    	if (value >= vm.getNumberOfSelectionButtons())
		    throw new IllegalArgumentException("Value cannot be greater than number of selection buttons");

    		//press the button... I dare you (not linked so nothing actually happens)
    	vm.getSelectionButton(value).press();

    	//sb.register(listener);

    	
    	//need to manually make the machine dispense change and pop to the delivery chute
    	//if (vm.getCoinReceptacle().)
    	//{
	    	//get the pop can rack corresponding to the button that was pressed
	    	//PopCanRack pcr = vm.getPopCanRack(value);
	    	//pcr.dispensePop();
	    	//pcr.re
	    	
    	//}
    	
    }
    
    public void insert(int value) throws DisabledException {
    	
    	//error checking
    	if (value <= 0)
    		throw new IllegalArgumentException("Coin value must be positive");
    	
    	CoinSlot cs = vm.getCoinSlot();
    	Coin coin = new Coin(value);
    	
    	cs.addCoin(coin);
    }
    
    public void load(List<Integer> coinCounts, List<Integer> popCanCounts) {
    	
		if(coinCounts == null || popCanCounts == null || coinCounts.size() < 0 || popCanCounts.size() < 0)
		    throw new IllegalArgumentException("Neither list may be empty");
    	
    	//loading coins
		int[] coinCountsArray = new int[coinCounts.size()];
		int i = 0;
    	for (Integer count : coinCounts){
    		coinCountsArray[i] = count;
    		i++;
    	}
    	vm.loadCoins(coinCountsArray);
    	
    	//loading pops
		int[] popCanCountsArray = new int[popCanCounts.size()];
		i = 0;
    	for (Integer count : popCanCounts){
    		popCanCountsArray[i] = count;
    		i++;
    	}
    	vm.loadPops(popCanCountsArray);	
    }
	
    public VendingMachineStoredContents unload() {
        	
    	vmStoredContents = new VendingMachineStoredContents(); 
    	
		//adds unused coins to vmStoredContents
		List<List<Coin>> unusedCoinList;
		unusedCoinList = new ArrayList<List<Coin>>();

    	for (int i = 0; i < vm.getNumberOfCoinRacks(); i++){
    		unusedCoinList.add(vm.getCoinRack(i).unloadWithoutEvents());
    	}
		vmStoredContents.unusedCoinsForChange = unusedCoinList;

		//adds unusedPaymentCoinsInStorageBin to vmStoredContents
		List<Coin> paymentCoinsInStorageBin = vm.getStorageBin().unloadWithoutEvents();
		vmStoredContents.paymentCoinsInStorageBin = paymentCoinsInStorageBin;
		
		//adds unsold popcans to vmStoredContents
		List<List<PopCan>> unsoldPopCanList;
		unsoldPopCanList = new ArrayList<List<PopCan>>();

    	for (int i = 0; i < vm.getNumberOfPopCanRacks(); i++){
    		unsoldPopCanList.add(vm.getPopCanRack(i).unloadWithoutEvents());
    	}
		vmStoredContents.unsoldPopCans = unsoldPopCanList;
		
		return vmStoredContents;
    }
    
	public List<Object> extract(){
		
		DeliveryChute dc = vm.getDeliveryChute();
		List<Object> chuteList;
		chuteList = Arrays.asList(dc.removeItems());
	
		return chuteList;
	}



}
