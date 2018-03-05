package book;
import java.net.URL;
import java.util.ResourceBundle;

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

    protected BookListController(BookGateway bookGateway) {
		super((GatewayBase)bookGateway);
	}

	@FXML private MenuItem menuClose;
    @FXML private ListView<Book> bookList;
    @FXML private Button addBookButton;
    @FXML private Button deleteBookButton;
    @FXML private TextField searchTerm;
    
    
    private ObservableList<Book> books;						//List of Books
    private Book selBook;

    @FXML
    void OnAddClicked(MouseEvent event) {
    	Book b = new Book();
		getLoader().LoadController(ControllerLoader.BOK_DETAIL, b);
    }

    @FXML
    void OnSearchKeyPressed(KeyEvent event) {

    }
    
    @FXML
    void OnBookClicked(MouseEvent event) {
    	if(event.getClickCount() == 2) {
			Book b = bookList.getSelectionModel().getSelectedItem();
			getLoader().LoadController(ControllerLoader.BOK_DETAIL, b);
		} else {
			selBook = bookList.getSelectionModel().getSelectedItem();
		}
    }

    @FXML
    void OnClose(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void OnDeleteClicked(MouseEvent event) {
    	viewLocation.delete(selBook);
    	getLoader().LoadController(getLoader().BOK_LIST, null);
    }

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
