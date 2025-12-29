# 鍥句功鎵撳垎绯荤粺鏁版嵁搴撳垵濮嬪寲鑴氭湰
# 鍔熻兘锛氬垱寤烘暟鎹簱銆佽〃缁撴瀯锛屽苟鎻掑叆绀轰緥鏁版嵁

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   鍥句功鎵撳垎绯荤粺 - 鏁版嵁搴撳垵濮嬪寲" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 閰嶇疆鍙傛暟
$MySQLPath = ""
$MySQLHost = "localhost"
$MySQLPort = "3306"
$MySQLUser = "root"
$MySQLPassword = ""
$DatabaseName = "book_rating_db"
$SqlScriptFile = "database/book_rating.sql"

# 鑷姩鏌ユ壘MySQL璺緞
Write-Host "姝ｅ湪鏌ユ壘MySQL..." -ForegroundColor Yellow

$possiblePaths = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 5.6\bin\mysql.exe",
    "C:\xampp\mysql\bin\mysql.exe",
    "C:\wamp64\bin\mysql\mysql8.0.31\bin\mysql.exe",
    "C:\wamp\bin\mysql\mysql5.7.36\bin\mysql.exe"
)

foreach ($path in $possiblePaths) {
    if (Test-Path $path) {
        $MySQLPath = $path
        Write-Host "鎵惧埌MySQL: $path" -ForegroundColor Green
        break
    }
}

if (-not $MySQLPath) {
    Write-Host "閿欒: 鏈壘鍒癕ySQL瀹夎" -ForegroundColor Red
    Write-Host "璇风‘淇滿ySQL宸插畨瑁呭苟娣诲姞鍒癙ATH鐜鍙橀噺" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "鎴栬€呮墜鍔ㄦ寚瀹歁ySQL璺緞:" -ForegroundColor Yellow
    $MySQLPath = Read-Host "璇疯緭鍏ysql.exe瀹屾暣璺緞"
    
    if (-not (Test-Path $MySQLPath)) {
        Write-Host "閿欒: 鎸囧畾鐨勮矾寰勬棤鏁? -ForegroundColor Red
        Read-Host "鎸夊洖杞﹂敭閫€鍑?
        exit 1
    }
}

# 妫€鏌QL鑴氭湰鏂囦欢
if (-not (Test-Path $SqlScriptFile)) {
    Write-Host "閿欒: 鏈壘鍒癝QL鑴氭湰鏂囦欢: $SqlScriptFile" -ForegroundColor Red
    Write-Host "璇风‘淇濊剼鏈枃浠跺瓨鍦? -ForegroundColor Yellow
    Read-Host "鎸夊洖杞﹂敭閫€鍑?
    exit 1
}

# 鑾峰彇MySQL瀵嗙爜
if (-not $MySQLPassword) {
    $securePassword = Read-Host "璇疯緭鍏ySQL root瀵嗙爜" -AsSecureString
    $MySQLPassword = [Runtime.InteropServices.Marshal]::PtrToStringAuto(
        [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    )
}

Write-Host ""
Write-Host "璇烽€夋嫨鎿嶄綔:" -ForegroundColor Yellow
Write-Host "1. 鍒濆鍖栨暟鎹簱锛堝垱寤烘暟鎹簱鍜岃〃锛?
Write-Host "2. 閲嶇疆鏁版嵁搴擄紙鍒犻櫎鍚庨噸鏂板垱寤猴級"
Write-Host "3. 浠呮墽琛孲QL鑴氭湰"
Write-Host "4. 閫€鍑?
Write-Host ""

$choice = Read-Host "璇疯緭鍏ラ€夐」 (1-4)"
Write-Host ""

# 鏋勫缓MySQL杩炴帴鍙傛暟
$mysqlArgs = @(
    "-h", $MySQLHost,
    "-P", $MySQLPort,
    "-u", $MySQLUser,
    "-p$MySQLPassword",
    "--default-character-set=utf8mb4"
)

switch ($choice) {
    "1" {
        # 鍒濆鍖栨暟鎹簱
        Write-Host "姝ｅ湪鍒濆鍖栨暟鎹簱..." -ForegroundColor Yellow
        
        # 鍏堝垱寤烘暟鎹簱锛堝鏋滀笉瀛樺湪锛?
        $createDbSQL = "CREATE DATABASE IF NOT EXISTS $DatabaseName CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
        
        Write-Host "鍒涘缓鏁版嵁搴?.." -ForegroundColor Gray
        & $MySQLPath @mysqlArgs -e $createDbSQL
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "鍒涘缓鏁版嵁搴撳け璐? -ForegroundColor Red
            Read-Host "鎸夊洖杞﹂敭閫€鍑?
            exit 1
        }
        
        Write-Host "鏁版嵁搴撳垱寤烘垚鍔? -ForegroundColor Green
        
        # 鎵цSQL鑴氭湰
        Write-Host "鎵цSQL鑴氭湰..." -ForegroundColor Gray
        & $MySQLPath @mysqlArgs $DatabaseName -e "source $SqlScriptFile"
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "鎵цSQL鑴氭湰澶辫触" -ForegroundColor Red
            Read-Host "鎸夊洖杞﹂敭閫€鍑?
            exit 1
        }
        
        Write-Host "鏁版嵁搴撳垵濮嬪寲瀹屾垚" -ForegroundColor Green
    }
    
    "2" {
        # 閲嶇疆鏁版嵁搴?
        Write-Host "姝ｅ湪閲嶇疆鏁版嵁搴?.." -ForegroundColor Yellow
        Write-Host "璀﹀憡: 杩欏皢鍒犻櫎鐜版湁鏁版嵁搴撳苟閲嶆柊鍒涘缓锛? -ForegroundColor Red
        
        $confirm = Read-Host "纭閲嶇疆鏁版嵁搴擄紵(杈撳叆 yes 缁х画)"
        if ($confirm -ne "yes") {
            Write-Host "鎿嶄綔宸插彇娑? -ForegroundColor Yellow
            Read-Host "鎸夊洖杞﹂敭閫€鍑?
            exit 0
        }
        
        # 鍒犻櫎鏁版嵁搴擄紙濡傛灉瀛樺湪锛?
        Write-Host "鍒犻櫎鐜版湁鏁版嵁搴?.." -ForegroundColor Gray
        & $MySQLPath @mysqlArgs -e "DROP DATABASE IF EXISTS $DatabaseName;"
        
        # 鍒涘缓鏂版暟鎹簱
        Write-Host "鍒涘缓鏂版暟鎹簱..." -ForegroundColor Gray
        $createDbSQL = "CREATE DATABASE $DatabaseName CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
        & $MySQLPath @mysqlArgs -e $createDbSQL
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "鍒涘缓鏁版嵁搴撳け璐? -ForegroundColor Red
            Read-Host "鎸夊洖杞﹂敭閫€鍑?
            exit 1
        }
        
        # 鎵цSQL鑴氭湰
        Write-Host "鎵цSQL鑴氭湰..." -ForegroundColor Gray
        & $MySQLPath @mysqlArgs $DatabaseName -e "source $SqlScriptFile"
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "鎵цSQL鑴氭湰澶辫触" -ForegroundColor Red
            Read-Host "鎸夊洖杞﹂敭閫€鍑?
            exit 1
        }
        
        Write-Host "鏁版嵁搴撻噸缃畬鎴? -ForegroundColor Green
    }
    
    "3" {
        # 浠呮墽琛孲QL鑴氭湰
        Write-Host "鎵цSQL鑴氭湰..." -ForegroundColor Yellow
        
        # 妫€鏌ユ暟鎹簱鏄惁瀛樺湪
        Write-Host "妫€鏌ユ暟鎹簱..." -ForegroundColor Gray
        $checkDbSQL = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '$DatabaseName';"
        $dbExists = & $MySQLPath @mysqlArgs -e $checkDbSQL 2>$null
        
        if (-not $dbExists) {
            Write-Host "閿欒: 鏁版嵁搴?$DatabaseName 涓嶅瓨鍦? -ForegroundColor Red
            Write-Host "璇峰厛鍒涘缓鏁版嵁搴撴垨閫夋嫨鍒濆鍖栭€夐」" -ForegroundColor Yellow
            Read-Host "鎸夊洖杞﹂敭閫€鍑?
            exit 1
        }
        
        # 鎵цSQL鑴氭湰
        & $MySQLPath @mysqlArgs $DatabaseName -e "source $SqlScriptFile"
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "鎵цSQL鑴氭湰澶辫触" -ForegroundColor Red
            Read-Host "鎸夊洖杞﹂敭閫€鍑?
            exit 1
        }
        
        Write-Host "SQL鑴氭湰鎵ц瀹屾垚" -ForegroundColor Green
    }
    
    "4" {
        Write-Host "閫€鍑? -ForegroundColor Cyan
        exit 0
    }
    
    default {
        Write-Host "鏃犳晥閫夐」" -ForegroundColor Red
        Read-Host "鎸夊洖杞﹂敭閫€鍑?
        exit 1
    }
}

# 楠岃瘉鏁版嵁搴?
Write-Host ""
Write-Host "楠岃瘉鏁版嵁搴?.." -ForegroundColor Yellow

$verifySQL = @"
USE $DatabaseName;
SHOW TABLES;
SELECT '鐢ㄦ埛琛? as '琛ㄥ悕', COUNT(*) as '璁板綍鏁? FROM users
UNION ALL
SELECT '鍥句功琛?, COUNT(*) FROM books
UNION ALL
SELECT '璇勫垎琛?, COUNT(*) FROM ratings;
SELECT 
    b.title as '鍥句功鏍囬',
    b.author as '浣滆€?,
    FORMAT(b.average_rating, 2) as '骞冲潎璇勫垎',
    COUNT(r.id) as '璇勫垎浜烘暟'
FROM books b
LEFT JOIN ratings r ON b.id = r.book_id
GROUP BY b.id
ORDER BY b.average_rating DESC
LIMIT 5;
"@

Write-Host "鏁版嵁搴撹〃缁撴瀯:" -ForegroundColor Gray
& $MySQLPath @mysqlArgs $DatabaseName -e "$verifySQL"

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "鏁版嵁搴撳垵濮嬪寲楠岃瘉瀹屾垚锛? -ForegroundColor Green
    Write-Host "鏁版嵁搴撳悕: $DatabaseName" -ForegroundColor Cyan
    Write-Host "涓绘満: $MySQLHost:$MySQLPort" -ForegroundColor Cyan
    Write-Host "鐢ㄦ埛: $MySQLUser" -ForegroundColor Cyan
}
else {
    Write-Host "鏁版嵁搴撻獙璇佸け璐? -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "涓嬩竴姝ユ搷浣?" -ForegroundColor Yellow
Write-Host "1. 淇敼Java鏁版嵁搴撹繛鎺ラ厤缃?" -ForegroundColor Gray
Write-Host "   鏂囦欢: src/main/java/com/bookrating/dao/DatabaseConnection.java" -ForegroundColor Gray
Write-Host "   淇敼URL: jdbc:mysql://$($MySQLHost):$($MySQLPort)/$($DatabaseName)" -ForegroundColor Gray
Write-Host "   淇敼鐢ㄦ埛鍚嶅拰瀵嗙爜" -ForegroundColor Gray
Write-Host ""
Write-Host "2. 杩愯鍥句功鎵撳垎绯荤粺:" -ForegroundColor Gray
Write-Host "   .\run.ps1" -ForegroundColor Gray
Write-Host "========================================" -ForegroundColor Cyan

Read-Host "鎸夊洖杞﹂敭閫€鍑?
