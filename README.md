# 概要
jsoupを使った水門水質データベースの自動観測装置スクレイピングツールです。<br>
水門水質データベースの自動観測装置データは、手動だと7日間ずつしかデータを取得できませんが長期間でも自動でスクレイピングできます。
→[取得元の例](http://163.49.30.82/cgi-bin/SrchWquaData.exe?ID=403031283303071&KIND=5&PAGE=0)

# 使い方
クローンし、```build.gradle```を実行。<br>
Main.javaの29行目の自動観測装置の地点IDと取得したい期間の始まりと終わり（yyyMMdd形式）を設定してMain.javaを実行。
```java
TargetLocation targetLocation = new TargetLocation("地点ID","期間開始日付","期間終了日付");
```

取得したデータは```jsoup-project/src/main/resorces```下に```result.csv```で保存されます。

# 注意
長期間の取得を何回も試行すると迷惑がかかるため、実行は数ヶ月単位とし、ある程度間隔を開けた方が良いと思います。
１回のスクレイピングは1秒間隔にしています。
