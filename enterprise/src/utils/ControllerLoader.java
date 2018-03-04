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

import author.Author;
import author.AuthorDetailController;
import author.AuthorListController;
import db.AuthorGateway;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ControllerLoader {
	public static final int AUT_LIST = 1;
	public static final int AUT_DETAIL = 2;
	public static final int BOK_LIST = 3;
	public static final int BOK_DETAIL = 4;
	
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
			switch(viewType) {
				case AUT_LIST:
					fxmlFile = this.getClass().getResource("../author/AuthorListView.fxml");
					controller = new AuthorListController(new AuthorGateway(connection));
					break;
				case AUT_DETAIL:
					fxmlFile = this.getClass().getResource("../author/AuthorDetailView.fxml");
					controller = new AuthorDetailController(new AuthorGateway(connection),(Author) arg);
					break;
			}
		
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(controller);
		
			Parent viewNode = loader.load();
			Scene scene = new Scene(viewNode,600,400);
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
