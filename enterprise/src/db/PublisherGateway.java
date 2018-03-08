package db;

import java.sql.Connection;
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
	//private static Logger logger = LogManager.getLogger();		//Logger
	private Connection conn;

	public PublisherGateway (Connection conn) {
		this.conn = conn;
	}
	
	public ObservableList<Publisher> getPublishers () throws AppException {
		ObservableList<Publisher> publishers = FXCollections.observableArrayList();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from publisher order by id");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Publisher publisher = new Publisher();
				
				publisher.setId(rs.getInt("id"));
				publisher.setPublisherName(rs.getString("publisher_name"));
				publishers.add(publisher);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		return publishers;
	}
	
	public Publisher getPublisherById (int pubID) {
		// In the future, use a query where I use the ID to fetch the publisher
		ObservableList<Publisher> publishers = getPublishers();
		
		for (Publisher publisher : publishers)
			if (publisher.getId() == pubID)
				return publisher;
		
		return null;
	}
}
