import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class ServerThread extends Thread {
	// Socket connection for the client
	private Socket myConnection;
	// Handle communication between client
	private ObjectOutputStream out;
	private ObjectInputStream in;
	// variables for communicating messages
	private String message;
	private int result;

	// Register User details
	private String name, employeeID, email, password, deptName, role;
	private String tempID, tempEmail;

	// shared Object 1
	private RegisterLogin shared;
	// Authenticate login variable
	private String auth = "-1";

	// Loop control
	private String repeat;

	// Shared object 2 for reports
	private HealthAndSafetyReport shared2;
	// Report details
	private String reportType;
	private String reportID;
	private String assignReportID, assignEmloyeeID;

	// Constructor
	public ServerThread(Socket s, RegisterLogin users, HealthAndSafetyReport reports) {
		myConnection = s;
		shared = users;
		shared2 = reports;
	}

	public void run() {
		try {
			// out,in object for communication with client
			out = new ObjectOutputStream(myConnection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(myConnection.getInputStream());

			// Initial loop to login or register
			do {
				sendMessage("To enter System ~ Enter 1 to Login or Enter 2 to Register");
				message = (String) in.readObject();
				result = Integer.parseInt(message);

				if (result != 1 && result != 2) {
					sendMessage("Invalid input, please try again.");
				}

			} while (result != 1 && result != 2);

			// Allow user to log in
			if (message.equalsIgnoreCase("1")) {

				do {
					sendMessage("Plase Enter email:");
					email = (String) in.readObject();
					sendMessage("Please enter password:");
					password = (String) in.readObject();
					// Authenticate method to ensure email and password are valid
					if (shared.authenticate(email, password) == true) {
						// Set auth to 1 to communicate with client
						auth = "1";
						// Fetch the employee ID for the logged-in user for report creations
						employeeID = shared.getEmployeeIDByEmail(email);

					}

					// If entry is valid send 1 to client otherwise sends -1
					sendMessage(auth);
					if (auth.equalsIgnoreCase("-1")) {
						sendMessage("Invalid details entered, try again");
					}
				} while (auth.equalsIgnoreCase("-1"));

			}
			// Rest auth to -1 for validation registration
			auth = "-1";

			if (message.equalsIgnoreCase("2")) {

				sendMessage("Enter Name");
				name = (String) in.readObject();
				
				//Ask user to enter employeeID until it is a unique id
				do {
					sendMessage("Enter Employee ID");
					tempID = (String) in.readObject();
					//isUniqueID() checks if it already exists in user.txt
					if (shared.isUniqueID(tempID) == true) {
						employeeID = tempID;
						auth = "1";
					}
					sendMessage(auth);
					
					if (auth.equalsIgnoreCase("-1")) {
						sendMessage("You must enter a unique employee ID");
					}
				} while (auth.equalsIgnoreCase("-1"));
				
				auth = "-1";
				
				//Same thing for entering email, it must be unique: calls usUniqueEmail() from registerlogin object
				do {
					sendMessage("Enter Email");
					tempEmail = (String) in.readObject();
					if (shared.isUniqueEmail(tempEmail) == true) {
						email = tempEmail;
						auth = "1";
					}
					sendMessage(auth);

					if (auth.equalsIgnoreCase("-1")) {
						sendMessage("You must enter a unique Email");
					}
				} while (auth.equalsIgnoreCase("-1"));
				
				//Get user to enter details
				sendMessage("Enter password");
				password = (String) in.readObject();

				sendMessage("Enter Department Name");
				deptName = (String) in.readObject();

				sendMessage("Enter Role");
				role = (String) in.readObject();
				
				//Adduser to list and write list to file
				shared.addUser(name, employeeID, email, password, deptName, role);
				shared.saveToFile(new User(name, employeeID, email, password, deptName, role));

			}
			
			//Main portion of code that loops upon successful login
			do {
				sendMessage("You have successfully signed in to the system");
				//List options
				sendMessage("**WELCOME TO THE HEALTH & SAFTEY REPORTING PLATFORM**\n"
						+ "**Please enter the corresponding number from the list of options(1-5)**\n"
						+ "(1)~Create a Health and Safety Report\n" + "(2)~Retrieve all Accident Reports\n"
						+ "(3)~Assign the health & safety report to an employee account\n"
						+ "(4)~View all health and safety report assigned to you\n" + "(5)~Update your password");
				message = (String) in.readObject();
				
				//Switch case handles each option
				switch (message) {
				
				//Create reports
				case "1":
					sendMessage("**Here, YOU CAN CREATE A HEALTH AND SAFETY REPORT**");
					sendMessage("(Enter 1: Accident Report)\n(Enter 2: New Health & Safety Risk Report)");
					String tempReportType;
					tempReportType = (String) in.readObject();
					if (tempReportType.equals("1")) {
						reportType = "Accident Report";
					} else if (tempReportType.equals("2")) {
						reportType = "Health & Safety Risk Report";
					}
					// Gets the current date for report date
					Date currentDate = new Date();

					do {
						// Generate a random 10-digit number as the report ID
						long random10DigitNumber = 1000000000L + (long) (Math.random() * 9000000000L);
						reportID = String.valueOf(random10DigitNumber);
					} while (!shared2.isUniqueID(reportID)); // Ensure the ID is unique

					shared2.addReport(reportType, currentDate.toString(), employeeID, "Open", " ", reportID);
					// Save the report to the file
					shared2.saveToFile(
							new Report(reportType, currentDate.toString(), employeeID, "Open", "", reportID));

					sendMessage("Health and Safety Report created successfully with Report ID: " + reportID);
					break;
				//Send list of accident reports to console
				case "2":
					String result = shared2.listAccidentReport();
					sendMessage(result);
					break;
				//Assign a report o an employee
				case "3":
					sendMessage("Enter the report ID you want to assign an employee to");
					assignReportID = (String) in.readObject();
					sendMessage("Enter the ID of the employee you want to assign the report to");
					assignEmloyeeID = (String) in.readObject();
					
					//assignReport method takes reportID and employeeID
					shared2.assignReport(assignReportID, assignEmloyeeID);
					break;
				//Lists all reports assigned to logged in user	
				case "4":
					String result2 = shared2.listReportsAssignedToMe(employeeID);
					sendMessage(result2);
					break;
				//Update password
				case "5":
					sendMessage("Enter new password");
					String newPassword = (String) in.readObject();
					shared.updatePassword(employeeID, newPassword);
					sendMessage("Password Updated!");
					break;
				default:
					sendMessage("You must enter a valid option from the list of options(1-5)");
				}
				//Repeat loop if user enters 1
				sendMessage("Please enter 1 to repeat or any other key to exit");
				repeat = (String) in.readObject();
				
			} while (repeat.equalsIgnoreCase("1"));

			sendMessage("Goodbye!"); // Exit message
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				myConnection.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//Method to send messages to client
	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("server>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
