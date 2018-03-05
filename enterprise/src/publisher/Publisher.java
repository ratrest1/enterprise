package publisher;

import javafx.beans.property.SimpleStringProperty;

public class Publisher {
	int id;
	SimpleStringProperty publisherName;
	
	public Publisher() {
		publisherName = new SimpleStringProperty();
	}
	
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
