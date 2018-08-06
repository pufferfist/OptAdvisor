package utf8.citicup.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户信息实体类
 * 应当包含用户名密码等基础信息，第一次使用推荐组合采集的信息以及持仓组合
 */
@Entity
public class User implements Serializable{
    @Id
    private String username;

    private String password;

    private String name;

    private String birthday;

    private String telephone;

    private String email;

    private String gender;

    private String avatarPath;

    private int w1;

    private int w2;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public int getW1() {
        return w1;
    }

    public void setW1(int w1) {
        this.w1 = w1;
    }

    public int getW2() {
        return w2;
    }

    public void setW2(int w2) {
        this.w2 = w2;
    }

    @Override
    public String toString() {
        return "{"+username+", "+password+"}";
    }
}
