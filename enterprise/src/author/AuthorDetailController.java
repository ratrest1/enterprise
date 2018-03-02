package author;


import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.AuthorGateway;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.ControllerBase;

public class AuthorDetailController extends ControllerBase implements Initializable{

	private static Logger logger = LogManager.getLogger();		//Logger
	
	@FXML private MenuItem authorMenu;							//JavaFX Stuff
	@FXML private MenuItem quitMenu;
    @FXML private Label firstName;
    @FXML private TextField fName;
    @FXML private TextField lName;
    @FXML private TextField gender;
    @FXML private TextField website;
    @FXML private Button saveButton;
    @FXML private DatePicker dob;
    
    private Author detailedAuthor;								//Author class of the specified author
    private int viewType;
    
    /**
     * 		Constructor
     * @param authorGateway	The view location on the disk
     * @param arg	The author being observed
     */
	public AuthorDetailController(AuthorGateway authorGateway, Author arg) {
		super(authorGateway);
		detailedAuthor = arg;
		if( detailedAuthor.getId() == 0 ) {
			viewType = 1;
			logger.info("CreateAuthor");
		}else {
			logger.info("UpdateAuthor");
			viewType = 0;
		}
	}

	/**
	 * 		When the user clicks the menubutton to go back to author list view.
	 * @param event
	 */
	@FXML void OnAuthorClicked(ActionEvent event) {
		getLoader().LoadController(getLoader().AUT_LIST, null);
    }
	
	/**
	 * 		When the user closes the application.
	 * @param event
	 */
	@FXML void OnCloseClicked(ActionEvent event) {
		System.exit(0);
	}
	
	/**
	 * 		When the user saves the author's details.
	 * @param event
	 */
	@FXML
    void OnSaveAuthorClicked(MouseEvent event) {
		detailedAuthor.setGateway(this.viewLocation);
		detailedAuthor.saveAuthor( viewType );
		getLoader().LoadController(getLoader().AUT_LIST, null);
    }

	/**
	 * 		When the controller is loaded.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fName.textProperty().bindBidirectional(detailedAuthor.fNameProperty());
		lName.textProperty().bindBidirectional(detailedAuthor.lNameProperty());
		dob.valueProperty().bindBidirectional(detailedAuthor.dateOfBirthProperty());
		gender.textProperty().bindBidirectional(detailedAuthor.genderProperty());
		website.textProperty().bindBidirectional(detailedAuthor.websiteProperty());
	}
	
}
