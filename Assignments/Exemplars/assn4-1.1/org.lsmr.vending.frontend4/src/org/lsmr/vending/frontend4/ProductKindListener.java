package org.lsmr.vending.frontend4;

/**
 * Supports objects interested in listening to events on product kinds.
 * 
 * @author Robert J. Walker
 */
public interface ProductKindListener {
    /**
     * Announces that the name has changed.
     * 
     * @param kind
     *            The product kind on which the event occurred.
     * @param oldName
     *            The previous name.
     * @param newName
     *            The new name.
     */
    public void nameChanged(ProductKind kind, String oldName, String newName);

    /**
     * Announces that the cost has changed.
     * 
     * @param kind
     *            The product kind on which the event occurred.
     * @param oldCost
     *            The previous cost.
     * @param newCost
     *            The new cost.
     */
    public void costChanged(ProductKind kind, Cents oldCost, Cents newCost);
}
