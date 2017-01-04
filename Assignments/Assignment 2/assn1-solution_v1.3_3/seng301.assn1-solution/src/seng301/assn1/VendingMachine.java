package seng301.assn1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lsmr.vending.frontend1.Coin;
import org.lsmr.vending.frontend1.Deliverable;
import org.lsmr.vending.frontend1.Pop;

/**
 * Represents vending machines.
 * 
 * @author Robert J. Walker
 */
public class VendingMachine {
    /**
     * Used to represent the physical delivery chute. Objects added here will be
     * removed when extracted.
     */
    private ArrayList<Deliverable> deliveryChute = new ArrayList<>();

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

    /**
     * Basic constructor.
     * 
     * @param coinKinds
     *            List of the legal coin kinds. Cannot be null. Each value must
     *            be unique and greater than 0.
     * @param buttonCount
     *            The number of selection buttons that the VM should have. Must
     *            be greater than 0.
     * @throws IllegalArgumentException
     *             If the above requirements are violated.
     */
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

    /**
     * Permits the pop brand names and costs to be configured.
     * 
     * @param names
     *            List of the brand names. Cannot be null. No name can be empty.
     * @param costs
     *            List of the costs. Cannot be null. No cost can be 0 or less.
     * @throws IllegalArgumentException
     *             If any of the above requirements is violated.
     */
    public void configure(List<String> names, List<Integer> costs) {
	if(names == null || names.size() != popKindNames.length)
	    throw new IllegalArgumentException("The names list must be of identical size as the number of selection buttons");

	if(costs == null || costs.size() != popKindCosts.length)
	    throw new IllegalArgumentException("The costs list must be of identical size as the number of selection buttons");

	int i = 0;
	for(String name : names)
	    if(name.equals("\"\""))
		throw new IllegalArgumentException("Names cannot have zero length");

	i = 0;
	for(int cost : costs)
	    if(cost < 1)
		throw new IllegalArgumentException("Costs must be > 0");

	i = 0;
	for(String name : names)
	    popKindNames[i++] = name;

	i = 0;
	for(int cost : costs)
	    popKindCosts[i++] = cost;
    }

    /**
     * Loads the indicated coins into the indicated position.
     * 
     * @param coinKindIndex
     *            The position to load the coins. Cannot be negative. Cannot be
     *            greater than or equal to the number of coin kinds.
     * @param coins
     *            The sequence of coins to be added. Cannot be null.
     * @throws IllegalArgumentException
     *             If any of the above conditions is violated.
     */
    public void loadCoins(int coinKindIndex, Coin... coins) {
	if(coins == null)
	    throw new IllegalArgumentException("The list cannot be null");

	if(coinKindIndex < 0 || coinKindIndex >= this.coinKinds.length)
	    throw new IllegalArgumentException("The coin kind index must be >= 0 and < number of coin kinds");

	ArrayList<Coin> coinList = this.coins.get(coinKindIndex);
	if(coinList == null) {
	    coinList = new ArrayList<>();
	    this.coins.put(coinKindIndex, coinList);
	}

	for(Coin coin : coins)
	    coinList.add(coin);
    }

    /**
     * Loads the specified pops into the indicated position.
     * 
     * @param popKindIndex
     *            The position to load the pops. Cannot be negative. Cannot be
     *            greater than or equal to the number of pop kinds.
     * @param pops
     *            The sequence of pops to be added. Cannot be null.
     * @throws IllegalArgumentException
     *             If any of the above conditions is violated.
     */
    public void loadPops(int popKindIndex, Pop... pops) {
	if(pops == null)
	    throw new IllegalArgumentException("The list cannot be empty");

	if(popKindIndex < 0 || popKindIndex >= this.popKindNames.length)
	    throw new IllegalArgumentException("The pop kind index must be >= 0 and < number of coin kinds");

	ArrayList<Pop> popList = this.pops.get(popKindIndex);
	if(popList == null) {
	    popList = new ArrayList<>();
	    this.pops.put(popKindIndex, popList);
	}

	for(Pop pop : pops)
	    popList.add(pop);
    }

    /**
     * Presses the selection button at the indicated position.
     * 
     * @param selectionButtonIndex
     *            The index of the selection button that is to be pressed.
     * @throws IllegalArgumentException
     *             If the index is &lt; 0 or the index is &gt;= the number of
     *             selection buttons.
     */
    public void press(int selectionButtonIndex) {
	if(selectionButtonIndex < 0 || selectionButtonIndex >= popKindNames.length)
	    throw new IllegalArgumentException("The button number must be between 0 and " + (popKindNames.length - 1));

	if(popKindCosts[selectionButtonIndex] <= valueOfEnteredCoins) {
	    ArrayList<Pop> popList = pops.get(selectionButtonIndex);
	    if(popList != null && !popList.isEmpty()) {
		deliveryChute.add(popList.remove(0));
		valueOfEnteredCoins = deliverChange(popKindCosts[selectionButtonIndex], valueOfEnteredCoins);
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
	    deliveryChute.add(c);
	    changeDue -= coinKinds[ck];
	}

	return changeDue;
    }

    /**
     * Inserts the indicated coin into the vending machine.
     * 
     * @param coin
     *            The coin to insert. Cannot be null.
     * @throws IllegalArgumentException
     *             If the argument is null.
     */
    public void insert(Coin coin) {
	if(coin == null)
	    throw new IllegalArgumentException();

	int value = coin.getValue();
	if(valueToIndexMap.get(value) == null)
	    deliveryChute.add(coin);
	else {
	    individualCoinsEntered.add(coin);
	    valueOfEnteredCoins += coin.getValue();
	}
    }

    /**
     * Unloads unused change coins, payment coins, and unsold pops.
     * 
     * @return A list of the above, in that order.
     */
    public List<List<?>> unload() {
	ArrayList<List<?>> list = new ArrayList<>();
	ArrayList<Object> unusedCoins = new ArrayList<>();
	ArrayList<Object> paymentCoins = new ArrayList<>();
	ArrayList<Object> unsoldPop = new ArrayList<>();
	list.add(unusedCoins);
	list.add(paymentCoins);
	list.add(unsoldPop);

	for(Integer ck : this.coins.keySet()) {
	    ArrayList<Coin> coins = this.coins.get(ck);
	    unusedCoins.addAll(coins);
	    coins.clear();
	}
	list.add(unusedCoins);

	paymentCoins.addAll(this.paymentCoins);
	this.paymentCoins.clear();

	// My mistake: The specification of CHECK_TEARDOWN implies that this
	// code shouldn't be here.
	// paymentCoins.addAll(this.individualCoinsEntered);
	// this.individualCoinsEntered.clear();

	for(Integer pk : this.pops.keySet()) {
	    ArrayList<Pop> pops = this.pops.get(pk);
	    unsoldPop.addAll(pops);
	    pops.clear();
	}

	return list;
    }

    /**
     * Extracts all items from the delivery chute and returns them.
     * 
     * @return A list of the items formerly in the delivery chute.
     */
    public List<Deliverable> extract() {
	ArrayList<Deliverable> res = new ArrayList<>();
	res.addAll(deliveryChute);
	deliveryChute.clear();
	return res;
    }
}
