package book;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import author.Author;
import db.AuthorGateway;
import db.BookGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import utils.ControllerBase;
import utils.ControllerLoader;
import utils.GatewayBase;

public class BookListController extends ControllerBase implements Initializable{

    
    private static Logger logger = LogManager.getLogger();		//Logger
    
	@FXML private MenuItem menuClose;							//JavaFX Stuff
    @FXML private ListView<Book> bookList;
    @FXML private Button addBookButton;
    @FXML private Button deleteBookButton;
    @FXML private TextField searchTerm;
    @FXML private Button firstBut;
    @FXML private Button prevBut;
    @FXML private Button NexBut;
    @FXML private Button lastBut;
    @FXML private Label fetRecLab;
    
    private ObservableList<Book> books;							//List of Books
    private Book selBook;										//Selected Book
    private int pageNr;
    
    /**
     * 		Constructor
     * @param bookGateway the gateway to book table
     */
    public BookListController(BookGateway bookGateway) {
		super((GatewayBase)bookGateway);
	}

    /**
     * 		When the user clicks the add book button.
     * @param event
     */
    @FXML
    void OnAddClicked(MouseEvent event) {
    	Book b = new Book();
		getLoader().LoadController(ControllerLoader.BOK_DETAIL, b);
    }

    /**
     * 		When the user presses a key in the search textfield.
     * @param event
     */
    @FXML
    void OnSearchKeyPressed(KeyEvent event) {
    	if(searchTerm.getText().length() > 1) { 
    		BookGateway bg = (BookGateway) viewLocation;
    		bookList.setItems( bg.searchBook( searchTerm.getText() ) );
    	}else {
    		ObservableList<Object> tmp = viewLocation.read();
    		books = FXCollections.observableArrayList();
    		for(int i = 0; i < tmp.size(); i++ ) {
    			books.add( (Book)tmp.get(i) );
    		}
    		bookList.setItems(books);
    	}
    }
    
    /**
     * 		When a book is selected.
     * @param event
     */
    @FXML
    void OnBookClicked(MouseEvent event) {
    	if(event.getClickCount() == 2) {
			Book b = bookList.getSelectionModel().getSelectedItem();
			getLoader().LoadController(ControllerLoader.BOK_DETAIL, b);
		} else {
			selBook = bookList.getSelectionModel().getSelectedItem();
		}
    }

    /**
     * 		When the user hits the close menu button.
     * @param event
     */
    @FXML
    void OnClose(ActionEvent event) {
    	System.exit(0);
    }

    /**
     * 		When the user hits the delete book button.
     * @param event
     */
    @FXML
    void OnDeleteClicked(MouseEvent event) {
    	viewLocation.delete(selBook);
    	getLoader().LoadController(getLoader().BOK_LIST, null);
    }
    
    @FXML
    void OnFirstButClicked(MouseEvent event) {
    	pageNr = 0;
    	ObservableList<Object> tmp = viewLocation.getFirstPage();
		books = FXCollections.observableArrayList();
		for(int i = 0; i < tmp.size(); i++ ) {
			books.add( (Book)tmp.get(i) );
		}
		bookList.setItems(books);
		fetRecLab.setText("Fetched Records " + ((pageNr * 50) + 1) + " through " + ((pageNr * 50) + 50) + " out of " + viewLocation.getNumRec());
    }

    @FXML
    void OnLastButClicked(MouseEvent event) {
    	pageNr = (viewLocation.getNumRec() / 50) - 1;
    	ObservableList<Object> tmp = viewLocation.getLastPage();
		books = FXCollections.observableArrayList();
		for(int i = 0; i < tmp.size(); i++ ) {
			books.add( (Book)tmp.get(i) );
		}
		bookList.setItems(books);
		fetRecLab.setText("Fetched Records " + ((pageNr * 50) + 1) + " through " + ((pageNr * 50) + 50) + " out of " + viewLocation.getNumRec());
    }

    @FXML
    void OnNexButClicked(MouseEvent event) {
    	if( pageNr != (viewLocation.getNumRec() / 50) - 1) {
    		pageNr = pageNr + 1;
    	}else {
    		OnLastButClicked(null);
    	}
		ObservableList<Object> tmp = viewLocation.getNextPage(pageNr);
		books = FXCollections.observableArrayList();
		for(int i = 0; i < tmp.size(); i++ ) {
			books.add( (Book)tmp.get(i) );
		}
		bookList.setItems(books);
		fetRecLab.setText("Fetched Records " + ((pageNr * 50) + 1) + " through " + ((pageNr * 50) + 50) + " out of " + viewLocation.getNumRec());
    }

    @FXML
    void OnPrevButClick(MouseEvent event) {
    	if (pageNr > 1) {
    		pageNr = pageNr - 1;
    	}else {
    		OnFirstButClicked(null);
    	}
    	ObservableList<Object> tmp = viewLocation.getPrevPage(pageNr);
		books = FXCollections.observableArrayList();
		for(int i = 0; i < tmp.size(); i++ ) {
			books.add( (Book)tmp.get(i) );
		}
		bookList.setItems(books);
		fetRecLab.setText("Fetched Records " + ((pageNr * 50) + 1) + " through " + ((pageNr * 50) + 50) + " out of " + viewLocation.getNumRec());
    }

    /**
     * 		Initialize
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		OnFirstButClicked(null);
	}
}
