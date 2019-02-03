package research_aspect;

import model.Sample;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHelper {

    public void txtToXls(String fromPathname, String toPathname) throws Exception {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Intervals RR");
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.DASHED);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontHeightInPoints((short) 11);
        font.setItalic(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Sample no. [-]");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Peak type [N/V]");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Duration [ms]");
        headerCell.setCellStyle(headerStyle);

        int linesCounter = 0;
        List<String[]> lines = loadTxtFile(fromPathname);

        for (String[] cells : lines) {
            linesCounter++;
            Row row = sheet.createRow(linesCounter);
            Cell cell = row.createCell(0);
            cell.setCellValue(Integer.valueOf(cells[0]));

            cell = row.createCell(1);
            cell.setCellValue(cells[1]);

            cell = row.createCell(2);
            cell.setCellValue(Math.round(Float.valueOf(cells[2]) * 1000.0f));
        }

        FileOutputStream outputStream = new FileOutputStream(toPathname);
        workbook.write(outputStream);
        workbook.close();

    }

    private List<String[]> loadTxtFile(String pathname) throws Exception {
        File file = new File(pathname);
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String[]> lines = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            String[] cells = line.split("\\s+");
            lines.add(cells);
        }

        return lines;
    }

    public void intervalsRRToXls(List<Sample> intervalsRR, String toPathname) throws Exception {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Intervals RR");
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);

        Row header = sheet.createRow(0);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Sample no. [-]");

        headerCell = header.createCell(1);
        headerCell.setCellValue("Peak type [N/V]");

        headerCell = header.createCell(2);
        headerCell.setCellValue("Duration [ms]");

        int linesCounter = 0;
        for (Sample intervalRR : intervalsRR) {
            linesCounter++;

            Row row = sheet.createRow(linesCounter);
            Cell cell = row.createCell(0);
            cell.setCellValue(intervalRR.getId());

            cell = row.createCell(1);
            cell.setCellValue((intervalRR.isPVC()) ? "V" : "N");

            cell = row.createCell(2);
            cell.setCellValue(intervalRR.getValue());
        }

        FileOutputStream outputStream = new FileOutputStream(toPathname);
        workbook.write(outputStream);
        workbook.close();
    }

    public List<Sample> xlsToIntervalsRR(String fromPathname, boolean pvc) throws FileNotFoundException, IOException {
        FileInputStream file = new FileInputStream(new File(fromPathname));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);
        sheet.removeRow(sheet.getRow(0));
        sheet.removeRow(sheet.getRow(1));
        Map<Integer, List<String>> data = new HashMap<>();
        List<Sample> intervalsRR = new ArrayList<>();

        if (pvc) {
            for (Row row : sheet) {
                int sampleNo = (int) row.getCell(0).getNumericCellValue();
                float duration = (float) row.getCell(2).getNumericCellValue();
                Sample sample = new Sample(sampleNo, duration);
                boolean isPVC = row.getCell(1).getStringCellValue().equals("V") ? true : false;
                sample.setPVC(isPVC);
                intervalsRR.add(sample);
            }
        } else {
            for (Row row : sheet) {
                int sampleNo = (int) row.getCell(0).getNumericCellValue();
                float duration = (float) row.getCell(2).getNumericCellValue();
                Sample sample = new Sample(sampleNo, duration);
                intervalsRR.add(sample);
            }
        }
        return intervalsRR;
    }
}
