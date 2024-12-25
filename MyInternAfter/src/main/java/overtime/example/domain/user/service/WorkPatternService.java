package overtime.example.domain.user.service;

import java.util.List;

import overtime.example.domain.user.model.WorkPatterns;

public interface WorkPatternService {

	/** 勤務パターンマスター取得 */
	public List<WorkPatterns> getWorkPatternMaster();

	/** 勤務パターン1件取得 */
	public WorkPatterns getWorkPattern(Integer id);
}
