package org.lsmr.vending.frontend4;

/**
 * Represents a quantity of money, including zero and negative quantities. When
 * javax.money.MonetaryAmount becomes standard in Java 9, it should replace the
 * need for this class.
 * 
 * @author Robert J. Walker
 */
public class Cents {
    private int quantity;

    /**
     * Basic constructor.
     * 
     * @param quantity
     *            The quantity, in units of the specified currency.
     */
    public Cents(int quantity) {
	this.quantity = quantity;
    }

    @Override
    public int hashCode() {
	return quantity;
    }

    @Override
    public boolean equals(Object other) {
	if(other instanceof Cents) {
	    Cents cents = (Cents)other;
	    return quantity == cents.quantity;
	}
	return false;
    }

    /**
     * Accesses the value of this quantity of money in cents, as an int.
     * 
     * @return The quantity, as an int.
     */
    public int getValue() {
	return quantity;
    }

    /**
     * Compares this quantity to the specified one.
     * 
     * @param other
     *            The other quantity against which to compare this one.
     * @return -1 if this quantity is less than the other; 0 if they are equal;
     *         +1 if this quantity is great than the other.
     * @throws NullPointerException
     *             If the argument is null.
     */
    public int compareTo(Cents other) {
	return ((quantity > other.quantity) ? 1 : ((quantity == other.quantity) ? 0 : -1));
    }

    /**
     * Adds the indicated quantity to this one.
     * 
     * @param other
     *            The other quantity to add to this one. Cannot be null.
     * @return This quantity. Useful for chaining calls.
     */
    public Cents add(Cents other) {
	quantity += other.quantity;
	return this;
    }

    /**
     * Subtracts the indicated quantity from this one.
     * 
     * @param other
     *            The other quantity to subtract from this one. Cannot be null.
     * @return This quantity. Useful for chaining calls.
     */
    public Cents subtract(Cents other) {
	quantity -= other.quantity;
	return this;
    }

    @Override
    public String toString() {
	return "" + quantity;
    }
}
