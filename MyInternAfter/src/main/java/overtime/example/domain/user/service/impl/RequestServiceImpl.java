package overtime.example.domain.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overtime.example.domain.user.model.Requests;
import overtime.example.domain.user.service.RequestService;
import overtime.example.repository.RequestMapper;


@Service
public class RequestServiceImpl implements RequestService {
	@Autowired
	private RequestMapper mapper;

	/** 残業申請データ取得 */
	@Override
	public List<Requests> getRequestList(Integer id) {
		return mapper.findMany(id);
	}

	/** 残業申請データ1件取得 */
	@Override
	public Requests getRequest(Integer id) {
		return mapper.findOne(id);
	}

	/** 残業申請データ1件作成 */
	@Override
	public int addRequest(Requests request) {
		return mapper.insertOne(request);
	}

	/** 次長//残業申請確認データ一覧取得 */
	@Override
	public List<Requests> getCheckDataList() {
		return mapper.findManyCheckData();
	}

	/** 次長//残業申請確認更新処理 */
	@Override
	public int updChecked(Integer id) {
		return mapper.updateOne(id);
	}

	/** 課長//残業申請承認データ一覧取得（次長確認済みデータ） */
	@Override
	public List<Requests> getApproveDataList() {
		return mapper.findManyApproveData();
	}

	/** 課長//残業申請承認更新処理 */
	@Override
	public int updateApproved(Integer id, Integer approvalUsersId) {
		return mapper.updateOneApprove(id, approvalUsersId);
	}

	/** 課長//残業申請差し戻し更新処理 */
	@Override
	public int updateReturn(Integer id) {
		return mapper.updateOneReturn(id);
	}

	/** 社員//申請書修正更新処理 */
	@Override
	public int updateEdit(Requests request) {
		return mapper.updateOneEdit(request);
	}
}
