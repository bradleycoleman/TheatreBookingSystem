package tbs.server;
import java.util.List;
import java.util.Vector;
/**
 * Represents an act which a single Artist is capable of performing. Has an expandable List of scheduled Performances
 * @author Bradley Coleman
 *
 */
public class Act {
	private String _title;
	private String _ID;
	private int _minutesDuration;
	private List<Performance> _performanceList = new Vector<Performance>();
	
	public Act(String title, String artistID, int minutesDuration, int actNo) {
		_title = title;
		_minutesDuration = minutesDuration;
		_ID = artistID + "_act" + Integer.toString(actNo);
	}
	/**
	 * Requests the server to add a Performance object with the specified details to the _performanceList of this Act object.
	 * @param theatre The Theatre object representing the theatre the performance takes place in.
	 * @param startTimeStr The start time for this performance. It must be in the ISO8601 format yyyy-mm-ddThh:mm (zero-padded)
	 * @param premiumPriceStr The price for the premium seats. It must be in the format $d where &lt;d&gt; is the number of dollars.
	 * @param cheapSeatsStr The price for the cheap seats. It must be in the format $d where &lt;d&gt; is the number of dollars.
	 * @return The ID for the new performance if the addition is successful, otherwise a 
	 * message explaining what went wrong
	 */
	public String addPerformance(Theatre theatre, String startTimeStr, String premiumPriceStr, String cheapSeatsStr) {
		int premiumPrice, cheapPrice;
		try {
			premiumPrice = Integer.parseInt(premiumPriceStr.replace("$", ""));
			cheapPrice = Integer.parseInt(cheapSeatsStr.replace("$", ""));
		} catch (NumberFormatException e){
			// If this exception is thrown it means the number after the $ isn't an integer.
			return ("ERROR prices must be of the format $d where d is a positive integer");
		}
		if (cheapPrice < 0 || premiumPrice < 0) {
			// If this is true then one of the prices is not positive so ERROR is returned
			return ("ERROR prices must be of the format $d where d is a positive integer");
		}
		// Performance with specified details is added to the list
		_performanceList.add(new Performance(_ID, _performanceList.size() + 1, theatre, startTimeStr, premiumPrice, cheapPrice));
		// This returns what the performance ID will be
		return(_ID + "_perf" + String.valueOf(_performanceList.size()));
	}
	/**
	 * 
	 * @return this Act's list of Performances (unsorted)
	 */
	public List<Performance> getPerfs() {
		return _performanceList;
	}
	/** 
	 * 
	 * @return A lexicographically sorted list of all the performanceIDs of the Performances of this Act.
	 * If there are no Performances then it returns an empty List
	 */
	public List<String> getPerfIDs() {
		List<String> perfIDs = new Vector<String>();
		// Iterates through each act in _performanceList, adding each ID to the list
		for(Performance search : _performanceList) {
			perfIDs.add(search.getID());
		}
		// If there are more than 1 IDs, then the list will be sorted lexicographically with .sort
		if (perfIDs.size() > 1) {
			java.util.Collections.sort(perfIDs);
		}
		return perfIDs;
	}
	/**
	 * 
	 * @return The _ID of this Performance
 	 */
	public String getID() {
		return _ID;
	}
	/**
	 * 
	 * @return The _title of this Performance
	 */
	public String getTitle() {
		return _title;
	}
	/**
	 * 
	 * @return The _minutesDuration of this Performance 
	 */
	public int getDur() {
		return _minutesDuration;
	}

}
