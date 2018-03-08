package audit;

import java.net.URL;
import java.util.ResourceBundle;

import book.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import utils.ControllerBase;
import utils.GatewayBase;

public class AuditTrailController extends ControllerBase implements Initializable{

	Book currBook;
	protected AuditTrailController(GatewayBase auditGateway, Book b) {
		super(auditGateway);
		currBook = b;
	}

	@FXML private ListView<AuditTrailEntry> auditList;
	@FXML private Label auditLabel;
    @FXML private Button backButton;

    @FXML
    void OnBackClicked(MouseEvent event) {
    	getLoader().LoadController(getLoader().BOK_DETAIL, currBook);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/**
		 *  RIGHT HERE CLAYTON
		 */
		//auditList.setItems( auditGateway.fetchAuditTrail() );
	}
}
