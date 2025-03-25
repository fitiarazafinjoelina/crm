package site.easy.to.build.crm.entity.temp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import site.easy.to.build.crm.customValidations.customer.UniqueEmail;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.User;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="customer_temp")
public class CustomerTemp  implements CsvClass {
    public interface CustomerUpdateValidationGroupInclusion {}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "name")
    @CsvBindByName(column = "name")
    @NotBlank(message = "Name is required", groups = {Default.class, Customer.CustomerUpdateValidationGroupInclusion.class})
    private String name;

    @Column(name = "email")
    @CsvBindByName(column = "email")
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email format")
    @UniqueEmail
    private String email;

    @Column(name = "position")
    @CsvBindByName(column = "position")
    private String position;

    @Column(name = "phone")
    @CsvBindByName(column = "phone")
    private String phone;

    @Column(name = "address")
    @CsvBindByName(column = "address")
    private String address;

    @Column(name = "city")
    @CsvBindByName(column = "city")
    private String city;

    @Column(name = "state")
    @CsvBindByName(column = "state")
    private String state;

    @Column(name = "country")
    @CsvBindByName(column = "country")
    @NotBlank(message = "Country is required", groups = {Default.class, Customer.CustomerUpdateValidationGroupInclusion.class})
    private String country;

    @Column(name = "description")
    @CsvBindByName(column = "description")
    private String description;

    @Column(name = "twitter")
    @CsvBindByName(column = "twitter")
    private String twitter;

    @Column(name = "facebook")
    @CsvBindByName(column = "facebook")
    private String facebook;

    @Column(name = "youtube")
    @CsvBindByName(column = "youtube")
    private String youtube;

    @CsvBindByName(column = "user_id")
    private Integer userId;

    @CsvBindByName(column = "profile_id")
    private Integer profileId;


    @Column(name = "created_at")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    @CsvBindByName(column = "created_at")
    private LocalDateTime createdAt;

    public String getTempTableName(){
        return "customer_temp";
    }
    public String getTempTable(){
        return "create temporary table customer_temp\n" +
                "(\n" +
                "    customer_id int unsigned auto_increment\n" +
                "        primary key,\n" +
                "    name        varchar(255) null,\n" +
                "    phone       varchar(20)  null,\n" +
                "    address     varchar(255) null,\n" +
                "    city        varchar(255) null,\n" +
                "    state       varchar(255) null,\n" +
                "    country     varchar(255) null,\n" +
                "    user_id     int          null,\n" +
                "    description text         null,\n" +
                "    position    varchar(255) null,\n" +
                "    twitter     varchar(255) null,\n" +
                "    facebook    varchar(255) null,\n" +
                "    youtube     varchar(255) null,\n" +
                "    created_at  datetime     null,\n" +
                "    email       varchar(255) null,\n" +
                "    profile_id  int          null\n" +
                ")";
    }
    @Override
    public boolean isValid() {
        return email!=null && !email.isEmpty();
    }
    public CustomerTemp() {
    }

    public CustomerTemp(String name, String email, String position, String phone, String address, String city, String state, String country,
                    String description, String twitter, String facebook, String youtube, Integer userId, Integer profileId,
                    LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.description = description;
        this.twitter = twitter;
        this.facebook = facebook;
        this.youtube = youtube;
        this.userId = userId;
        this.profileId = profileId;
        this.createdAt = createdAt;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerTemp customer = (CustomerTemp) o;
        return customer.getEmail().compareTo(email) == 0 &&
                Objects.equals(name, customer.getName()) &&
                Objects.equals(address, customer.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, address);
    }
}
