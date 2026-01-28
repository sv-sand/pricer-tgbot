package ru.svsand.pricer.tgbot.db;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 28.10.2025
 */

@Entity
@Data
@Table(name = "searches")
public class SearchDao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserDao user;

	@Column(name = "store")
	private String store;

	@Column(name = "key_words")
	private String keyWords;

	@Column(name = "target_price")
	private Double targetPrice;

	@Version
	private Long version;
}
