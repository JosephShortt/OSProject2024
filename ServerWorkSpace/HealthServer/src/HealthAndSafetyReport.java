import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

public class HealthAndSafetyReport {
	//Linked list for storing reports
	private LinkedList<Report> list;
	//decalre filename
	private static final String FILE_NAME = "reports.txt";
	
	//Constructor to create ann instance of list with reports
	public HealthAndSafetyReport() {
		list = new LinkedList<Report>();
		//Load in reports to list
		loadFromFile();
	}
	
	//Add a report to the list
	public synchronized void addReport(String reportType, String date, String employeeID, String status,
			String assigneeID, String reportID) {
		Report temp = new Report(reportType, date, employeeID, status, assigneeID, reportID);

		list.add(temp);
	}
	
	//Appenda a report to file
	public synchronized void saveToFile(Report report) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) { // Append mode
			writer.println(report.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Load in reports from text file
	public synchronized void loadFromFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
			String line;
			while ((line = reader.readLine()) != null) {
				//Splits each data point at ||
				String[] data = line.split("\\|\\|");
				if (data.length == 6) { // Ensure correct format
					Report report = new Report(data[0], data[1], data[2], data[3], data[4], data[5]);
					list.add(report); // Add user to in-memory list
				}
			}
		} catch (FileNotFoundException e) {
			// If the file doesn't exist, start with an empty list
			System.out.println("User file not found. Starting fresh.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Checks to see if the rport id is unique when generating  a random number
	public synchronized boolean isUniqueID(String reportID) {
		for (Report report : list) {
			if (report.getID().equals(reportID)) {
				return false;
			}
		}
		return true;
	}
	
	//Uses string builder to append only accident reports
	public synchronized String listAccidentReport() {
		StringBuilder result = new StringBuilder();
		for (Report report : list) {
			if (report.getReportType().equalsIgnoreCase("Accident Report")) { // Check if it's an Accident Report
				result.append(report.toString()).append("\n");
			}
		}
		return result.toString(); // Return accident reports
	}
	//Loops through the list of reports and returns only reports assigned to logged in user
	public synchronized String listReportsAssignedToMe(String employeeID) {
		StringBuilder result = new StringBuilder();

		for (Report report : list) {
			if (report.getAssigneeID().equalsIgnoreCase(employeeID)) {
				result.append(report.toString()).append("\n");
			}
		}
		return result.toString(); // Return accident reports
	}
	
	//Method to assign a report given reportID and assignee id
	public synchronized void assignReport(String reportID, String assigneeID) {
		boolean reportFound = false;
		for (Report report : list) {
			if (report.getID().equals(reportID)) {
				report.setAssigneeID(assigneeID);
				report.setStatusToAssigned();
				reportFound = true;
				break;
			}
		}

		if (reportFound) {
			saveAllReportsToFile(); // Save the updated list to the file
		} else {
			System.out.println("Report with ID " + reportID + " not found.");
		}
	}
	//Overwrite the text file
	private synchronized void saveAllReportsToFile() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) { // Overwrite mode
			for (Report report : list) {
				writer.println(report.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
