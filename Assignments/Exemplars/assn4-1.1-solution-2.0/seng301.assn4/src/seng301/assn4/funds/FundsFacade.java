package seng301.assn4.funds;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.Coin;
import org.lsmr.vending.frontend4.hardware.AbstractHardware;
import org.lsmr.vending.frontend4.hardware.AbstractHardwareListener;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.CoinRack;
import org.lsmr.vending.frontend4.hardware.CoinSlot;
import org.lsmr.vending.frontend4.hardware.CoinSlotListener;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;

/**
 * Controls all interaction with funds.
 * 
 * @author Robert J. Walker
 */
public class FundsFacade {
    private Cents available = new Cents(0);
    private Cents storedCredit = new Cents(0);
    private final HardwareFacade hf;
    private final Set<FundsListener> listeners = new HashSet<>();

    private class CSL implements CoinSlotListener {
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    // ignore
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    // ignore
	}

	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
	    available.add(coin.getValue());
	}

	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
	    // ignore
	}
    }

    /**
     * Basic constructor.
     * 
     * @param hf
     *            The hardware facade that will be used to implement all
     *            low-level functions.
     */
    public FundsFacade(HardwareFacade hf) {
	if(hf == null)
	    throw new IllegalArgumentException("The argument cannot be null");

	this.hf = hf;
	hf.getCoinSlot().register(new CSL());
    }

    /**
     * Registers the given listener with this facade so that the listener will
     * be notified of events emanating from here.
     * 
     * @param listener
     *            The listener to be registered. No effect if it is already
     *            registered. Cannot be null.
     */
    public void register(FundsListener listener) {
	listeners.add(listener);
    }

    /**
     * De-registers the given listener from this facade so that the listener
     * will no longer be notified of events emanating from here.
     * 
     * @param listener
     *            The listener to be de-registered. No effect if it is not
     *            already registered or null.
     */
    public void deregister(FundsListener listener) {
	listeners.remove(listener);
    }

    /**
     * Accesses the total amount of funds that can possibly be used. In some
     * circumstances this is not a good idea (like with credit cards) so it is
     * deprecated.
     * 
     * @deprecated The total amount of funds will not be a simple calculation in
     *             all circumstances. Use {@link #getAvailableFunds()} instead.
     *             The method will not be removed any time soon.
     * @return The total funds available for use.
     */
    public Cents getAvailableFunds() {
	Cents total = new Cents(0);
	total.add(available);
	total.add(storedCredit);
	return total;
    }

    /**
     * Checks to see if the available funds in the machine are at least equal to
     * the indicated amount.
     * 
     * @param funds
     *            The amount to check for. To be meaningful, this should really
     *            be a positive amount.
     * @return true if the indicated amount is available; otherwise false.
     */
    public boolean hasAvailableFunds(Cents funds) {
	return (available.compareTo(new Cents(0)) > 0) || (storedCredit.compareTo(new Cents(0)) > 0);
    }

    /**
     * Stores the indicated amount of funds so that it will no longer be
     * available for use. The remainder will continue to be available for use.
     * 
     * @param funds
     *            The amount to be stored.
     * @throws CapacityExceededException
     *             If storing this amount will cause the capacity of some
     *             hardware to be exceeded.
     * @throws DisabledException
     *             If storing this amount will cause the use of a disabled piece
     *             of hardware.
     */
    public void storeFunds(Cents funds) throws CapacityExceededException, DisabledException {
	if(!hasAvailableFunds(funds))
	    throw new InternalError("The funds to be stored is more than the funds available, which should not happen");

	if(funds.compareTo(storedCredit) <= 0)
	    storedCredit.subtract(funds);
	else {
	    funds.subtract(storedCredit);
	    storedCredit = new Cents(0);
	    // funds now contains the amount to be stored

	    if(funds.compareTo(available) <= 0) {
		hf.getCoinReceptacle().storeCoins();

		storedCredit = available.subtract(funds);
		available = new Cents(0);
	    }
	}
    }

    /**
     * Returns as much of the unused funds as is possible. Any excess amount
     * that cannot be returned will continue to be available as a credit.
     * 
     * @throws CapacityExceededException
     *             If returning this amount will cause the capacity of some
     *             hardware to be exceeded.
     * @throws DisabledException
     *             If returning this amount will cause the use of a disabled
     *             piece of hardware.
     */
    public void returnUnusedFunds() throws CapacityExceededException, DisabledException {
	try {
	    hf.getCoinReceptacle().returnCoins();
	    storedCredit = new Cents(deliverChange(storedCredit.getValue()));
	}
	catch(EmptyException e) {
	    throw new InternalError(e);
	}
    }

    private int deliverChange(int changeDue) throws CapacityExceededException, EmptyException, DisabledException {
	if(changeDue < 0)
	    throw new InternalError("Change due is negative, which should not happen");

	ArrayList<Cents> values = new ArrayList<>();
	for(int i = 0, count = hf.getNumberOfCoinRacks(); i < count; i++) {
	    Cents ck = hf.getCoinKind(i);
	    values.add(ck);
	}

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
	    CoinRack cr = hf.getCoinRack(ck);
	    cr.releaseCoin();
	    changeDue -= hf.getCoinKindForCoinRack(ck).getValue();
	}

	return changeDue;
    }

    private Map<Integer, List<Integer>> changeHelper(ArrayList<Cents> values, int index, int changeDue) {
	if(index >= values.size())
	    return null;

	Cents value = values.get(index);
	int ck = 0;
	for(int count = hf.getNumberOfCoinRacks(); ck < count; ck++)
	    if(hf.getCoinKind(ck).compareTo(value) == 0)
		break;

	CoinRack cr = hf.getCoinRack(ck);
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
		int newTotal = count * value.getValue() + total;
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
}
