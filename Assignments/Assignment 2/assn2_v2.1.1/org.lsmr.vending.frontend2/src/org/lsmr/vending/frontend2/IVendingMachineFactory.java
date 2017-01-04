package org.lsmr.vending.frontend2;

import java.util.List;

import org.lsmr.vending.frontend2.hardware.DisabledException;

/**
 * This interface specifies a set of event callbacks that your vending machine
 * implementation has to implement. The frontend calls these callbacks when the
 * corresponding script commands are encountered. The factory maintains a set of
 * vending machines, indexed from 0 to (number of vending machines - 1).
 */
public interface IVendingMachineFactory {
    /**
     * Constructs a vending machine that accepts a specified set of coin kinds
     * (positive integer denominations) and a certain number of pop selection
     * buttons. Coin kinds must have unique denominations. The constructed
     * vending machine is indexed with the next available number (i.e., the
     * first vending machine has index 0, the second has index 1, etc.). The
     * system starts WITHOUT any vending machine constructed by default.
     * 
     * @param coinKinds
     *            A list of the values to be used for valid coin kinds. The
     *            values must be unique. The values must be positive. Any order
     *            can be used, but the order will be used for later reference.
     * @param selectionButtonCount
     *            The number of selection buttons that the vending machine
     *            should have. Must be greater than 0.
     * @param receptacleCapacity
     *            The maximum number of items that can be stored in each
     *            receptacle and delivery chute.
     * @param popCanRackCapacity
     *            The maximum number of items that can be stored in each pop can
     *            rack.
     * @param coinRackCapacity
     *            The maximum number of items that can be stored in each coin
     *            rack.
     * @return The index of the vending machine just constructed.
     * @throws IllegalArgumentException
     *             if any of the coin kinds is not positive or if the selection
     *             button count is not positive
     * @throws IllegalStateException
     *             if the coin kinds do not have unique denominations
     */
    public int constructNewVendingMachine(List<Integer> coinKinds, int selectionButtonCount, int coinRackCapacity, int popCanRackCapacity,
	    int receptacleCapacity);

    /**
     * Configures the indicated vending machine to use the indicated names and
     * costs for the pop kinds related to the selection buttons at the indexed
     * position. For example, the string at position 0 of popNames is to be used
     * as the name of the pops loaded into the vending machine that would be
     * vended by selection button 0. PopCan names and pop costs DO NOT need to
     * be unique.
     * 
     * @param vmIndex
     *            The index of the vending machine to configure.
     * @param popNames
     *            A list of the names to use for the pop cans. The length must
     *            equal the number of selection buttons in the machine. Each
     *            index position in the list corresponds to the selection button
     *            with the same index. Names must be legal names. The same name
     *            can be used for more than one position. Names can be enclosed
     *            in double-quotation marks (like &quot;this&quot;) in which
     *            case these marks are stripped off.
     * @param popCosts
     *            A list of the costs to use for the pop cans. The length must
     *            equal the number of selection buttons in the machine. Each
     *            index position in the list corresponds to the selection button
     *            with the same index. Costs must be greater than 0. The same
     *            cost can be used for more than one position.
     * @throws IndexOutOfBoundsException
     *             If the index is less than 0 or greater than or equal to the
     *             number of vending machines currently constructed.
     * @throws IllegalArgumentException
     *             If any arguments are null, if the number of elements in
     *             either list is not identical to the number of selection
     *             buttons for the vending machine, if a pop name is not a
     *             string at least 1 character long, or if a pop cost is not a
     *             positive integer.
     */
    public void configureVendingMachine(int vmIndex, List<String> popNames, List<Integer> popCosts);

    /**
     * A set of coins is ADDED to the indicated vending machine. Note that it is
     * legal to load coins of the wrong kind into the location intended for
     * another coin kind; this will lead to incorrect coins being returned at
     * times, but the vending machine will otherwise function normally.
     * 
     * @param vmIndex
     *            The index of the vending machine to use.
     * @param coinKindIndex
     *            The index of the coin kinds in which to add the coins.
     * @param coins
     *            A sequence of the coins to add to the machine.
     * @throws IndexOutOfBoundsException
     *             If the vmIndex is less than 0 or greater than or equal to the
     *             number of vending machines currently constructed; or if the
     *             coinKindIndex is less than 0 or greater than or equal to the
     *             number of coin kinds in the indicated vending machine.
     * @throws NullPointerException
     *             If any of the arguments is null
     */
    public void loadCoins(int vmIndex, int coinKindIndex, Coin... coins);

    /**
     * Loads a list of pop cans into the pop can rack at the indicated index.
     * 
     * @param vmIndex
     *            The index of the vending machine used.
     * @param popCanRackIndex
     *            The index of the pop can rack to be used.
     * @param popCans
     *            A sequence of pop cans to be loaded. Cannot be null. Can be
     *            empty.
     * @throws NullPointerException
     *             If the list is null.
     * @throws IndexOutOfBoundsException
     *             If either index is &lt; 0 or &gt;= number of pop can racks.
     */
    public void loadPopCans(int vmIndex, int popCanRackIndex, PopCan... popCans);

    /**
     * Called to remove all coins and pops from the vending machine. Should not
     * prevent the vending machine from continuing to function.
     * 
     * @param vmIndex
     *            The index of the vending machine to use.
     * @return The stored contents of the vending machine.
     * @throws IndexOutOfBoundsException
     *             If the vmIndex is less than 0 or greater than or equal to the
     *             number of vending machines currently constructed.
     */
    public VendingMachineStoredContents unloadVendingMachine(int vmIndex);

    /**
     * Called to remove all coins and pop cans from the delivery chute of the
     * vending machine, returning them in whatever order is convenient (as
     * {@link Coin} and {@link PopCan} instances). Should not prevent the
     * vending machine from continuing to function.
     * 
     * @param vmIndex
     *            The index of the vending machine to use.
     * @return A list of removed coins and pop cans from the delivery chute, as
     *         described above.
     * @throws IndexOutOfBoundsException
     *             If the vmIndex is less than 0 or greater than or equal to the
     *             number of vending machines currently constructed.
     */
    public List<Deliverable> extractFromDeliveryChute(int vmIndex);

    /**
     * Called to insert a coin in a vending machine.
     * 
     * @param vmIndex
     *            The index of the vending machine used.
     * @param coin
     *            The coin inserted in the machine. Cannot be null.
     * @throws DisabledException
     *             If the hardware is disabled.
     * @throws IndexOutOfBoundsException
     *             If the vmIndex is less than 0 or greater than or equal to the
     *             number of vending machines currently constructed.
     * @throws NullPointerException
     *             If the coin argument is null.
     */
    public void insertCoin(int vmIndex, Coin coin) throws DisabledException;

    /**
     * Press the specified button on the specified vending machine. Buttons are
     * numbered starting from 0.
     * 
     * @param vmIndex
     *            The index of the vending machine used.
     * @param value
     *            The index of the button pressed
     * @throws IndexOutOfBoundsException
     *             If the vmIndex is less than 0 or greater than or equal to the
     *             number of vending machines currently constructed.
     */
    public void pressButton(int vmIndex, int value);
}
