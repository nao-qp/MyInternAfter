package overtime.example.domain.user.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;

import lombok.Data;

@Data
public class ReportsSumDedail {
	private YearMonth monthly;
	private Integer reportsId;
	private Integer usersId;
	private LocalDate overtimeDate;
	private LocalTime startTime;
	private LocalTime workPatternsStartTime;
	private LocalTime workPatternsEndTime;
	private LocalTime endTime;
	private String reason;
	private Double allSum;
	private Double wdayDtUnder60;
	private Double wdayDtOver60;
	private Double wdayEmnUnder60;
	private Double wdayEmnOver60;
	private Double hdayDtUnder60;
	private Double hdayDtOver60;
	private Double hdayEmnUnder60;
	private Double hdayEmnOver60;
	private String usersName;
	// 表示用フィールド
	private String formatOvertimeDate;
}
