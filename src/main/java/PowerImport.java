import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class PowerImport {
    public static final String  baseSql="";
    public String[] znName={"00","角色管理","创建角色","复制并创建角色","删除角色","修改角色名","分配角色","权限管理","修改权限"};
    public String[] enName={"00","role","create","copy","delete","changename","distribute","auth","changemodule"};
    public String[] arr={"5","5","51","52","53","54","56","6","57"};
    public PowerImport(){

    }
    public void writeToTxt(String filepathe,List<String> msg){
        File file = new File(filepathe);
        try {

            FileWriter writer = new FileWriter(file, true);
            for(String str:msg){
                writer.write(str+"\n\r");
            }
            writer.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            ex.getMessage();

        }
    }
    public String addString(String str){
            return  str+",";
        }
    public String addString(int str){
        return  str+",";
    }
    public String addInt(int a){
        return a+",";
    }
    public void getPowerImport(int bigroleId,List<String> sql,String zn_name,int startid){
        znName[0]=zn_name;
        enName[0]=bigroleId+"";
        int prd1=startid+1;
        int[] parent={0,startid,prd1,prd1,prd1,prd1,prd1,startid,startid+7};
        String prd2=0+"."+startid+"."+prd1;
        String prd3=0+"."+startid;
        int mi=startid+7;
        String[] parentId={"0",prd3,prd2,prd2,prd2,prd2,prd2,prd3,prd3+"."+mi};
        for(int i=0;i<9;i++){
            String mysql = startid+addString(znName[i])+ addInt(parent[i])+addString(parentId[i])+addString(getNext(parentId[i])+startid)+addString(enName[i])+addInt(0)+addString(arr[i])+addInt(3)+addString("liyiwang");
            startid++;
            sql.add(mysql);
        }
    }

    public String getNext(String str){
        if(str.contains(".")){
            String mm=str.substring(2,str.length());
            String newStr=mm.replace('.','-');
            return newStr+"-";
        }else {
            return "";
        }
    }

    public void getSql(ImportBody importBody,List<String> list){
        StringBuilder sb=new StringBuilder();
        int id=importBody.getId();
        sb.append("id:");
        sb.append(id);
//        String zhname=importBody.getZhName();
//        sb.append("zhname:");
//        sb.append(zhname);
        int parentId=importBody.getParentId();
        sb.append("parentId:");
        sb.append(parentId);
        String parentLevel=importBody.getParentLevel();
        sb.append("parentLevel:");
        sb.append(parentLevel);
        String level=importBody.getLevel();
        sb.append("level:");
        sb.append(level);
//        String time=importBody.getCreatTime();
//        sb.append("time:");
//        sb.append(time);
//        String creator=importBody.getCreator();
//        sb.append("creator:");
//        sb.append(creator);
//        int bigrole=importBody.getBigRole();
//        sb.append("bigrole");
//        sb.append(bigrole);
        sb.append("\r\n");
        list.add(sb.toString());
        if(importBody.getChild()!=null){
            importBody.getChild().forEach(item->{
                getSql(item,list);
            });
        }
    }

}
