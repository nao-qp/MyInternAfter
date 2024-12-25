package overtime.example.domain.user.model;

import lombok.Data;

@Data
public class Users {
	private Integer id;
	private String account;
	private String pass;
	private String name;
	private Integer departmentsId;
	private String departmentsName;
	private Integer rolesId;
	private Integer role;
	private Integer workPatternsId;

}
