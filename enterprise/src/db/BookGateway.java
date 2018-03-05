package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import author.Author;
import utils.AppException;
import utils.GatewayBase;
import book.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import publisher.Publisher;

public class BookGateway extends GatewayBase{
	private static Logger logger = LogManager.getLogger();		//Logger
	private Connection conn;
	public PublisherGateway pubGateway;

	public BookGateway (Connection conn) {
		this.conn = conn;
		pubGateway = new PublisherGateway(conn);
	}
	
	public void createBook (Book book) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into book( title, summary, year_published, isbn ) values( ?, ?, ?, ? )");
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPublished());
			st.setString(4, book.getIsbn());
			
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
	public ObservableList<Book> readBook () throws AppException {
		ObservableList<Book> books = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book order by id");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book();
				int pubID;
				
				book.setGateway(this);
				book.setId(rs.getInt("id"));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setYearPublished(rs.getInt("year_published"));
				pubID = rs.getInt("publisher_id");
				book.setPublisher(pubGateway.getPublisherById(pubID));
				book.setIsbn(rs.getString("isbn"));
				book.setDateAdded(rs.getDate("date_added").toLocalDate());
				
				books.add(book);
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
		return books;
	}
	
	public void updateBook (Book book) throws AppException {
		logger.info("Updating Book.");
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update book set isbn = ?, publisher_id = ?, year_published = ?, summary = ?, title = ? where id = ?");
			st.setString(1, book.getIsbn());
			//st.setString(2, book.getPublisher());
			st.setInt(3, book.getYearPublished());
			st.setString(4, book.getSummary());
			st.setString(5, book.getTitle());
			st.setInt(6, book.getId());
			
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

	public void deleteBook (Book book) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from book where id = ?");
			st.setLong(1, book.getId());
			
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
	
	public ObservableList<Book> searchBook (String searchStr) {
		ObservableList<Book> books = FXCollections.observableArrayList();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select title from book where title like ?");
			st.setString(1, "%" + searchStr + "%");
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book();
				int pubID;
				
				book.setGateway(this);
				book.setId(rs.getInt("id"));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setYearPublished(rs.getInt("year_published"));
				pubID = rs.getInt("publisher_id");
				book.setPublisher(pubGateway.getPublisherById(pubID));
				book.setIsbn(rs.getString("isbn"));
				book.setDateAdded(rs.getDate("date_added").toLocalDate());
				
				books.add(book);
			}
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
		return books;
	}

	@Override
	public ObservableList<Object> read() {
		ObservableList<Book> tmpAuthors = readBook();
		ObservableList<Object> retList = FXCollections.observableArrayList();
		for(int i = 0; i < tmpAuthors.size(); i++)
		{
			retList.add( tmpAuthors.get(i) );
		}
		return retList;
	}

	@Override
	public void delete(Object arg) {
		Book tmp = (Book) arg;
		deleteBook(tmp);
	}
	
}

