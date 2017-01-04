package org.lsmr.vending.frontend4;

/**
 * Instances of this class represent individual coins.
 */
public class Coin implements Deliverable {
    private Cents value;

    /**
     * Basic constructor.
     * 
     * @param value
     *            The value of the coin.
     * @throws IllegalArgumentException
     *             If the value is &lt;= 0.
     */
    public Coin(Cents value) {
	if(value.compareTo(new Cents(0)) <= 0)
	    throw new IllegalArgumentException("The value must be greater than 0: the argument passed was " + value);
	this.value = value;
    }

    /**
     * Accessor for the value.
     * 
     * @return The value of the coin. Should always be greater than 0.
     */
    public Cents getValue() {
	return value;
    }

    @Override
    public String toString() {
	return "" + getValue();
    }
}
