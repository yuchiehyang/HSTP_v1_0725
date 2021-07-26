package com;

import connector.TeacherDAO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    static TeacherDAO teacherDAO;
    static List<Teacher> listTeachers = new ArrayList<>();



    public static void readExcelFile() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/hstp?sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
        String username = "root";
        String password = "m0920776286";

        teacherDAO = new TeacherDAO(jdbcURL,username,password);
        String excelFilePath = "C:\\Users\\Chloe\\Desktop\\1072.xlsx";

        int batchSize = 50;

        Connection connection = null;

        try {
            long start = System.currentTimeMillis();

            FileInputStream inputStream = new FileInputStream(excelFilePath);

            System.out.println("o");
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            System.out.println("ok");
            Sheet firstSheet = workbook.getSheetAt(5);
            Iterator<Row> rowIterator = firstSheet.iterator();

            connection = DriverManager.getConnection(jdbcURL, username, password);

            /*begin the transaction
            -- we have to disable the auto commit mode to enable two or more statements to be grouped into a transaction)*/
            connection.setAutoCommit(false);

            String sql = "INSERT INTO teacher (idTeacher,name, preferredTimeslots) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);


            int count = 0;

            rowIterator.next(); // skip the header row

            while (rowIterator.hasNext()) {
                Teacher teacher = new Teacher();
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
                            teacher.setId(id);
                            System.out.println(id);
                            break;
                        case 1:
                            System.out.println("teacherName"+nextCell.getCellType());
                            int name = (int) nextCell.getNumericCellValue();
                            statement.setInt(2,name);
                            teacher.setName(Integer.toString(name));
                            System.out.println(name);
                            break;

                        case 2:
                            System.out.println(nextCell.getCellType());
                            String preferred = nextCell.getStringCellValue();
                            statement.setString(3, preferred);
                            teacher.setPreferredTimeSlots(preferred);
                            System.out.println(preferred);
                            break;
                    }



                }

                statement.addBatch();
                listTeachers.add(teacher);

                if (count % batchSize ==0) {
                    statement.executeBatch();

                }else{
                    break;
                }

            }

            workbook.close();

            // execute the remaining queries
            statement.executeBatch();


            //commit the transaction
            connection.commit();
            connection.close();

            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));

        } catch (IOException ex1) {
            System.out.println("Error reading file");
            ex1.printStackTrace();
        } catch (SQLException ex2) {
            System.out.println("Database error");
            // abort the transaction(save points)
            connection.rollback();
            ex2.printStackTrace();
        }


    }


    public static void main(String[] args) throws SQLException {
        readExcelFile();
        for (Teacher teacher: listTeachers){
            System.out.println("老師: "+teacher.id+"\t"+teacher.name+"\t"+teacher.preferredTimeSlots);
        }

//        Teacher teacher148 = new Teacher(148,"chloe","11,22");
//        teacherDAO.insertTeacher(teacher148);
//
//        teacherDAO.deleteTeacher(teacher148);


    }
}
