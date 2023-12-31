package shop.mulmagi.app.domain;

import lombok.*;
import shop.mulmagi.app.domain.base.BaseEntity;
import shop.mulmagi.app.domain.enums.NotificationType;

import javax.persistence.*;

@Entity
@Builder @Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rental extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rental_umbrella_stand_id")
    private UmbrellaStand rentalUmbrellaStand;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "return_umbrella_stand_id")
    private UmbrellaStand returnUmbrellaStand;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isWrong;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isOverdue;

    @Column(columnDefinition = "Integer default 0")
    private Integer overdueAmount;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isReturn;

    public void updateReturn(UmbrellaStand umbrellaStand){
        this.isReturn = true;
        this.returnUmbrellaStand = umbrellaStand;
    }

    public void updateIsOverdue(){
        this.isOverdue = true;
    }

    public Boolean updateOverdueAmount(Integer rentalPeriod){
        Integer overduePeriod = rentalPeriod - 7;

        this.overdueAmount = 500*overduePeriod;

        if(this.overdueAmount >= 9000){
            this.isReturn = true;
            this.overdueAmount = 9000;
            return true;
        }
        return false;
    }
    public void receiveReport(){ this.isWrong = true; }

    public void solveReport(){ this.isWrong = false; }
}
