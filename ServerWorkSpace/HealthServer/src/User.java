import java.io.*;

public class User {

	private String name, employeeID, email,password,deptName,role;
	
	public User(String name, String employeeID, String email, String password, String deptName, String role) {
		this.name=name;
		this.employeeID=employeeID;
		this.email=email;
		this.password=password;
		this.deptName=deptName;
		this.role=role;	
	}
	
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
	
	public void updatePassword(String newPassword) {
		this.password=newPassword;
	}
    
}
