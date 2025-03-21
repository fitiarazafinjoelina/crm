package site.easy.to.build.crm.entity.temp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_login_info_temp")
public class CustomerLoginInfoTemp implements CsvClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    @CsvBindByName
    private String username;

    @Column(name = "password")
    @CsvBindByName
    private String password;

    @Column(name = "token")
    @CsvBindByName
    private String token;

    @Column(name = "password_set")
    @CsvBindByName
    private Boolean passwordSet;

    public String getTempTableName(){
        return "customer_login_info_temp";
    }
    public String getTempTable(){
        return "create temporary table if not exists customer_login_info_temp\n" +
                "(\n" +
                "    id           int auto_increment\n" +
                "        primary key,\n" +
                "    password     varchar(255)         null,\n" +
                "    username     varchar(255)         null,\n" +
                "    token        varchar(500)         null,\n" +
                "    password_set tinyint(1) default 0 null,\n" +
                "    constraint token\n" +
                "        unique (token)\n" +
                ");";
    }

    public CustomerLoginInfoTemp() {
    }

    public CustomerLoginInfoTemp(String username, String password, String token, Boolean passwordSet) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.passwordSet = passwordSet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isPasswordSet() {
        return passwordSet;
    }

    public void setPasswordSet(Boolean passwordSet) {
        this.passwordSet = passwordSet;
    }

    public String getEmail() {
        return username;
    }

    public void setEmail(String email) {
        this.username = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getPasswordSet() {
        return passwordSet;
    }
}
