package overtime.example.domain.user.service;

import java.util.List;

import overtime.example.domain.user.model.Requests;

public interface RequestService {

	/** 残業申請データ一覧取得 */
	public List<Requests> getRequestList(Integer id);

	/** 残業申請データ1件取得 */
	public Requests getRequest(Integer id);

	/** 残業申請データ1件更新 */
	public int addRequest(Requests request);

	/** 次長//残業申請確認データ一覧取得 */
	public List<Requests> getCheckDataList();

	/** 次長//残業申請確認更新処理 */
	public int updChecked(Integer id);

	/** 課長//残業申請承認データ一覧取得（次長確認済みデータ） */
	public List<Requests> getApproveDataList();

	/** 課長//残業申請承認更新処理 */
	public int updateApproved(Integer id, Integer approvalUsersId);

	/** 課長//残業申請差し戻し更新処理 */
	public int updateReturn(Integer id);

	/** 社員//申請書修正更新処理 */
	public int updateEdit(Requests request);
}
