package overtime.example.repository;

import org.apache.ibatis.annotations.Mapper;

import overtime.example.domain.user.model.Users;

@Mapper
public interface UserMapper {

	/** ユーザー情報取得 */
	public Users findUser(String account);
}
