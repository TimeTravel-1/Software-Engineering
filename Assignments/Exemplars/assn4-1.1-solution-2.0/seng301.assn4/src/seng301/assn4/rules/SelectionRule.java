package seng301.assn4.rules;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;
import org.lsmr.vending.frontend4.hardware.SimulationException;

import seng301.assn4.communication.CommunicationFacade;
import seng301.assn4.communication.CommunicationListener;
import seng301.assn4.funds.FundsFacade;
import seng301.assn4.product.ProductFacade;

/**
 * This rule deals with &quot;selected&quot; events from the communication
 * facade, coordinating product dispensing, funds entry, funds return, etc.
 * 
 * @author Robert J. Walker
 */
public class SelectionRule {
    private final FundsFacade ff;
    private final ProductFacade pf;

    private class CL implements CommunicationListener {
	@Override
	public void selected(CommunicationFacade cf, ProductKind pk) {
	    Cents cost = pk.getCost();
	    if(ff.hasAvailableFunds(cost)) {
		try {
		    ff.storeFunds(cost);
		    pf.dispense(pk);
		    ff.returnUnusedFunds();
		}
		catch(EmptyException | DisabledException | CapacityExceededException e) {
		    throw new SimulationException(e);
		}
	    }
	}
    }

    /**
     * Basic constructor.
     * 
     * @param cf
     *            The communication facade that will be used to listen for
     *            &quot;selected&quot; events indicating that the user wants a
     *            given product.
     * @param ff
     *            The funds facade that will be used to determine the quantity
     *            of funds entered and to return excess.
     * @param pf
     *            The product facade that will be used to determine product
     *            availability and to dispense product.
     */
    public SelectionRule(CommunicationFacade cf, FundsFacade ff, ProductFacade pf) {
	if(cf == null || ff == null || pf == null)
	    throw new IllegalArgumentException("The arguments cannot be null");

	this.ff = ff;
	this.pf = pf;

	cf.register(new CL());
    }
}
