# AdTwse

簡短描述
-
這是一個使用 Kotlin 與 Android 的範例應用，透過台灣證券交易所（TWSE）公開 API 取得股票相關資料並顯示於 RecyclerView，提供搜尋與簡單排序功能。

重點特色
-
- 使用 `Retrofit` + `Gson` 連接 `https://openapi.twse.com.tw/` 取得三個資料端點：BWIBBU_ALL、STOCK_DAY_AVG_ALL、STOCK_DAY_ALL。
- 使用 Kotlin Coroutines 進行非同步網路請求。
- 顯示股票代號、名稱、收盤價、月平均價、本益比、殖利率、成交量等資訊。
- 提供搜尋（依名稱或代號）與三向排序（預設、代碼↑、代碼↓）。

系統需求
-
- Android Studio Arctic Fox 以上（建議使用能支援 AGP 9 的版本）。
- JDK 11
- minSdkVersion: 29
- targetSdkVersion: 36

建置與執行
-
1. 使用 Android Studio 打開專案並同步 Gradle。或使用命令列：

```bash
# Windows
.\gradlew.bat assembleDebug

# macOS / Linux
./gradlew assembleDebug
```

2. 在模擬器或實機上安裝並執行 `app` 模組。

快速開發提示
-
- 專案已授權網路權限（AndroidManifest）：會需要連線權限 `INTERNET`。
- 若 API 回傳格式或網路不可用，ViewModel 中會捕捉錯誤並避免阻塞 UI。

主要檔案與位置
-
- 應用設定: [app/build.gradle.kts](app/build.gradle.kts)
- 入口 Activity: [app/src/main/AndroidManifest.xml](app/src/main/AndroidManifest.xml#L1-L40)
- 主畫面與 ViewModel: [app/src/main/java/com/example/adtwse/ui/main/MainActivity.kt](app/src/main/java/com/example/adtwse/ui/main/MainActivity.kt#L1-L200), [app/src/main/java/com/example/adtwse/ui/main/MainViewModel.kt](app/src/main/java/com/example/adtwse/ui/main/MainViewModel.kt#L1-L400)
- 網路: [app/src/main/java/com/example/adtwse/network/TwseApiService.kt](app/src/main/java/com/example/adtwse/network/TwseApiService.kt#L1-L200), [app/src/main/java/com/example/adtwse/network/RetrofitClient.kt](app/src/main/java/com/example/adtwse/network/RetrofitClient.kt#L1-L200)
- 資料模型與倉儲: [app/src/main/java/com/example/adtwse/data/model/StockInfo.kt](app/src/main/java/com/example/adtwse/data/model/StockInfo.kt#L1-L200), [app/src/main/java/com/example/adtwse/data/repository/StockRepository.kt](app/src/main/java/com/example/adtwse/data/repository/StockRepository.kt#L1-L200)

相依套件（概覽）
-
- Retrofit 2.9.0
- Gson converter
- AndroidX Core KTX, AppCompat, Material, Activity KTX, ConstraintLayout
- Kotlin Coroutines (kotlinx-coroutines-android)

注意事項與建議
-
- 若要增加錯誤處理或離線快取，可在 `StockRepository` 加入 Retrofit 的 OkHttpClient 並加上快取或重試策略。
- 若 TWSE API 回傳結構改變，需同步更新 `TwseApiService` 的資料類別。

貢獻
-
歡迎提出 issues 或 pull requests。請在 PR 中描述變更與測試方式。

授權
-
目前未放置授權檔，建議加入 `LICENSE`（例如 MIT）以說明授權條款。
