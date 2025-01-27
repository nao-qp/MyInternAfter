package overtime.example.domain.user.service;

import overtime.example.domain.user.model.Users;

public interface UserService {

	/** ユーザー情報取得 */
	public Users getUser(String account);
}
