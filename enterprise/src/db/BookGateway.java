package db;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audit.AuditTrailEntry;
import author.Author;
import authorBook.AuthorBook;
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
		logger.info("Deleting Book...");
		//DeleteAuthorBookRecord(book.getId());
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
	
	
	
	
	
//-------------------------------------------AUTHORBOOK RELATIONSHIP-----------------------------------------------------//
	
	
	
	
	
	/**
	 * GetAuthorsForBook : gets all authors for a specific book
	 */
	public ObservableList<AuthorBook> GetAuthorsForBook (Book book) throws AppException {
		ObservableList<AuthorBook> authorBooks = FXCollections.observableArrayList();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author_book where book_id = ?");
			st.setInt(1, book.getId());
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				AuthorBook authorBook = new AuthorBook();
				int authorId;
				//int val;
				
				authorId = rs.getInt("author_id");
				authorBook.setMyAut(GetAuthorById(authorId));
				authorBook.setMyBook(book);
				BigDecimal num = rs.getBigDecimal("royalty");
				authorBook.setRoyalty(num);
				authorBooks.add(authorBook);
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
		return authorBooks;
	}
	
	/**
	 * GetAuthorById : gets a specific author by Id
	 */
	
	public Author GetAuthorById (int authorId) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author where id = ?");
			st.setInt(1, authorId);
			
			ResultSet rs = st.executeQuery();
			rs.next();
			Author author = new Author(rs.getString("first_name"), rs.getString("last_name"));
				
			author.setId(rs.getInt("id"));
			author.setDateOfBirth(rs.getDate("dob").toLocalDate());
			author.setGender(rs.getString("gender"));
			author.setWebsite(rs.getString("website"));
				
			return author;
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
	 * CreateAuthorBookRecord : creates an AuthorBook record as specified by user
	 */
	public void CreateAuthorBookRecord (int authorId, int bookId, BigDecimal royalty) throws AppException {
		PreparedStatement st = null;
		try {
			// BigDecimal route (if royalty is a BigDecimal)
			BigDecimal bd = royalty;
			
			// Int route (if royalty is an int)
			//BigDecimal bd = new BigDecimal(royalty);
			
			bd.divide(new BigDecimal(100000));
			
			st = conn.prepareStatement("insert into author_book( author_id, book_id, royalty ) values( ?, ?, ? )");
			st.setInt(1, authorId);
			st.setInt(2, bookId);
			st.setBigDecimal(3, bd);
			
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
		createAuditTrailEntry(bookId, "Author added to book.");
	}
	
	/**
	 * UpdateAuthorBookRecord : Updates an AuthorBook record
	 */
	public void UpdateRoyalty (AuthorBook AB) throws AppException {
		logger.info("Updating Book...");
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update AuthorBook set royalty = ? where author_id = ? and book_id = ?");
			BigDecimal num = AB.getRoyalty(); // if royalty is a BigDecimal
			//BigDecimal num = new BigDecimal(royalty); // if royalty is an int
			num = num.divide(new BigDecimal(100000));
			st.setBigDecimal(1,  num);
			st.setInt(2, AB.getMyAut().getId());
			st.setInt(3, AB.getMyBook().getId());
			
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
		createAuditTrailEntry(AB.getMyBook().getId(), "Author's royalty has changed.");
	}
	
	/**
	 * DeleteAuthorBookRecord : deletes corresponding AuthorBook records of a book upon deletion
	 */
	public void DeleteAuthorBookRecord (int bookId, int authorId) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from author_book where book_id = ? AND author_id = ?");
			st.setInt(1, bookId);
			st.setInt(2, authorId);
			
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
		createAuditTrailEntry(bookId, "Author deleted from book.");
	}
	
	
	
	
	
//-------------------------------------------------AUDIT TRAIL----------------------------------------------------------//
	
	
	
	
	
	/**
	 *    createEntry : This method creates an entry when CreateBook is called
	 */
	private void createEntry (String title) throws AppException {
		PreparedStatement st = null;
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
	private void updateEntry (Book nBook) throws AppException {
		Book oBook = new Book();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book where id = ?");
			st.setInt(1, nBook.getId());
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				oBook.setId(rs.getInt("id"));
				oBook.setTitle(rs.getString("title"));
				oBook.setSummary(rs.getString("summary"));
				oBook.setYearPublished(rs.getInt("year_published"));
				oBook.setIsbn(rs.getString("isbn"));
				oBook.setDateAdded(rs.getDate("date_added").toLocalDate());
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
	
	public ObservableList<AuditTrailEntry> fetchAuditTrail (int bookId) throws AppException {
		logger.info("Fetching Audit Trail...");
		ObservableList<AuditTrailEntry> auditTrail = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book_audit_trail order by book_id = ?");
			st.setInt(1, bookId);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				AuditTrailEntry entry = new AuditTrailEntry();
				
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