# MySQLの接続情報
DB_USER="root"
DB_PASS="mysql"

# 複数のSQLスクリプトを順番に実行
SQL_FILES=("01_orvertimedb_DBcreate.sql" "02_data_set.sql" "03_insert_test_data.sql")

# ループでSQLファイルを順に実行
for SQL_SCRIPT in "${SQL_FILES[@]}"; do
    echo "実行中: $SQL_SCRIPT"
    sudo mysql -u $DB_USER -p$DB_PASS < $SQL_SCRIPT
done

# スクリプト実行
sudo mysql -u $DB_USER -p$DB_PASS < $SQL_SCRIPT