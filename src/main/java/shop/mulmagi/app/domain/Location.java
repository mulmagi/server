package shop.mulmagi.app.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder @Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "Integer default 0")
    private Integer umbrellaCount;

    @Column(columnDefinition = "Integer default 0")
    private Integer wrongCount;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    public void updateRental(){
        this.umbrellaCount -= 1;
    }


    public void updateReturn(){
        this.umbrellaCount += 1;
    }

    public void receiveReport(){
        this.wrongCount += 1;
        this.umbrellaCount -= 1;
    }

    public void solveReport(){
        this.wrongCount -= 1;
        this.umbrellaCount += 1;
    }
}
