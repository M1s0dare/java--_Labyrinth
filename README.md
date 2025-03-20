# JAVA演習で作成　ネットワーク対戦ゲーム[Labyrinth]
大学の講義で作成したゲームになります  
大学の授業内で作成したままなため，突貫工事のコードになっています．  
ゲーム紹介：Labyrinth説明.mp4  
仕様書：java演仕様書  
※時間があれば仕様書通りに作り直すつもり  

以下にjava_report.docxの内容を記述する．  

## 1. ゲームの起動方法  
1. MyServer2を起動した状態で，Labyrinth.javaを起動する  
2. 名前を入力（プレイヤーの区別は名前で行っているため）
3. IPアドレスを入力（特に設定しない場合，Localhostに接続される）


## 2.	実行画面のキャプチャ
### 2.1.	対戦準備画面  
プログラムを起動すると次の画面が出てくる  

これは対戦相手と迷路を作りあい，相手にプレイしてもらうための迷路の作成画面である．  

ゲームの背景設定として，「お互いが作成した迷宮に迷い込んだため，いち早く出口を目指す」としているので，スタートとゴールではなく，入口と出口と表記している．  
<br>

![image](https://github.com/user-attachments/assets/2dcced74-2aed-436e-baaf-5a67491fcac6)  
<br>

この状況では，右の手順で入口と出口を設定するまでは準備完了ボタンが押せなくなっている．  

また，先に準備完了を押したプレイヤーが先行になることも心理戦の一環として取り入れている．（先行ならば試行回数を稼げる・先にゴールできる可能性がある．後攻ならば相手の行動を先にみることができ，相手の行動から壁がどこに置かれていそうかを考えることができる）  
<br>

実際に入口と出口を配置するとこのようになる  
<br>

![image](https://github.com/user-attachments/assets/94768d72-fd0d-4a65-a911-42cbc1fe3fb7)
<br>

またこの時，クリアできないような迷路を作成することを防ぐために，エラーメッセージがでて来るようにしている．  

それぞれのエラーメッセージについて，左上から右に順番に次の状況で出て来るようになっています．  
「入口とゴールを分断する壁を設定しようとした」，「20枚以上壁を設置しようとした」，「入口と出口を重ねて配置しようとした」，「四方を壁で囲まれた場所に入口または出口を設定しようとした」，「出口を配置する場所が悪く，ゴールできなくなる迷宮になる」  
<br>

![image](https://github.com/user-attachments/assets/d01dd44a-0f9a-40c5-aa0e-1cbc6dc94537)
<br>

次に，準備完了を押すと，相手を待つようにポップアップが出てくる．そして，相手も準備完了を押すとゲーム開始のポップアップが出て対戦画面に移行する  
<br>

![image](https://github.com/user-attachments/assets/5f658e84-dacf-4ddd-b132-007300063ab9)
<br>

### 2.2. 対戦画面
今回はプレイヤーとして「太郎」「次郎」の二人に戦ってもらう．先行の次郎の画面は次のようになる．  

ゲームの背景設定では，「自分は迷宮の入り口に飛ばされ，出口の場所のみ書かれた地図をもらった．四方に扉があるが，開けられるかどうかはシステムに宣言して確かめなければならない．」ということになっている．四方が扉に囲まれた部屋をイメージして作成した．  
<br>

![image](https://github.com/user-attachments/assets/16afc6e7-682d-4707-b05c-6a8ec97148d2)
<br>

左は自分の作成した迷宮と壁があり，相手の行動が見えるようになっている．（相手の現在地は青のひし形で表示されるが，入口と被っている場合は表示されない）  

右は相手が作成した迷宮で，自分が攻略するものになる．そのため，壁は見えず現在位置（赤のひし形）と出口しか見えなくなっている．  

真ん中にはチャット画面と，自分が移動するための移動宣言マスがある．ここで心理戦をしてもらう．なお，移動宣言マスは，自分のターンでのみ書き込めるようになっており，相手のターンの時はクリックできないようになっている.  

対戦イメージは次のようになる  
<br>

![image](https://github.com/user-attachments/assets/b2c99adc-ffaa-4367-96f3-034e0677ad2a)
<br>

まず，チャット画面で心理戦をし，どの方向に進めるかを確認．  

行きたいマスの座標を移動宣言マスに入れて移動ボタンを押す．するとシステムが二秒の時間をかけた後，壁に進めるかどうかを返答する．  

もし，壁に当たらなければ移動できてもう一度自分が移動できるようになる．しかし壁に当たると，上の画像のように壁が表示され，相手のターンになる．  

出口に到達すると，次のような画面になる．  
<br>

![image](https://github.com/user-attachments/assets/e9f5e4e4-050c-4b18-af6f-cace80950305)  
<br>

相手には次のようなポップアップとチャットが流れる  

![image](https://github.com/user-attachments/assets/eace2971-2db0-442f-a859-869fe0f8ffb9)
<br>

これでゲームが終了する．  
<br>

## 3. プログラムの機能一覧表
*	__画面遷移__  
Swingを用いて画面遷移を実現した．また，画面遷移の時に相手と迷宮情報を送りあうようにした．

*	__幅優先探索__  
壁設置の判定に幅優先探索を使用した．このとき，一度gridに迷宮全体の情報をコピーし，幅優先探索を行えるようにした．

*	__押せないアイコンの設定__  
自分のターンでないときに移動できないようにした．  

*	__チャット__  
相手との心理戦のためにチャット欄を付けた．ここではシステムがゲーム進行をしてくれるようにもした．

*	__時間をかけた判定__  
心理戦のドキドキ感のために，判定に二秒かけるようにした．
<br>

## 4.  アピールポイント
*	__壁の設定__  
初めの壁の設定を行うときに，ゴールできる経路が確保できているかの確認に，幅優先探索を用いたことがアピールポイントとなっています．また，壁のボタンと入口と出口を置ける部屋のボタンを一緒に扱うために，11×11のグリッドを作成し，そこに情報を転写したうえで幅優先探索を行いました．
*	__入口と出口の置きなおし__  
入口と出口を置きなおすときにもゴールできるような通路があるかどうかを判定するようにしました．

*	__移動宣言マスのボタン__  
自分のターンでないときには入力できないようにしました．

* __戦画面の左の迷宮__  
自分と相手の設定した迷宮情報を相手に表示させるようにしました．そして，相手の行動を反映させるようにしました．ここは心理戦を行うために必要だったため，ここだけはきちんと動くように注意を払いました．

*	__対戦画面の右の迷宮__  
壁を非表示にすることに注力しました．そして，壁に当たった時のみ表示させるようにしました．ものすごく苦労しました．  
<br>

## 5. 演習の感想
時間がなく，仕様書通りの機能を追加することがかなわなかった．また，机に紙を置いてそれを俯瞰するような見た目にしたかったが，デザインまで手が伸びなかった．  
開発は今回が初めてだが，できなかったことが多くとても悔しい．それに，想像以上に仕様書の設計が重要だと感じた．  
仕様書に技術仕様まで書いたりすることで，もう少し楽に進めることができたかもしれない．  
演習を通してTAの方々にお世話になりました．本当にありがとうございます．










