package org.lsmr.vending.frontend4.hardware;

import org.lsmr.vending.frontend4.Product;

/**
 * Represents the org.lsmr.vending.frontend4.hardware through which a product is
 * carried from one device to another. Once the
 * org.lsmr.vending.frontend4.hardware is configured, product channels will not
 * be used directly by other applications.
 */
public final class ProductChannel {
    private AbstractProductAcceptor sink;

    /**
     * Creates a new product channel whose output will go to the indicated sink.
     * 
     * @param sink
     *            The output of the channel. Can be null, which disconnects any
     *            current output device.
     */
    public ProductChannel(AbstractProductAcceptor sink) {
	this.sink = sink;
    }

    /**
     * This method should only be called from hardware devices.
     * 
     * @param product
     *            The product to be accepted. Cannot be null.
     * @throws CapacityExceededException
     *             If the output sink cannot accept the product.
     * @throws DisabledException
     *             If the output sink is currently disabled.
     */
    void acceptProduct(Product product) throws CapacityExceededException, DisabledException {
	sink.acceptProduct(product);
    }
}
