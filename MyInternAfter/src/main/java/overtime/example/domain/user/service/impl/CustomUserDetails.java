package overtime.example.domain.user.service.impl;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	private Integer id; 		// (追加)ユーザーID
    private String account;		// ログイン時に使用するユーザーID
    private String pass;
	private Integer role;
    // (追加)
    private String name;		// ユーザー名
	private Integer departmentsId;
	private String departmentsName;
	private Integer rolesId;
	private Integer workPatternsId;

    private Collection<? extends GrantedAuthority> authorities;

    //idを追加。（usersテーブルのid。）
    public CustomUserDetails(Integer id, String account, String pass, Integer role, 
    		String name, Integer departmentsId, String departmentsName, Integer rolesId, Integer workPatternsId,
    		Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.account = account;
        this.pass = pass;
        this.role = role;
        this.name = name;
        this.departmentsId = departmentsId;
        this.departmentsName = departmentsName;
        this.rolesId = rolesId;
        this.workPatternsId = workPatternsId;
        
        this.authorities = authorities;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return account;		 // ログイン時に使用するユーザーID
    }

    @Override
    public String getPassword() {
        return pass;				//TODO: pass？password？確認
    }

    public Integer getRole() {
        return role;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getDepartmentsId() {
        return departmentsId;
    }
    
    public String getDepartmentsName() {
        return departmentsName;
    }
    
    public Integer getRolesId() {
        return rolesId;
    }
    
    public Integer getWorkPatternsId() {
        return workPatternsId;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
