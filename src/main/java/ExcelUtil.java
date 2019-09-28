import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * POI读取Excel封装,仅做参考，并不用
 */
public class ExcelUtil {

    /**
     * 日期格式化
     */
    private String formatter = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式化对象
     */
    private SimpleDateFormat sdf = null;
    /**
     * 单例对象
     */
    private static ExcelUtil instance = new ExcelUtil();

    /**
     * 单例
     *
     * @param dateFormatter
     * @return
     */
    public static ExcelUtil getInstance(String dateFormatter) {
        if (dateFormatter != null && dateFormatter.length() != 0) {
            instance.formatter = dateFormatter;
            instance.sdf = null;
        }
        return instance;
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
        return cell.getStringCellValue();
//        switch (type) {
//            case STRING:
//                return cell.getStringCellValue();
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case NUMERIC:
//                if (HSSFDateUtil.isCellDateFormatted(cell)) {
//                    if (sdf == null) {
//                        try {
//                            sdf = new SimpleDateFormat(formatter);
//                        } catch (Exception e) {
//                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        }
//                    }
//                    return sdf.format(cell.getDateCellValue());
//                }
//                return String.valueOf(cell.getNumericCellValue());
//            case FORMULA:
//                return String.valueOf(cell.getCellFormula());
//            default:
//                return cell.toString();
//        }
    }

    /**
     * 判断Excel类型（2003,2007）并返回实例
     *
     * @param in
     * @return
     * @throws IOException
     */
    public Workbook readStreamToWorkBook(InputStream in) throws IOException {
        checkNull(in);
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(in);
        } catch (IOException e) {
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }


    /**
     * 判断Excel类型（2003/2007）并返回
     * 读取结果第一行是每一个map中的key 每行对应的值为map的value
     * <i>如果第一行没有表头但是后面的行对应的列却有数据，则忽略</i>
     *
     * @param excelFile
     * @param sdf
     * @return
     * @throws IOException
     */
    public List<Map<String, String>> readToListMap(File excelFile, SimpleDateFormat sdf) throws IOException {
        checkNull(excelFile);
        return readToListMap(new FileInputStream(excelFile));
    }

    /**
     * 读取第一个Sheet表并映射为List<HashMap<String, String> 数据类型
     * 读取结果第一行是每一个map中的key 每行对应的值为map的value
     * <i>如果第一行没有表头但是后面的行对应的列却有数据，则忽略</i>
     * 如：
     * a,b,c
     * 1,2,3,4
     * 读取时4则被忽略
     *
     * @param excelStream
     * @return
     * @throws IOException
     */
    public List<Map<String, String>> readToListMap(InputStream excelStream) throws IOException {
        Workbook wb = readStreamToWorkBook(excelStream);
        Sheet sheet = wb.getSheetAt(0);
        return readSheetToListMap(sheet);
    }

    /**
     * 读取某个sheet，映射为List<HashMap<String, String> 数据类型
     * 读取结果第一行是每一个map中的key 每行对应的值为map的value
     * <i>如果第一行没有表头但是后面的行对应的列却有数据，则忽略</i>
     *
     * @param sheet
     * @return
     */
    public List<Map<String, String>> readSheetToListMap(Sheet sheet) {
        List<Map<String, String>> result = new ArrayList<>(sheet.getLastRowNum());
        if (sheet == null || sheet.getFirstRowNum() == sheet.getLastRowNum()) {
            return null;
        }
        Row firstRow = sheet.getRow(sheet.getFirstRowNum());
        // 遍历所有行
        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Map<String, String> rowMap = new HashMap<>();
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                String k = readCellValueToString(firstRow.getCell(j));
                if (k == null) {
                    continue;
                }
                rowMap.put(k, readCellValueToString(row.getCell(j)));
            }
            result.add(rowMap);
        }
        return result;
    }

    /**
     * 读取Excel中第一个Sheet并映射为 List<List<String>> 数据类型
     * 默认跳过第一行
     * 外层List里面每一个对象是一行信息
     * 内层List按顺序0对应第一列1对应第二列
     *
     * @param file
     * @return
     * @throws IOException
     */
    public List<List<String>> readToListList(File file) throws IOException {
        checkNull(file);
        return readToListList(new FileInputStream(file), true);
    }

    /**
     * 读取Excel中第一个Sheet并映射为 List<List<String>> 数据类型
     * 外层List里面每一个对象是一行信息
     * 内层List按顺序0对应第一列1对应第二列
     *
     * @param file
     * @param skipFirst 是否跳过第一行
     * @return
     * @throws IOException
     */
    public List<List<String>> readToListList(File file, boolean skipFirst) throws IOException {
        checkNull(file);
        return readToListList(new FileInputStream(file), skipFirst);
    }

    /**
     * 读取第一个Sheet表格，将结果映射为List<List<String>>数据类型
     * 默认跳过第一行
     * 外层List里面每一个对象是一行信息
     * 内层List按顺序0对应第一列1对应第二列
     *
     * @param excelStream
     * @return
     * @throws IOException
     */
    public List<List<String>> readToListList(InputStream excelStream) throws IOException {
        Workbook wb = readStreamToWorkBook(excelStream);
        Sheet sheet = wb.getSheetAt(0);
        return readSheetToListList(sheet, true);
    }

    /**
     * 读取第一个Sheet表格，将结果映射为List<List<String>>数据类型
     * 外层List里面每一个对象是一行信息
     * 内层List按顺序0对应第一列1对应第二列
     *
     * @param excelStream
     * @param skipFirst   是否跳过第一行
     * @return
     * @throws IOException
     */
    public List<List<String>> readToListList(InputStream excelStream, boolean skipFirst) throws IOException {
        Workbook wb = readStreamToWorkBook(excelStream);
        Sheet sheet = wb.getSheetAt(0);
        return readSheetToListList(sheet, skipFirst);
    }

    /**
     * 读取第给定的Sheet表格，将结果映射为List<List<String>>数据类型
     * 外层List里面每一个对象是一行信息
     * 内层List按顺序0对应第一列1对应第二列
     *
     * @param sheet
     * @param skipFirst
     * @return
     * @throws IOException
     */
    public List<List<String>>
    readSheetToListList(Sheet sheet, boolean skipFirst) {
        if (sheet == null) {
            return null;
        }
        List<List<String>> result = new ArrayList<>();
        int start = skipFirst ? sheet.getFirstRowNum() : sheet.getFirstRowNum() + 1;
        for (int i = start; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            List<String> rowList = new ArrayList<>(row.getLastCellNum() - row.getFirstCellNum());
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                rowList.add(readCellValueToString(row.getCell(j)));
            }
            result.add(rowList);
        }
        return result;
    }

    /**
     * 读取文件中所有Sheet表格
     * 最外层List中每一个对象就是一个Sheet的信息
     * 中间的List每一个对象是一行信息
     * Map中每一个键值对是列信息，key是第一行某一列的的内容value是对应行对应列的内容
     *
     * @param file
     * @return
     * @throws IOException
     */
    public List<List<Map<String, String>>> readAllSheetToListMap(File file) throws IOException {
        checkNull(file);
        return readAllSheetToListMap(new FileInputStream(file));
    }

    /**
     * 读取文件中所有的Excel表格信息映射为 List<List<Map<String, String>>>
     * 最外层List中每一个对象就是一个Sheet的信息
     * 中间的List每一个对象是一行信息
     * 内层List按顺序0对应第一列1对应第二列
     *
     * @param excelStream
     * @return
     * @throws IOException
     */
    public List<List<Map<String, String>>> readAllSheetToListMap(FileInputStream excelStream) throws IOException {
        Workbook sheets = readStreamToWorkBook(excelStream);
        List<List<Map<String, String>>> result = new ArrayList<>();
        int numberOfSheets = sheets.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = sheets.getSheetAt(i);
            result.add(readSheetToListMap(sheet));
        }
        return result;
    }

    /**
     * 读取文件中所有的Excel表格信息映射为
     * 跳过第一行
     * 最外层List中每一个对象就是一个Sheet的信息
     * 中间的List每一个对象是一行信息
     * 内中每一个键值对是列信息，key是第一行某一列的的内容value是对应行对应列的内容
     *
     * @param excelFile
     * @return
     */
    public List<List<List<String>>> readAllSheetToListList(File excelFile) throws IOException {
        return readAllSheetToListList(excelFile, true);
    }

    /**
     * 读取文件中所有的Excel表格信息映射为
     * 最外层List中每一个对象就是一个Sheet的信息
     * 中间的List每一个对象是一行信息
     * 内中每一个键值对是列信息，key是第一行某一列的的内容value是对应行对应列的内容
     *
     * @param excelFile
     * @param skipFirst 是否跳过第一行
     * @return
     */
    public List<List<List<String>>> readAllSheetToListList(File excelFile, boolean skipFirst) throws IOException {
        checkNull(excelFile);
        return readAllSheetToListList(new FileInputStream(excelFile), skipFirst);
    }

    /**
     * 读取文件中所有的Excel表格信息映射为
     * 跳过第一行
     * 最外层List中每一个对象就是一个Sheet的信息
     * 中间的List每一个对象是一行信息
     * 内中每一个键值对是列信息，key是第一行某一列的的内容value是对应行对应列的内容
     *
     * @param excelStream
     * @return
     */
    public List<List<List<String>>> readAllSheetToListList(FileInputStream excelStream) throws IOException {
        return readAllSheetToListList(excelStream, true);
    }

    /**
     * 读取文件中所有的Excel表格信息映射为
     * 最外层List中每一个对象就是一个Sheet的信息
     * 中间的List每一个对象是一行信息
     * 内中每一个键值对是列信息，key是第一行某一列的的内容value是对应行对应列的内容
     *
     * @param excelStream
     * @param skipFirst   是否跳过第一行
     * @return
     */
    public List<List<List<String>>> readAllSheetToListList(FileInputStream excelStream, boolean skipFirst) throws IOException {
        checkNull(excelStream);
        Workbook sheets = readStreamToWorkBook(excelStream);
        List<List<List<String>>> result = new ArrayList<>();
        int numberOfSheets = sheets.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            result.add(readSheetToListList(sheets.getSheetAt(i), skipFirst));
        }
        return result;
    }

    private void checkNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("read file can not empty");
        }
    }
}
