package utlities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionFactory {
  final static Logger logger = Logger.getLogger(ConnectionFactory.class);
  
  private static ConnectionFactory cf = null;
  
  public static synchronized ConnectionFactory getInstance() {
    if (cf == null) {
      cf = new ConnectionFactory();
    }
    
    return cf;
  }
  
  public Connection getConnection(boolean production) {
    Connection conn = null;
    Properties prop = new Properties();
    
    try {
      if (production) {
        prop.load(new FileReader("src/main/resources/database.properties"));
      } else {
        prop.load(new FileReader("src/test/resources/database.properties"));
      }
      Class.forName(prop.getProperty("driver"));
      conn = DriverManager.getConnection(
          prop.getProperty("url"),
          prop.getProperty("usr"),
          prop.getProperty("psw")
        );
      conn.setAutoCommit(false);
    } catch (FileNotFoundException e) {
      // This really shouldn't happen. We preconfigured the database settings
      logger.fatal("Database properties cold not be loaded", e);
    } catch (IOException e) {
      // This is even worse than not finding the file
      logger.fatal("OS would not grant database properties read rights", e);
    } catch (ClassNotFoundException e) {
      // I'm not sure what this means
      logger.fatal(e);
    } catch (SQLException e) {
      // I think this means we weren't able to connect to the database
      logger.fatal(e);
    }
    
    return conn;
    
  }

}
