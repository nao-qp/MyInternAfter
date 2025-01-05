USE overtimedb;

DELETE FROM requests;
-- テストデータ
-- 作成し直す

INSERT INTO requests (id, users_id, departments_id, work_patterns_id, request_date, 
    start_time, end_time, rest_period, reason, approval_date, approval_users_id, is_checked, approval_status)
VALUE (1, 1, 1, 1, '2024-11-22',
    '2024-11-22 17:15:00', '2024-11-22 18:15:00', '00:00:00', '顧客対応のため', NULL, NULL, 0, NULL);
INSERT INTO requests (id, users_id, departments_id, work_patterns_id, request_date, 
    start_time, end_time, rest_period, reason, approval_date, approval_users_id, is_checked, approval_status)
VALUE (2, 1, 1, 1, '2024-11-23',
    '2024-11-23 17:15:00', '2024-11-23 18:15:00', '00:00:00', 'XX会議資料作成のため', NULL, NULL, 1, NULL);


