import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        String path="E:\\学习\\powerImport.xlsx";
        try {
            FileInputStream fileInputStream=new FileInputStream(path);
            XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
            MyExcelUtil myExcelUtil=new MyExcelUtil(workbook,3,"liyiwang");
            ImportBody importBody=myExcelUtil.getValue(0,"0","",22,0);
            PowerImport powerImport = new PowerImport();
            List<String> list=new ArrayList<>();
            powerImport.getSql(importBody,list);
            powerImport.writeToTxt("E:\\学习\\sqlproduct.txt",list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
