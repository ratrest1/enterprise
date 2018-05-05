package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audit.AuditTrailEntry;
import author.Author;
import book.Book;
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
		createEntry(author.getfName(), author.getlName());
		logger.info("Author Created.");
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
			updateEntry(author);
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
		
		// delete audit trail
				st = null;
				try {
					st = conn.prepareStatement("delete from author_audit_trail where author_id = ?");
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
	
	/**
	 *    createEntry : This method creates an entry when CreateAuthor is called
	 */
	private void createEntry (String fname, String lname) throws AppException {
		PreparedStatement st = null;
		// get author's id
		try {
			st = conn.prepareStatement("select id from author where first_name = ? and last_name = ?");
			st.setString(1, fname);
			st.setString(2, lname);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Author author = new Author();
				author.setId(rs.getInt("id"));
				createAuditTrailEntry(author.getId(), "Author Created.");
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
	private void updateEntry (Author nAuthor) throws AppException {
		Author oAuthor = new Author();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author where id = ?");
			st.setInt(1, nAuthor.getId());
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				oAuthor.setId(rs.getInt("id"));
				oAuthor.setfName(rs.getString("first_name"));
				oAuthor.setlName(rs.getString("last_name"));
				oAuthor.setDateOfBirth(rs.getDate("dob").toLocalDate());
				oAuthor.setGender(rs.getString("gender"));
				oAuthor.setWebsite(rs.getString("website"));
			}
			
			if ( !nAuthor.getfName().equals(oAuthor.getfName()) ) {
				createAuditTrailEntry(nAuthor.getId(), "First name changed from " 
				+ oAuthor.getfName() + " to " + nAuthor.getfName());
			}
				
			if ( !nAuthor.getlName().equals(oAuthor.getlName()) ) {
				createAuditTrailEntry(nAuthor.getId(), "Last name changed from " 
				+ oAuthor.getfName() + " to " + nAuthor.getfName());
			}
			
			if (!nAuthor.getDateOfBirth().toString().equals(oAuthor.getDateOfBirth().toString())) {
				createAuditTrailEntry(nAuthor.getId(), "dob changed from " 
				+ oAuthor.getDateOfBirth() + " to " + nAuthor.getDateOfBirth());
			}
			
			if ( !nAuthor.getGender().equals(oAuthor.getGender()) ) {
				createAuditTrailEntry(nAuthor.getId(), "Gender changed from " 
				+ oAuthor.getGender() + " to " + nAuthor.getGender());
			}
			
			if ( !nAuthor.getWebsite().equals(oAuthor.getWebsite()) ) {
				createAuditTrailEntry(nAuthor.getId(), "Website changed from " 
				+ oAuthor.getWebsite() + " to " + nAuthor.getWebsite());
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
	 * createAuditTrailEntry : called by the above 2 functions and creates an audit trail entry in the db
	 */
	public void createAuditTrailEntry (int authorId, String message) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into author_audit_trail ( author_id, entry_msg ) values ( ?, ? )");
			st.setInt(1, authorId);
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
	
	public ObservableList<AuditTrailEntry> fetchAuditTrail (int authorId) throws AppException {
		logger.info("Fetching Audit Trail...");
		ObservableList<AuditTrailEntry> auditTrail = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author_audit_trail order by author_id = ?");
			st.setInt(1, authorId);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				AuditTrailEntry entry = new AuditTrailEntry();
				
				entry.setId(rs.getInt("author_id"));
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

	@Override
	public ObservableList<Object> getFirstPage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObservableList<Object> getNextPage(int pageNr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObservableList<Object> getPrevPage(int pageNr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObservableList<Object> getLastPage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
