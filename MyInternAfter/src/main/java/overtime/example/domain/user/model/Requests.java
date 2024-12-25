package overtime.example.domain.user.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Requests {
	private Integer id;
	private Integer usersId;
	private Integer departmentsId;
	private Integer workPatternsId;
	private LocalDateTime requestDate;
	private LocalDate overtimeDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private LocalTime restPeriod;
	private String reason;
	private LocalDate approvalDate;
	private Integer approvalUsersId;
	private Integer isChecked;
	private String approvalStatus;
	private LocalDateTime createDateTime;
	private LocalDateTime updateDateTime;
	private String usersName;
	private String departmentsName;
	private String approvalUsersName;
	private LocalTime workPatternsStartTime;
	private LocalTime workPatternsEndTime;
	private String workPatternsName;
}
