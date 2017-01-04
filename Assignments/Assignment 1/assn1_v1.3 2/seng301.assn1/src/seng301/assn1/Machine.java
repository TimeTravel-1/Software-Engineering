package seng301.assn1;

import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;


import org.lsmr.vending.frontend1.Coin;
import org.lsmr.vending.frontend1.Deliverable;
import org.lsmr.vending.frontend1.Pop;

public class Machine 
{
	
	public Machine (List<Integer> coinKinds, int choice)
	{
		int i = 0;
		
		typeCoins = new int[coinKinds.size()];
		coins = new HashMap<Integer, ArrayList<Coin>>();
		
		namePops = new String[choice];
		pricePops = new int[choice];
		pops = new HashMap<Integer, ArrayList<Pop>>();
		
		if (choice < 1)
		{
			throw new IllegalArgumentException("There should be at least one button.");
		}
	    
		if (coinKinds == null || coinKinds.size() < 1)
		{
			throw new IllegalArgumentException("There should be at least one type of coin.");
		}
		
		for (int j = 0; j < namePops.length; j++)
		{
			namePops[j] = "";
		}
		
		for (Integer num : coinKinds) 
		{
	    Integer index = mapCoins.get(num);
	    
	    if (num <= 0)
	    {
	    	throw new IllegalArgumentException("The coins should all have a positive value.");
	    }
	    
	    if (index != null)
	    {
	    	throw new IllegalStateException("The coins should be unique.");
	    }

	    mapCoins.put(num, i);
	    typeCoins[i++] = num.intValue();
		}
  }

	public void insert(Coin c)
	{
		if (c == null) 
		{
			throw new NullPointerException();   
		}
		else
		{
			int coinvalue = c.getValue();
	    
	    if (mapCoins.get(coinvalue) != null)
	    {
	    	loadCoins.add(c);
	    	submitCoins += c.getValue();
	    }	
	    else 
	    {
	    	outputValues.add(c);
	    }
		}
  }
	
	public void configure (List<String> popNames, List<Integer> popCosts)
	{
		int i = 0;
		
		for (Integer price: popCosts)
		{
			if (price <= 0)
			{
				throw new IllegalArgumentException("The cost should be greater than 0.");
			}
		}
		
		if (popCosts.size() != pricePops.length || popCosts == null)
		{
			throw new IllegalArgumentException("The prices should fill the selection buttons.");
		}
		
		for(Integer prices : popCosts)
	  {
			pricePops[i++] = prices.intValue();
	  }
		
		if (popNames.size() != namePops.length || popNames == null)
		{
			throw new IllegalArgumentException("The pops should fill the selection buttons.");
		}
		
		int j = 0;
		
		for (String flavor : popNames) 
		{
	    if (flavor.equals("\"\"") == false)
	    {
	    	namePops[j++] = flavor;
	    }
	    else
	    {
	    	throw new IllegalArgumentException("The name should be legal.");
	    }
		}
	}
	
	public void press (int value) 
	{
		if (value >= namePops.length || value < 0)
		{
			throw new IllegalArgumentException("The value should correspond to one of the buttons.");
		}
	
		if (pricePops[value] <= submitCoins) 
		{
		  ArrayList<Pop> popAry = pops.get(value);
		  
		  if (popAry != null && popAry.isEmpty() == false) 
		  {
		  	Pop temporary = popAry.remove(0);
		  	outputValues.add(temporary);
		  	
		  	int change = submitCoins - pricePops[value];
				ArrayList<Integer> ary = new ArrayList<Integer>();
				
				for (Integer vals : mapCoins.keySet())
				{
					ary.add(vals);
				}
			
				Map<Integer, ArrayList<Integer>> mapping = leftovers(0, change, ary);
				ArrayList<Integer> lsts = mapping.get(change);
				
				if (lsts == null) 
				{
				    Integer maximumValue = 0;
				    Iterator<Integer> iter = mapping.keySet().iterator();
				    
				    while (iter.hasNext()) 
				    {
				    	Integer temp = iter.next();
				    	
				    	if (temp > maximumValue)
				    	{
				    		maximumValue = temp;
				    	}
				    }
				    
				    lsts = mapping.get(maximumValue);
				}
			
				for (Integer number : lsts) 
				{
				    ArrayList<Coin> lstCoins = coins.get(number);
				    Coin c = lstCoins.remove(0);
				    outputValues.add(c);
	
				    change = change - typeCoins[number];
				}
			
		    submitCoins = change;
		    paidCoins.addAll(loadCoins);
		    loadCoins.clear(); 
		  } 
		}
  }
	
	private Map<Integer, ArrayList<Integer>> leftovers(int startVal, int change, ArrayList<Integer> arys) 
	{
		if(startVal >= arys.size())
		{
			return null;
		}
	
		int num = arys.get(startVal);
		Integer number = mapCoins.get(num);
		
		ArrayList<Coin> lstCoins = coins.get(number);
		Map<Integer, ArrayList<Integer>> mapping = leftovers(startVal + 1, change, arys);
	
		if (mapping == null) 
		{
		    mapping = new TreeMap<Integer, ArrayList<Integer>>(new Comparator<Integer>() 
		    {
		    	
		    	public int compare(Integer num1, Integer num2) 
		    	{
		    		int returnVal = num2-num1;
				    return returnVal;
		    	}
		    });
		    
		    mapping.put(0, new ArrayList<Integer>());
		}
	
		Map<Integer, ArrayList<Integer>> mapping2 = new TreeMap<Integer, ArrayList<Integer>>(mapping);
		
		for (Integer all : mapping.keySet()) 
		{
		    int count = 0;
		    int ans = all;
		    
		    ArrayList<Integer> lsts = mapping.get(all);
		    
		    if (lstCoins != null)
		    {
		    	count = lstCoins.size();
		    }
		    
		    while (count >= 0 && lstCoins != null) 
		    {
		    	int numbers = num * count + ans;
		    	
		    	if (numbers <= change) 
		    	{
				    ArrayList<Integer> lst2 = new ArrayList<Integer>();
				    if (lsts != null)
				    {
				    	lst2.addAll(lsts);
				    }
		
				    for (int i = 0; i < count; i++)
				    {
				    	lst2.add(number);
				    }
		
				    mapping2.put(numbers, lst2);
		    	}
		    	
		    	count = count - 1;
		    }
			}
	
		return mapping2;
	}

	public void loadP(int popKindIndex, Pop[] pval)
	{
		if (pval.length < 0 || pval == null)
		{
			throw new IllegalArgumentException("Pop should not be empty.");
		}
		
		ArrayList<Pop> popList = pops.get(popKindIndex);
		
		if (popList == null)
		{
			popList = new ArrayList<Pop>();
			pops.put(popKindIndex, popList);
		}
		
		for (Pop i : pval)
		{
			popList.add(i);
		}
	}

	public void loadC(int coinKindIndex, Coin[] cval) 
	{
		if (cval.length < 0 || cval == null)
		{
			throw new IllegalArgumentException("Coin should not be empty.");
		}
		
		ArrayList<Coin> lstCoins = coins.get(coinKindIndex);
		
		if (lstCoins == null)
		{
			lstCoins = new ArrayList<Coin>();
			coins.put(coinKindIndex, lstCoins);
		}
		
		for (Coin i : cval)
		{
			lstCoins.add(i);
		}
	}
	
	public List<Deliverable> extract()
	{
		ArrayList<Deliverable> lsts = new ArrayList<Deliverable>();
		
		for (Object objects : outputValues)
		{
			lsts.add((Deliverable)objects);
		}
		
		outputValues.clear();
		return lsts;
	}
	
	public List<List<?>> unload() 
	{
		
		ArrayList<List<?>> lst = new ArrayList<List<?>>();
		ArrayList<Coin> listu = new ArrayList<Coin>();
		ArrayList<Coin> listc = new ArrayList<Coin>();
		ArrayList<Pop> listp = new ArrayList<Pop>();
		
		for (Integer number : coins.keySet()) 
		{
		    ArrayList<Coin> coinAry = coins.get(number);
		    
		    for(Coin c : coinAry)
		    {
		    	listu.add(c);
		    }
		    
		    coinAry.clear();
		}
		
		for(Coin c : paidCoins)
		{
			listc.add(c);
		}
		
		paidCoins.clear();
	
		for(Coin c : loadCoins)
		{
			listc.add(c);
		}
		
		loadCoins.clear();
	
		for (Integer popSVals : pops.keySet()) 
		{
		    ArrayList<Pop> popAry = pops.get(popSVals);
		    
		    for(Pop p : popAry)
		    {
		    	listp.add(p);
		    }
		    
		    popAry.clear();
		}
	
		lst.add(listu);
		lst.add(listc);
		lst.add(listp);
		
		return lst;
	}
	
	private Map<Integer, ArrayList<Coin>> coins;
  private Map<Integer, ArrayList<Pop>> pops;
   
	private ArrayList<Object> outputValues = new ArrayList<Object>();
	 
	private int[] typeCoins;
  private int[] pricePops;
   
  private int submitCoins = 0;
  private String[] namePops;
   
  private ArrayList<Coin> loadCoins = new ArrayList<Coin>();
  private ArrayList<Coin> paidCoins = new ArrayList<Coin>();

  private Map<Integer, Integer> mapCoins = new TreeMap<Integer, Integer>();
}
