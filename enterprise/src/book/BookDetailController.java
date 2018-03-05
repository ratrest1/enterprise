package book;

import publisher.Publisher;
import utils.ControllerBase;
import utils.GatewayBase;
import db.BookGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class BookDetailController extends ControllerBase{

    protected BookDetailController(GatewayBase bookGateway, Book arg) {
		super(bookGateway);
		detailedBook = arg;
		if( detailedBook.getId() == 0 ) {
			viewType = 1;
		}else {
			viewType = 0;
		}
	}

	@FXML private MenuItem authorMenu;
    @FXML private MenuItem bookMenu;
    @FXML private MenuItem quitMenu;
    @FXML private Label firstName;
    @FXML private TextField title;
    @FXML private ComboBox<Publisher> pubCombo;
    @FXML private TextField Isbn;
    @FXML private TextField yrPublished;
    @FXML private TextArea summary;
    @FXML private Button saveButton;
    
    private Book detailedBook;								//Author class of the specified author
    private int viewType;

    @FXML
    void OnAuthorClicked(ActionEvent event) {
    	getLoader().LoadController(getLoader().AUT_LIST, null);
    }

    @FXML
    void OnBookClicked(ActionEvent event) {
    	getLoader().LoadController(getLoader().BOK_LIST, null);
    }

    @FXML
    void OnCloseClicked(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void OnSaveBookClicked(MouseEvent event) {
    	detailedBook.setGateway((BookGateway)this.viewLocation);
		detailedBook.saveBook( viewType );
		getLoader().LoadController(getLoader().BOK_LIST, null);
    }

}