package Service;

import java.io.Serializable;
import java.time.Instant;

public class Log implements Serializable {
    public long versionNumber;
    public String scope;
    public String content;
    public Instant timestamp;
    Log(long versionNumber,String scope,String content){
        this.versionNumber=versionNumber;
        this.scope=scope;
        this.content=content;
        timestamp=Instant.now();
    }
    Log(long versionNumber,String scope,String content,Instant timestamp){
        this.versionNumber=versionNumber;
        this.scope=scope;
        this.content=content;
        this.timestamp=timestamp;
    }



}
