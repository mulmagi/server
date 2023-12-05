package shop.mulmagi.app.domain;

import lombok.*;
import shop.mulmagi.app.domain.base.BaseEntity;
import shop.mulmagi.app.domain.enums.UserStatus;

import javax.persistence.*;

@Entity
@Builder @Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;

    @Column(unique = true, nullable = false)
    private  String phoneNumber;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isAdmin;

    @Column(columnDefinition = "Integer default 0")
    private Integer level;

    @Column(columnDefinition = "Integer default 0")
    private Double experience;

    @Column(columnDefinition = "Integer default 0")
    private Integer point;

    private String profileUrl;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isRental;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isComplaining;

    private String firebaseToken;
    @Column(columnDefinition = "Boolean default false")
    private boolean agreeTerms;
    @Column(columnDefinition = "Boolean default false")
    private boolean notificationEnabled;



    public void updateRental(){
        this.isRental = true;
    }

    public void updatePoint(){
        this.point -= 10000;
    }
    public void chargePoint(Integer amount){
        this.point += amount;
    }

    public void updateReturn(){
        this.isRental = false;
        //this.rentalCount += 1;
    }

    public void returnPoint(Integer amount){
        this.point += amount;
    }

    public void updateExperience(Integer payment){
        this.experience += payment * 0.05;
    }
    public void resetExperience(){ this.experience = 0.0; }

    public void startComplain(){ this.isComplaining = true; }
    public void endComplain(){ this.isComplaining = false; }

    public void updateNotificationEnabled(boolean notificationEnabled){
        this.notificationEnabled = notificationEnabled;

    }

    public void updateStatus(UserStatus status){
        this.status = status;
    }
    public void resetUser(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.level = 0;
        this.experience = 0.0;
        this.point = 0;
        this.profileUrl = " ";
        this.isRental = false;
        this.isAdmin = false;
        this.isComplaining = false;
        this.status = UserStatus.ACTIVE;
        this.notificationEnabled = false;
        this.agreeTerms = false;
    }


}
