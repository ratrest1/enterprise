package audit;

import java.time.LocalDate;
import java.util.Date;

public class AuditTrailEntry {
	int id;				//Id of the book/author
	Date dateAdded;		//When the audit trail entry was added
	String message;		//The message in the audit trail entry
	
	//Accesors
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
