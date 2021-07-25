package connector;

import java.io.*;
import java.sql.*;
import java.util.*;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;


/**
 * Sample Java program that imports data from an Excel file to MySQL database.
 *
 * @author chloe yang
 */

public class Main {


    public static void main(String[] args) {
        // write your code here
        String jdbcURL = "jdbc:mysql://localhost:3306/hstp?sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
        String username = "root";
        String password = "m0920776286";

        String excelFilePath = "C:\\Users\\Chloe\\Desktop\\1072.xlsx";

        int batchSize = 1;

//        Connection connection = null;

        try {
            long start = System.currentTimeMillis();

            FileInputStream inputStream = new FileInputStream(excelFilePath);

            System.out.println("o");
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            System.out.println("ok");
            Sheet firstSheet = workbook.getSheetAt(5);
            Iterator<Row> rowIterator = firstSheet.iterator();

            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sql = "INSERT INTO teacher (idteacher,name, preferredTimeslots) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            int count = 0;

            rowIterator.next(); // skip the header row

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();

                    int columnIndex = nextCell.getColumnIndex();

                    switch (columnIndex) {
                        case 0:
                            System.out.println(nextCell.getCellType());
                            int id = (int) nextCell.getNumericCellValue();
                            statement.setInt(1,id);
                            System.out.println(id);
                            break;
                        case 1:
                            System.out.println("teacherName"+nextCell.getCellType());
                            int name = (int) nextCell.getNumericCellValue();
                            statement.setInt(2,name);
                            System.out.println(name);
                            break;
//                            int teacherName = (int) nextCell.getNumericCellValue();
//                            statement.setInt(2,teacherName);

                        case 2:
                            System.out.println(nextCell.getCellType());
//                            int preffered = (int)nextCell.getNumericCellValue();
//                            statement.setInt(3,preffered);


                            String preffered = nextCell.getStringCellValue();
                            statement.setString(3, preffered);
                            System.out.println(preffered);
                            break;
                    }

                }

                statement.addBatch();

                if (count % batchSize ==0) {
                    statement.executeBatch();
                }else{
                    break;
                }

            }

            workbook.close();

            // execute the remaining queries
            statement.executeBatch();

            connection.commit();
            connection.close();

            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));

        } catch (IOException ex1) {
            System.out.println("Error reading file");
            ex1.printStackTrace();
        } catch (SQLException ex2) {
            System.out.println("Database error");
            ex2.printStackTrace();
        }

    }
}
