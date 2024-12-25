package overtime.example.domain.user.model;

import java.time.LocalTime;

import lombok.Data;

@Data
public class WorkPatterns {
	private Integer id;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
}
