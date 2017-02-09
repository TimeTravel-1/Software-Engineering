package seng301.assn4.communication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.AbstractHardware;
import org.lsmr.vending.frontend4.hardware.AbstractHardwareListener;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;
import org.lsmr.vending.frontend4.hardware.SelectionButton;
import org.lsmr.vending.frontend4.hardware.SelectionButtonListener;

/**
 * Contains all communication functionality, which currently means, it deals
 * with users' selections.
 * 
 * @author Robert J. Walker
 */
public class CommunicationFacade {
    private final Set<CommunicationListener> listeners = new HashSet<>();
    private Map<SelectionButton, ProductKind> bindings = new HashMap<>();

    private class SBL implements SelectionButtonListener {
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    // ignore
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    // ignore
	}

	@Override
	public void pressed(SelectionButton button) {
	    notifySelected(bindings.get(button));
	}
    }

    /**
     * Basic constructor.
     * 
     * @param hf
     *            The hardware facade to be used to implement low-level
     *            functionality.
     */
    public CommunicationFacade(HardwareFacade hf) {
	SBL sbl = new SBL();
	Iterator<SelectionButton> iterator = hf.selectionButtonIterator();
	while(iterator.hasNext())
	    iterator.next().register(sbl);
    }

    /**
     * Permits bindings between {@link SelectionButton}s and {@link ProductKind}
     * s to be specified.
     * 
     * @param bindings
     *            A map of the bindings from {@link SelectionButton}s to
     *            {@link ProductKind}s. More than one {@link SelectionButton}
     *            can map to a given {@link ProductKind}.
     */
    public void configure(Map<SelectionButton, ProductKind> bindings) {
	this.bindings.putAll(bindings);
    }

    /**
     * Registers the given listener so that it will receive events from this
     * communication facade.
     * 
     * @param listener
     *            The listener to be registered. If it is already registered,
     *            this call has no effect.
     */
    public void register(CommunicationListener listener) {
	listeners.add(listener);
    }

    private void notifySelected(ProductKind pk) {
	for(CommunicationListener listener : listeners)
	    listener.selected(this, pk);
    }
}
