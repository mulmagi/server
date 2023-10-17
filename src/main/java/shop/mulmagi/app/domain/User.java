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

    public void updateRental(){
        this.isRental = true;
    }

    public void updatePoint(){
        this.point -= 10000;
    }

    public void updateReturn(){
        this.isRental = false;
        //this.rentalCount += 1;
    }

    public void returnPoint(Integer amount){
        this.point += amount;
    }
}
