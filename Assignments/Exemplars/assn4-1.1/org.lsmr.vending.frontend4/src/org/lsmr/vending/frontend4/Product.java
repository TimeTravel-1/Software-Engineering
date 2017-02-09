package org.lsmr.vending.frontend4;

/**
 * Instances of this class represent products.
 */
public class Product implements Deliverable {
    private String name;

    /**
     * Basic constructor.
     * 
     * @param name
     *            The name of the product. Cannot be null.
     * @throws IllegalArgumentException
     *             If the argument is null.
     */
    public Product(String name) {
	if(name == null)
	    throw new IllegalArgumentException("The argument cannot be null");

	this.name = name;
    }

    /**
     * Accessor for the name of the product.
     * 
     * @return The name of the product. Should never be null or an empty string.
     */
    public String getName() {
	return name;
    }

    @Override
    public String toString() {
	return getName();
    }
}
