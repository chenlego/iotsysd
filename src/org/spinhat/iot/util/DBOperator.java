package org.spinhat.iot.util;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 

import org.spinhat.iot.IotSysdConfig;
 
public class DBOperator { 
  private Connection dbconn = null;
  private PreparedStatement pstat = null; 
  private String mysqlMaster = null;
  private int mysqlPort;
  
  public DBOperator() 
  { 
	this.mysqlMaster = IotSysdConfig.getMysqlMaster();
	this.mysqlPort = IotSysdConfig.getmysqlPort();
	
    try 
    { 
      Class.forName("com.mysql.jdbc.Driver");
      String url = "jdbc:mysql://" + this.mysqlMaster + "/iot_system";
      String user = "root";
      String password = "iot9134";
      dbconn = DriverManager.getConnection(url, user, password);       
    } 
    catch(ClassNotFoundException e) 
    { 
      System.out.println("DriverClassNotFound :" + e.toString()); 
    }
    catch(SQLException e) 
    { 
      System.out.println("Exception :"+ e.toString()); 
    } 
  } 

  public boolean isDeviceActivated(String model, String machine_id) 
  { 
    String sql = "SELECT count(*) AS count FROM Devices WHERE model = ? AND machine_id = ? AND activated = 'Y'";
    int did = 0;
    try 
    { 
      pstat = dbconn.prepareStatement(sql); 
      pstat.setString(1, model);
      pstat.setString(2, machine_id);
      ResultSet rs = pstat.executeQuery(); 
      if (rs.next())
      {
          did = rs.getInt("count");
          if (did == 1)
          {
        	  return true;
          }
      }
    }
    catch(SQLException e) 
    { 
      System.out.println("InsertDB Exception :" + e.toString()); 
    } 
    return false;
  }
}