package shop.mulmagi.app.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mulmagi.app.domain.base.BaseEntity;
import shop.mulmagi.app.domain.enums.MessageType;

import javax.persistence.*;

@Entity
@Builder @Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private User user; //userId -> 실제로는 roomId(admin과 채팅하는 일반 사용자 id)

	@Enumerated(EnumType.STRING)
	private MessageType type;

	private String content; //type==IMAGE면 img url

	@Column(columnDefinition = "Boolean default false")
	private boolean isAdmin; //true면 admin이 사용자에게 보낸 message
}