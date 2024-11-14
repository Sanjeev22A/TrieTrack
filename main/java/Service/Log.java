package Service;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

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

    // Override equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Log log = (Log) obj;
        return versionNumber == log.versionNumber &&
                Objects.equals(scope, log.scope) &&
                Objects.equals(content, log.content) &&
                Objects.equals(timestamp, log.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionNumber, scope, content, timestamp);
    }

}
