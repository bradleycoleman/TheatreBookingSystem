package tbs.server;

import java.util.List;
import java.util.Vector;
/**
 * Represents an artist capable of performing an expandable list of acts.
 * @author Bradley Coleman
 *
 */
public class Artist {
	private String _ID;
	private String _Name;
	private List<Act> _actList = new Vector<Act>();
	public Artist(String name, int artistNo) {
		_Name = name;
		_ID = "artist" + Integer.toString(artistNo + 1);
	}
	/**
	 * This method requests the server add an Act object with the specified details to the
	 * _actList for this Artist
	 * @param title The title of this act.
	 * @param minutesDuration The length of the act in minutes.
	 * @return The ID of the Act that is constructed 
	 */
	public String addAct(String title, int minutesDuration) {
		// This method adds an Act with the specified details to this Artist's _actList
		//
		// The actNo input is 1 more than the current size of the List as the first act 
		// is added when the list is of size zero etc...
		_actList.add(new Act(title,_ID,minutesDuration,_actList.size() + 1));
		// This returns what the ID of the added act will be. 
		return (_ID + "_act" + Integer.toString(_actList.size()));
	}
	
	/**
	 * 
	 * @return A lexicographically sorted List of all the actIDs of the Acts of this Artist.
	 */
	public List<String> getActIDs() {
		List<String> actIDs = new Vector<String>();
		// Iterates through each act in _actList, adding each ID to the list
		for(Act search : _actList) {
			actIDs.add(search.getID());
		}
		// If there are more than 1 IDs, then the list will be sorted lexicographically with .sort
		if (actIDs.size() > 1) {
			java.util.Collections.sort(actIDs);
		}
		return actIDs;
	}
	/**
	 * 
	 * @return An unsorted List of all the Acts in this Artist's _actList
	 */
	public List<Act> getActs() {
		return _actList;
	}
	/**
	 * 
	 * @return The _Name of this Artist
	 */
	public String getName() {
		return _Name;
	}
	/**
	 * 	
	 * @return The _ID of this Artist
	 */
	public String getID() {
		return _ID;
	}
}
