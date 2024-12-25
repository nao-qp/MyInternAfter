package overtime.example.domain.user.model;

import lombok.Data;

@Data
public class Overtime {
	private int actualOvertime;
	private int wdayDtUnder60;
	private int wdayDtOver60;
	private int wdayEmnUnder60;
	private int wdayEmnOver60;
	private int hdayDtUnder60;
	private int hdayDtOver60;
	private int hdayEmnUnder60;
	private int hdayEmnOver60;
}