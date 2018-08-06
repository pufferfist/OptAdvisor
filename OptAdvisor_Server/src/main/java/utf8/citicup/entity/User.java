package utf8.citicup.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户信息实体类
 * 应当包含用户名密码等基础信息，第一次使用推荐组合采集的信息以及持仓组合
 */
@Entity
public class User implements Serializable{
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{"+username+", "+password+"}";
    }
}
