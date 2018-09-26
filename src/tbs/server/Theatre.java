package tbs.server;

import java.util.List;
/**
 * Represents a theatre in which performances will take place.
 * @author Bradley Coleman
 *
 */
public class Theatre {
	private String _ID;
	private int _rows;
	private int _area;
	
	// 'info' is a string with the ID, rows, and area of a theatre
	public Theatre(String info, List<Theatre> theatres) throws TBSServerUserException {
		// removing the new line character
		info = info.replace("\r", "");
		// splitting 'info' by "\t" into a readable array
		String[] words = info.split("\t");
		if (!words[0].equals("THEATRE")) {
			throw new TBSServerUserException("String 'info' must begin with \"THEATRE\"");
		}
		// ID is the second entry after THEATRE, rows third, area fourth.
		_ID = words[1];
		_rows = Integer.parseInt(words[2]);
		_area = Integer.parseInt(words[3]);
		// iterates through all theatres in _theatreList to determine if any have the same
		// ID
		for (Theatre theatre : theatres) {
			if (theatre.getID().equals(_ID)) {
				throw new TBSServerUserException("ERROR theatre ID is not unique");
			}
		}
	}
	
	/**
	 * 
	 * @return The _ID of this Theatre
	 */
	public String getID() {
		return _ID;
	}
	/**
	 * 
	 * @return The dimension (in rows) of this Theatre's seating
	 */
	public int getDim() {
		return _rows;
	}
	/**
	 * 
	 * @return The floor area in square metres of this Theatre
	 */
	public int getArea() {
		return _area;
	}
}
