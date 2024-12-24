import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

	public static void main(String args[])
	{
		ServerSocket provider;
		Socket connection;
		ServerThread handler;
		RegisterLogin Users = new  RegisterLogin();
		HealthAndSafetyReport Reports = new HealthAndSafetyReport();
		try 
		{
			provider = new ServerSocket(2005,10);
			
			while(true)
			{
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
