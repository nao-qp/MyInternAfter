package overtime.example.domain.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overtime.example.domain.user.model.WorkPatterns;
import overtime.example.domain.user.service.WorkPatternService;
import overtime.example.repository.WorkPatternMapper;

@Service
public class WorkPatternServiceImpl implements WorkPatternService {
	@Autowired
	private WorkPatternMapper mapper;

	/** 勤務パターンマスター取得 */
	@Override
	public List<WorkPatterns> getWorkPatternMaster() {
		return mapper.findMany();
	}

	/** 勤務パターン1件取得 */
	@Override
	public WorkPatterns getWorkPattern(Integer id) {
		return mapper.findOne(id);
	}
}
