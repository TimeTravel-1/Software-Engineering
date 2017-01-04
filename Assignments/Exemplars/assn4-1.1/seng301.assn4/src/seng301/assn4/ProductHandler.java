package seng301.assn4;

import java.util.HashMap;
import java.util.Map;
import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;
import org.lsmr.vending.frontend4.hardware.ProductRack;
import org.lsmr.vending.frontend4.hardware.SelectionButton;
import org.lsmr.vending.frontend4.hardware.SimulationException;

public class ProductHandler {

	private HardwareFacade h;
  private Map<SelectionButton, Integer> buttonToIndex = new HashMap<>();
 
  //Class deals with products in vending machine
	public ProductHandler(HardwareFacade hf) 
	{
		h = hf;
	}

	//Get index corresponding to button
	public int getButton(SelectionButton button)
	{
		if (buttonToIndex.get(button) == null)
		{
			throw new SimulationException("An invalid selection button was pressed");
		}
		
		return buttonToIndex.get(button);
	}
	
	//Get cost with specified index
	public int getCost(int i)
	{
		Cents cost = h.getProductKind(i).getCost();
		return cost.getValue();
	}
	
	//Get cost of product with specified button
	public int getCost(SelectionButton button)
	{
		if (buttonToIndex.get(button) == null)
		{
			throw new SimulationException("An invalid selection button was pressed");
		}
		
		Cents cost = h.getProductKind(buttonToIndex.get(button)).getCost();
		return cost.getValue();
	}
	
	//Get rack with specified index
	public ProductRack getRack(int i)
	{
		return h.getProductRack(i);
	}
	
	//Get rack with specified button
	public ProductRack getRack(SelectionButton button)
	{
		int i = buttonToIndex.get(button);
		return h.getProductRack(i);
	}
	
	//Determine whether rack of specified index is empty
	public boolean rackIsEmpty(int i)
	{
		return h.getProductRack(i).size() == 0;
	}
	
	//Determine whether rack of specified button is empty
	public boolean rackIsEmpty(SelectionButton button)
	{
		int i = buttonToIndex.get(button);
		return h.getProductRack(i).size() == 0;
	}
	
	//Dispense the product to user
	public void dispenseProduct(SelectionButton button) throws DisabledException, EmptyException, CapacityExceededException
	{
		int i = buttonToIndex.get(button);
		h.getProductRack(i).dispenseProduct();
	}
	
	//Store buttons
	public void setButton(SelectionButton button, int i)
	{
		buttonToIndex.put(button, i);
	}
}
