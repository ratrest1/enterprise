package audit;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import author.Author;
import db.AuthorGateway;
import javafx.fxml.Initializable;
import utils.ControllerBase;
import utils.GatewayBase;

public class AuthorAuditTrailController extends ControllerBase implements Initializable{

	Author currAut;
	@FXML private Label auditLabel;
    @FXML private Button backButton;
    @FXML private ListView<AuditTrailEntry> auditList;
	
	public AuthorAuditTrailController(GatewayBase authorGateway, Author a) {
		super(authorGateway);
		currAut = a;
	}
	
	@FXML
    void OnBackClicked(MouseEvent event) {
		
		getLoader().LoadController(getLoader().AUT_DETAIL, currAut);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		AuthorGateway bg = (AuthorGateway) this.viewLocation;
		auditList.setItems( bg.fetchAuditTrail(currAut.getId()) );
		auditLabel.setText("Audit Trail for " + currAut.getfName());
	}

}
