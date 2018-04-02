package book;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import author.Author;
import authorBook.AuthorBook;
import db.AuthorGateway;
import db.BookGateway;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.ControllerBase;
import utils.GatewayBase;
import utils.ControllerLoader;

public class BookAddAuthorController extends ControllerBase implements Initializable{

	@FXML private MenuItem authorMenu;
    @FXML private MenuItem bookMenu;
    @FXML private MenuItem quitMenu;
    @FXML private TextField authorTextField;
    @FXML private TextField lastNameField;
    @FXML private TextField royaltyTextField;
    @FXML private Button addAuthorBut;
    
    AuthorBook  currAB;
    AuthorGateway ag;
    
    @FXML void OnAuthorClicked(ActionEvent event) {
    	getLoader().LoadController(getLoader().AUT_LIST, null);
    }

    @FXML void OnBookClicked(ActionEvent event) {
    	getLoader().LoadController(getLoader().BOK_LIST, null);
    }

    @FXML void OnCloseClicked(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML void OnAddAuthorClicked(MouseEvent event) {
    	BookGateway bg = (BookGateway) this.viewLocation;
    	ObservableList<Author> authors = ag.readAuthor();
    	for( int i = 0; i < authors.size(); i++ ) {
    		if( authors.get(i).getfName().equalsIgnoreCase( authorTextField.getText() ) &&
    				authors.get(i).getlName().equalsIgnoreCase( lastNameField.getText() )	) {
    			currAB.setMyAut( authors.get(i));
    		}
    	}
    	currAB.setRoyalty( BigDecimal.valueOf( Integer.parseInt( royaltyTextField.getText() ) ) );
    	bg.CreateAuthorBookRecord(currAB.getMyAut().getId(), currAB.getMyBook().getId(), currAB.getRoyalty().intValueExact());
    	getLoader().LoadController(ControllerLoader.BOK_DETAIL, currAB.getMyBook());
    }

	
	public BookAddAuthorController(GatewayBase authorGateway, Book currBook) {
		super(authorGateway);
		currAB = new AuthorBook();
		currAB.setMyBook(currBook);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ag = new AuthorGateway( getLoader().getConnection() );
	}

}
