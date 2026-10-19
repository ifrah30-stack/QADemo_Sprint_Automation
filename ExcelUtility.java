
package genericUtility;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

    public static String getCellData(
            String sheetName, int row, int column) {

        File excelFile = new File("TestData.xlsx");

        if (!excelFile.exists()) {
            throw new RuntimeException(
                "TestData.xlsx not found at: "
                + excelFile.getAbsolutePath()
            );
        }

        try (
            FileInputStream file =
                new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(file)
        ) {
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException(
                    "Sheet not found: " + sheetName
                );
            }

            if (sheet.getRow(row) == null
                    || sheet.getRow(row).getCell(column) == null) {
                throw new RuntimeException(
                    "Excel cell is empty. Row: "
                    + (row + 1) + ", Column: "
                    + (column + 1)
                );
            }

            DataFormatter formatter = new DataFormatter();

            return formatter.formatCellValue(
                sheet.getRow(row).getCell(column)
            ).trim();

        } catch (Exception e) {
            throw new RuntimeException(
                "Unable to read Excel data from sheet: "
                + sheetName, e
            );
        }
    }
}
