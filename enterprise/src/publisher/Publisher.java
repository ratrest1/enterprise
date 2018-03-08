package publisher;

import javafx.beans.property.SimpleStringProperty;

public class Publisher {
	int id;									//Id of the publisher
	SimpleStringProperty publisherName;		//Name of publisher
	
	/**
	 * 		Constructor
	 */
	public Publisher() {
		publisherName = new SimpleStringProperty();
	}
	
	//Accessors
	public Publisher( String s ) {
		publisherName.set( s );
	}
	
	public String toString() {
		return publisherName.get();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public SimpleStringProperty getPublisherNameProperty() {
		return publisherName;
	}
	public String getPublisherName() {
		return publisherName.get();
	}
	public void setPublisherName(String publisherName) {
		this.publisherName.set( publisherName );
	}
	
}
