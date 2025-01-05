-- DB作成
create database IF NOT EXISTS overtimedb;
USE overtimedb;

-- 文字コード、文字照合順序を設定
ALTER DATABASE overtimedb
CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- テーブル作成
CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    account VARCHAR(20) NOT NULL COMMENT '社員番号',
    pass VARCHAR(60) NOT NULL COMMENT 'パスワード',
    name VARCHAR(20) NOT NULL COMMENT '社員名',
    departments_id INT NOT NULL COMMENT '部署',
    roles_id INT NOT NULL COMMENT '役職',
    work_patterns_id INT NOT NULL COMMENT '勤務パターン',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '削除フラグ',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id),
    UNIQUE(account),
    INDEX IX_users_account (account)
) COMMENT = 'ユーザー情報'
;

CREATE TABLE IF NOT EXISTS requests(
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    users_id INT NOT NULL COMMENT 'ユーザーID',
    departments_id INT NOT NULL COMMENT '部署ID',
    work_patterns_id INT NOT NULL COMMENT '勤務パターンID',
    request_date DATETIME NOT NULL COMMENT '申請日',
    overtime_date DATE NOT NULL COMMENT '残業実施日',
    start_time TIME NOT NULL COMMENT '前残業開始時間',
    end_time TIME NOT NULL COMMENT '後残業終了時間',
    rest_period INT NOT NULL COMMENT '休憩時間',
    reason VARCHAR(500) NOT NULL COMMENT '残業理由',
    approval_date DATE COMMENT '承認日',
    approval_users_id INT COMMENT '承認者ID',
    is_checked INT NOT NULL DEFAULT 0 COMMENT '確認完了フラグ',
    approval_status VARCHAR(10) COMMENT '決裁状態',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id)
) COMMENT = '残業申請データ'
;

CREATE TABLE IF NOT EXISTS roles (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    name VARCHAR(20) NOT NULL COMMENT '役職名',
    role VARCHAR(3) NOT NULL COMMENT '権限',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id)
) COMMENT = '役職・権限マスター'
;

CREATE TABLE IF NOT EXISTS departments (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    name VARCHAR(30) NOT NULL COMMENT '部署名',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id)
) COMMENT = '部署マスター'
;

CREATE TABLE IF NOT EXISTS work_patterns (
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    name VARCHAR(10) NOT NULL COMMENT '勤務パターン名',
    start_time TIME NOT NULL COMMENT '開始時刻',
    end_time TIME NOT NULL COMMENT '終了時刻',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id)
) COMMENT = '勤務パターンマスター'
;

CREATE TABLE IF NOT EXISTS reports(
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    users_id INT NOT NULL COMMENT 'ユーザーID',
    requests_id INT NOT NULL COMMENT '申請ID',
    work_patterns_id INT NOT NULL COMMENT '勤務パターンID',
    report_date DATE NOT NULL COMMENT '報告日',
    overtime_date DATE NOT NULL COMMENT '残業実施日',
    start_time TIME NOT NULL COMMENT '前残業開始時間',
    end_time TIME NOT NULL COMMENT '後残業終了時間',
    rest_start_time TIME NOT NULL DEFAULT '00:00:00' COMMENT '休憩開始時間',
    rest_end_time TIME NOT NULL DEFAULT '00:00:00' COMMENT '休憩終了時間',
    rest_period INT NOT NULL DEFAULT 0 COMMENT '休憩時間',
    actual_overtime INT NOT NULL DEFAULT 0 COMMENT '残業時間',
    reason VARCHAR(500) COMMENT '残業報告',
    is_checked INT NOT NULL DEFAULT 0 COMMENT '確認完了フラグ',
    wday_dt_under60 INT NOT NULL DEFAULT 0 COMMENT '累計・平日日中60未満',
    wday_dt_over60 INT NOT NULL DEFAULT 0 COMMENT '累計・平日日中60超',
    wday_emn_under60 INT NOT NULL DEFAULT 0 COMMENT '累計・平日深夜早朝60未満',
    wday_emn_over60 INT NOT NULL DEFAULT 0 COMMENT '累計・平日深夜早朝60超',
    hday_dt_under60 INT NOT NULL DEFAULT 0 COMMENT '累計・休日日中60未満',
    hday_dt_over60 INT NOT NULL DEFAULT 0 COMMENT '累計・休日日中60超',
    hday_emn_under60 INT NOT NULL DEFAULT 0 COMMENT '累計・休日深夜早朝60未満',
    hday_emn_over60 INT NOT NULL DEFAULT 0 COMMENT '累計・休日深夜早朝60超',
    create_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
    update_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY(id)
) COMMENT = '残業報告データ'
;

CREATE TABLE IF NOT EXISTS monthlysubmitdatas(
    id INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    yearmonth VARCHAR(7) NOT NULL COMMENT '年月',
    submitted TINYINT NOT NULL DEFAULT 0 COMMENT '提出済みフラグ',
    PRIMARY KEY(id)
    ) COMMENT = '月次資料報告済データ'
;

    