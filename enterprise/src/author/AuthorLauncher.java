/**
 * 		MenuLauncher:
 * 			The driver for testing the AuthorDetailView and AuthorListView.
 * 
 * 		CS 4743 Assignment 2 by Richard Trest and Clayton Varnon
 */
package author;
import java.net.URL;
import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.ConnectionFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import utils.AppException;
import utils.ControllerLoader;

public class AuthorLauncher extends Application{
	private static Logger logger = LogManager.getLogger();
	private Connection connection;
	
	/**
	 * 		The overridden class for javaFX to start the application.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		ControllerLoader loader = ControllerLoader.getInstance();
		loader.setConnection(connection);
		loader.setStage(stage);
		loader.LoadController(loader.AUT_LIST, null);
	}
	/**
	 * 		Initialize
	 */
	@Override
	public void init() throws Exception {
		super.init();
		logger.info("Creating connection...");	
		try {
			connection = ConnectionFactory.createConnection();
			logger.info("Connection complete.");
		} catch(AppException e) {
			logger.fatal("Cannot connect to db.");
			Platform.exit();
		}
	}

	/**
	 * 		When the application stops
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
		
		//TODO: find out how to attach to shutdown hook
		logger.info("Closing connection...");
		
		connection.close();
	}
	
	/**
	 * 		Main.
	 * @param args parameters for application.
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
