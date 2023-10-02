package shop.mulmagi.app.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import shop.mulmagi.app.domain.base.BaseEntity;

import javax.persistence.*;

@Entity
@Builder
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

    @Column(columnDefinition = "Integer default 9000")
    private Integer depositAmount;

    @Column(columnDefinition = "Integer default 1000")
    private Integer paymentAmount;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isReturn;

    @Column(columnDefinition = "Boolean default false")
    private Boolean beforeUse;
}
