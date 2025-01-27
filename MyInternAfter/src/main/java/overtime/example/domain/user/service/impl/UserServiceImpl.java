package overtime.example.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overtime.example.domain.user.model.Users;
import overtime.example.domain.user.service.UserService;
import overtime.example.repository.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper mapper;

	/** ユーザー情報取得 */
	@Override
	public Users getUser(String account) {
		return mapper.findUser(account);
	}
}
