package book;

import publisher.Publisher;
import authorBook.AuthorBook;
import utils.ControllerBase;
import utils.GatewayBase;

import java.net.URL;
import java.util.ResourceBundle;

import db.BookGateway;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.NumberStringConverter;

public class BookDetailController extends ControllerBase implements Initializable{
	@FXML private MenuItem authorMenu;						//JavaFX stuff
    @FXML private MenuItem bookMenu;
    @FXML private MenuItem quitMenu;
    @FXML private Label firstName;
    @FXML private TextField title;
    @FXML private ComboBox<Publisher> pubCombo;
    @FXML private TextField Isbn;
    @FXML private TextField yrPublished;
    @FXML private TextArea summary;
    @FXML private Button saveButton;
    @FXML private Button auditTrailButton;
    @FXML private Button addAuthor;
    @FXML private Button delAuthor;
    @FXML private ListView<AuthorBook> authorList;
    
    private Book detailedBook;								//Book class of the specified book
    private int viewType;									//Whether the book is being created=1 or updated=0

	/**
	 * 		Constructor
	 * @param bookGateway the gateway of the book
	 * @param arg	the book being looked at
	 */
    public BookDetailController(GatewayBase bookGateway, Book arg) {
		super(bookGateway);
		detailedBook = arg;
		if( detailedBook.getId() == 0 ) {
			viewType = 1;
		}else {
			viewType = 0;
		}
	}

    /**
     * 		When the Author List menu button is pressed.
     * @param event
     */
    @FXML
    void OnAuthorClicked(ActionEvent event) {
    	getLoader().LoadController(getLoader().AUT_LIST, null);
    }

    /**
     * 		When the Book List menu button is pressed.
     * @param event
     */
    @FXML
    void OnBookClicked(ActionEvent event) {
    	getLoader().LoadController(getLoader().BOK_LIST, null);
    }
    
    @FXML
    void OnAddAuthorClicked(MouseEvent event) {
    	getLoader().LoadController(getLoader().BOK_ADDAUT, detailedBook);
    }
    
    @FXML
    void OnDelAutClicked(MouseEvent event) {
    	BookGateway bg = (BookGateway)this.viewLocation;
    	bg.DeleteAuthorBookRecord(detailedBook.getId(), authorList.getSelectionModel().getSelectedItem().getMyAut().getId());
    	authorList.setItems( bg.GetAuthorsForBook(detailedBook) );
    }

    /**
     * 		When the close menu button is pressed.
     * @param event
     */
    @FXML
    void OnCloseClicked(ActionEvent event) {
    	System.exit(0);
    }

    /**
     * 		When the Save Book button is pressed.
     * @param event
     */
    @FXML
    void OnSaveBookClicked(MouseEvent event) {
    	detailedBook.setGateway((BookGateway)this.viewLocation);
		detailedBook.saveBook( viewType );
		getLoader().LoadController(getLoader().BOK_LIST, null);
    }
    
    /**
     * 		When the Audit Trail button is clicked.
     * @param event
     */
    @FXML
    void OnAuditClicked(MouseEvent event) {
    	getLoader().LoadController(getLoader().BOK_AUDIT, detailedBook);
    }
    
    /**
     * 		When the view is initialized
     */
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		title.textProperty().bindBidirectional(detailedBook.titleProperty());
		summary.textProperty().bindBidirectional(detailedBook.summaryProperty());
		yrPublished.textProperty().bindBidirectional(detailedBook.yearPublishedProperty(),  new NumberStringConverter() );
		BookGateway bg =  (BookGateway) this.viewLocation;
		pubCombo.setItems( bg.pubGateway.getPublishers() );
		pubCombo.setValue(detailedBook.getPublisher());
		Isbn.textProperty().bindBidirectional(detailedBook.isbnProperty());
		authorList.setItems( bg.GetAuthorsForBook(detailedBook) );
	}

}