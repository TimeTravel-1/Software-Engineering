package seng301.assn4.communication;

import org.lsmr.vending.frontend4.ProductKind;

/**
 * Permits objects to listen to one or more {@link CommunicationFacade}s when
 * registered with them.
 * 
 * @author Robert J. Walker
 */
public interface CommunicationListener {
    /**
     * Signals an event in which a selection has occurred.
     * 
     * @param communicationFacade
     *            The facade in which the event occurred.
     * @param productKind
     *            The kind of the selected item. Can be null if the facade has
     *            not been configured correctly.
     */
    public void selected(CommunicationFacade communicationFacade, ProductKind productKind);
}
