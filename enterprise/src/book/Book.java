package book;

import java.time.LocalDate;
import java.util.Calendar;

import db.AuthorGateway;
import db.BookGateway;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import publisher.Publisher;

public class Book {
	private BookGateway gateway;
	
	private int id;
	private SimpleStringProperty title;
	private SimpleStringProperty summary;
	private SimpleIntegerProperty yearPublished;
	private SimpleObjectProperty<Publisher> publisher;
	private SimpleStringProperty isbn;
	private SimpleObjectProperty<LocalDate> dateAdded;
	
	public Book() {
		id = 0;
		title = new SimpleStringProperty();
		summary = new SimpleStringProperty();
		yearPublished = new SimpleIntegerProperty();
		publisher = new SimpleObjectProperty<Publisher>();
		isbn = new SimpleStringProperty();
	}
	
	public boolean saveBook() {
		if(id < 0) {
			return false;
		}
		
		if(title.get().length() < 1 || title.get().length() > 255) {
			return false;
		}
		
		if(summary.get().length() > 65536) {
			return false;
		}
		
		if(yearPublished.get() > Calendar.getInstance().get(Calendar.YEAR)) {
			return false;
		}
		
		if(isbn.get().length() > 13) {
			return false;
		}
		
		//insert to db
		
		return true;
	}

	//Accessors
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public SimpleStringProperty getTitleProperty() {
		return title;
	}
	public String getTitle() {
		return title.get();
	}
	public void setTitle(String title) {
		this.title.set(title);
	}
	public SimpleStringProperty getSummaryProperty() {
		return summary;
	}
	public String getSummary() {
		return summary.get();
	}
	public void setSummary(String summary) {
		this.summary.set( summary );
	}
	public SimpleIntegerProperty getYearPublishedProperty() {
		return yearPublished;
	}
	public int getYearPublished() {
		return yearPublished.get();
	}
	public void setYearPublished(int yearPublished) {
		this.yearPublished.set( yearPublished );
	}
	public SimpleObjectProperty<Publisher> getPublisherProperty() {
		return publisher;
	}
	public Publisher getPublisher() {
		return publisher.get();
	}
	public void setPublisher( Publisher publisher) {
		this.publisher.set( publisher );
	}
	public SimpleStringProperty getIsbnProperty() {
		return isbn;
	}
	public String getIsbn() {
		return isbn.get();
	}
	public void setIsbn(String isbn) {
		this.isbn.set( isbn );
	}
	public SimpleObjectProperty<LocalDate> getDateAddedProperty() {
		return dateAdded;
	}
	public LocalDate getDateAdded() {
		return dateAdded.get();
	}
	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded.set( dateAdded );
	}

	public void setPublisher(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setGateway(BookGateway bookGateway) {
		// TODO Auto-generated method stub
		this.gateway = bookGateway;
	}
}
