/**
 * 		ControllerLoader:
 * 			ControllerLoader is a singleton class meant to switch views and controllers.
 */
package utils;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audit.AuthorAuditTrailController;
import audit.BookAuditTrailController;
import author.Author;
import author.AuthorDetailController;
import author.AuthorListController;
import book.Book;
import book.BookDetailController;
import book.BookListController;
import db.AuthorGateway;
import db.BookGateway;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ControllerLoader {
	public static final int AUT_LIST = 1;
	public static final int AUT_DETAIL = 2;
	public static final int AUT_AUDIT = 3;
	
	public static final int BOK_LIST = 4;
	public static final int BOK_DETAIL = 5;
	public static final int BOK_AUDIT = 6;
	
	
	private Connection connection;
	private static Logger logger = LogManager.getLogger();		//Logger
	private static ControllerLoader instance = null;			    //Instance of singleton
	private Stage stage;										    //Stage for placing views and controllers
	private BorderPane rootPane = null;
	
	/**
	 * 		Constructor
	 */
	protected ControllerLoader() {
		//Specifying the default constructor
	}
	
	/**
	 * 		Gets the current instance of the class
	 * @return the instance of the class
	 */
	public static ControllerLoader getInstance() {
		if(instance == null) {
			instance = new ControllerLoader();
			
		}
		return instance;
	}
	
	/**
	 * 		Loads a view and controller based on ControllerBase class
	 * @param controller of the view that will be loaded into the stage
	 */
	public void LoadController(int viewType, Object arg){
		try {
			ControllerBase controller = null;
			URL fxmlFile = null;
			int wPos = 600;
			int hPos = 400;
			switch(viewType) {
				case AUT_LIST:
					fxmlFile = this.getClass().getResource("../author/AuthorListView.fxml");
					controller = new AuthorListController(new AuthorGateway(connection));
					break;
				case AUT_DETAIL:
					fxmlFile = this.getClass().getResource("../author/AuthorDetailView.fxml");
					controller = new AuthorDetailController(new AuthorGateway(connection),(Author) arg);
					break;
				case AUT_AUDIT:
					fxmlFile = this.getClass().getResource("../audit/authorAuditTrailView.fxml"); 
					controller = new AuthorAuditTrailController(new AuthorGateway(connection),(Author) arg);
					break;
				case BOK_LIST:
					fxmlFile = this.getClass().getResource("../book/BookListView.fxml");
					controller = new BookListController(new BookGateway(connection));
					break;
				case BOK_DETAIL:
					fxmlFile = this.getClass().getResource("../book/BookDetailView.fxml");
					controller = new BookDetailController(new BookGateway(connection),(Book) arg);
					wPos = 900;
					hPos = 600;
					break;
				case BOK_AUDIT:
					fxmlFile = this.getClass().getResource("../audit/bookAuditTrailView.fxml"); 
					controller = new BookAuditTrailController(new BookGateway(connection),(Book) arg);
					break;
			}
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(controller);
			Parent viewNode = loader.load();
			Scene scene = new Scene(viewNode,wPos,hPos);
			stage.setScene(scene);
			stage.setTitle("Book List");
			stage.show();
			//rootPane.setCenter(viewNode);
		} catch (IOException e) {
			throw new AppException(e);
		}
		
	}

	//Accessor
	public void setStage( Stage stage ) {
		this.stage = stage;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
