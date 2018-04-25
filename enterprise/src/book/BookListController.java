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
    
    private ObservableList<Book> books;							//List of Books
    private Book selBook;										//Selected Book
    
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
    	
    }

    @FXML
    void OnLastButClicked(MouseEvent event) {
    	
    }

    @FXML
    void OnNexButClicked(MouseEvent event) {
    	
    }

    @FXML
    void OnPrevButClick(MouseEvent event) {
    	
    }

    /**
     * 		Initialize
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Object> tmp = viewLocation.read();
		books = FXCollections.observableArrayList();
		for(int i = 0; i < tmp.size(); i++ ) {
			books.add( (Book)tmp.get(i) );
		}
		bookList.setItems(books);
	}
}
