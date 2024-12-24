import java.io.*;
import java.util.LinkedList;

public class RegisterLogin {
	private LinkedList<User> list;
	private static final String FILE_NAME = "users.txt"; // File to store user details

	public RegisterLogin() {
		list = new LinkedList<User>();
		loadFromFile(); // Load users from file when the system starts

	}

	public synchronized void addUser(String name, String employeeID, String email, String password, String deptName,
			String role) {
		User temp = new User(name, employeeID, email, password, deptName, role);

		list.add(temp);
	}

	public synchronized boolean authenticate(String email, String password) {
		for (User user : list) {
			if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
				return true; // Valid email and password combination
			}
		}
		return false; // Authentication failed
	}

	public synchronized void saveToFile(User user) {
	    try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) { // Append mode
	        writer.println(user.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	public synchronized void loadFromFile() {
	    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
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
	
	public synchronized boolean isUniqueID(String employeeID) {
		for (User user : list) {
			if(user.getID().equals(employeeID)) {
				return false;
			}
		}
		return true;
	}
	public synchronized boolean isUniqueEmail(String email) {
		for (User user : list) {
			if(user.getEmail().equals(email)) {
				return false;
			}
		}
		return true;
	}
	
	public String getEmployeeIDByEmail(String email) {
	    for (User user : list) { // Assuming userList stores all registered users
	        if (user.getEmail().equals(email)) {
	            return user.getEmployeeID();
	        }
	    }
	    return null; // Return null if no matching email is found
	}
	
	public synchronized void updatePassword(String employeeID, String newPassword) {
		for(User user: list) {
			if(user.getID().equals(employeeID)) {
				user.updatePassword(newPassword);
				break;
			}
		}
		saveAllUsersToFile();
	}
	
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
