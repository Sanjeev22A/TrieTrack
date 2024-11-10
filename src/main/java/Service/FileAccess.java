package Service;
//This class is used to access a particular file from a file system not the database


import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {
    private String filename;
    private String path;

    FileAccess(String filename,String path){
        this.filename=filename;
        this.path=path;
        System.out.println("From FilaAccess:18:"+path+filename);
    }

    BufferedReader getReader(){
        String overallPath=path+"\\"+filename;
        try{
            FileReader fr=new FileReader(overallPath);
            return new BufferedReader(fr);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

    BufferedWriter getWriter(){
        String overallPath=path+"/"+filename;
        try{
            FileWriter fw=new FileWriter(overallPath);
            return new BufferedWriter(fw);
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public List<String> getContentFromFile(){
        List<String> content=new ArrayList<>();

        String line;
        try(BufferedReader br=this.getReader();) {
            while ((line = br.readLine()) != null) {
                content.add(line);
            }
        }
        catch(IOException e){

            throw new RuntimeException(e);
        }
        return content;
    }

    public void setContentToFile(@NotNull List<String> content){
        try(BufferedWriter bw=this.getWriter();){
            for(String line:content){
                bw.write(line);
                bw.newLine();
            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

}
