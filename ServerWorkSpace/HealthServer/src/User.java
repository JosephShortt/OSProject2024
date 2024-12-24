import java.io.*;

public class User {
	//User detials
	private String name, employeeID, email,password,deptName,role;
	
	//Constructor for user details
	public User(String name, String employeeID, String email, String password, String deptName, String role) {
		this.name=name;
		this.employeeID=employeeID;
		this.email=email;
		this.password=password;
		this.deptName=deptName;
		this.role=role;	
	}
	
	//concatenates the user details with ||
	public String toString()
	{
	    return name + "||" + employeeID + "||" + email + "||" + password + "||" + deptName + "||" + role;
	}

	   // Getters for email and password
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public String getID() {
    	return employeeID;
    }

	public String getEmployeeID() {
		return employeeID;
	}
	
	//Setter for password to update password
	public void updatePassword(String newPassword) {
		this.password=newPassword;
	}
    
}
