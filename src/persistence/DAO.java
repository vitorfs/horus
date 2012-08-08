package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Vitor Freitas
 * vitorfs@gmail.com
 */
public abstract class DAO {
    
    public void closeResources(Connection conn, Statement st, ResultSet rs){
        try {
            if(st!=null) st.close();
            if(conn!=null) conn.close();
            if(rs!=null) rs.close();
        } catch (SQLException e){
            
        }
    }    
    
    public void closeResources(Connection conn, Statement st){
        try {
            if(st!=null) st.close();
            if(conn!=null) conn.close();
        } catch (SQLException e){
            
        }
    }
    
    public void setParam(PreparedStatement st, int index, String value) throws SQLException{
        if (value==null) 
            st.setNull(index, java.sql.Types.NULL);
        else
            st.setString(index, value);
        
    }

    public void setParam(PreparedStatement st, int index, Integer value) throws SQLException{
        if (value==null) 
            st.setNull(index, java.sql.Types.NULL);
        else
            st.setInt(index, value);        
    }  
    
    public void setParam(PreparedStatement st, int index, Float value) throws SQLException{
        if (value==null)
            st.setNull(index, java.sql.Types.NULL);
        else
            st.setFloat(index, value);
    }
    
    public synchronized ResultSet execute(){
        return null;
    }
    
}
