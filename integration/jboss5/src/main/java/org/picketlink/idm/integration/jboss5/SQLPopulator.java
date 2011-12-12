/**
 * 
 */
package org.picketlink.idm.integration.jboss5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * 
 * It uses datasource to populate the sqlscript file into target database.
 * 
 * @author  Jeff Yu
 *
 */
public class SQLPopulator {
	
	private static Logger logger = Logger.getLogger(SQLPopulator.class.getName());
	
	private String SQLScript;
	
	private String exitSQL;
	
	private String datasource;
	
	public SQLPopulator(String datasource, String SQLScript, String exitSQL) {
		this.SQLScript = SQLScript;
		this.exitSQL = exitSQL;
		this.datasource = datasource;
	}
	
	public void populateSchema() throws Exception {
		   DataSource ds = (DataSource)new InitialContext().lookup(datasource);
		   Connection conn = ds.getConnection();
		   boolean load = false;
	
		   Statement st = conn.createStatement();
		   ResultSet rs = null;
	      try
	      {
	         rs = st.executeQuery(exitSQL.trim());
	         rs.close();
	      }
	      catch (SQLException e)
	      {
	         load = true;
	      }
	      st.close();
	      if (!load)
	      {
	         logger.info(datasource + " datasource is already initialized");
	         return;
	      }
	
	      logger.info("Initializing " + datasource + " from listed sql files");
	
	      String[] list = SQLScript.split(",");
	      for (String sql : list)
	      {
	         executeSql(sql.trim(), conn);
	      }		
	}
	
	private void executeSql(String resource, Connection conn)
	{
			  URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
			  try {
				  String sql = new String(readStream(url.openStream()) , "UTF-8");
				  sql = sql.replaceAll("(?m)^--([^\n]+)?$", ""); // Remove all commented lines
				  final String[] statements ; 
				  statements = sql.split(";");
				  
				  for (String statement : statements)
				  {
					  if ((statement == null) || ("".equals(statement.trim()))) {
					  } else {
				         Statement sqlStatement = conn.createStatement();
				         try
				         {
				            sqlStatement.executeUpdate(statement);
				         } catch (Exception e) {
				        	 logger.log(Level.WARNING, "Exception in executing :" + statement, e);
				         }
				         finally
				         {
				            sqlStatement.close();
				         }
					  }
				  }
			  } catch (Exception e) {
				  logger.log(Level.WARNING, "Exception in populating :" + resource, e);
			  }
		}


	private byte[] readStream(InputStream stream) {
		if(stream == null) {
			throw new IllegalArgumentException("null 'stream' arg passed in method call.");
		}
		
		ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
		byte[] buffer = new byte[256];
		int readCount = 0;
		
		try {
			while((readCount = stream.read(buffer)) != -1) {
				outBuffer.write(buffer, 0, readCount);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error reading stream.", e);
		}		
		
		return outBuffer.toByteArray();
	}

}
