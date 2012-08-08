package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Vitor Freitas
 * contact vitorfs@gmail.com
 */
public final class DatabaseLocator {
    private static final DatabaseLocator INSTANCE = new DatabaseLocator();
    
    public static DatabaseLocator getInstance(){
        return INSTANCE;
    }
    
    private DatabaseLocator(){
        
    }
    
    public Connection getConnection() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/horus", "root", "");
        return conn;
    }
    
}
