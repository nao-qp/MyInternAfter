package overtime.example.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MonthlySubmitDataMapper {

	/** 次長月次資料提出（1件データ作成） */
	public int insertOne(String yearmonth);
	
	/** 月次資料が提出済みかどうか（1件データ取得） */
	public Integer findOne(String yearmonth);
}
