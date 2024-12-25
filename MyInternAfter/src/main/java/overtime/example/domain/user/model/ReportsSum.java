package overtime.example.domain.user.model;

import java.time.YearMonth;

import lombok.Data;

@Data
public class ReportsSum {
	private YearMonth monthly;
	private Integer usersId;
	private Integer allSum;
	private Integer wdayDtUnder60;
	private Integer wdayDtOver60;
	private Integer wdayEmnUnder60;
	private Integer wdayEmnOver60;
	private Integer hdayDtUnder60;
	private Integer hdayDtOver60;
	private Integer hdayEmnUnder60;
	private Integer hdayEmnOver60;
	private String usersName;
}
