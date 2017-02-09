package org.lsmr.vending.frontend4.hardware;

import org.lsmr.vending.frontend4.Product;

/**
 * Listens for events emanating from a product rack.
 */
public interface ProductRackListener extends AbstractHardwareListener {
    /**
     * An event announced when the indicated product is added to the indicated
     * product rack.
     * 
     * @param productRack
     *            The device on which the event occurred.
     * @param product
     *            The product added.
     */
    void productAdded(ProductRack productRack, Product product);

    /**
     * An event announced when the indicated product is removed from the
     * indicated product rack.
     * 
     * @param productRack
     *            The device on which the event occurred.
     * @param product
     *            The product removed.
     */
    void productRemoved(ProductRack productRack, Product product);

    /**
     * An event announced when the indicated product rack becomes full.
     * 
     * @param productRack
     *            The device on which the event occurred.
     */
    void productsFull(ProductRack productRack);

    /**
     * An event announced when the indicated product rack becomes empty.
     * 
     * @param productRack
     *            The device on which the event occurred.
     */
    void productsEmpty(ProductRack productRack);

    /**
     * Announces that the indicated sequence of products has been added to the
     * indicated rack. Used to simulate direct, physical loading of
     * the rack.
     * 
     * @param rack
     *            The rack where the event occurred.
     * @param products
     *            The products that were loaded.
     */
    void productsLoaded(ProductRack rack, Product... products);

    /**
     * Announces that the indicated sequence of products has been removed to the
     * indicated product rack. Used to simulate direct, physical unloading of
     * the rack.
     * 
     * @param rack
     *            The rack where the event occurred.
     * @param products
     *            The products that were unloaded.
     */
    void productsUnloaded(ProductRack rack, Product... products);
}
