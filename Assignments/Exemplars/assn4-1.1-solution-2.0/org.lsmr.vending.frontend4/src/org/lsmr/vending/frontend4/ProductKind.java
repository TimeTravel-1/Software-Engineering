package org.lsmr.vending.frontend4;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents certain properties of products in order to permit sets of products
 * to share these without having to subclass. These properties will, in general,
 * be globally changeable.
 * 
 * @author Robert J. Walker
 */
public class ProductKind {
    private Cents cost; // In Java 9, this will move to MonetaryAmount
    private String name;
    private Set<ProductKindListener> listeners = new HashSet<>();

    /**
     * Basic constructor.
     * 
     * @param name
     *            The name of the product kind. Cannot be null.
     * @param cost
     *            The cost of the product kind. Cannot be non-positive.
     */
    public ProductKind(String name, Cents cost) {
	if(name == null)
	    throw new IllegalArgumentException("The name cannot be null");

	if(cost.getValue() <= 0)
	    throw new IllegalArgumentException("The cost cannot be non-positive");

	this.name = name;
	this.cost = cost;
    }

    @Override
    public boolean equals(Object other) {
	if(other instanceof ProductKind)
	    return name.equals(((ProductKind)other).name);
	return false;
    }

    @Override
    public int hashCode() {
	return name.hashCode();
    }

    /**
     * Permits listeners to register their interest in events on this product
     * kind.
     * 
     * @param listener
     *            The listener interested in events. Can already be registered,
     *            in which case this call has no effect. Cannot be null.
     * @throws NullPointerException
     *             If the argument is null.
     */
    public void register(ProductKindListener listener) {
	listeners.add(listener);
    }

    /**
     * Permits listeners to de-register their interest in events on this product
     * kind.
     * 
     * @param listener
     *            The listener no longer interested in events. If it is not
     *            registered, this call has no effect. Cannot be null.
     * @throws NullPointerException
     *             If the argument is null.
     */
    public void deregister(ProductKindListener listener) {
	listeners.remove(listener);
    }

    /**
     * Accessor for the cost.
     * 
     * @return The cost for this product kind. Cannot be non-positive.
     */
    public Cents getCost() {
	return cost;
    }

    /**
     * Accessor for the name.
     * 
     * @return The name for this product kind. Cannot be null.
     */
    public String getName() {
	return name;
    }

    /**
     * Mutator for the name. Announces a &quot;name changed&quot; event if the
     * name is actually a different object.
     * 
     * @param name
     *            The new name for this product kind. Cannot be null.
     */
    public void setName(String name) {
	if(name == null)
	    throw new IllegalArgumentException("The name cannot be null");

	if(name != this.name) {
	    String temp = this.name;
	    this.name = name;

	    notifyNameChanged(temp, name);
	}
    }

    /**
     * Mutator for the cost. Announces a &quot;cost changed&quot; event if the
     * cost is actually a different object.
     * 
     * @param cost
     *            The new cost for this product kind. Cannot be null.
     */
    public void setCost(Cents cost) {
	if(cost.compareTo(new Cents(0)) != 1)
	    throw new IllegalArgumentException("The cost cannot be non-positive");

	if(cost != this.cost) {
	    Cents temp = this.cost;
	    this.cost = cost;

	    notifyCostChanged(temp, cost);
	}
    }

    @Override
    public String toString() {
	return getName();
    }

    private void notifyNameChanged(String oldName, String newName) {
	for(ProductKindListener l : listeners)
	    l.nameChanged(this, oldName, newName);
    }

    private void notifyCostChanged(Cents oldCost, Cents newCost) {
	for(ProductKindListener l : listeners)
	    l.costChanged(this, oldCost, newCost);
    }
}
