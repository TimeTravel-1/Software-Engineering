package seng301.assn4.product;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lsmr.vending.frontend4.Product;
import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.AbstractHardware;
import org.lsmr.vending.frontend4.hardware.AbstractHardwareListener;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;
import org.lsmr.vending.frontend4.hardware.ProductRack;
import org.lsmr.vending.frontend4.hardware.ProductRackListener;

/**
 * Controls all interaction with products of a given kind.
 * 
 * @author Robert J. Walker
 */
public class ProductController {
    private final Set<ProductControllerListener> listeners = new HashSet<>();
    private int available = 0;
    private final int index;
    private final HardwareFacade hf;

    private class PRL implements ProductRackListener {
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    // ignore
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    // ignore
	}

	@Override
	public void productAdded(ProductRack productRack, Product product) {
	    available++;
	    notifyProductsAdded(1);
	}

	@Override
	public void productRemoved(ProductRack productRack, Product product) {
	    available--;
	    notifyProductsRemoved(1);
	}

	@Override
	public void productsFull(ProductRack productRack) {
	    // ignore
	}

	@Override
	public void productsEmpty(ProductRack productRack) {
	    // ignore
	}

	@Override
	public void productsLoaded(ProductRack rack, Product... products) {
	    available += products.length;
	    notifyProductsAdded(products.length);
	}

	@Override
	public void productsUnloaded(ProductRack rack, Product... products) {
	    available -= products.length;
	    notifyProductsRemoved(products.length);
	}
    }

    /**
     * Basic constructor.
     * 
     * @param hf
     *            The hardware facade that this controller will use to perform
     *            low-level functions.
     * @param index
     *            The index at which the controlled product will reside.
     */
    public ProductController(HardwareFacade hf, int index) {
	if(hf == null)
	    throw new IllegalArgumentException("The argument cannot be null");
	if(index < 0)
	    throw new IllegalArgumentException("The index cannot be negative");

	this.hf = hf;
	this.index = index;
	hf.getProductRack(index).register(new PRL());
    }

    /**
     * Registers the given listener with this controller so that the listener
     * will be notified of events emanating from here.
     * 
     * @param listener
     *            The listener to be registered. No effect if it is already
     *            registered. Cannot be null.
     */
    public void register(ProductControllerListener listener) {
	listeners.add(listener);
    }

    /**
     * De-registers the given listener from this controller so that the listener
     * will no longer be notified of events emanating from here.
     * 
     * @param listener
     *            The listener to be de-registered. No effect if it is not
     *            already registered or is null.
     */
    public void deregister(ProductControllerListener listener) {
	listeners.remove(listener);
    }

    /**
     * Determines whether the controller has free space for additional products.
     * 
     * @return true if there is free space for at least one additional product,
     *         else false.
     */
    public boolean hasFreeSpace() {
	return hf.getProductRack(index).hasSpace();
    }

    /**
     * Loads the sequence of products into this controller. Produces zero or
     * more &quot:productAdded&quot; events whose total count arguments equal
     * the number of added products.
     * 
     * @param products
     *            The sequence of products to be loaded. Can be empty. None of
     *            these can be null.
     * @throws CapacityExceededException
     *             If there is insufficient free space to load all the products.
     */
    public void load(Product... products) throws CapacityExceededException {
	for(Product p : products)
	    hf.getProductRack(index).load(p);
    }

    /**
     * Removes all the products from this controller. Produces zero or more
     * &quot;productRemoved&quot; events whose total count arguments equal the
     * number of removed products.
     * 
     * @return A list of the unloaded products. Cannot be null. Can be empty.
     */
    public List<Product> unload() {
	return hf.getProductRack(index).unload();
    }

    /**
     * Accesses the product kind for this controller.
     * 
     * @return The product kind.
     */
    public ProductKind getProductKind() {
	return hf.getProductKind(index);
    }

    /**
     * Accesses the count of available products.
     * 
     * @return The count of available products.
     */
    public int getQuantityAvailable() {
	return available;
    }

    /**
     * Dispenses one product from this controller.
     * 
     * @throws DisabledException
     *             If the hardware needed for dispensing is disabled.
     * @throws EmptyException
     *             If no products are available.
     * @throws CapacityExceededException
     *             If the delivery is impaired by insufficient capacity.
     */
    public void dispense() throws DisabledException, EmptyException, CapacityExceededException {
	hf.getProductRack(index).dispenseProduct();
    }

    private void notifyProductsRemoved(int count) {
	for(ProductControllerListener l : listeners)
	    l.productsRemoved(this, count);
    }

    private void notifyProductsAdded(int count) {
	for(ProductControllerListener l : listeners)
	    l.productsAdded(this, count);
    }
}
