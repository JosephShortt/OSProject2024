import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Requester {
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	Scanner input;
	String result;
	String repeat;
	Requester() {

		input = new Scanner(System.in);
	}

	void run() {
		try {
			requestSocket = new Socket("127.0.0.1", 2005);
			System.out.println("Connected to localhost in port 2005");
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());

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
			

			if (message.equals("2")) {

				// Name
				result = (String) in.readObject();
				System.out.println(result);
				result = input.nextLine();
				sendMessage(result);

				// EmployeeID
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
				

				// Email
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
					case "2":
						message=(String) in.readObject();
						System.out.println(message);
						break;
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
					case "4":
						message=(String) in.readObject();
						System.out.println(message);
						break;
					case "5":
						message=(String) in.readObject();
						System.out.println(message);
						
						message = input.nextLine();
						sendMessage(message);
						
						message=(String) in.readObject();
						System.out.println(message);
						break;
					default:
						message=(String) in.readObject();
						System.out.println(message);
						
					}
					//System.out.println("End of ");

					
				}while(!result.equalsIgnoreCase("1")&& !result.equalsIgnoreCase("2")&& !result.equalsIgnoreCase("3")&& !result.equalsIgnoreCase("4")&& !result.equalsIgnoreCase("5"));
				
				// Enter 1 to repeat
				//System.out.println("Testing the repeat");
				
				message = (String) in.readObject();
				System.out.println(message);
				repeat = input.nextLine();
				sendMessage(repeat);

			} while (repeat.equalsIgnoreCase("1"));

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

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) {
		Requester client = new Requester();
		client.run();
	}
}