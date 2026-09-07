# 消灭泡泡糖 - 编译运行脚本
Set-Location $PSScriptRoot

Write-Host "=== 编译中 ===" -ForegroundColor Cyan
javac -encoding GBK -d bin -sourcepath src --module-path "C:\javafx-sdk-26\javafx-sdk-26.0.2\lib" --add-modules javafx.controls,javafx.fxml src/cn/campsg/practical/bubble/MainClass.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "=== 编译成功，启动游戏 ===" -ForegroundColor Green
    java -cp bin --module-path "C:\javafx-sdk-26\javafx-sdk-26.0.2\lib" --add-modules javafx.controls,javafx.fxml cn.campsg.practical.bubble.MainClass
} else {
    Write-Host "=== 编译失败，请检查上方错误信息 ===" -ForegroundColor Red
}
