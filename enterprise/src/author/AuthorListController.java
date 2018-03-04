/**
 * 		AuthorListController:
 * 			The controller for the AuthorListView.
 */
package author;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.AuthorGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import utils.ControllerBase;
import utils.ControllerLoader;

public class AuthorListController extends ControllerBase implements Initializable
{
	private static Logger logger = LogManager.getLogger();		//Logger
	
	@FXML private MenuItem menuClose;							//JavaFX Stuff
    @FXML private MenuItem sortBy;
    @FXML private ListView<Author> authorList;
    @FXML private Button deleteAuthorButton;
    @FXML private Button addAuthorButton;
    
    private ObservableList<Author> authors;						//List of Authors
    private Author selAuthor;
    
    /**
     * 		Constructor
     * @param dogGateway the view location on the disk.
     */
    public AuthorListController(AuthorGateway gateway) {
		super(gateway);
	}
    
    /**
     * 		When the user hits close application.
     * @param event
     */
    @FXML void OnClose(ActionEvent event) {
    	System.exit(0);
    }
    
    /**
     * 		When the user double-clicks on an author.
     * @param event
     */
    @FXML
    void OnAuthorClicked(MouseEvent event) {
    		if(event.getClickCount() == 2) {
    			logger.info("Author double-clicked.");
    			Author a = authorList.getSelectionModel().getSelectedItem();
    			getLoader().LoadController(ControllerLoader.AUT_DETAIL, a);
    		} else {
    			selAuthor = authorList.getSelectionModel().getSelectedItem();
    		}
    }
    
    @FXML
    void OnAddClicked(MouseEvent event) {
    	Author a = new Author();
		getLoader().LoadController(ControllerLoader.AUT_DETAIL, a);
    }
    
    @FXML
    void OnDeleteClicked(MouseEvent event) {
    	viewLocation.delete(selAuthor);
    	getLoader().LoadController(getLoader().AUT_LIST, null);
    }

    /**
     * 		When the controller is loaded.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<Object> tmp = viewLocation.read();
		authors = FXCollections.observableArrayList();
		for(int i = 0; i < tmp.size(); i++ ) {
			authors.add( (Author)tmp.get(i) );
		}
		
		authorList.setItems(authors);
	}
}
