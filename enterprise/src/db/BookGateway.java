package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audit.BookAuditTrailEntry;
import utils.AppException;
import utils.GatewayBase;
import book.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookGateway extends GatewayBase{
	private static Logger logger = LogManager.getLogger();		//Logger
	private Connection conn;
	public PublisherGateway pubGateway;

	public BookGateway (Connection conn) {
		this.conn = conn;
		pubGateway = new PublisherGateway(conn);
	}
	
	public void createBook (Book book) throws AppException {
		logger.info("Creating Book...");
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
		createEntry(book.getTitle());
		logger.info("Book Created.");
	}
	
	// add authors from database
	public ObservableList<Book> readBook () throws AppException {
		//logger.info("Reading Book.");
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
		logger.info("Updating Book...");
		PreparedStatement st = null;
		try {
			updateEntry(book);
			st = conn.prepareStatement("update book set isbn = ?, "
					+ "publisher_id = ?, year_published = ?, "
					+ "summary = ?, title = ? where id = ?");
			st.setString(1, book.getIsbn());
			st.setInt(2, book.getPublisher().getId());
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
		logger.info("Book Updated.");
	}

	public void deleteBook (Book book) throws AppException {
		// delete book
		logger.info("Deleting Book...");
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
		logger.info("Book Deleted.");
		
		// delete audit trail
		st = null;
		try {
			st = conn.prepareStatement("delete from book_audit_trail where book_id = ?");
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
		//logger.info("Searching for books.");
		ObservableList<Book> books = FXCollections.observableArrayList();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book where title like ?");
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
	
	/**
	 *    createEntry : This method creates an entry when CreateBook is called
	 */
	private void createEntry (String title) throws AppException {
		PreparedStatement st = null;
		// get book's id
		try {
			st = conn.prepareStatement("select * from book where title = ?");
			st.setString(1, title);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt("id"));
				createAuditTrailEntry(book.getId(), "Book Created.");
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
	}
	
	/**
	 * updateEntry : This method creates an entry when UpdateBook is called
	 */
	private void updateEntry (Book oBook) throws AppException {
		Book nBook = new Book();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book where id = ?");
			st.setInt(1, oBook.getId());
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				nBook.setId(rs.getInt("id"));
				nBook.setTitle(rs.getString("title"));
				nBook.setSummary(rs.getString("summary"));
				nBook.setYearPublished(rs.getInt("year_published"));
				nBook.setIsbn(rs.getString("isbn"));
				nBook.setDateAdded(rs.getDate("date_added").toLocalDate());
			}
			
			if ( !nBook.getTitle().equals(oBook.getTitle()) )
				createAuditTrailEntry(nBook.getId(), "title changed from " + oBook.getTitle() + " to " + nBook.getTitle());
			
			if ( !nBook.getSummary().equals(oBook.getSummary()) )
				createAuditTrailEntry(nBook.getId(), "Summary changed.");
			
			if (nBook.getYearPublished() != oBook.getYearPublished()) {
				createAuditTrailEntry(nBook.getId(), "year_published changed from " 
				+ oBook.getYearPublished() + " to " + nBook.getYearPublished());
			}
			
			if ( !nBook.getIsbn().equals(oBook.getIsbn()) )
				createAuditTrailEntry(nBook.getId(), "isbn changed from " + oBook.getIsbn() + " to " + nBook.getIsbn());
			
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
	
	/**
	 * createAuditTrailEntry : called by the above 2 functions and creates an audit trail entry in the db
	 */
	public void createAuditTrailEntry (int bookId, String message) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into book_audit_trail ( book_id, entry_msg ) values ( ?, ? )");
			st.setInt(1, bookId);
			st.setString(2, message);
			
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
		logger.info("Created Audit Trail Entry.");
	}
	
	public ObservableList<BookAuditTrailEntry> fetchAuditTrail (int bookId) throws AppException {
		logger.info("Fetching Audit Trail...");
		ObservableList<BookAuditTrailEntry> auditTrail = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book_audit_trail order by book_id = ?");
			st.setInt(1, bookId);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				BookAuditTrailEntry entry = new BookAuditTrailEntry();
				
				entry.setId(rs.getInt("book_id"));
				entry.setDateAdded(rs.getDate("date_added"));
				entry.setMessage(rs.getString("entry_msg"));
				
				auditTrail.add(entry);
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
		logger.info("Got Audit Trail.");
		return auditTrail;
	}
	
}