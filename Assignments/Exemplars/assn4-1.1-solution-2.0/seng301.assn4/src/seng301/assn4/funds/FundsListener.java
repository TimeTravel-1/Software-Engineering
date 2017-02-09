package seng301.assn4.funds;

import org.lsmr.vending.frontend4.Cents;

/**
 * Permits objects to listen to one or more {@link FundsFacade}s when registered
 * with them.
 * 
 * @author Robert J. Walker
 */
public interface FundsListener {
    /**
     * Signals an event in which funds have been added to the facade.
     * 
     * @param fundsFacade
     *            The facade in which the event occurred.
     * @param funds
     *            The quantity of funds involved.
     */
    public void fundsAdded(FundsFacade fundsFacade, Cents funds);

    /**
     * Signals an event in which funds have been removed from the facade, no
     * longer available for use.
     * 
     * @param fundsFacade
     *            The facade in which the event occurred.
     * @param funds
     *            The quantity of funds involved.
     */
    public void fundsRemoved(FundsFacade fundsFacade, Cents funds);

    /**
     * Signals an event in which funds have been removed from the facade, but
     * stored internally, no longer available for use.
     * 
     * @param fundsFacade
     *            The facade in which the event occurred.
     * @param funds
     *            The quantity of funds involved.
     */
    public void fundsStored(FundsFacade fundsFacade, Cents funds);
}
