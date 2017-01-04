package seng301.assn2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import org.lsmr.vending.frontend2.Coin;
import org.lsmr.vending.frontend2.hardware.AbstractHardware;
import org.lsmr.vending.frontend2.hardware.AbstractHardwareListener;
import org.lsmr.vending.frontend2.hardware.CapacityExceededException;
import org.lsmr.vending.frontend2.hardware.CoinRack;
import org.lsmr.vending.frontend2.hardware.CoinSlot;
import org.lsmr.vending.frontend2.hardware.CoinSlotListener;
import org.lsmr.vending.frontend2.hardware.DisabledException;
import org.lsmr.vending.frontend2.hardware.Display;
import org.lsmr.vending.frontend2.hardware.EmptyException;
import org.lsmr.vending.frontend2.hardware.PopCanRack;
import org.lsmr.vending.frontend2.hardware.SelectionButton;
import org.lsmr.vending.frontend2.hardware.SelectionButtonListener;
import org.lsmr.vending.frontend2.hardware.SimulationException;
import org.lsmr.vending.frontend2.hardware.VendingMachine;

public class Logic implements CoinSlotListener, SelectionButtonListener {

	public Logic(VendingMachine vm) {
	
		vendMach = vm;
		
		vm.getCoinSlot().register(this);
  	for(int i = 0, count = vm.getNumberOfSelectionButtons(); i < count; i++) {
  		SelectionButton sb = vm.getSelectionButton(i);
  		sb.register(this);
  		buttonToIndex.put(sb, i);
  	}
  	
  	for (int i = 0; i < vendMach.getNumberOfCoinRacks(); i++)
  	{
  		int num = vendMach.getCoinKindForCoinRack(i);
  		valueToIndexMap.put(num, i);
  	}
	}

	private VendingMachine vendMach;
	private int money = 0;
	private Map<SelectionButton, Integer> buttonToIndex = new HashMap<>();
  private Map<Integer, Integer> valueToIndexMap = new HashMap<>();

	
	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	}

	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		money += coin.getValue();
	}

	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
	}

	@Override
	public void pressed(SelectionButton button) {
	
		Integer start = buttonToIndex.get(button);
		
		if (start == null)
		{
			throw new IllegalArgumentException("Please select an appropriate button.");
		}
		
		int val = vendMach.getPopKindCost(start);
		
		if (val <= money)
		{
			PopCanRack rackP = vendMach.getPopCanRack(start);
			if (rackP.size() > 0)
			{
				try {
					rackP.dispensePopCan();
					vendMach.getCoinReceptacle().storeCoins();
					money = deliverChange(val, money);
				}
				catch(EmptyException | DisabledException | CapacityExceededException e) 
				{
			    throw new SimulationException(e);
				}
			}
			else
			{
				 Display disp = vendMach.getDisplay();
			    disp.display("Cost of pop is " + val + ", but only " + money + " was loaded.");
			    final Timer times = new Timer();
			    times.schedule(new TimerTask() {
				
				public void run() {
				    times.cancel();
				}
			    }, 5000);
			}
		}
	
	}
	
	private Map<Integer, List<Integer>> changeHelper(ArrayList<Integer> values, int index, int changeDue) {
		if(index >= values.size())
		    return null;

		int value = values.get(index);
		Integer ck = valueToIndexMap.get(value);
		CoinRack cr = vendMach.getCoinRack(ck);

		Map<Integer, List<Integer>> map = changeHelper(values, index + 1, changeDue);

		if(map == null) {
		    map = new TreeMap<>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
			    return o2 - o1;
			}
		    });
		    map.put(0, new ArrayList<Integer>());
		}

		Map<Integer, List<Integer>> newMap = new TreeMap<>(map);
		for(Integer total : map.keySet()) {
		    List<Integer> res = map.get(total);

		    for(int count = cr.size(); count >= 0; count--) {
			int newTotal = count * value + total;
			if(newTotal <= changeDue) {
			    List<Integer> newRes = new ArrayList<>();
			    if(res != null)
				newRes.addAll(res);

			    for(int i = 0; i < count; i++)
				newRes.add(ck);

			    newMap.put(newTotal, newRes);
			}
		    }
		}

		return newMap;
	    }

	    private int deliverChange(int cost, int entered) throws CapacityExceededException, EmptyException, DisabledException {
		int changeDue = entered - cost;

		if(changeDue < 0)
		    throw new InternalError("Cost was greater than entered.");

		ArrayList<Integer> values = new ArrayList<>();
		for(Integer ck : valueToIndexMap.keySet())
		    values.add(ck);

		Map<Integer, List<Integer>> map = changeHelper(values, 0, changeDue);

		List<Integer> res = map.get(changeDue);
		if(res == null) {
		    // An exact match was not found, so do your best
		    Iterator<Integer> iter = map.keySet().iterator();
		    Integer max = 0;
		    while(iter.hasNext()) {
			Integer temp = iter.next();
			if(temp > max)
			    max = temp;
		    }
		    res = map.get(max);
		}

		for(Integer ck : res) {
		    CoinRack cr = vendMach.getCoinRack(ck);
		    cr.releaseCoin();
		    changeDue -= vendMach.getCoinKindForCoinRack(ck);
		}

		return changeDue;
	    }
}
