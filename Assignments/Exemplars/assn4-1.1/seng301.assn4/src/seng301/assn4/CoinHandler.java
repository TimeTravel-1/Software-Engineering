package seng301.assn4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.Coin;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.CoinRack;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;

public class CoinHandler implements PaymentHandler<Coin>{

	private HardwareFacade h;

	private int availableFunds = 0;
	private Map<Integer, Integer> valueToIndexMap = new HashMap<>();
	
	//Class deals with operations involving coins
	public CoinHandler(HardwareFacade hf) 
	{
		h = hf;
	}

	//Store coins
	public void setCoin(Integer value, int i)
	{
		valueToIndexMap.put(value, i);
	}
	
	//Retrieve coins
	public Integer getCoin(int i)
	{
		return valueToIndexMap.get(i);
	}
	
	//Adding funds that are inserted into machine
	public void addAvailableFunds(Coin coin) 
	{
		//Add value of coins to a running total of credit that the user has to make purchases
		Cents funds = coin.getValue();
		availableFunds += funds.getValue();
	}
	
	//Get keys
	public Set<Integer> getKeys()
	{
		return valueToIndexMap.keySet();
	}
	
	//Store coins when transaction is completed
	public void storeCoins() throws CapacityExceededException, DisabledException
	{
		h.getCoinReceptacle().storeCoins();
	}

	//Return the credit that the user currently has
	public int getAvailableFunds() 
	{
		return availableFunds;
	}

	//Set the credit that the user currently has
	public void setAvailableFunds(int setValue) 
	{
		availableFunds = setValue;
	}
	
	//Update value of credits after a transaction takes place
	public void updateAvailableFunds(int cost) throws CapacityExceededException, EmptyException, DisabledException
	{
		setAvailableFunds(deliverChange(cost, getAvailableFunds())); 
	}

	 private Map<Integer, ArrayList<Integer>> changeHelper(ArrayList<Integer> values, int index, int changeDue) {
			if(index >= values.size())
			    return null;

			int value = values.get(index);
			Integer ck = valueToIndexMap.get(value);
			CoinRack cr = h.getCoinRack(ck);

			Map<Integer, ArrayList<Integer>> map = changeHelper(values, index + 1, changeDue);

			if(map == null) {
			    map = new TreeMap<>(new Comparator<Integer>() {
			
				public int compare(Integer o1, Integer o2) {
				    return o2 - o1;
				}
			    });
			    map.put(0, new ArrayList<Integer>());
			}

			Map<Integer, ArrayList<Integer>> newMap = new TreeMap<>(map);
			for(Integer total : map.keySet()) {
			    ArrayList<Integer> res = map.get(total);

			    for(int count = cr.size(); count >= 0; count--) {
				int newTotal = count * value + total;
				if(newTotal <= changeDue) {
				    ArrayList<Integer> newRes = new ArrayList<>();
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
			    throw new InternalError("Cost was greater than entered, which should not happen");

			ArrayList<Integer> values = new ArrayList<>();
			for(Integer ck : valueToIndexMap.keySet())
			    values.add(ck);

			Map<Integer, ArrayList<Integer>> map = changeHelper(values, 0, changeDue);

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
			    CoinRack cr = h.getCoinRack(ck);
			    cr.releaseCoin();
			    changeDue -= h.getCoinKindForCoinRack(ck).getValue();
			}
			return changeDue;
		    }


	
	}

	
 

