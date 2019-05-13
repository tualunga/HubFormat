package com.zyxo.hubformatapp.base.services;

import com.zyxo.hubformatapp.base.domain.Extent;
import com.zyxo.hubformatapp.base.domain.Hbdf;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class WriteInExcelService {

    private static final String FILE_NAME_MATRIX = "src/main/resources/data/HBDF_matrix.xls";
    private static final String FILE_NAME_COO = "src/main/resources/data/HBDF_coo.xls";

    public XSSFWorkbook writeMatrix(Hbdf hbdf) {
        List<Extent> torso = hbdf.getTypeOfMeasurement().getIso_7250().getTorso().getExtent();
        List<Extent> head = hbdf.getTypeOfMeasurement().getIso_7250().getHead().getExtent();
        List<Extent> arm = hbdf.getTypeOfMeasurement().getIso_7250().getArm().getExtent();
        List<Extent> leg = hbdf.getTypeOfMeasurement().getIso_7250().getLeg().getExtent();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("HBDF_matrix");
        Object[][] datatypes = {
                {"","sn","gn", "nk", "us", "ch", "an1","an2", "is1", "is2", "ew", "hd", "ke", "ta", "gd", "ft", "bk"},
                {"sn",
                        getValue(head, "Sellion"),
                        calcWeight(head, "Sellion", head, "Gnathion"),
                        0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {"gn",
                        getValue(head, "Gnathion"), 0,
                        calcWeight(torso, "Neck", head, "Gnathion"),
                        0,0,0,0,0,0,0,0,0,0,0,0,0},
                {"nk",0,
                        calcWeight(head, "Gnathion", torso, "Neck"),
                        getValue(torso, "Neck"),
                        0,calcWeight(torso, "Neck", torso, "Umbilicus"),
                        0, calcWeight(torso, "Neck", torso, "Acromion"),
                        0,0,0,0,0,0,0,0,0},
                {"us",0,0,calcWeight(torso, "Umbilicus", torso, "Neck"),
                        getValue(torso, "Umbilicus"),
                        calcWeight(torso, "Umbilicus", torso, "Crotch"),
                        0,0,0,0,0,0,0,0,0,0,0},
                {"ch",0,0,0,calcWeight(torso, "Crotch", torso, "Umbilicus"),
                        0,0,0, calcWeight(torso, "Crotch", torso, "Iliac spine"),
                        0,0,0,0,0,0,0,0},
                {"an1",0,0,calcWeight(torso, "Acromion", torso, "Neck"),
                        0,0,0, calcWeight(torso, "Acromion", torso, "Acromion"),
                        0,0,calcWeight(torso, "Crotch", arm, "Elbow"),
                        0,0,0,0,0,0},
                {"an2", 0,0,0,0,0, calcWeight(torso, "Acromion", torso, "Acromion"),
                        getValue(torso, "Acromion"),0,0,0,0,0,0,0,0,0},
                {"is",0,0,0,0, calcWeight(torso, "Iliac spine", torso, "Crotch"),
                        0,0,0,calcWeight(torso, "Iliac spine", torso, "Iliac spine"),
                        0,0,calcWeight(torso, "Iliac spine", leg, "Knee"),0,0,0,0},
                {"is",0,0,0,0,0,0,0, calcWeight(torso, "Iliac spine", torso, "Iliac spine"),
                        0,0,0,0,0,0,0,0},
                {"ew",0,0,0,0,0,calcWeight(arm, "Elbow", torso, "Acromion"),
                        0,0,0,0,calcWeight(arm, "Elbow", arm, "Hand"),0,0,0,0,0},
                {"hd",0,0,0,0,0,0,0,0,0,calcWeight(arm, "Hand", arm, "Elbow"),
                        getValue(arm, "Hand"),0,0,0,0,0},
                {"ke",0,0,0,0,0,0,0,calcWeight(leg, "Knee", torso, "Iliac spine"),0,0,0,
                        getValue(leg, "Knee"),
                        calcWeight(torso, "Iliac spine", leg, "Tibia"),0,0,
                        calcWeight(torso, "Iliac spine", torso, "Buttlock")},
                {"ta",0,0,0,0,0,0,0,0,0,0,0,0,
                        calcWeight(leg, "Tibia", leg, "Knee"),
                        getValue(leg, "Tibia"),0,0},
                {"gd",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {"ft",0,0,0,0,0,0,0,0,0,0,0,0,0,
                        getValue(leg, "Foot"),0,0},
                {"bk",0,0,0,0,0,0,0,0,0,0,0,
                        calcWeight(torso, "Buttlock", leg, "Knee"),0,0,0,0}
        };

        int rowNum = 0;
        System.out.println("Creating excel for matrix");

        if (getPrintXlsx(workbook, sheet, datatypes, rowNum)) return workbook;

        return null;
    }

    private boolean getPrintXlsx(XSSFWorkbook workbook, XSSFSheet sheet, Object[][] datatypes, int rowNum) {
        for (Object[] datatype : datatypes) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME_MATRIX);
            workbook.write(outputStream);
            workbook.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public XSSFWorkbook writeCoo(Hbdf hbdf) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("HBDF_coo");
        Object[][] datatypes = {
                {"val", "C11", "M11", "M12", "C12", "M21", "M24", "M21","C21","M22","M22","M26",
                        "M24","M23","M31","M23","C22","M26","M25","M41","M25","M31","M32","M32",
                        "C31","M41","C41","M42","M45","M42","C42","M43","M43","M44","M44","M45"},
                {"row", 2,2,4,4,4,4,5,5,5,6,6,7,7,7,8,8,9,9,9,10,11,11,12,12,13,13,13,13,14,14,14,15,16,16,17},
                {"col", 2,3,2,4,3,4,6,8,4,5,6,5,9,4,8,11,7,8,6,10,13,9,7,12,11,12,9,13,14,17,14,15,15,13,16}
        };

        int rowNum = 0;
        System.out.println("Creating excel for COO");

        for (Object[] datatype : datatypes) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME_COO);
            workbook.write(outputStream);
            workbook.close();
            return workbook;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String calcWeight(List<Extent> firstEx, String firstType, List<Extent> secondEx, String SecondType){
        Double firstMes = Double.parseDouble(getValue(firstEx, firstType));
        Double secondMes = Double.parseDouble(getValue(secondEx, SecondType));
        return String.valueOf(Math.sqrt((Math.pow(firstMes, 2)/4) + Math.pow(firstMes-secondMes, 2)));
    }

    private String getValue(List<Extent> typeOfBody, String type) {
        return typeOfBody.stream().filter(p -> p.getType().equals(type)
                && p.getMeasurement().equals("height")).findAny().get().getValue();
    }
}