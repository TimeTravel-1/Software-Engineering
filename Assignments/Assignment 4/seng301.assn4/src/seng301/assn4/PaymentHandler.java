package seng301.assn4;

import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;

public interface PaymentHandler<coinCardOrBitcoin> {
	
	//Interface specifies that any form of payment needs to allow for adding, getting, setting, and updating credit
	public void addAvailableFunds(coinCardOrBitcoin t);
	
	public int getAvailableFunds();

	public void setAvailableFunds(int setValue);
	
	public void updateAvailableFunds(int cost) throws CapacityExceededException, EmptyException, DisabledException ;
		
}
