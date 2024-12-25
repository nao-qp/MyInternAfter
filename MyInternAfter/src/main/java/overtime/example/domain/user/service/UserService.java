package overtime.example.domain.user.service;

import overtime.example.domain.user.model.LoginUsers;
import overtime.example.domain.user.model.Users;

public interface UserService {

	/** ログインユーザー情報取得 */
	public LoginUsers getLoginUser(String account);

	/** ユーザー情報取得 */
	public Users getUser(Integer id);
}
