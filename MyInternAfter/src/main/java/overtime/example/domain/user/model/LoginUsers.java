package overtime.example.domain.user.model;

import lombok.Data;

@Data
public class LoginUsers {
	private Integer id;
	private String account;
	private String pass;
	private String role;
}
