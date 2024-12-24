import java.io.*;
import java.util.LinkedList;

public class RegisterLogin {
	//Create list for storing users
	private LinkedList<User> list;
	private static final String FILE_NAME = "users.txt"; // File to store user details
	
	//Constructor creates a instance of list with User data
	public RegisterLogin() {
		list = new LinkedList<User>();
		loadFromFile(); // Load users from file when the system starts

	}
	
	//Add user to list given user details
	public synchronized void addUser(String name, String employeeID, String email, String password, String deptName,
			String role) {
		User temp = new User(name, employeeID, email, password, deptName, role);

		list.add(temp);
	}
	//Loops through list to validate email and password
	public synchronized boolean authenticate(String email, String password) {
		for (User user : list) {
			if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
				return true; // Valid email and password combination
			}
		}
		return false; // Authentication failed
	}
	
	//Appends new user object to file 
	public synchronized void saveToFile(User user) {
	    try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) { // Append mode
	        writer.println(user.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	//Loads in users to list upon execution
	public synchronized void loadFromFile() {
	    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	//Splits each data point at || to store indivdual array items
	            String[] data = line.split("\\|\\|");
	            if (data.length == 6) { // Ensure correct format
	                User user = new User(data[0], data[1], data[2], data[3], data[4], data[5]);
	                list.add(user); // Add user to in-memory list
	            }
	        }
	    } catch (FileNotFoundException e) {
	        // If the file doesn't exist, start with an empty list
	        System.out.println("User file not found. Starting fresh.");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	//Checks to see if the ID is unique
	public synchronized boolean isUniqueID(String employeeID) {
		for (User user : list) {
			if(user.getID().equals(employeeID)) {
				return false;
			}
		}
		return true;
	}
	//Checks to see if email is unique
	public synchronized boolean isUniqueEmail(String email) {
		for (User user : list) {
			if(user.getEmail().equals(email)) {
				return false;
			}
		}
		return true;
	}
	
	//Returns employeeID when passed unique email for creating report objects
	public String getEmployeeIDByEmail(String email) {
	    for (User user : list) {
	        if (user.getEmail().equals(email)) {
	            return user.getEmployeeID();
	        }
	    }
	    return null; // Return null if no matching email is found
	}
	
	//Update password
	public synchronized void updatePassword(String employeeID, String newPassword) {
		//find specific user and change set password to new password 
		for(User user: list) {
			if(user.getID().equals(employeeID)) {
				user.updatePassword(newPassword);
				break;
			}
		}
		//overwrite text file with new password added
		saveAllUsersToFile();
	}
	
	//Overwrites the text file
	private synchronized void saveAllUsersToFile() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) { // Overwrite mode
			for (User user : list) {
				writer.println(user.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
