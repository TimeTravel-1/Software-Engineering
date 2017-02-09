package seng301.assn4.product;

import org.lsmr.vending.frontend4.ProductKind;

/**
 * Permits objects to listen to one or more {@link ProductFacade}s when
 * registered with them.
 * 
 * @author Robert J. Walker
 */
public interface ProductFacadeListener {
    /**
     * Signals an event in which one or more products have been removed from the
     * facade.
     * 
     * @param productFacade
     *            The facade in which the event occurred.
     * @param kind
     *            The kind of the product involved.
     * @param count
     *            The count of products involved.
     */
    void productsRemoved(ProductFacade productFacade, ProductKind kind, int count);

    /**
     * Signals an event in which one or more products have been added from the
     * facade.
     * 
     * @param productFacade
     *            The facade in which the event occurred.
     * @param kind
     *            The kind of the product involved.
     * @param count
     *            The count of products involved.
     */
    void productsAdded(ProductFacade productFacade, ProductKind kind, int count);
}
