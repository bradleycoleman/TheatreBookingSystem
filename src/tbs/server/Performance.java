package tbs.server;

import java.util.List;
import java.util.Vector;
/**
 * Represents a scheduled performance of a specified Act in a specified Theatre.
 * Has a map of Tickets the same size as the Theatre.
 * @author Bradley Coleman
 *
 */
public class Performance {
	private String _ID;
	private int _premiumPrice;
	private int _cheapPrice;
	private Ticket[][] _tickets;
	private String _startTime;
	private int _dim;
	private int _premiumSold;
	private int _cheapSold;
	public Performance(String actID, int perfNo, Theatre theatre, String startTimeStr, int premiumPrice, int cheapSeats) {
		// the n'th performance of an act has an ID which is [actID]_perf[n]
		_ID = actID + "_perf" + Integer.toString(perfNo);
		_premiumPrice = premiumPrice;
		_cheapPrice = cheapSeats;
		_startTime = startTimeStr;
		_dim = theatre.getDim();
		_premiumSold = 0;
		_cheapSold = 0;
		// creating a 2D array with the theatre's dimensions of unsold tickets
		_tickets = new Ticket[_dim][_dim];
		for (int j = 0; j < _dim; j++) {
			for (int i = 0; i <= _dim/2; i++) {
				_tickets[i][j] = new Ticket(i, j,_ID);
			}
			for (int i = _dim/2 + 1; i < _dim; i++) {
				_tickets[i][j] = new Ticket(i, j,_ID);
			}
		}
	}
	
	/**
	 * Requests the server change the _sold state of the ticket at the given row
	 * and column in the 2D _tickets array for this Performance to be 'true'. 
	 * @param row The row index for the desired ticket.
	 * @param col The column index for the desired ticket.
	 * @return The ID of the ticket if the state is changed, otherwise an ERROR-
	 * message if the ticket is already sold or does not exist in the first place. 
	 */
	public String sellTicket(int row, int col) {
		String result = null;
		try {
			result = _tickets[row][col].sell(); 
			if (row <= _dim/2) {
				_premiumSold++;
			} else {
				_cheapSold++;
			}
			return(result);
		} catch(ArrayIndexOutOfBoundsException e) {
			return ("ERROR no seat at specified location");
		}
	}
	/**
	 * @return A List of Strings for the IDs of each of the tickets both sold and  
	 * unsold for this Performance only.
	 */
	public List<String> getTickIDs() {
		List<String> tickIDs = new Vector<String>();
		for (int i = 0; i < _dim; i++) {
			for (int j = 0; j < _dim; j++) {
				tickIDs.add(_tickets[i][j].getID());
			}
		}
		return tickIDs;
	}
	
	/**
	 * @return A List of Strings for the IDs of each of the unsold tickets for  
	 * this Performance only.
	 */
	public List<String> getFreeTicks() {
		List<String> tickIDs = new Vector<String>();
		for (int i = 0; i < _dim; i++) {
			for (int j = 0; j < _dim; j++) {
				if(available(i,j)) {
					tickIDs.add(_tickets[i][j].getID());
				}
			}
		}
		return tickIDs;
	}
	
	/**
	 * Checks the _sold state of the Ticket at the given row and column in the 
	 * _tickets 2D array of this Performance.
	 * @param row the row position of the desired Ticket in _tickets
	 * @param col the column position of the desired Ticket in _tickets
	 * @return Boolean value 'true' if specified Ticket is not sold, 'false' otherwise
	 */
	public Boolean available(int row, int col) {
		return(!_tickets[row][col].isSold());
	}
	
	/**
	 * @return A String containing details of this Performance of format:<br>
	 * &lt;performanceID&gt; "\t" &lt;start time&gt; "\t" &lt;number of tickets sold&gt; "\t' &lt;totals sales receipts for performance&gt;
	 */
	public String salesReport() {
		return (_ID + "\t" + _startTime + "\t" + Integer.toString(_cheapSold + _premiumSold) +"\t$" + 
				Integer.toString(_cheapSold*_cheapPrice + _premiumSold*_premiumPrice));
	}
	/**
	 * 
	 * @return The ID of this Performance
	 */
	public String getID() {
		return _ID;
	}
}
