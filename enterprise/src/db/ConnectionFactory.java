package db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import utils.AppException;

public class ConnectionFactory {
	public static Connection createConnection() throws AppException {
		Logger logger = LogManager.getLogger();
		Connection conn = null;
		
		//read properties file
		try {
    			Properties props = new Properties();
    			FileInputStream fis = new FileInputStream("db.properties");
	        props.load(fis);
	        fis.close();

	        //create the datasource
	        MysqlDataSource ds = new MysqlDataSource();
	        ds.setURL(props.getProperty("MYSQL_DB_URL"));
	        ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
	        ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
			
			//create connection
	        conn = ds.getConnection();
	        
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		}
				
		return conn;
	}
}