星探しゲーム

　
・Android端末におけるジャイロセンサーを用いた、空間上の星アイコンを探す疑似ARゲーム。

＊開発環境＊＊＊＊＊＊＊＊＊＊＊＊
　・AndroidStudio、Java


＊遊び方＊＊＊＊＊＊＊＊＊＊＊＊＊
　・ジャイロセンサーが搭載されている端末、推奨バージョンAndroid5.0以上の端末にアプリをダウンロード
　
　・タイトル画面
　　スタートボタンにてゲームスタート。
　　ランキングボタンにてランキング表示画面へと遷移。
　　右上セッティングアイコンにてランキング管理者パスワード変更

　・レベル選択画面
　　タイトル画面、スタートボタンから遷移
　　ゲーム説明、レベル選択ボタンを配置
　　レベルは３つあり、レベルによって出現星数が変化
　　レベルボタンを選択にてゲーム画面へ遷移

　・ゲーム画面
　　端末をユーザーが動かし、空間上に配置されている星を探す。
　　タップすることで発見、残り星数がトースト表示される。
　　画面上部にスコア表示され、スコアは減点方式となっている。
　　画面右下にはヒントボタンが設置されており、タップすることで方角のヒントが得られるがスコアが減点される。
　　メニューバー内に隠れボタンがあり、タップすることで星の座表をトースト表示。
　　すべての星を見つけることでクリア画面へと遷移。

　・クリア画面
　　スコア、評価、ランキング登録ボタン、リトライボタン、タイトルボタンを表示。
　　評価は７段階で行われ、コメントが表示される。
　　ランキング登録ボタンにて登録画面へ遷移。
　　リトライボタンにて同一レベルのゲームを再スタート。
　　タイトルボタンにてタイトル画面へ遷移。
　　
　・ランキング登録内容記述画面
　　クリア画面におけるランキング登録ボタンより遷移。
　　レベル、スコア、名前入力欄、登録ボタンを表示。
　　名前入力欄にはランキングに登録される名前を記入する、空欄であると警告が表示される。
　　登録ボタンにて内容確認ウィジットが表示され、ランキングに登録される、その後ランキング画面へと遷移。

　・ランキング画面
　　タイトル画面でのランキングボタン、ランキング登録内容記述画面での登録ボタンから遷移。
　　レベルごとのボタンが表示され、下には選択されたレベルのランキングが表示される。
　　メニューバーには削除機能があり、レベルごとまたは全削除を選択できる。
　　データの削除にはパスワードが必要であり、正しくないと削除不可。

　　
　　