import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Requester {
	//requester socket
	Socket requestSocket;
	//input and output stream variables
	ObjectOutputStream out;
	ObjectInputStream in;
	//Message variables for communication with the server
	String message;
	//Scanner for client input
	Scanner input;
	String result;
	String repeat;
	
	Requester() {
		
		input = new Scanner(System.in);
	}
	
	void run() {
		try {
			//Connect local host to server socket port 2005
			requestSocket = new Socket("127.0.0.1", 2005);
			System.out.println("Connected to localhost in port 2005");
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			//Initial loop to login or register
			do {
				message = (String) in.readObject();
				System.out.println(message);
				message = input.nextLine();
				sendMessage(message);

				if (!message.equalsIgnoreCase("1") && !message.equalsIgnoreCase("2")) {
					result = (String) in.readObject();
					System.out.println(result);
				}

			} while (!message.equalsIgnoreCase("1") && !message.equalsIgnoreCase("2"));
			
			//Allow user to login
			if (message.equals("1")) {
				do {
					// Email entry
					result = (String) in.readObject();
					System.out.println(result);
					result = input.nextLine();
					sendMessage(result);

					// Password entry
					result = (String) in.readObject();
					System.out.println(result);
					result = input.nextLine();
					sendMessage(result);
					
					//Taking in value of authenticator variable to see 
					//if user entered right user details for login
					message = (String) in.readObject();
					
					if(message.equalsIgnoreCase("-1")) {
						result = (String) in.readObject();
						System.out.println(result);
					}
					
				}while(message.equalsIgnoreCase("-1"));
			}
			
			//Allows user to register
			if (message.equals("2")) {

				// Name
				result = (String) in.readObject();
				System.out.println(result);
				result = input.nextLine();
				sendMessage(result);

				// EmployeeID - ensure its unique
				do {
					result = (String) in.readObject();
					System.out.println(result);
					result = input.nextLine();
					sendMessage(result);
					
					message = (String) in.readObject();
					
					if(message.equalsIgnoreCase("-1")) {
						result = (String) in.readObject();
						System.out.println(result);
					}

				}while(message.equalsIgnoreCase("-1"));
				

				// Email - also must be unique
				do {
					result = (String) in.readObject();
					System.out.println(result);
					result = input.nextLine();
					sendMessage(result);
					
					message = (String) in.readObject();
					
					if(message.equalsIgnoreCase("-1")) {
						result = (String) in.readObject();
						System.out.println(result);
					}

				}while(message.equalsIgnoreCase("-1"));

				// Password
				result = (String) in.readObject();
				System.out.println(result);
				result = input.nextLine();
				sendMessage(result);

				// Department
				result = (String) in.readObject();
				System.out.println(result);
				result = input.nextLine();
				sendMessage(result);

				// Role
				result = (String) in.readObject();
				System.out.println(result);
				result = input.nextLine();
				sendMessage(result);
			}

			do {
				// Successful Sign in message
				message = (String) in.readObject();
				System.out.println(message);
				
				//Options Page
				message = (String) in.readObject();
				System.out.println(message);
				result = input.nextLine();
				sendMessage(result);
				
				do {
					
					switch(result) {
					//User creates a report
					case "1":
						message=(String) in.readObject();
						System.out.println(message);
						//Report type
						message=(String) in.readObject();
						System.out.println(message);
						message = input.nextLine();
						sendMessage(message);
						
						//Successfully created report
						message=(String) in.readObject();
						System.out.println(message);
						
						break;
					//lists all accident reports
					case "2":
						message=(String) in.readObject();
						System.out.println(message);
						break;
					//Assign a report to an employee
					case "3":
						//Report ID
						message=(String) in.readObject();
						System.out.println(message);
						message = input.nextLine();
						sendMessage(message);
						
						//Employee ID
						message=(String) in.readObject();
						System.out.println(message);
						message = input.nextLine();
						sendMessage(message);
						
						break;
					//Lists all reports to logged in employee
					case "4":
						message=(String) in.readObject();
						System.out.println(message);
						break;
					//Update password
					case "5":
						//Enter new password message
						message=(String) in.readObject();
						System.out.println(message);
						//Read in new password an send to server
						message = input.nextLine();
						sendMessage(message);
						
						//Password successfully update message
						message=(String) in.readObject();
						System.out.println(message);
						break;
					default:
						message=(String) in.readObject();
						System.out.println(message);
						
					}

				//repeat if user does not enter a correct option
				}while(!result.equalsIgnoreCase("1")&& !result.equalsIgnoreCase("2")&& !result.equalsIgnoreCase("3")&& !result.equalsIgnoreCase("4")&& !result.equalsIgnoreCase("5"));
				
				// Enter 1 to repeat				
				message = (String) in.readObject();
				System.out.println(message);
				repeat = input.nextLine();
				sendMessage(repeat);

			} while (repeat.equalsIgnoreCase("1"));
			
			//Goodbye message
			message = (String) in.readObject();
			System.out.println(message);

		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//Send message method
	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//Call run method 
	public static void main(String args[]) {
		Requester client = new Requester();
		client.run();
	}
}