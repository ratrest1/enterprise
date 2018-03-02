package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.AppException;
import publisher.Publisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PublisherGateway {
	private static Logger logger = LogManager.getLogger();		//Logger
	private Connection conn;

	public PublisherGateway (Connection conn) {
		this.conn = conn;
	}
}
