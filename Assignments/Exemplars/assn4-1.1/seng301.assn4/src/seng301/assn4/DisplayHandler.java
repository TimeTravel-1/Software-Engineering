package seng301.assn4;

import java.util.Timer;
import java.util.TimerTask;
import org.lsmr.vending.frontend4.hardware.Display;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;

public class DisplayHandler {

	private HardwareFacade h;
	
	//Display class deals with operations involving the display on vending machine
	public DisplayHandler(HardwareFacade hf) {
		h = hf;
	}
	
	//Prints out error message when a transaction fails
	public void printSummary(int cost, int availableFunds)
	{
		 Display disp = h.getDisplay();
	   disp.display("Cost: " + cost + "; available funds: " + availableFunds);
	   final Timer timer = new Timer();
	   timer.schedule(new TimerTask() 
	   {
	  	 @Override
	  	 public void run() {
	  		 timer.cancel();
	  	 }
	   }, 5000);
	}
	
	//Prints out message saying vending machine is enabled
	public void printEnabled()
	{
		 Display disp = h.getDisplay();
	   disp.display("Vending Machine is enabled");
	   final Timer timer = new Timer();
	   timer.schedule(new TimerTask() 
	   {
	  	 @Override
	  	 public void run() {
	  		 timer.cancel();
	  	 }
	   }, 5000);
	}
	
	//Prints out message saying vending machine is disabled
	public void printDisabled()
	{
		 Display disp = h.getDisplay();
	   disp.display("Vending Machine is disabled");
	   final Timer timer = new Timer();
	   timer.schedule(new TimerTask() 
	   {
	  	 @Override
	  	 public void run() {
	  		 timer.cancel();
	  	 }
	   }, 5000);
	}
	
	//Prints out message saying an invalid coin was inserted
	public void printInvalidCoin()
	{
		 Display disp = h.getDisplay();
	   disp.display("Invalid coin was entered.");
	   final Timer timer = new Timer();
	   timer.schedule(new TimerTask() 
	   {
	  	 @Override
	  	 public void run() {
	  		 timer.cancel();
	  	 }
	   }, 5000);
	}
	
}


