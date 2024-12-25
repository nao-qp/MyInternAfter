package overtime.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import overtime.example.domain.user.model.WorkPatterns;

@Mapper
public interface WorkPatternMapper {

	/** 勤務パターンマスター取得 */
	public List<WorkPatterns> findMany();

	/** 勤務パターン1件取得 */
	public WorkPatterns findOne(Integer id);

}
