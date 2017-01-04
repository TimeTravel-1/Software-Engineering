package seng301.assn4.product;

/**
 * Permits objects to listen to one or more {@link ProductController}s when
 * registered with them.
 * 
 * @author Robert J. Walker
 */
public interface ProductControllerListener {
    /**
     * Signals an event in which one or more products have been removed from the
     * controller.
     * 
     * @param productController
     *            The controller in which the event occurred.
     * @param count
     *            The count of products involved.
     */
    public void productsRemoved(ProductController productController, int count);

    /**
     * Signals an event in which one or more products have been added from the
     * controller.
     * 
     * @param productController
     *            The controller in which the event occurred.
     * @param count
     *            The count of products involved.
     */
    public void productsAdded(ProductController productController, int count);
}
