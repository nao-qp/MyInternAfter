package overtime.example.repository;

import org.apache.ibatis.annotations.Mapper;

import overtime.example.domain.user.model.LoginUsers;
import overtime.example.domain.user.model.Users;

@Mapper
public interface UserMapper {

	/** ログインユーザー情報取得 */
	public LoginUsers findLoginUser(String account);

	/** ユーザー情報取得 */
	public Users findUser(Integer id);
}
