package shop.mulmagi.app.domain;

import lombok.*;
import shop.mulmagi.app.domain.base.BaseEntity;
import shop.mulmagi.app.domain.enums.PaymentMethod;
import javax.persistence.*;

@Entity
@Builder @Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String impUid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer amount;

    @Column(columnDefinition = "Double default 1")
    private Double discountRate;
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

}
