package ru.svsand.pricer.tgbot.db;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.svsand.pricer.tgbot.logic.User;

import java.util.List;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 05.11.2025
 */

@Service
public class UserManager {
	@Autowired
	UserRepository UserRepository;

	@Transactional
	public User findByTgId(Long tgId) {
		List<UserEntity> entities = UserRepository.findByTgId(tgId);
		if (entities.isEmpty())
			return null;

		return map(entities.get(0));
	}

	@Transactional
	public User save(@NotNull User user) {
		return map(UserRepository.save(map(user)));
	}

	// Conversion

	public static UserEntity map(@NotNull User User) {
		UserEntity UserEntity = new UserEntity();
		if (!User.isNew())
			UserEntity.setId(User.getId());

		UserEntity.setName(User.getName());
		UserEntity.setTgId(User.getTgId());
		UserEntity.setVersion(User.getVersion());

		return UserEntity;
	}

	public static User map(UserEntity UserEntity) {
		if (UserEntity == null)
			return null;

		return User.builder()
				.id(UserEntity.getId())
				.name(UserEntity.getName())
				.tgId(UserEntity.getTgId())
				.version(UserEntity.getVersion())
				.build();
	}
}
