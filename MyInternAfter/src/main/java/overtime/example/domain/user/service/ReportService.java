package overtime.example.domain.user.service;

import java.util.List;

import overtime.example.domain.user.model.Overtime;
import overtime.example.domain.user.model.Reports;
import overtime.example.domain.user.model.ReportsSum;

public interface ReportService {

	/** 報告データ1件作成 */
	public int addReport(Reports report);

	/** 報告データ一1件修正更新 */
	/** 申請書差戻編集時の変更内容をreportデータに反映 */
	public int updateEditReport(Reports report);
	
	/** 社員/報告データ一一覧取得 */
	public List<Reports> getReportList(Integer id);

	/** 社員/報告データ1件取得 */
	public Reports getReport(Integer id);

	/** 社員/報告データ更新 */
	public int editReport(Reports report);

	/** 社員/報告データ新規1件作成（事後報告） */
	public int addNewReport(Reports report);

	/** 次長/報告データ一一覧取得 */
	public List<Reports> getCheckDataList();

	/** 次長//残業報告確認更新処理 */
	public int updChecked(Integer id);

	/** 月次資料集計（確認済みデータ） */
	public List<ReportsSum> getMonthlySum();

	/** 月次資料CSV出力データ一覧取得（全件） */
	public List<Reports> getMonthlyListAll();
	
	/** 個人の最新の累計残業時間1件取得 */
	public Overtime GetTotalOvertime(Integer id);
}
