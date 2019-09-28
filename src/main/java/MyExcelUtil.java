import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 为自己定制的读取模板类
 */
public class MyExcelUtil {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int id;
    private int cellNum=0;
    private String creator="";
    private int cout=0;
    public MyExcelUtil(){}
    public MyExcelUtil(XSSFWorkbook workbook,Integer id,String creator){
        this.workbook=workbook;
        this.creator=creator;
        this.id=id;
        if(this.workbook.getNumberOfSheets()>0){
            this.sheet=workbook.getSheetAt(0);
        }
    }

    public ImportBody getValue(Integer parentId,String parentLevel,String level,Integer bigroleId,Integer rowNum) throws Exception {
        if(cout<10000) {
            cout++;
            ImportBody importBody = new ImportBody();
            id++;
            Cell cell = sheet.getRow(rowNum).getCell(cellNum);
            String value = readCellValueToString(cell);
            String level1 = "";
            if (level.equals("")) {
                level1 = id + "";
            } else {
                level1 = level + "-" + id;
            }
            String[] temp = value.split(";");
            if (temp.length < 2)
                throw new Exception("单元格信息不充足");
            importBody.setId(id);
            importBody.setZhName(temp[0]);
            importBody.setEnName(temp[1]);
            importBody.setParentId(parentId);
            importBody.setParentLevel(parentLevel);
            importBody.setLevel(level1);
            importBody.setCreator(creator);
            importBody.setBigRole(bigroleId);
            importBody.setCreatTime(getTime());
            if (temp.length == 4) {
                cellNum++;
                List<ImportBody> list = new ArrayList<>();
                int start = Integer.parseInt(temp[2]);
                int end = Integer.parseInt(temp[3]);
                String parent = parentLevel + "." + id;
                Integer parentid = id;
                for (int i = start; i <= end; i++) {
                    ImportBody importBody1 = getValue(parentid, parent, level1, bigroleId, i);
                    list.add(importBody1);
                }
                importBody.setChild(list);
            }
            return importBody;
        }else {
            return null;
        }
    }


    /**
     * 判断cell格式并转换为String
     *
     * @param cell
     * @return
     */
    public String readCellValueToString(Cell cell) {

        if (cell == null) {
            return null;
        }
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case FORMULA:
                return String.valueOf(cell.getCellFormula());
            default:
                return cell.toString();
        }
    }


    public List<String> getListValue(int cellNum,int beginRowNum,int endRowNum){
        List<String> list=new ArrayList<>();
        for(int i=beginRowNum;i<=endRowNum;i++){
            Cell cell=sheet.getRow(i).getCell(cellNum);
            String value=readCellValueToString(cell);
            list.add(value);
        }
        return list;
    }

    private String getTime(){
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
