package org.lsmr.vending.frontend4.hardware;

import org.lsmr.vending.frontend4.Product;

/**
 * A simple interface to allow a device to communicate with another device that
 * accepts products.
 */
public interface AbstractProductAcceptor {
    /**
     * Instructs the device to take the product as input.
     * 
     * @param product
     *            The product to be taken as input. Cannot be null.
     * @throws CapacityExceededException
     *             If the device does not have enough space for the product.
     * @throws DisabledException
     *             If the device is currently disabled.
     */
    public void acceptProduct(Product product) throws CapacityExceededException, DisabledException;

    /**
     * Checks whether the device has enough space to expect one more item. If
     * this method returns true, an immediate call to acceptCoin should not
     * throw CapacityExceededException, unless an asynchronous addition has
     * occurred in the meantime.
     * 
     * @return true if there is space, false if there is not space
     */
    boolean hasSpace();
}
