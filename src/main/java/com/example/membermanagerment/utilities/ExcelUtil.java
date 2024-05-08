package com.example.membermanagerment.utilities;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class ExcelUtil {

    /**
     * Reads an Excel file at a given path and returns its data
     *
     * @param filePath   - The path to the Excel file
     * @param sheetIndex - The index of the sheet to read data from (starting from
     *                   0)
     * @return A List of Lists containing the data from the Excel file
     * @throws IOException if the file is not found or cannot be opened
     */
    public static List<List<String>> readExcel(String filePath, int sheetIndex) {
        List<List<String>> data = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + filePath);
        }
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                List<String> rowData = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    rowData.add(cell.toString());
                }
                data.add(rowData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + filePath, e);
        }
        return data;
    }

    /**
     * Writes the given data to an Excel file.
     *
     * @param data      The data to write.
     * @param filePath  The path where the file should be saved.
     * @param sheetName The name of the sheet to create.
     * @throws IOException If an error occurs while creating the file or
     *                     writing to it.
     */
    public static void writeExcel(List<List<String>> data, String filePath, String sheetName) throws IOException {
        Objects.requireNonNull(data, "Data cannot be null");
        Objects.requireNonNull(filePath, "File path cannot be null");
        Objects.requireNonNull(sheetName, "Sheet name cannot be null");

        Path parentDirectory = Paths.get(filePath).getParent();
        if (parentDirectory != null && !Files.exists(parentDirectory)) {
            Files.createDirectories(parentDirectory);
        }

        try (Workbook workbook = WorkbookFactory.create(true);
                FileOutputStream fos = new FileOutputStream(filePath)) {
            Sheet sheet = workbook.createSheet(sheetName);
            for (List<String> rowData : data) {
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);

                int columnIndex = 0;
                for (String cellData : rowData) {
                    Cell cell = row.createCell(columnIndex++);
                    Optional.ofNullable(cellData).ifPresent(cell::setCellValue);
                }
            }

            workbook.write(fos);
        } catch (IOException e) {
            throw new IOException("Error writing to spreadsheet", e);
        }
    }

}