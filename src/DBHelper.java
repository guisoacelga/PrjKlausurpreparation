import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    String url = "jdbc:sqlite:C://sqlite/db/DBExamPrep.db";

    public  void createCustomerTable( ) {

        // SQL statement for creating a new table
        String dllCreateTableCustomer = "CREATE TABLE ";
        dllCreateTableCustomer += "Customer (CustomerId INTEGER PRIMARY KEY AUTOINCREMENT,\n";
        dllCreateTableCustomer += "	FirstName VARCHAR(255) NOT NULL,\n";
        dllCreateTableCustomer += "	LastName VARCHAR(255) NOT NULL, \n";
        dllCreateTableCustomer += " Gender VARCHAR(5), \n";
        dllCreateTableCustomer += "	BonusPoints DECIMAL(10,2)\n";
        dllCreateTableCustomer += ")";
        System.out.println("DLL to create Customer-Table " + dllCreateTableCustomer);

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // create a new table --> executes the query passed in the parameter
            stmt.execute(dllCreateTableCustomer);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createInvoiceTable(){
        String ddlCreateInvoicesCustomer = "CREATE TABLE ";
        ddlCreateInvoicesCustomer += " Invoice (InvoiceId INTEGER PRIMARY KEY AUTOINCREMENT, ";
        ddlCreateInvoicesCustomer += " CustomerId INTEGER, ";
        ddlCreateInvoicesCustomer += " DateInvoice varchar(20), ";
        ddlCreateInvoicesCustomer += " TotalAmount decimal(10,2), ";
        ddlCreateInvoicesCustomer += " FOREIGN KEY (CustomerId) REFERENCES Customer(CustomerId)";
        ddlCreateInvoicesCustomer += ")";
        System.out.println("DDL to create the Invoice-Table " +  ddlCreateInvoicesCustomer);

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(ddlCreateInvoicesCustomer);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int insertCustomer(Customer newCustomer){

        int lastId = 0;

        String insertSQL="INSERT INTO Customer(FirstName, LastName, Gender, BonusPoints) ";
        insertSQL += "Values(?,?,?,?)";
        String sqlText = "SELECT last_insert_rowid() as rowid;";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pStmt = conn.prepareStatement(insertSQL);
             PreparedStatement stmtLastRowId = conn.prepareStatement(sqlText)) {
            pStmt.setString(1,newCustomer.getFirstName());
            pStmt.setString(2,newCustomer.getLastName());
            pStmt.setString(3,newCustomer.getGender());
            pStmt.setDouble(4,newCustomer.getBonusPoints());

            pStmt.executeUpdate();

            ResultSet rs = null;

            rs = stmtLastRowId.executeQuery();
            rs.next();
            lastId = rs.getInt("rowid");
            rs.close();
            pStmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        newCustomer.setCustomerId(lastId);
        return lastId;
    }
}
