package org.lsmr.vending.frontend4.hardware;

import java.util.ArrayList;

import org.lsmr.vending.frontend4.Coin;
import org.lsmr.vending.frontend4.Deliverable;
import org.lsmr.vending.frontend4.Product;

/**
 * Represents a simple delivery chute device. The delivery chute has a finite
 * capacity of objects (products or coins) that it can hold. This is obviously
 * not a realistic element of the simulation, but sufficient here.
 */
public final class DeliveryChute extends AbstractHardware<DeliveryChuteListener> implements AbstractCoinAcceptor, AbstractProductAcceptor {
    private ArrayList<Deliverable> chute = new ArrayList<Deliverable>();
    private int maxCapacity;

    /**
     * Creates a delivery cute with the indicated maximum capacity of products
     * and/or coins.
     * 
     * @param capacity
     *            The maximum number of items that the delivery chute can
     *            contain. Must be positive.
     * @throws SimulationException
     *             If the capacity is not a positive integer.
     */
    public DeliveryChute(int capacity) {
	if(capacity <= 0)
	    throw new SimulationException("Capacity must be a positive value: " + capacity);

	this.maxCapacity = capacity;
    }

    /**
     * Accesses the current number of items in the chute.
     * 
     * @return The current number of items. Cannot be negative.
     */
    public int size() {
	return chute.size();
    }

    /**
     * Returns the maximum capacity of this delivery chute in number of products
     * and/or coins that it can hold. Causes no events.
     * 
     * @return The maximum number of items that can be in the chute. Cannot be
     *         negative.
     */
    public int getCapacity() {
	return maxCapacity;
    }

    /**
     * Tells this delivery chute to deliver the indicated product. If the
     * delivery is successful, an "itemDelivered" event is announced to its
     * listeners. If the successful delivery causes the chute to become full, a
     * "chuteFull" event is announced to its listeners.
     * 
     * @throws CapacityExceededException
     *             If the chute is already full and the product cannot be
     *             delivered.
     * @throws DisabledException
     *             If the chute is currently disabled.
     */
    @Override
    public void acceptProduct(Product product) throws CapacityExceededException, DisabledException {
	if(isDisabled())
	    throw new DisabledException();

	if(chute.size() >= maxCapacity)
	    throw new CapacityExceededException();

	chute.add(product);

	notifyItemDelivered();

	if(chute.size() >= maxCapacity)
	    notifyChuteFull();
    }

    /**
     * Tells this delivery chute to deliver the indicated coin. If the delivery
     * is successful, an "itemDelivered" event is announced to its listeners. If
     * the successful delivery causes the chute to become full, a "chuteFull"
     * event is announced to its listeners.
     * 
     * @throws CapacityExceededException
     *             if the chute is already full and the coin cannot be
     *             delivered.
     * @throws DisabledException
     *             if the chute is currently disabled.
     */
    @Override
    public void acceptCoin(Coin coin) throws CapacityExceededException, DisabledException {
	if(isDisabled())
	    throw new DisabledException();

	if(chute.size() >= maxCapacity)
	    throw new CapacityExceededException();

	chute.add(coin);

	notifyItemDelivered();

	if(chute.size() >= maxCapacity)
	    notifyChuteFull();
    }

    /**
     * Simulates the opening of the door of the delivery chute and the removal
     * of all items therein. Announces a "doorOpened" event to its listeners
     * before the items are removed, and a "doorClosed" event after the items
     * are removed. Disabling the chute does not prevent this.
     * 
     * @return The items that were in the delivery chute.
     */
    public Deliverable[] removeItems() {
	notifyDoorOpened();
	Deliverable[] items = new Deliverable[chute.size()];
	chute.toArray(items);
	chute.clear();
	notifyDoorClosed();
	return items;
    }

    /**
     * Determines whether this delivery chute has space for at least one more
     * item. Causes no events.
     */
    @Override
    public boolean hasSpace() {
	return chute.size() < maxCapacity;
    }

    private void notifyItemDelivered() {
	for(DeliveryChuteListener listener : listeners)
	    listener.itemDelivered(this);
    }

    private void notifyDoorOpened() {
	for(DeliveryChuteListener listener : listeners)
	    listener.doorOpened(this);
    }

    private void notifyDoorClosed() {
	for(DeliveryChuteListener listener : listeners)
	    listener.doorClosed(this);
    }

    private void notifyChuteFull() {
	for(DeliveryChuteListener listener : listeners)
	    listener.chuteFull(this);
    }
}
