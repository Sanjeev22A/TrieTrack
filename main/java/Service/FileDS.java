package Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class ListNode implements Serializable {
    private String dataField;
    private HashSet<Integer> versionNumbers;
    ListNode prev;
    HashSet<ListNode> next;

    ListNode(String dataField,int versionNumber){
        this.versionNumbers=new HashSet<>();
        versionNumbers.add(versionNumber);
        this.dataField=dataField;
        this.prev=null;
        this.next=new HashSet<>();
    }

    void addVersionNumber(int versionNumber){
        versionNumbers.add(versionNumber);
    }

    boolean checkVersionExist(int versionNumber){
        return versionNumbers.contains(versionNumber);
    }

    boolean removeVersionNumber(int versionNumber){
        if(checkVersionExist(versionNumber)){
            versionNumbers.remove(versionNumber);
            return true;
        }
        return false;
    }

    String getDataField(){
        return dataField;
    }
    void changeDataField(String dataField){
        this.dataField=dataField;
    }
    HashSet<Integer> getVersions(){
        return versionNumbers;
    }

    ListNode CheckIfNextChildHas(String content){
        for(ListNode n:next){
            if(n.getDataField().equals(content)){
                return n;
            }
        }
        return null;
    }
}

class ContentHolder implements Serializable{
    List<String> content;
    int versionNumber;
    ContentHolder(List<String> content,int versionNumber){
        this.content=content;
        this.versionNumber=versionNumber;
    }
}

public class FileDS implements Serializable{
    ListNode head;
    private int versionNumberCounter=1;

    private ListNode create(List<String> mainBranch,int versionNumber){
        head=new ListNode("Start",versionNumber);
        ListNode prev=head,next=null;
        for(String data:mainBranch){
            next=new ListNode(data,versionNumber);
            prev.next.add(next);
            next.prev=prev;
            prev=next;
        }
        return head;
    }

    public FileDS(List<String> mainBranch,int versionNumberCounter){
        this.versionNumberCounter=versionNumberCounter;
        create(mainBranch,versionNumberCounter);
        this.versionNumberCounter++;
    }

    private int createNewBranch(List<String> sideBranch,int versionNumber){

        ListNode current=head;
        int i=0;
        for(i=0;i<sideBranch.size();i++){
            String content=sideBranch.get(i);
            ListNode each=current.CheckIfNextChildHas(content);
            if(each!= null){
                each.addVersionNumber(versionNumber);
                current=each;
            }
            else{
                break;
            }
        }
        while(i<sideBranch.size()){
            ListNode each=new ListNode(sideBranch.get(i),versionNumber );
            current.next.add(each);
            each.prev=current;
            current=each;
            i++;
        }
        return versionNumber;
    }

    public int createNewBranch(List<String> sideBranch){
        return createNewBranch(sideBranch,versionNumberCounter++);
    }

    void deleteBranch(int versionNumber){
        ListNode current=head;
        while(current!=null){
            ListNode nextNodeToVisit=null;
            for(ListNode each: current.next){
                if(each.checkVersionExist(versionNumber)){
                    each.removeVersionNumber(versionNumber);
                }
                if(each.getVersions().isEmpty()){
                    current.next.remove(each);
                    break;
                }
                else{
                    nextNodeToVisit=each;
                }
            }
            if(nextNodeToVisit==null){
                break;
            }
            else{
                current=nextNodeToVisit;
            }
        }
    }


    // Logic to traverse through the branch
    List<String> traverse(int versionNumber) {
        List<String> contentString=new ArrayList<>();
        ListNode current = head;
        while (current != null) {
            boolean flag = false;
            for (ListNode each : current.next) {
                if (each.checkVersionExist(versionNumber)) {
                    contentString.add(each.getDataField());
                    current = each;
                    flag = true;
                    break;
                }

            }
            if (!flag) {
                current = null;
            }
        }
        return contentString;
    }

    //Update a particular branch
    int updateBranch(List<String> contents,int versionNumber){
        deleteBranch(versionNumber);
        createNewBranch(contents, versionNumber);
        return versionNumber;
    }

    public HashSet<Integer> versionNumbers(){
        HashSet<Integer> allVersions=new HashSet<>();
        for(ListNode e:head.next){
            allVersions.addAll(e.getVersions());
        }
        return allVersions;
    }
    //Traversing all the branches
    List<ContentHolder> traverseAllBranches() {
        HashSet<Integer> allVersions=versionNumbers();
        List<ContentHolder> contentHolder=new ArrayList<>();



        for(Integer e:allVersions){
            ContentHolder ch=new ContentHolder(traverse(e),e);
            contentHolder.add(ch);
        }
        return contentHolder;
    }


}
