package utf8.citicup.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用于风险跟踪过程中的提示信息实体类
 */
@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    private String userName;

    private boolean readStatus;

    private String message;

    public Message(String userName,String message){
        readStatus=false;
        this.userName=userName;
        this.message=message;
    }

    public Message(){}

    public Long getId() { return id; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public boolean isReadStatus() { return readStatus; }

    public void setReadStatus(boolean readStatus) { this.readStatus = readStatus; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}
