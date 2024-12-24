import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

	public static void main(String args[])
	{	
		//Using socket to handle client connections to server
		ServerSocket provider;
		Socket connection;
		ServerThread handler;
		//Instance of shared object for users and creating reports
		RegisterLogin Users = new  RegisterLogin();
		HealthAndSafetyReport Reports = new HealthAndSafetyReport();
		
		try 
		{	//Connect to port 2005
			provider = new ServerSocket(2005,10);
			
			while(true)
			{	
				//accepts connection when client requess it
				connection = provider.accept();
				handler = new ServerThread(connection,Users,Reports);
				handler.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
