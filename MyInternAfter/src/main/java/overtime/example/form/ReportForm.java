package overtime.example.form;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import overtime.example.validation.MoreThanRestPeriod;
import overtime.example.validation.ReportEndAfterWorkEnd;
import overtime.example.validation.ReportStartAndEndIsNull;
import overtime.example.validation.ReportStartBeforeWorkStart;
import overtime.example.validation.RestStartBeforeRestEnd;
import overtime.example.validation.RestTimeInOverTime;

@Data
@ReportStartAndEndIsNull(message = "残業開始時間または終了時間を入力してください。")
@ReportStartBeforeWorkStart(message = "開始時間は勤務時間よりも前の時間を入力してください。")
@ReportEndAfterWorkEnd(message = "終了時間は勤務時間よりも後の時間を入力してください。")
@RestStartBeforeRestEnd(message = "休憩開始時間は休憩終了時間よりも前の時間を入力下さい。")
@MoreThanRestPeriod(message = "規定休憩時間以上を入力して下さい。")
@RestTimeInOverTime(message = "休憩時間は残業時間内の時間を入力して下さい。")
public class ReportForm {
	private Integer workPatternsId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate overtimeDate;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime restStartTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime restEndTime;
	private Integer restPeriod;		// 規定休憩時間。入力した休憩開始〜終了時間が規定休憩時間(分)以上かどうか比較用。JavaScriptで設定しバリデーションクラスへ。
	private String reason;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime workPatternsStartTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime workPatternsEndTime;
}
