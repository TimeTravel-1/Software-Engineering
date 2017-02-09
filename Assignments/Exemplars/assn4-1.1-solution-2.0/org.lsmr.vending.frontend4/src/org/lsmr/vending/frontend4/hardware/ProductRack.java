package org.lsmr.vending.frontend4.hardware;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.lsmr.vending.frontend4.Product;

/**
 * Represents a storage rack for products within the vending machine. More than
 * one would typically exist within the same vending machine. The product rack
 * has finite, positive capacity. A product rack can be disabled, which prevents
 * it from dispensing products.
 */
public final class ProductRack extends AbstractHardware<ProductRackListener> implements AbstractProductAcceptor {
    private int maxCapacity;
    private Queue<Product> queue = new LinkedList<Product>();
    private ProductChannel sink;

    /**
     * Creates a new product rack with the indicated maximum capacity. The
     * product rack initially is empty.
     * 
     * @param capacity
     *            Positive integer indicating the maximum capacity of the rack.
     * @throws SimulationException
     *             if the indicated capacity is not positive.
     */
    public ProductRack(int capacity) {
	if(capacity <= 0)
	    throw new SimulationException("Capacity cannot be non-positive: " + capacity);

	this.maxCapacity = capacity;
    }

    /**
     * The current number of products stored.
     * 
     * @return The current count. Will be non-negative.
     */
    public int size() {
	return queue.size();
    }

    /**
     * Returns the maximum capacity of this product rack. Causes no events.
     * 
     * @return The maximum number of items that this device can store.
     */
    public int getCapacity() {
	return maxCapacity;
    }

    /**
     * Connects the product rack to an outlet channel, such as the delivery
     * chute. Causes no events.
     * 
     * @param sink
     *            The channel to be used as the outlet for dispensed products.
     */
    public void connect(ProductChannel sink) {
	this.sink = sink;
    }

    /**
     * Adds the indicated product to this product rack if there is sufficient
     * space available. If the product is successfully added to this product
     * rack, a "productAdded" event is announced to its listeners. If, as a result
     * of adding this product, this product rack has become full, a "productsFull"
     * event is announced to its listeners.
     * 
     * @param product
     *            The product to be added.
     * @throws CapacityExceededException
     *             if the product rack is already full.
     * @throws DisabledException
     *             if the product rack is currently disabled.
     */
    @Override
    public void acceptProduct(Product product) throws CapacityExceededException, DisabledException {
	if(isDisabled())
	    throw new DisabledException();

	if(queue.size() >= maxCapacity)
	    throw new CapacityExceededException();

	queue.add(product);

	notifyProductAdded(product);

	if(queue.size() >= maxCapacity)
	    notifyProductsFull();
    }

    /**
     * Causes one product to be removed from this product rack, to be placed in
     * the output channel to which this product rack is connected. If a product
     * is removed from this product rack, a "productRemoved" event is announced to
     * its listeners. If the removal of the product causes this product rack to
     * become empty, a "productsEmpty" event is announced to its listeners.
     * 
     * @throws DisabledException
     *             if this product rack is currently disabled.
     * @throws EmptyException
     *             if no products are currently contained in this product rack.
     * @throws CapacityExceededException
     *             if the output channel cannot accept the dispensed product.
     */
    public void dispenseProduct() throws DisabledException, EmptyException, CapacityExceededException {
	if(isDisabled())
	    throw new DisabledException();

	if(queue.isEmpty())
	    throw new EmptyException();

	Product product = queue.remove();
	notifyProductRemoved(product);

	if(sink == null)
	    throw new SimulationException("The output channel is not connected");

	sink.acceptProduct(product);

	if(queue.isEmpty())
	    notifyProductEmpty();
    }

    /**
     * Allows products to be loaded into the product rack, to simulate direct,
     * physical loading. Note that any existing products in the rack are not
     * removed. Causes a "productsLoaded" event to be announced.
     * 
     * @param products
     *            One or more products to be loaded into this product rack.
     * @throws SimulationException
     *             If the number of cans to be loaded exceeds the capacity of
     *             this product rack.
     */
    public void load(Product... products) throws SimulationException {
	if(maxCapacity < queue.size() + products.length)
	    throw new SimulationException("Capacity exceeded by attempt to load");

	for(Product product : products)
	    queue.add(product);

	notifyLoad(products);
    }

    private void notifyLoad(Product[] products) {
	for(ProductRackListener listener : listeners)
	    listener.productsLoaded(this, products);
    }

    /**
     * Unloads products from the rack, to simulate direct, physical unloading.
     * Causes a "productsUnloaded" event to be announced.
     * 
     * @return A list of the items unloaded.
     */
    public List<Product> unload() {
	List<Product> result = new ArrayList<>(queue);
	queue.clear();
	notifyUnload(result.toArray(new Product[result.size()]));
	return result;
    }

    private void notifyUnload(Product[] products) {
	for(ProductRackListener listener : listeners)
	    listener.productsUnloaded(this, products);
    }

    private void notifyProductAdded(Product product) {
	for(ProductRackListener listener : listeners)
	    listener.productAdded(this, product);
    }

    private void notifyProductsFull() {
	for(ProductRackListener listener : listeners)
	    listener.productsFull(this);
    }

    private void notifyProductEmpty() {
	for(ProductRackListener listener : listeners)
	    listener.productsEmpty(this);
    }

    private void notifyProductRemoved(Product product) {
	for(ProductRackListener listener : listeners)
	    listener.productRemoved(this, product);
    }

    @Override
    public boolean hasSpace() {
	return size() < getCapacity();
    }
}
