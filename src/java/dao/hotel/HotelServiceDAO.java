/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author A00288569
 */
public class HotelServiceDAO {
    
    private Connection con = null;
    
    public static void main(String[] args) {
        HotelServiceDAO dao = new HotelServiceDAO();
      
        int nextHccNumber = dao.getNextCustomerNumber("123456");
        System.out.println(nextHccNumber);
    }
    
public HotelServiceDAO() {
        try {
            System.out.println("Loading db driver...");
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            con = DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/HotelManagement",
                    "shalini",
                    "shalini");
        } catch (Exception ex) {
            //Logger.getLogger(BankServiceDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Exception.");
            ex.printStackTrace();
        }
    }
public int getNextCustomerNumber(String branchCode){
        int newAccountNumber = -1;
        
        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT MAX(ACCOUNT_NUMBER) AS MAX_ACCOUNT_NUMBER FROM "
                    + " APP.HOTEL_TABLE WHERE (BRANCH_CODE = ?)");
            ps.setString(1, branchCode);

            ResultSet rs = ps.executeQuery();

            // move the cursor to the first row
            if (!rs.next()) {  // !F==T
                return -1;
            }

            newAccountNumber = rs.getInt("MAX_ACCOUNT_NUMBER") + 1;
            

        } catch (SQLException sqle) {
            System.err.println("SQLException in getNextAccountNumber()");
            sqle.printStackTrace();
        }
        
        return newAccountNumber;
    }
 public HotelAccount getAccountDetails(String branchCode,
            String accountNo) {
        HotelAccount hotelAccount = null;

        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM APP.HOTEL_TABLE WHERE "
                    + "(BRANCH_CODE = ? AND ACCOUNT_NUMBER=?)");
            ps.setString(1, branchCode);
            ps.setString(2, accountNo);

            ResultSet rs = ps.executeQuery();

            // move the cursor to the first row
            if (!rs.next()) {  // !F==T
                return null;
            }
            // process the record
            
            hotelAccount = new HotelAccount(
                    rs.getString("BRANCH_CODE"),
                    rs.getString("ACCOUNT_NUMBER"),
                    rs.getString("CUST_NAME"),
                    rs.getString("CUST_ADDRESS"),
                    rs.getString("CUST_TYPE"),
                    rs.getString("CUST_RATING"),
                    rs.getDouble("BILL"));

        } catch (SQLException sqle) {
            System.err.println("SQLException in getAccountDetails()");
            sqle.printStackTrace();
        }
        

        return hotelAccount;
    }
  public ArrayList<HotelAccount> getAllAccounts() {
        ArrayList<HotelAccount> hotelAccounts
                = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM APP.HOTEL_TABLE");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // process the record
                HotelAccount hotelAccount = new HotelAccount(
                        rs.getString("BRANCH_CODE"),
                        rs.getString("ACCOUNT_NUMBER"),
                        rs.getString("CUST_NAME"),
                        rs.getString("CUST_ADDRESS"),
                        rs.getString("CUST_TYPE"),
                        rs.getString("CUST_RATING"),
                        rs.getDouble("BILL"));

                hotelAccounts.add(hotelAccount);
            }
            } catch (SQLException sqle) {
            System.err.println("SQLException in getAccountDetails()");
            sqle.printStackTrace();
        }

        return hotelAccounts;

    }
  public void deleteHotelAccount(String branchCode,
            String accountNo) {
        try {
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM APP.HOTEL_TABLE WHERE "
                    + "(BRANCH_CODE = ? AND ACCOUNT_NUMBER=?)");
            ps.setString(1, branchCode);
            ps.setString(2, accountNo);

            ps.executeUpdate();

        } catch (SQLException sqle) {
            System.err.println("SQLException in deleteHotelAccount()");
            sqle.printStackTrace();
        }
    }
  public void deleteAllAccounts() {
        try {
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM APP.HOTEL_TABLE");

            ps.execute();

        } catch (SQLException sqle) {
            System.err.println("SQLException in deleteAllAccounts()");
            sqle.printStackTrace();
        }

    }
   public int addHotelAccount(HotelAccount ba) {
        try {
            // make sure that this account is not already
            // in the db
            if (getAccountDetails(ba.getBranchCode(), ba.getAccountNo()) != null) {
                // bank account is already in the db
                return -1;
            } else {
                // account is not in the db already
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO APP.Hotel_TABLE "
                                + "(BRANCH_CODE, ACCOUNT_NUMBER,"
                                + "CUST_NAME, CUST_ADDRESS, CUST_TYPE,"
                                + "CUST_RATING, BILL) "
                                + "VALUES (?,?,?,?,?,?,?)");

                ps.setString(1, ba.getBranchCode());
                ps.setString(2, ba.getAccountNo());
                ps.setString(3, ba.getCustName());
                ps.setString(4, ba.getCustAddress());
                ps.setString(5, ba.getCustType());
                ps.setString(6, ba.getCustRating());
               // ps.setDouble(7, ba.getBill());
                ps.executeUpdate();
            }   
        } catch (SQLException sqle) {
            System.err.println("SQLException in addHotelAccount()");
            sqle.printStackTrace();
            return -1;
        }
        return 1; // ok
    }

    public int updateHotelAccount(HotelAccount ba) {
        try {
            // make sure that this account is already
            // in the db
            if (getAccountDetails(ba.getBranchCode(), ba.getAccountNo()) == null) {
                // bank account is not in the db
                return -1;
            } else {
                // account is in the db 
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE APP.Hotel_TABLE "
                                + "SET CUST_NAME=?, CUST_ADDRESS=?, CUST_TYPE=?,"
                                + "CUST_RATING=?, BILL=? "
                                + "WHERE (BRANCH_CODE=? AND ACCOUNT_NUMBER=?)");

                ps.setString(1, ba.getCustName());
                ps.setString(2, ba.getCustAddress());
                ps.setString(3, ba.getCustType());
                ps.setString(4, ba.getCustRating());
               ps.setDouble(5, ba.getBill());
                ps.setString(6, ba.getBranchCode());
                ps.setString(7, ba.getAccountNo());

                ps.executeUpdate();
            }   
        } catch (SQLException sqle) {
            System.err.println("SQLException in addHotelAccount()");
            sqle.printStackTrace();
            return -1;
        }
        
        return 1; // ok
    }

}