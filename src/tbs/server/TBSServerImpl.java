package tbs.server;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class TBSServerImpl implements TBSServer {
	private List<Theatre> _theatreList = new Vector<Theatre>();
	private List<Artist> _artistList = new Vector<Artist>();
	public String initialise(String path) {
		try {
		Scanner scanner = new Scanner(new File(path));
        // Each line represents a set of theatre information so scanner
		// iterates every new line "\n"
        scanner.useDelimiter("\n");
        while(scanner.hasNext()){
        	String theatre = scanner.next();
        	try {
        		_theatreList.add(new Theatre(theatre, _theatreList));
        	} catch (ArrayIndexOutOfBoundsException | NumberFormatException | TBSServerUserException e) {
        		scanner.close();
        		// If 'theatre' does not have 4 values separated by tabs the 
        		// Theatre constructor will throw ArrayIndex... error
        		
        		// If 'theatre' has a non-integer value where an integer is expected 
        		// the Theatre constructor with throw NumberFormat... error
        		if (e.getMessage().equals("ERROR theatre ID is not unique")) {
        			return(e.getMessage());
        		}
        		return ("ERROR incorrect format");
        	}
        		
        }
        scanner.close();
        return("");
        // Scanner will throw this error if not file in path isn't found
		} catch (FileNotFoundException e){
			return ("ERROR no file found at specified path");
		}
	}

	
	public List<String> getTheatreIDs() {
		List<String> theatreIDs = new Vector<String>();
		// Iterates through all theatres in _theatreList, adding each of their IDs to the list
		for (Theatre search : _theatreList) {
			theatreIDs.add(search.getID());
		}
		// If there are more than 1 IDs, then the list will be sorted lexicographically with .sort()
		if(theatreIDs.size() > 1) {
			java.util.Collections.sort(theatreIDs);
		}
		return theatreIDs;
	}

	
	public List<String> getArtistIDs() {
		List<String> artistIDs = new Vector<String>();
		// Iterates through all artists in _artistList, adding each of their IDs to the list
		for(Artist search : _artistList) {
			artistIDs.add(search.getID());
		}
		// If there are more than 1 IDs, then the list will be sorted lexicographically with .sort
		if(artistIDs.size() > 1) {
			java.util.Collections.sort(artistIDs);
		}
		return artistIDs;
	}

	
	public List<String> getArtistNames() {
		List<String> artistNames = new Vector<String>();
		// Iterates through all artists in _artistList, adding each of their Names to the list
		for(Artist search : _artistList) {
			artistNames.add(search.getName());
		}
		// If there are more than 1 Names, then the list will be sorted lexicographically with .sort
		if(artistNames.size() > 1) {
			java.util.Collections.sort(artistNames);
		}
		return artistNames;
	}

	
	public List<String> getActIDsForArtist(String artistID) {
		// This List will have an ERROR message added if an ERROR occurs
		List<String> fail = new Vector<String>();
		for(Artist search : _artistList) {
			// going through _artistList to find artist with correct ID
			if (search.getID().equals(artistID)) {
				return search.getActIDs();
			}
		}
		// If the for loop completes then no artist with the ID was in _artistList
		fail.add("ERROR no artist with specified ID");
		return fail;
	}

	
	public List<String> getPeformanceIDsForAct(String actID) {
		// This List will have an ERROR message added if an ERROR occurs
		List<String> fail = new Vector<String>();
		// Iterating through every act in every artist until the correct one is found
		for(Artist artistSearch : _artistList) {
			for(Act actSearch : artistSearch.getActs()) {
				if (actID.equals(actSearch.getID())) {
					// once the correct act is found, getPerfIDs returns a lexicographically
					// sorted list of IDs for each act
					return(actSearch.getPerfIDs());
				}
			}
		}
		// If the for loop completes then no act with the ID was in any artist in _artistList
		fail.add("ERROR no act with specified ID");
		return fail;
	}

	public List<String> getTicketIDsForPerformance(String performanceID) {
		// This List will have an ERROR message added if an ERROR occurs
		List<String> fail = new Vector<String>();
		// Iterating through every performance in every act in every artist until the correct one is found
		for(Artist artistSearch : _artistList) {
			for(Act actSearch : artistSearch.getActs()) {
				for(Performance perfSearch : actSearch.getPerfs()) {
					if(performanceID.equals(perfSearch.getID())) {
						// once the correct performance is found, getTickIDs returns a lexicographically
						// sorted list of IDs for each ticket
						return(perfSearch.getTickIDs());
					}
				}
			}
		}
		// If the for loop completes then no performance with the ID was in any act in any artist in _artistList
		fail.add("ERROR no performance with specified ID");
		return fail;
	}

	
	public String addArtist(String name){
		if(name.isEmpty()) {
			return("ERROR name is empty");
		}
		for (Artist search : _artistList) {
			// if the name specified matches any of the current artists' names the request fails
			if (name.equalsIgnoreCase(search.getName())) {
				return("ERROR artist already exists");
			}
		}
		_artistList.add(new Artist(name, _artistList.size()));
		// the created artist is the last in the list so the ID of the last artist in _artistList
		// is returned
		return(_artistList.get(_artistList.size() - 1).getID());
	}

	
	public String addAct(String title, String artistID, int minutesDuration) {
		if(title.isEmpty()) {
			return("ERROR act title is empty");
		}
		if(minutesDuration < 0) {
			return("ERROR act duration cannot be negative");
		}
		// iterating through each Artist in _artistList until it finds the one with the same ID
		for (Artist artist : _artistList) {
			if (artist.getID().equals(artistID)) {
				// the Artist with the correct ID has the act with the specified details added
				return(artist.addAct(title, minutesDuration));
			}
		}
		return("ERROR no artist with specified ID");
	}

	
	public String schedulePerformance(String actID, String theatreID, String startTimeStr, String premiumPriceStr,
			String cheapSeatsStr) {
		// start time is checked to comply with ISO 8601 format.
		if (!startTimeStr.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
			return("ERROR start time is not in ISO 8601 format");
		}
		// price strings are checked to contain the '$' character
		if (!(premiumPriceStr.charAt(0) == '$' & cheapSeatsStr.charAt(0) == '$')) {
			return ("ERROR prices must be entered in format \"$d\" where d is an integer");
		}
		// runs through each theatre in list to find one with matching ID
		for (Theatre theatreSearch : _theatreList) {
			if (theatreID.equals(theatreSearch.getID())) {
				// runs through the list of acts in each of the artists in _artistList to find one 
				// with matching ID
				for (Artist artistSearch : _artistList) {
					for (Act actSearch : artistSearch.getActs()) {
						if (actID.equals(actSearch.getID())) {
							// addPerformance returns the performance ID as a string, or an ERROR message.
							return (actSearch.addPerformance(theatreSearch, startTimeStr, premiumPriceStr, cheapSeatsStr));
						}
					}
				}
				return("ERROR no act with specified ID");
			}
		}
		return("ERROR no theatre with specified ID");
	}

	public String issueTicket(String performanceID, int rowNumber, int seatNumber) {
		// Iterating through every performance in every act in every artist until the correct one is found
		for (Artist artistSearch : _artistList) {
			for (Act actSearch : artistSearch.getActs()) {
				for (Performance perfSearch : actSearch.getPerfs()) {
					if (performanceID.equals(perfSearch.getID())) {
						// sellTicket will return the ticket ID or an ERROR message if ticket is already sold
						// or out of bounds
						return(perfSearch.sellTicket(rowNumber, seatNumber));
					}
				}
			}
		}
		// If the loop completes then no performance with the ID has been added yet
		return("ERROR no performance with specified ID ");
	}

	
	public List<String> seatsAvailable(String performanceID) {
		// This List will have an ERROR message added if an ERROR occurs
		List<String> fail = new Vector<String>();
		// Iterating through every performance in every act in every artist until the correct one is found
		for (Artist artistSearch : _artistList) {
			for (Act actSearch : artistSearch.getActs()) {
				for (Performance perfSearch : actSearch.getPerfs()) {
					if (performanceID.equals(perfSearch.getID())) {
						// getFreeTicks will return a list of all the unsold ticket IDs as Strings
						return(perfSearch.getFreeTicks());
					}					
				}
			}
		}
		// If the loop completes then no performance with the ID has been added yet
		fail.add("ERROR no performance with specified ID");
		return fail;
	}

	
	public List<String> salesReport(String actID) {
		List<String> salesReport = new Vector<String>();
		// iterating through all performances of all acts of all artists to find correct performance
		for(Artist artistSearch : _artistList) {
			for(Act actSearch : artistSearch.getActs()) {
				if (actID.equals(actSearch.getID())) {
					for(Performance perfSearch : actSearch.getPerfs()) {
						// each performance generates it's own sales report String
						salesReport.add(perfSearch.salesReport());  
					}
					// once all performances for the act have reports added the final string is returned
					return(salesReport);
				}
			}
		}
		// the only way this code is reached is if the act ID is not found.
		salesReport.add("ERROR no act with specified ID");
		return(salesReport);
	}


	public List<String> dump() {
		return null;
	} 

}
