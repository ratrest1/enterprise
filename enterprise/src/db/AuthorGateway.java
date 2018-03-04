package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import author.Author;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.AppException;
import utils.GatewayBase;

public class AuthorGateway extends GatewayBase{
	private static Logger logger = LogManager.getLogger();		//Logger
	private Connection conn;

	public AuthorGateway (Connection conn) {
		this.conn = conn;
	}
	
	public void createAuthor (Author author) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into author( first_name, last_name, dob, gender, website ) values( ?, ?, ?, ?, ?)");
			st.setString(1, author.getfName());
			st.setString(2, author.getlName());
			st.setDate(3, Date.valueOf( author.getDateOfBirth() ) );
			st.setString(4, author.getGender());
			st.setString(5, author.getWebsite());
			
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	
	// add authors from database
	public ObservableList<Author> readAuthor () throws AppException {
		ObservableList<Author> authors = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author order by id");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Author author = new Author(rs.getString("first_name"), rs.getString("last_name"));
				
				author.setGateway(this);
				author.setId(rs.getInt("id"));
				author.setDateOfBirth(rs.getDate("dob").toLocalDate());
				author.setGender(rs.getString("gender"));
				author.setWebsite(rs.getString("website"));
				authors.add(author);
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
		return authors;
	}
	
	public void updateAuthor (Author author) throws AppException {
		logger.info("Updating.");
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update author set website = ?, gender = ?, dob = ?, last_name = ?, first_name = ? where id = ?");
			st.setString(1, author.getWebsite());
			st.setString(2, author.getGender());
			st.setDate(3, Date.valueOf( author.getDateOfBirth() ) );
			logger.info(author.getlName());
			st.setString(4, author.getlName());
			st.setString(5, author.getfName());
			st.setInt(6, author.getId());
			
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}

	public void deleteAuthor (Author author) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from author where id = ?");
			st.setLong(1, author.getId());
			
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}

	@Override
	public ObservableList<Object> read() {
		ObservableList<Author> tmpAuthors = readAuthor();
		ObservableList<Object> retList = FXCollections.observableArrayList();
		for(int i = 0; i < tmpAuthors.size(); i++)
		{
			retList.add( tmpAuthors.get(i) );
		}
		return retList;
	}

	@Override
	public void delete(Object arg) {
		Author tmp = (Author) arg;
		deleteAuthor(tmp);
	}
}
