package overtime.example.domain.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import overtime.example.domain.user.model.LoginUsers;
import overtime.example.domain.user.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserService service;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		//ユーザー情報取得
		LoginUsers loginUser = service.getLoginUser(username);

		//ユーザーが存在しない場合
		if (loginUser == null) {
			throw new UsernameNotFoundException("user not found");
		}

		//権限List作成
		GrantedAuthority authority = new SimpleGrantedAuthority(loginUser.getRole());
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(authority);

		//UserDetails生成(CustomUserDetails:usersテーブルのidを追加)
		CustomUserDetails userDetails = new CustomUserDetails(loginUser.getId(),
				loginUser.getAccount(),
				loginUser.getPass(),
				authorities);

		return userDetails;
	}
}
