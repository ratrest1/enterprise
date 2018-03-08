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
	private BookGateway gateway;							//Gateway to the book table
	
	private int id;											//Book Id
	private SimpleStringProperty title;						//Title of Book
	private SimpleStringProperty summary;					//Summary of book
	private SimpleIntegerProperty yearPublished;			//Year the book was publisher
	private SimpleObjectProperty<Publisher> publisher;		//Publisher of the book
	private SimpleStringProperty isbn;						//Isbn of the book
	private SimpleObjectProperty<LocalDate> dateAdded;		//Date book was added to db
	
	/**
	 * 		Constructor
	 */
	public Book() {
		id = 0;
		title = new SimpleStringProperty();
		summary = new SimpleStringProperty();
		yearPublished = new SimpleIntegerProperty();
		publisher = new SimpleObjectProperty<Publisher>();
		isbn = new SimpleStringProperty();
		dateAdded = new SimpleObjectProperty<LocalDate>();
	}
	/**
	 * 		Saves the Book to the db
	 * @param viewType Whether the book is being created=1 or updated=0
	 * @return whether or not the book was successfully saved.
	 */
	public boolean saveBook(int viewType) {
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
		if( viewType == 0 ) { 
			// update Author table
			gateway.updateBook(this);
		}else {
			gateway.createBook(this);
		}
		return true;
	}

	//Accessors
	public String toString() {
		return title.get();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public SimpleStringProperty titleProperty() {
		return title;
	}
	public String getTitle() {
		return title.get();
	}
	public void setTitle(String title) {
		this.title.set(title);
	}
	public SimpleStringProperty summaryProperty() {
		return summary;
	}
	public String getSummary() {
		return summary.get();
	}
	public void setSummary(String summary) {
		this.summary.set( summary );
	}
	public SimpleIntegerProperty yearPublishedProperty() {
		return yearPublished;
	}
	public int getYearPublished() {
		return yearPublished.get();
	}
	public void setYearPublished(int yearPublished) {
		this.yearPublished.set( yearPublished );
	}
	public SimpleObjectProperty<Publisher> publisherProperty() {
		return publisher;
	}
	public Publisher getPublisher() {
		return publisher.get();
	}
	public void setPublisher( Publisher publisher) {
		this.publisher.set( publisher );
	}
	public SimpleStringProperty isbnProperty() {
		return isbn;
	}
	public String getIsbn() {
		return isbn.get();
	}
	public void setIsbn(String isbn) {
		this.isbn.set( isbn );
	}
	public SimpleObjectProperty<LocalDate> dateAddedProperty() {
		return dateAdded;
	}
	public LocalDate getDateAdded() {
		return dateAdded.get();
	}
	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded.set( dateAdded );
	}

	public void setGateway(BookGateway bookGateway) {
		this.gateway = bookGateway;
	}
}
