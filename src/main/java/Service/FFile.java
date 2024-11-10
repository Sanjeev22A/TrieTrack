package Service;

import Database.DB;
import Database.DBMongo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FFile {
    public String filename;
    String path;
    FileDS fileDSTest;
    FileDS fileDSProd;
    FileAccess fa;
    static DB db;
    List<Log> logs;
    static {
        db=new DBMongo();
    }
    public FFile(String filename,String path,List<Log> logs){
        new FFile(filename,path);
        this.logs=logs;
        System.out.println("checker in FFile line 32");
        for(Log l:logs){
            System.out.println(l.versionNumber+l.scope);
        }
    }
    public FFile(String filename, String path){
        this.filename=filename;
        this.path=path;
        this.logs=db.getStorer(filename,path,"Test").getLogs();
        fa=new FileAccess(filename,path);
        fileDSTest=setFileDS("Test");
        fileDSProd=setFileDS("Production");
    }

    public FileDS setFileDS(String scope){
        Storer s=db.getStorer(filename,path,scope);
        if(s==null){
            FileDS cur=new FileDS(getContentFromfile());
            System.out.println(cur.head);
            for(int i:cur.versionNumbers()){
                System.out.println(i);
            }
            for(String line:cur.traverse(1)){
                System.out.println(line);
            }
            return cur;
        }
        return s.fileDS;
    }

    public void addLog(long versionNumber,String scope,String content){
        Log l=new Log(versionNumber,scope,content);
        logs.add(l);
        System.out.println(l);
    }
    public List<Log> getLogs(int versionNumber,String scope){
        List<Log> result=new ArrayList<>();
        for(Log l:logs){
            if(l.versionNumber==versionNumber && l.scope.equalsIgnoreCase(scope)){
                result.add(l);
            }
        }
        return result;
    }
    public List<Log> getLogs(int versionNumber){
        List<Log> result=new ArrayList<>();
        for(Log l:logs){
            if(l.versionNumber==versionNumber){
                result.add(l);
            }
        }
        return result;
    }
    public void deleteLogs(int versionNumber,String scope){
        List<Log> result=new ArrayList<>();
        for(Log l:logs){
            if(l.versionNumber!=versionNumber){
                result.add(l);
            }
            else if(!l.scope.equalsIgnoreCase(scope)){
                result.add(l);
            }
        }
        logs=result;
    }
    public List<Log> getLogs(String scope){
        List<Log> result=new ArrayList<>();
        for(Log l:logs){
            if(l.scope.equalsIgnoreCase(scope)){
                result.add(l);
            }
        }
        return result;
    }

    public List<String> getContentFromfile(){
        return fa.getContentFromFile();
    }
    public void setContentInFile(List<String> content){
        fa.setContentToFile(content);
    }
    public void updateToDb(String scope){
        FileDS cur;
        if(scope.equalsIgnoreCase("Test")){
            cur=fileDSTest;
        }
        else{
            cur=fileDSProd;
        }
        Storer ser=new Storer(filename,path,cur,scope,logs);
        db.addStorer(ser);
    }
    public int saveToFile(List<String> content,String scope,int versionNumber){
        FileDS cur;
        if(scope.equalsIgnoreCase("Test")){
            cur=fileDSTest;
        }
        else{
            cur=fileDSProd;
        }
        setContentInFile(content);

        int ver=cur.updateBranch(content,versionNumber);
        updateToDb(scope);
        return ver;
    }

    public int saveToFile(List<String> content,String scope){
        FileDS cur;
        if(scope.equalsIgnoreCase("Test")){
            cur=fileDSTest;
        }
        else{
            cur=fileDSProd;
        }
        setContentInFile(content);

        int ver=cur.createNewBranch(content);
        updateToDb(scope);
        return ver;
    }

    public int updateVersion(List<String> content,int versionNumber,String scope){
        FileDS cur;
        if(scope.equalsIgnoreCase("Test")){
            cur=fileDSTest;
        }
        else{
            cur=fileDSProd;
        }
        setContentInFile(content);

        int ver=cur.updateBranch(content,versionNumber);
        updateToDb(scope);
        return ver;
    }

    public boolean deleteVersion(int versionNumber,String scope){
        FileDS cur;
        if(scope.equalsIgnoreCase("Test")){
            cur=fileDSTest;
        }
        else{
            cur=fileDSProd;
        }
        cur.deleteBranch(versionNumber);
        updateToDb(scope);
        return true;
    }

    public List<String> getContentByVersionAndScope(int versionNumber,String scope){
        FileDS cur;
        if(scope.equalsIgnoreCase("Test")){
            cur=fileDSTest;
        }
        else{
            cur=fileDSProd;
        }
        return cur.traverse(versionNumber);
    }

    public HashSet<Integer> getAllVersions(String scope){
        FileDS cur;
        if(scope.equalsIgnoreCase("Test")){
            cur=fileDSTest;
        }
        else{
            cur=fileDSProd;
        }
        return cur.versionNumbers();
    }
}
