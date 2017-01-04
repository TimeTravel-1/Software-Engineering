package ca.ucalgary.seng301.myvendingmachine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.Pop;

public class VendingMachine {
    /**
     * Used to represent the physical delivery chute. Objects added here will be
     * removed when extracted.
     */
    private ArrayList<Object> deliveryChute = new ArrayList<>();

    /**
     * Keeps track of the value of the coins that have been inserted but not yet
     * used.
     */
    private int valueOfEnteredCoins = 0;
    private List<Coin> individualCoinsEntered = new ArrayList<>();
    private List<Coin> paymentCoins = new ArrayList<>();

    private int[] coinKinds;
    private Map<Integer, Integer> valueToIndexMap = new TreeMap<>();
    private Map<Integer, ArrayList<Coin>> coins;
    private String[] popKindNames;
    private int[] popKindCosts;
    private Map<Integer, ArrayList<Pop>> pops;

    public VendingMachine(List<Integer> coinKinds, int buttonCount) {
		if(coinKinds == null || coinKinds.size() < 1)
		    throw new IllegalArgumentException("There must be at least one coin kind");
	
		if(buttonCount < 1)
		    throw new IllegalArgumentException("There must be at least one selection button");
	
		this.coinKinds = new int[coinKinds.size()];
		coins = new HashMap<>();
		int i = 0;
		for(Integer ck : coinKinds) {
		    Integer index = valueToIndexMap.get(ck);
		    if(index != null)
		    	throw new IllegalStateException("Coin kinds must have unique denominations");
		    if(ck <= 0)
		    	throw new IllegalArgumentException("Each coin kind must be postive");
	
		    valueToIndexMap.put(ck, i);
		    this.coinKinds[i++] = ck.intValue();
		}
	
		popKindNames = new String[buttonCount];
		for(int j = 0; j < popKindNames.length; j++)
		    popKindNames[j] = "";
	
		popKindCosts = new int[buttonCount];
		pops = new HashMap<>();
    }

    public void configure(List<String> names, List<Integer> costs) {
		if(names == null || names.size() != popKindNames.length)
		    throw new IllegalArgumentException("The names list must be of identical size as the number of selection buttons");
	
		if(costs == null || costs.size() != popKindCosts.length)
		    throw new IllegalArgumentException("The costs list must be of identical size as the number of selection buttons");
	
		int i = 0;
		for(String name : names) {
		    if(name.equals("\"\""))
			throw new IllegalArgumentException("Names cannot have zero length");
		    popKindNames[i++] = name;
		}
	
		i = 0;
		for(Integer cost : costs)
		    popKindCosts[i++] = cost.intValue();
    }

    public void load(List<Integer> coinCounts, List<Integer> popCounts) {
		if(coinCounts == null || popCounts == null || coinCounts.size() < 0 || popCounts.size() < 0)
		    throw new IllegalArgumentException("Neither list may be empty");
	
		int ck = 0;
		for(Integer coinCount : coinCounts) {
		    ArrayList<Coin> coinList = coins.get(ck);
		    if(coinList == null) {
				coinList = new ArrayList<>();
				coins.put(ck, coinList);
		    }
	
		    for(int i = 0; i < coinCount; i++)
		    	coinList.add(new Coin(coinKinds[ck]));
	
		    ck++;
		}

		int pk = 0;
		for(Integer popCount : popCounts) {
		    ArrayList<Pop> popList = pops.get(pk);
		    if(popList == null) {
				popList = new ArrayList<>();
				pops.put(pk, popList);
		    }
		    for(int i = 0; i < popCount; i++)
		    	popList.add(new Pop(popKindNames[pk]));
	
		    pk++;
		}
    }

    public void select(int button) {
		if(button < 0 || button >= popKindNames.length)
		    throw new IllegalArgumentException("The button number must be between 0 and " + (popKindNames.length - 1));
	
		if(popKindCosts[button] <= valueOfEnteredCoins) {
		    ArrayList<Pop> popList = pops.get(button);
		    if(popList != null && !popList.isEmpty()) {
				deliveryChute.add(popList.remove(0));
				valueOfEnteredCoins = deliverChange(popKindCosts[button], valueOfEnteredCoins);
				paymentCoins.addAll(individualCoinsEntered);
				individualCoinsEntered.clear(); // coinsValue may be > 0
		    }
		    else {
			// do nothing
		    }
		}
		else {
		    // do nothing
		}
    }

    private Map<Integer, List<Integer>> changeHelper(ArrayList<Integer> values, int index, int changeDue) {
		if(index >= values.size())
		    return null;
	
		int value = values.get(index);
		Integer ck = valueToIndexMap.get(value);
		ArrayList<Coin> coinList = coins.get(ck);
	
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
		    int intTotal = total;
	
		    for(int count = coinList.size(); count >= 0; count--) {
				int newTotal = count * value + intTotal;
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

    private int deliverChange(int cost, int entered) {
		int changeDue = entered - cost;
	
		if(changeDue < 0)
		    throw new InternalError("Cost was greater than entered, which should not happen");
	
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
		    List<Coin> coinList = coins.get(ck);
		    Coin c = coinList.remove(0);
		    deliveryChute.add(c.getValue());
		    changeDue -= coinKinds[ck];
		}
	
		return changeDue;
    }

    public void insert(Coin coin) {
		if(coin != null) {
		    int value = coin.getValue();
		    if(valueToIndexMap.get(value) == null)
		    	deliveryChute.add(coin);
		    else {
				individualCoinsEntered.add(coin);
				valueOfEnteredCoins += coin.getValue();
		    }
		}
		else
		    throw new NullPointerException();
    }

    public List<Object> unload() {
		ArrayList<Object> list = new ArrayList<>();
		int unusedCoins = 0;
		int paymentCoins = 0;
	
		for(Integer ck : this.coins.keySet()) {
		    ArrayList<Coin> coins = this.coins.get(ck);
		    for(Coin c : coins)
			unusedCoins += c.getValue();
		    coins.clear();
		}
		list.add(unusedCoins);
	
		for(Coin c : this.paymentCoins)
		    paymentCoins += c.getValue();
		this.paymentCoins.clear();
	
		for(Coin c : this.individualCoinsEntered)
		    paymentCoins += c.getValue();
		this.individualCoinsEntered.clear();
	
		list.add(paymentCoins);
	
		for(Integer pk : this.pops.keySet()) {
		    ArrayList<Pop> pops = this.pops.get(pk);
		    list.addAll(pops);
		    pops.clear();
		}
	
		return list;
    }

    public List<Object> extract() {
		ArrayList<Object> res = new ArrayList<>();
		for(Object obj : deliveryChute)
		    if(obj instanceof Coin)
		    	res.add(((Coin)obj).getValue());
		    else
		    	res.add(obj);
	
		deliveryChute.clear();
		return res;
    }
}
