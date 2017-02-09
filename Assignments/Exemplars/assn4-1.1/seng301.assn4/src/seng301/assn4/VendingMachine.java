package seng301.assn4;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.Coin;
import org.lsmr.vending.frontend4.hardware.AbstractHardware;
import org.lsmr.vending.frontend4.hardware.AbstractHardwareListener;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.CoinSlot;
import org.lsmr.vending.frontend4.hardware.CoinSlotListener;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;
import org.lsmr.vending.frontend4.hardware.SelectionButton;
import org.lsmr.vending.frontend4.hardware.SelectionButtonListener;
import org.lsmr.vending.frontend4.hardware.SimulationException;

/**
 * Represents vending machines, fully configured and with all software
 * installed.
 * 
 * @author Robert J. Walker
 */
public class VendingMachine implements CoinSlotListener, SelectionButtonListener{
    private HardwareFacade hf;
    
    /* YOU CAN ADD OTHER COMPONENTS HERE */
    private CoinHandler ch;
    private ProductHandler ph;
    private DisplayHandler dh;
    
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
	
    	//Set up Hardware Facade and Handlers of information
    	hf = new HardwareFacade(coinKinds, selectionButtonCount, coinRackCapacity, productRackCapacity, receptacleCapacity);
    	ph = new ProductHandler(hf);
    	ch = new CoinHandler(hf);
    	dh = new DisplayHandler(hf);
    	
    	hf.getCoinSlot().register(this);
    	
    	//Register listeners
    	for(int i = 0; i < hf.getNumberOfSelectionButtons(); i++) 
    	{
    		SelectionButton sb = hf.getSelectionButton(i);
    		sb.register(this);
    		ph.setButton(sb, i);
    	}

    	for(int i = 0; i < hf.getNumberOfCoinRacks(); i++) 
    	{
    		int value = hf.getCoinKindForCoinRack(i).getValue();
    		ch.setCoin(value, i);
    	}
    }
    
	/* YOU CAN BUILD AND INSTALL THE HARDWARE HERE */
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) 
	{
		//Print a message saying that the vending machine is enabled
		dh.printEnabled();
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) 
	{
		//Print a message saying that the vending machine is disabled
		dh.printDisabled();
	}

	//This is logic of what vending machine does when a particular button is pressed
	public void pressed(SelectionButton button) 
	{
		//If cost is less than money entered, and rack is not empty
		if((ph.getCost(button) <= ch.getAvailableFunds()) && (ph.rackIsEmpty(button) == false)) 
		{
		    try 
		    {
		    	//Dispense pop, store cash used to pay and subtract this amount from credit the user still has
		    	ph.dispenseProduct(button);
					ch.storeCoins();
					ch.updateAvailableFunds(ph.getCost(button));
		    }
		    catch(DisabledException | EmptyException | CapacityExceededException e) 
		    {
		    	throw new SimulationException(e);
		    }
		}
		else 
		{
			//Print a message to display showing basic information when transaction fails
		  dh.printSummary(ph.getCost(button),ch.getAvailableFunds());
		}
	}
	
	//This is logic of what vending machine does when a valid coin is inserted
	public void validCoinInserted(CoinSlot slot, Coin coin) 
	{
		//Add value of coin to credit that the user has
		ch.addAvailableFunds(coin);
	}

	public void coinRejected(CoinSlot slot, Coin coin) 
	{
		//Print a message saying that the coin was rejected
		dh.printInvalidCoin();
	}
}
