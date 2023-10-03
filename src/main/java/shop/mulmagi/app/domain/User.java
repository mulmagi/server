package shop.mulmagi.app.domain;

import lombok.*;
import shop.mulmagi.app.domain.base.BaseEntity;
import shop.mulmagi.app.domain.enums.UserStatus;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private  Integer phoneNumber;

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
}
