package seng301.assn4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;
import org.lsmr.vending.frontend4.hardware.SelectionButton;

import seng301.assn4.communication.CommunicationFacade;
import seng301.assn4.funds.FundsFacade;
import seng301.assn4.product.ProductController;
import seng301.assn4.product.ProductFacade;
import seng301.assn4.rules.SelectionRule;

/**
 * Represents vending machines, fully configured and with all software
 * installed.
 * 
 * @author Robert J. Walker
 */
public class VendingMachine {
    private HardwareFacade hf;
    private CommunicationFacade cf;
    private FundsFacade ff;
    private ProductFacade pf;

    /**
     * Accessor for the hardware facade for this vending machine.
     * 
     * @return The hardware.
     */
    public HardwareFacade getHardware() {
	return hf;
    }

    /**
     * Creates a standard arrangement for the vending machine. All the
     * components are created and interconnected. The hardware is initially
     * empty. The product kind names and costs are initialized to &quot; &quot;
     * and 1 respectively.
     * 
     * @param coinKinds
     *            The values (in cents) of each kind of coin. The order of the
     *            kinds is maintained. One coin rack is produced for each kind.
     *            Each kind must have a unique, positive value.
     * @param selectionButtonCount
     *            The number of selection buttons on the machine. Must be
     *            positive.
     * @param coinRackCapacity
     *            The maximum capacity of each coin rack in the machine. Must be
     *            positive.
     * @param productRackCapacity
     *            The maximum capacity of each product rack in the machine. Must
     *            be positive.
     * @param receptacleCapacity
     *            The maximum capacity of the coin receptacle, storage bin, and
     *            delivery chute. Must be positive.
     * @throws IllegalArgumentException
     *             If any of the arguments is null, or the size of productCosts
     *             and productNames differ.
     */
    public VendingMachine(Cents[] coinKinds, int selectionButtonCount, int coinRackCapacity, int productRackCapacity, int receptacleCapacity) {
	hf = new HardwareFacade(coinKinds, selectionButtonCount, coinRackCapacity, productRackCapacity, receptacleCapacity);
	ff = new FundsFacade(hf);

	List<SelectionButton> sbList = new ArrayList<>();
	for(int i = 0, sbCount = hf.getNumberOfSelectionButtons(); i < sbCount; i++)
	    sbList.add(hf.getSelectionButton(i));
	cf = new CommunicationFacade(hf);

	List<ProductController> pcList = new ArrayList<>();
	for(int i = 0, pkCount = hf.getNumberOfProductKinds(); i < pkCount; i++) {
	    ProductController pc = new ProductController(hf, i);
	    pcList.add(pc);
	}
	pf = new ProductFacade(pcList.toArray(new ProductController[pcList.size()]));

	new SelectionRule(cf, ff, pf);
    }

    /**
     * Configures the hardware to use a set of names and costs for products.
     * 
     * @param kinds
     *            A sequence of product kinds, each position of which will
     *            correspond to a selection button. No kind object can be null.
     *            The same kind can be used for more than one position.
     */
    public void configure(ProductKind... kinds) {
	hf.configure(kinds);

	Map<SelectionButton, ProductKind> bindings = new HashMap<>();
	for(int i = 0, size = hf.getNumberOfSelectionButtons(); i < size; i++)
	    bindings.put(hf.getSelectionButton(i), kinds[i]);
	
	cf.configure(bindings);
    }

}
