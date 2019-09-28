import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class PowerImport {
    public static final String  baseSql="";
    public PowerImport(){

    }
    public void writeToTxt(String filepathe,List<String> msg){
        File file = new File(filepathe);
        try {

            FileWriter writer = new FileWriter(file, true);
            for(String str:msg){
                writer.write(str);
            }
            writer.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            ex.getMessage();

        }
    }

    public void getSql(ImportBody importBody,List<String> list){
        StringBuilder sb=new StringBuilder();
        int id=importBody.getId();
        sb.append("id:");
        sb.append(id);
        String zhname=importBody.getZhName();
        sb.append("zhname:");
        sb.append(zhname);
        int parentId=importBody.getParentId();
        sb.append("parentId:");
        sb.append(parentId);
        String parentLevel=importBody.getParentLevel();
        sb.append("parentLevel:");
        sb.append(parentLevel);
        String level=importBody.getLevel();
        sb.append("level:");
        sb.append(level);
        String time=importBody.getCreatTime();
        sb.append("time:");
        sb.append(time);
        String creator=importBody.getCreator();
        sb.append("creator:");
        sb.append(creator);
        int bigrole=importBody.getBigRole();
        sb.append("bigrole");
        sb.append(bigrole);
        sb.append("\r\n");
        list.add(sb.toString());
        if(importBody.getChild()!=null){
            importBody.getChild().forEach(item->{
                getSql(item,list);
            });
        }
    }

}
