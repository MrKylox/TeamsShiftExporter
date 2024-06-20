package edu.ITSolutions.Export.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ITSolutions.Export.Member;

public class ExcelUtil {

    private final Workbook workbook;
    private final Map<String, Sheet> sheets = new HashMap<>();

    public ExcelUtil(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(fileInputStream);
        for (Sheet sheet : workbook) {
            sheets.put(sheet.getSheetName(), sheet);
        }
    }

    public List<Member> getMembers() {
        List<Member> members = new ArrayList<>();
        Sheet sheet = workbook.getSheet("Members");
        if (sheet != null) {
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                String name = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                members.add(new Member(name, email));
            }
        }
        return members;
    }

    public Sheet getSheet(String name) {
        return sheets.get(name);
    }

    public Workbook getWorkbook(){
        return workbook;
    }

    public void save(File file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            workbook.write(fileOutputStream);
        }
    }
}

