package seng301.assn4.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lsmr.vending.frontend4.Product;
import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;

/**
 * Controls all interaction with all products.
 * 
 * @author Robert J. Walker
 */
public class ProductFacade {
    private final ProductController[] controllers;
    private final Set<ProductFacadeListener> listeners = new HashSet<>();

    private class PCL implements ProductControllerListener {
	@Override
	public void productsRemoved(ProductController productController, int count) {
	    notifyProductsRemoved(productController.getProductKind(), count);
	}

	@Override
	public void productsAdded(ProductController productController, int count) {
	    notifyProductsAdded(productController.getProductKind(), count);
	}
    }

    /**
     * Basic constructor.
     * 
     * @param controllers
     *            Sequence of individual controllers for specific products. A
     *            given product kind can be shared between controllers.
     *            Controllers are assumed to possess a unique index.
     */
    public ProductFacade(ProductController... controllers) {
	PCL listener = new PCL();
	this.controllers = controllers;
	for(int i = 0, count = controllers.length; i < count; i++)
	    controllers[i].register(listener);
    }

    /**
     * Registers the given listener with this facade so that the listener will
     * be notified of events emanating from here.
     * 
     * @param listener
     *            The listener to be registered. No effect if it is already
     *            registered. Cannot be null.
     */
    public void register(ProductFacadeListener listener) {
	listeners.add(listener);
    }

    /**
     * De-registers the given listener from this facade so that the listener
     * will no longer be notified of events emanating from here.
     * 
     * @param listener
     *            The listener to be de-registered. No effect if it is not
     *            already registered or is null.
     */
    public void deregister(ProductFacadeListener listener) {
	listeners.remove(listener);
    }

    /**
     * Dispenses one product from this facade.
     * 
     * @param kind
     *            The kind of the product to dispense. Cannot be null.
     * @throws DisabledException
     *             If the hardware needed for dispensing is disabled.
     * @throws EmptyException
     *             If no products are available.
     * @throws CapacityExceededException
     *             If the delivery is impaired by insufficient capacity.
     */
    public void dispense(ProductKind kind) throws EmptyException, DisabledException, CapacityExceededException {
	for(int i = 0, count = controllers.length; i < count; i++) {
	    if(controllers[i].getProductKind().equals(kind) && controllers[i].getQuantityAvailable() > 0) {
		controllers[i].dispense();
		return;
	    }
	}

	throw new EmptyException();
    }

    /**
     * Loads the sequence of products into this facade. Produces zero or more
     * &quot:productAdded&quot; events whose total count arguments equal the
     * number of added products.
     * 
     * @param products
     *            The sequence of products to be loaded. Can be empty. None of
     *            these can be null.
     * @throws CapacityExceededException
     *             If there is insufficient free space to load all the products.
     */
    public void load(Product... products) throws CapacityExceededException {
	NEXT_PRODUCT: for(Product p : products) {
	    for(int i = 0, count = controllers.length; i < count; i++) {
		if(controllers[i].getProductKind().getName().equals(p.getName()) && controllers[i].hasFreeSpace()) {
		    controllers[i].load(p);
		    continue NEXT_PRODUCT;
		}
	    }
	    throw new CapacityExceededException();
	}
    }

    /**
     * Removes all the products from this facade. Produces zero or more
     * &quot;productRemoved&quot; events whose total count arguments equal the
     * number of removed products.
     * 
     * @return A list of the unloaded products. Cannot be null. Can be empty.
     */
    public List<Product> unloadAll() {
	List<Product> list = new ArrayList<>();

	for(ProductController pc : controllers)
	    list.addAll(pc.unload());

	return list;
    }

    /**
     * Removes all the products of a specified kind from this controller.
     * Produces zero or more &quot;productRemoved&quot; events whose total count
     * arguments equal the number of removed products.
     * 
     * @param kind
     *            The kind of the products to unload. Cannot be null.
     * @return A list of the unloaded products. Cannot be null. Can be empty.
     */
    public List<Product> unload(ProductKind kind) {
	List<Product> list = new ArrayList<>();

	for(ProductController pc : controllers)
	    if(pc.getProductKind().equals(kind))
		list.addAll(pc.unload());

	return list;
    }

    private void notifyProductsRemoved(ProductKind kind, int count) {
	for(ProductFacadeListener l : listeners)
	    l.productsRemoved(this, kind, count);
    }

    private void notifyProductsAdded(ProductKind kind, int count) {
	for(ProductFacadeListener l : listeners)
	    l.productsAdded(this, kind, count);
    }

    /**
     * Accesses the controller at the specified index.
     * 
     * @param index
     *            &gt;= 0 && &lt; the total number of controllers.
     * @return The controller at the specified index.
     */
    public ProductController getController(int index) {
	return controllers[index];
    }
}
