package tbs.server;
/** 
 * When constructed, represents a Ticket available for purchase in a scheduled performance.
 * Can be sold, and once sold it cannot be sold again.
 * @author Bradley Coleman
 *
 */
public class Ticket {
	private Boolean _sold;
	private String _ID;
	public Ticket(int row, int col, String perfID) {
		// ID of ticket is [perfID]([row],[col]).
		// the row and col values are increased by 1 for the ID because the 0 values represent
		// row/column 1 of the theatre.
		_ID = perfID + "(" + Integer.toString(row + 1) + "," + Integer.toString(col + 1) + ")";
		// Upon construction no ticket is sold
		_sold = false;
	}
	/**
	 * This method changes the state of this Ticket's _sold field to be true if it isn't 
	 * already.
	 * @return The ID of this ticket if the state is changed, otherwise a message beginning
	 * with ERROR if the ticket was already sold.
	 */
	public String sell() {
		// If ticket is unsold, sell and return the ID.
		if (!_sold) {
			_sold = true;
			return _ID;
		} else {
			// If ticket has already been sold, return an ERROR
			return("ERROR seat has already been taken");
		}
	}
	/**
	 * 
	 * @return The ID of this ticket
	 */
	public String getID() {
		return _ID;
	}
	/**
	 * 
	 * @return true if this Ticket has been sold already, false if not.
	 */
	public Boolean isSold() {
		return _sold;
	}
}
