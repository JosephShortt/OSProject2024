
public class Report {
	
	private String reportType,date,employeeID,status,assigneeID,reportID;
	
	
	public Report(String reportType,String date, String employeeID, String status, String assigneeID, String reportID) {
		this.reportType=reportType;
		this.date = date;
		this.employeeID=employeeID;
		this.status = status;
		this.assigneeID=assigneeID;
		this.reportID=reportID;
	}
	
	public String toString()
	{
	    return reportType + "||" + date + "||" + employeeID + "||" + status + "||" + assigneeID + "||" + reportID;
	}

	public Object getID() {
		return reportID;
	}
	
	public String getReportType() {
		return reportType;
	}
	
	public String getAssigneeID() {
		return assigneeID;
	}
	
	public void setAssigneeID(String id) {
		this.assigneeID = id;
	}
	
	public void setStatusToAssigned() {
		this.status = "Assigned";
	}
}