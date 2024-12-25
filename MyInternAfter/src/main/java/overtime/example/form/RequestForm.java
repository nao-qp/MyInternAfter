package overtime.example.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import overtime.example.validation.EndAfterWorkEnd;
import overtime.example.validation.StartAndEndIsNull;
import overtime.example.validation.StartBeforeWorkStart;

@Data
@StartAndEndIsNull(message = "残業開始時間または終了時間を入力してください。")
@StartBeforeWorkStart(message = "開始時間は勤務時間よりも前の時間を入力してください。")
@EndAfterWorkEnd(message = "終了時間は勤務時間よりも後の時間を入力してください。")
public class RequestForm {
	private Integer workPatternsId;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime workPatternsStartTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime workPatternsEndTime;
	private LocalDateTime requestDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate overtimeDate;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime restPeriod;
	private String reason;
}
