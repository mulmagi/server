package shop.mulmagi.app.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder @Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UmbrellaStand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(unique = true, nullable = false)
    private Long qrCode;

    private Integer number;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isWrong;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isUmbrella;

    public void updateRental(){
        this.isUmbrella = false;
    }

    public void updateReturn(){
        this.isUmbrella = true;
    }
}
