package org.lsmr.vending.frontend4.hardware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.Coin;
import org.lsmr.vending.frontend4.Product;
import org.lsmr.vending.frontend4.ProductKind;

/**
 * Represents a standard configuration of the vending machine hardware:
 * <ul>
 * <li>one coin slot;</li>
 * <li>one coin receptacle (called the coin receptacle) to temporarily store
 * coins entered by the user;</li>
 * <li>one coin receptacle (called the storage bin) to store coins that have
 * been accepted as payment;</li>
 * <li>a set of one or more coin racks (the number and the denomination of coins
 * stored by each is specified in the constructor);</li>
 * <li>one delivery chute used to deliver products and to return coins;</li>
 * <li>a set of one or more product racks (the number, cost, and product name
 * stored in each is specified in the constructor);</li>
 * <li>one textual display;</li>
 * <li>one money return button;</li>
 * <li>a set of one or more selection selectionButtons (exactly one per product
 * rack); and</li>
 * <li>two indicator lights: one to indicate that exact change should be used by
 * the user; the other to indicate that the machine is out of order.</li>
 * </ul>
 * <p>
 * The component devices are interconnected as follows:
 * <ul>
 * <li>the output of the coin slot is connected to the input of the coin
 * receptacle;</li>
 * <li>the outputs of the coin receptacle are connected to the inputs of the
 * coin racks (for valid coins to be stored therein), the delivery chute (for
 * invalid coins or other coins to be returned), and the storage bin (for coins
 * to be accepted that do not fit in the coin racks);</li>
 * <li>the output of each coin rack is connected to the delivery chute; and</li>
 * <li>the output of each product rack is connected to the delivery chute.</li>
 * </ul>
 * <p>
 * Each component device can be disabled to prevent any physical movements.
 * Other functionality is not affected by disabling a device; hence devices that
 * do not involve physical movements are not affected by "disabling" them.
 * <p>
 * Most component devices have some sort of maximum capacity (e.g., of the
 * number of products that can be stored therein). In some cases, this is a
 * simplification of the physical reality for the sake of simulation.
 */
public final class HardwareFacade {
    private boolean safetyOn = false;

    private Lock lock;
    private Cents[] coinKinds;
    private CoinSlot coinSlot;
    private CoinReceptacle receptacle, storageBin;
    private DeliveryChute deliveryChute;
    private CoinRack[] coinRacks;
    private Map<Cents, CoinChannel> coinRackChannels;
    private ProductRack[] productRacks;
    private Display display;
    private SelectionButton returnButton;
    private SelectionButton[] selectionButtons;
    private ProductKind[] productKinds;
    private IndicatorLight exactChangeLight, outOfOrderLight;

    /**
     * Creates a standard arrangement for the hardware. All the components are
     * created and interconnected. The hardware is initially empty. The product
     * kind names and costs are initialized to &quot; &quot; and 1 respectively.
     * 
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
    public HardwareFacade(Cents[] coinKinds, int selectionButtonCount, int coinRackCapacity, int productRackCapacity, int receptacleCapacity) {
	if(coinKinds == null)
	    throw new IllegalArgumentException("Arguments may not be null");

	if(selectionButtonCount < 1 || coinRackCapacity < 1 || productRackCapacity < 1)
	    throw new IllegalArgumentException("Counts and capacities must be positive");

	if(coinKinds.length < 1)
	    throw new IllegalArgumentException("At least one coin kind must be accepted");

	this.coinKinds = Arrays.copyOf(coinKinds, coinKinds.length);

	Set<Cents> currentCoinKinds = new HashSet<>();
	for(Cents coinKind : coinKinds) {
	    Cents zero = new Cents(0);
	    if(coinKind.compareTo(zero) < 1)
		throw new IllegalArgumentException("Coin kinds must have positive values");

	    if(currentCoinKinds.contains(coinKind))
		throw new IllegalArgumentException("Coin kinds must have unique values");

	    currentCoinKinds.add(coinKind);
	}

	lock = new Lock();
	display = new Display();
	coinSlot = new CoinSlot(coinKinds);
	receptacle = new CoinReceptacle(receptacleCapacity);
	storageBin = new CoinReceptacle(receptacleCapacity);
	deliveryChute = new DeliveryChute(receptacleCapacity);
	coinRacks = new CoinRack[coinKinds.length];
	coinRackChannels = new HashMap<Cents, CoinChannel>();
	for(int i = 0; i < coinKinds.length; i++) {
	    coinRacks[i] = new CoinRack(coinRackCapacity);
	    coinRacks[i].connect(new CoinChannel(deliveryChute));
	    coinRackChannels.put(coinKinds[i], new CoinChannel(coinRacks[i]));
	}

	productRacks = new ProductRack[selectionButtonCount];
	for(int i = 0; i < selectionButtonCount; i++) {
	    productRacks[i] = new ProductRack(productRackCapacity);
	    productRacks[i].connect(new ProductChannel(deliveryChute));
	}

	productKinds = new ProductKind[selectionButtonCount];
	returnButton = new SelectionButton();

	selectionButtons = new SelectionButton[selectionButtonCount];
	for(int i = 0; i < selectionButtonCount; i++)
	    selectionButtons[i] = new SelectionButton();

	coinSlot.connect(new CoinChannel(receptacle), new CoinChannel(deliveryChute));
	receptacle.connect(coinRackChannels, new CoinChannel(deliveryChute), new CoinChannel(storageBin));

	exactChangeLight = new IndicatorLight();
	outOfOrderLight = new IndicatorLight();
    }

    /**
     * Accessor for the lock.
     * 
     * @return The unique lock for the vending machine. Cannot be null.
     */
    public Lock getLock() {
	return lock;
    }

    /**
     * Accessor for coin kinds. Coin kinds follow the same index as coin racks.
     * 
     * @param index
     *            The index at which to access the coin kind.
     * @return The coin kind at the given index.
     * @throws IndexOutOfBoundsException
     *             If the index &lt; 0 or &gt;= the number of coin kinds/racks.
     */
    public Cents getCoinKind(int index) {
	return coinKinds[index];
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
	if(kinds.length != selectionButtons.length)
	    throw new IllegalArgumentException("The number of product kinds must be equal to the number of selection buttons");

	for(ProductKind kind : kinds)
	    if(kind == null)
		throw new IllegalArgumentException("No product kind may be null");

	System.arraycopy(kinds, 0, productKinds, 0, kinds.length);
    }

    /**
     * Disables all the components of the hardware that involve physical
     * movements. Activates the out of order light.
     */
    public void enableSafety() {
	safetyOn = true;
	coinSlot.disable();
	deliveryChute.disable();

	for(int i = 0; i < productRacks.length; i++)
	    productRacks[i].disable();

	for(int i = 0; i < coinRacks.length; i++)
	    coinRacks[i].disable();

	outOfOrderLight.activate();
    }

    /**
     * Enables all the components of the hardware that involve physical
     * movements. Deactivates the out of order light.
     */
    public void disableSafety() {
	safetyOn = false;
	coinSlot.enable();
	deliveryChute.enable();

	for(int i = 0; i < productRacks.length; i++)
	    productRacks[i].enable();

	for(int i = 0; i < coinRacks.length; i++)
	    coinRacks[i].enable();

	outOfOrderLight.deactivate();
    }

    /**
     * Returns whether the safety is currently engaged.
     * 
     * @return true if the safety is enabled; false if the safety is disabled.
     */
    public boolean isSafetyEnabled() {
	return safetyOn;
    }

    /**
     * Accesses the exact change light.
     * 
     * @return The relevant device.
     */
    public IndicatorLight getExactChangeLight() {
	return exactChangeLight;
    }

    /**
     * Accesses the out of order light.
     * 
     * @return The relevant device.
     */
    public IndicatorLight getOutOfOrderLight() {
	return outOfOrderLight;
    }

    /**
     * Accesses a selection button at the indicated index.
     * 
     * @param index
     *            The index of the desired selection button.
     * @return The relevant device.
     * @throws IndexOutOfBoundsException
     *             if the index &lt; 0 or the index &gt;= number of selection
     *             selectionButtons.
     */
    public SelectionButton getSelectionButton(int index) {
	return selectionButtons[index];
    }

    /**
     * Creates an iterator over the selection buttons, in order of increasing
     * index.
     * 
     * @return The iterator.
     */
    public Iterator<SelectionButton> selectionButtonIterator() {
	return new Iterator<SelectionButton>() {
	    private int index = 0;

	    @Override
	    public boolean hasNext() {
		return index < getNumberOfSelectionButtons();
	    }

	    @Override
	    public SelectionButton next() {
		return getSelectionButton(index++);
	    }
	};
    }

    /**
     * Accesses the money return button.
     * 
     * @return The money return button.
     */
    public SelectionButton getReturnButton() {
	return returnButton;
    }

    /**
     * Accesses the number of selection buttons.
     * 
     * @return The number of selection buttons. Will be positive.
     */
    public int getNumberOfSelectionButtons() {
	return selectionButtons.length;
    }

    /**
     * Accesses the coin slot.
     * 
     * @return The relevant device.
     */
    public CoinSlot getCoinSlot() {
	return coinSlot;
    }

    /**
     * Accesses the coin receptacle.
     * 
     * @return The relevant device.
     */
    public CoinReceptacle getCoinReceptacle() {
	return receptacle;
    }

    /**
     * Accesses the storage bin.
     * 
     * @return The relevant device.
     */
    public CoinReceptacle getStorageBin() {
	return storageBin;
    }

    /**
     * Accesses the delivery chute.
     * 
     * @return The relevant device.
     */
    public DeliveryChute getDeliveryChute() {
	return deliveryChute;
    }

    /**
     * Accesses the number of coin racks.
     * 
     * @return The number of coin racks. Will be positive.
     */
    public int getNumberOfCoinRacks() {
	return coinRacks.length;
    }

    /**
     * Accesses a coin rack at the indicated index.
     * 
     * @param index
     *            The index of the desired coin rack.
     * @return The relevant device.
     * @throws IndexOutOfBoundsException
     *             if the index &lt; 0 or the index &gt;= number of coin racks.
     */
    public CoinRack getCoinRack(int index) {
	return coinRacks[index];
    }

    /**
     * Accesses the coin rack that handles coins of the specified kind. If none
     * exists, null is returned.
     * 
     * @param kind
     *            The value of the coin kind for which the rack is sought.
     * @return The relevant device.
     */
    public CoinRack getCoinRackForCoinKind(int kind) {
	CoinChannel cc = coinRackChannels.get(kind);
	if(cc != null)
	    return (CoinRack)cc.getSink();
	return null;
    }

    /**
     * Accesses a coin kind that corresponds to a coin rack at the specified
     * index.
     * 
     * @param index
     *            The index of the coin rack.
     * @return The coin kind at the specified index.
     * @throws IndexOutOfBoundsException
     *             If the index is &lt; 0 or the index is &gt;= the number of
     *             coin racks.
     */
    public Cents getCoinKindForCoinRack(int index) {
	return coinKinds[index];
    }

    /**
     * Accesses the number of product racks.
     * 
     * @return The number of product racks in the machine.
     */
    public int getNumberOfProductRacks() {
	return productRacks.length;
    }

    /**
     * Accesses the number of product kinds.
     * 
     * @return The number of product kinds supported by the machine.
     */
    public int getNumberOfProductKinds() {
	return productKinds.length;
    }

    /**
     * Accesses the product kind whose selection button and rack are at the
     * indicated index.
     * 
     * @param index
     *            The index of the desired product rack/selection button.
     * @return The product kind corresponding to the product rack/selection
     *         button at the specified index.
     * @throws IndexOutOfBoundsException
     *             if the index &lt; 0 or the index &gt;= the number of product
     *             kinds.
     */
    public ProductKind getProductKind(int index) {
	return productKinds[index];
    }

    /**
     * Accesses a product rack at the indicated index.
     * 
     * @param index
     *            The index of the desired product rack.
     * @return The product rack at the indicated index.
     * @throws IndexOutOfBoundsException
     *             if the index &lt; 0 or the index &gt;= number of product
     *             racks.
     */
    public ProductRack getProductRack(int index) {
	return productRacks[index];
    }

    /**
     * Creates an iterator over the product racks, in order of increasing index.
     * 
     * @return The iterator.
     */
    public Iterator<ProductRack> productRackIterator() {
	return new Iterator<ProductRack>() {
	    private int index = 0;

	    @Override
	    public boolean hasNext() {
		return index < getNumberOfProductRacks();
	    }

	    @Override
	    public ProductRack next() {
		return getProductRack(index++);
	    }
	};
    }

    /**
     * Accesses the textual display.
     * 
     * @return The relevant device.
     */
    public Display getDisplay() {
	return display;
    }

    /**
     * A convenience method for constructing and loading a set of products into
     * the machine.
     * 
     * @param productCounts
     *            A list representing the number of products to create and load
     *            into the corresponding rack.
     * @throws SimulationException
     *             If the number of arguments is different than the number of
     *             racks, or if any of the counts are negative.
     */
    public void loadProducts(int... productCounts) {
	if(productCounts.length != getNumberOfProductRacks())
	    throw new SimulationException("Product counts have to equal number of racks");

	int i = 0;
	for(int productCount : productCounts) {
	    if(productCount < 0)
		throw new SimulationException("Each count must not be negative");

	    ProductRack pcr = getProductRack(i);
	    ProductKind kind = getProductKind(i);
	    for(int products = 0; products < productCount; products++)
		pcr.load(new Product(kind.getName()));

	    i++;
	}
    }

    /**
     * A convenience method for constructing and loading a set of coins into the
     * machine.
     * 
     * @param coinCounts
     *            A variadic list of ints each representing the number of coins
     *            to create and load into the corresponding rack.
     * @throws SimulationException
     *             If the number of arguments is different than the number of
     *             racks, or if any of the counts are negative.
     */
    public void loadCoins(int... coinCounts) {
	if(coinCounts.length != getNumberOfCoinRacks())
	    throw new SimulationException("Coin counts have to equal number of racks");

	int i = 0;
	for(int coinCount : coinCounts) {
	    if(coinCount < 0)
		throw new SimulationException("Each count must not be negative");

	    CoinRack cr = getCoinRack(i);
	    Cents value = getCoinKindForCoinRack(i);
	    for(int coins = 0; coins < coinCount; coins++)
		cr.load(new Coin(value));

	    i++;
	}
    }

    /**
     * Determines the index of the given button within this hardware facade.
     * 
     * @param button
     *            The button to find.
     * @return The index of the indicated button, or -1 if it does not exist
     *         within this hardware facade.
     */
    public int indexOf(SelectionButton button) {
	for(int i = 0; i < selectionButtons.length; i++)
	    if(selectionButtons[i] == button)
		return i;
	return -1;
    }
}
