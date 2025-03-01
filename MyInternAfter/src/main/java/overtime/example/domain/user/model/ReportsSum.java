package overtime.example.domain.user.model;

import java.time.YearMonth;

import lombok.Data;

@Data
public class ReportsSum {
	private YearMonth monthly;
	private Integer usersId;
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
}
