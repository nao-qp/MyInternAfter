-- 申請・報告データリセット
delete from requests;
delete from reports;


-- 承認リセット
update requests set approval_status = null;

-- 次長月次資料提出リセット
delete from monthlysubmitdatas;