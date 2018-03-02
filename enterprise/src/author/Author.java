/**
 * 		Author:
 * 			The model of an author.
 */
package author;

import db.AuthorGateway;
import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Author {
	private AuthorGateway gateway;
	
	private int id;								//db ID
	private SimpleStringProperty fName;			//First Name
	private SimpleStringProperty lName;			//Last Name
	private SimpleObjectProperty<LocalDate> dateOfBirth;	//Date of Birth
	private SimpleStringProperty gender;			//Gender
	private SimpleStringProperty website;		//Website of the author
	
	/**
	 * 		Default Constructor
	 */
	public Author() {
		id = 0;
		fName = new SimpleStringProperty();
		lName = new SimpleStringProperty();
		dateOfBirth = new SimpleObjectProperty<LocalDate>();
		dateOfBirth.setValue(LocalDate.now());
		gender = new SimpleStringProperty();
		website = new SimpleStringProperty();
	}
	
	/**
	 * 		Constructor
	 * @param f	First name of the author
	 * @param l	Last name of the author
	 */
	public Author (String f, String l) {
		this();
		fName.set(f);
		lName.set(l);
	}
	
	/**
	 * SaveAuthor : saves current contents of an author object and uploads changes to database
	 */
	public boolean saveAuthor( int viewType ) {
		// call validation methods
		if( id < 0 ) {
			return false;
		}
		if( fName.get().isEmpty() || fName.get().length() > 100 ) {
			return false;
		}
		
		if( !( this.gender.get().equalsIgnoreCase("Male") ||
				this.gender.get().equalsIgnoreCase("Female") ||
				this.gender.get().equalsIgnoreCase("Unknown") ) ) {
			return false;
		}
		if( website.get().length() > 100 ) {
			return false;
		}
		if( this.dateOfBirth.get().isEqual( LocalDate.now() ) ) {
			return false;
		}
		
		if( viewType == 0 ) { 
			// update Author table
			gateway.updateAuthor(this);
		}else {
			gateway.createAuthor(this);
		}
		return true;
	}
	
	
	//Accessors
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String toString() {
		return getfName() + " " + getlName();
	}
	
	public String getfName() {
		return fName.get();
	}

	public void setfName(String fName) {
		this.fName.set(fName);
	}
	
	public SimpleStringProperty fNameProperty() {
		return fName;
	}

	public String getlName() {
		return lName.get();
	}

	public void setlName(String lName) {
		this.lName.set(lName);
	}
	
	public SimpleStringProperty lNameProperty() {
		return lName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth.get();
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth.set(dateOfBirth);
	}
	
	public SimpleObjectProperty<LocalDate> dateOfBirthProperty() {
		return dateOfBirth;
	}

	public String getGender() {
		return gender.get();
	}

	public void setGender(String gender) {
		this.gender.set(gender);
	}
	
	public SimpleStringProperty genderProperty() {
		return gender;
	}

	public String getWebsite() {
		return website.get();
	}

	public void setWebsite(String website) {
		this.website.set(website);
	}
	
	public SimpleStringProperty websiteProperty() {
		return website;
	}
	
	public void setGateway (AuthorGateway gateway) {
		this.gateway = gateway;
	}
}