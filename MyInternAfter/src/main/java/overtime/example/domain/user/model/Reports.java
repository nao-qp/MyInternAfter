package overtime.example.domain.user.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Reports {
	private Integer id;
	private Integer usersId;
	private Integer account;
	private Integer requestsId;
	private LocalDate reportDate;
	private LocalDate overtimeDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private LocalTime restStartTime;
	private LocalTime restEndTime;
	private Integer restPeriod;
	private String reason;
	private Integer isChecked;
	private Integer actualOvertime;
	private Integer wdayDtUnder60;
	private Integer wdayDtOver60;
	private Integer wdayEmnUnder60;
	private Integer wdayEmnOver60;
	private Integer hdayDtUnder60;
	private Integer hdayDtOver60;
	private Integer hdayEmnUnder60;
	private Integer hdayEmnOver60;
	private LocalDateTime createDateTime;
	private LocalDateTime updateDateTime;
	//申請データ情報
	private Integer departmentsId;
	private Integer workPatternsId;
	private LocalDateTime requestDate;
	private LocalTime requestsStartTime;
	private LocalTime requestsEndTime;
	private LocalTime requestsRestPeriod;
	private String requestsReason;
	private LocalDate approvalDate;
	private Integer approvalUsersId;
	private String usersName;
	private String departmentsName;
	private String approvalUsersName;
	private String workPatternsName;
	private LocalTime workPatternsStartTime;
	private LocalTime workPatternsEndTime;
}









