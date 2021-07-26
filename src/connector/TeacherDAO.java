package connector;

import java.awt.print.Book;
import java.io.*;
import java.sql.*;
import java.util.*;


import com.Teacher;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;


/**
 * Sample Java program that imports data from an Excel file to MySQL database.
 *
 * @author chloe yang
 */

public class TeacherDAO {
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;

    public TeacherDAO(String jdbcURL,String jdbcUsername,String jdbcPassword){
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    public void connect() throws SQLException {
        if (jdbcConnection ==null || jdbcConnection.isClosed()){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");

            }catch (ClassNotFoundException e){
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }



    //ATTENTION Create: : insertTeacher(Teacher)- this inserts a new row into the table Teacher.
    public boolean insertTeacher(Teacher teacher) throws SQLException {
        String sql = "INSERT INTO teacher ( name, preferredTimeSlots) VALUES (?, ?)";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, teacher.getName());
        statement.setString(2,teacher.getPreferredTimeSlots());

        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    //ATTENTION Read: listAllTeachers() - this retrieves all rows; and getTeacher(idTeacher)- returns a specific row based on the primary key value (ID).
    public List<Teacher> listAllTeachers() throws SQLException {
        List<Teacher> listTeacher = new ArrayList<>();

        String sql = "SELECT * FROM teacher";

        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int idTeacher = resultSet.getInt("idTeacher");
            String name = resultSet.getString("name");
            String preferredTimeSlots = resultSet.getString("preferredTimeSlots");

            Teacher teacher = new Teacher(idTeacher,name,preferredTimeSlots);
            listTeacher.add(teacher);
        }

        resultSet.close();
        statement.close();

        disconnect();

        return listTeacher;
    }


    //ATTENTION Update: updateBook(Teacher)- this updates an existing row in the database.
    public boolean updateTeacher(Teacher teacher) throws SQLException {
        String sql = "UPDATE teacher SET name = ?, preferredTimeSlots = ?";
        sql += " WHERE idTeacher = ?";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, teacher.getName());
        statement.setString(2, teacher.getPreferredTimeSlots());
        statement.setInt(3,teacher.getId());

        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    //ATTENTION Delete: deleteTeacher(Teacher) - this removes an existing row in the database based on the primary key value (ID).
    public boolean deleteTeacher(Teacher teacher) throws SQLException {
        String sql = "DELETE FROM teacher where idTeacher = ?";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, teacher.getId());

        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowDeleted;
    }

    public Teacher getTeacher(int idTeacher) throws SQLException {
        Teacher teacher = null;
        String sql = "SELECT * FROM teacher WHERE idTeacher = ?";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1,teacher.getId());

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String preferredTimeSlots = resultSet.getString("preferredTimeSlots");

            teacher = new Teacher(idTeacher,name,preferredTimeSlots);
        }

        resultSet.close();
        statement.close();

        return teacher;
    }

//    public static void main(String[] args) throws SQLException {
//        // write your code here
//        String jdbcURL = "jdbc:mysql://localhost:3306/hstp?sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
//        String username = "root";
//        String password = "m0920776286";
//
//        String excelFilePath = "C:\\Users\\Chloe\\Desktop\\1072.xlsx";
//
//        int batchSize = 50;
//
//        Connection connection = null;
//
//        try {
//            long start = System.currentTimeMillis();
//
//            FileInputStream inputStream = new FileInputStream(excelFilePath);
//
//            System.out.println("o");
//            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//            System.out.println("ok");
//            Sheet firstSheet = workbook.getSheetAt(5);
//            Iterator<Row> rowIterator = firstSheet.iterator();
//
//            connection = DriverManager.getConnection(jdbcURL, username, password);
//
//            /*begin the transaction
//            -- we have to disable the auto commit mode to enable two or more statements to be grouped into a transaction)*/
//            connection.setAutoCommit(false);
//
//            String sql = "INSERT INTO teacher (idteacher,name, preferredTimeslots) VALUES (?,?,?)";
//            PreparedStatement statement = connection.prepareStatement(sql);
//
//            int count = 0;
//
//            rowIterator.next(); // skip the header row
//
//            while (rowIterator.hasNext()) {
//                Row nextRow = rowIterator.next();
//                Iterator<Cell> cellIterator = nextRow.cellIterator();
//
//                while (cellIterator.hasNext()) {
//                    Cell nextCell = cellIterator.next();
//
//                    int columnIndex = nextCell.getColumnIndex();
//
//                    switch (columnIndex) {
//                        case 0:
//                            System.out.println(nextCell.getCellType());
//                            int id = (int) nextCell.getNumericCellValue();
//                            statement.setInt(1,id);
//                            System.out.println(id);
//                            break;
//                        case 1:
//                            System.out.println("teacherName"+nextCell.getCellType());
//                            int name = (int) nextCell.getNumericCellValue();
//                            statement.setInt(2,name);
//                            System.out.println(name);
//                            break;
//
//                        case 2:
//                            System.out.println(nextCell.getCellType());
//
//
//
//                            String preffered = nextCell.getStringCellValue();
//                            statement.setString(3, preffered);
//                            System.out.println(preffered);
//                            break;
//                    }
//
//                }
//
//                statement.addBatch();
//
//                if (count % batchSize ==0) {
//                    statement.executeBatch();
//                }else{
//                    break;
//                }
//
//            }
//
//            workbook.close();
//
//            // execute the remaining queries
//            statement.executeBatch();
//
//
//            //commit the transaction
//            connection.commit();
//            connection.close();
//
//            long end = System.currentTimeMillis();
//            System.out.printf("Import done in %d ms\n", (end - start));
//
//        } catch (IOException ex1) {
//            System.out.println("Error reading file");
//            ex1.printStackTrace();
//        } catch (SQLException ex2) {
//            System.out.println("Database error");
//            // abort the transaction(save points)
//            connection.rollback();
//            ex2.printStackTrace();
//        }
//
//    }
}
