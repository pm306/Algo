package main;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Judge {
	private Player atkP;  //攻撃側のプレイヤー
	private Player defP;  //防御側のプレイヤー
	private Player[] playerPosition = new Player[2]; //画面表示用のポジション

	//ゲーム進行の処理。【引数】プレイヤー、プレイヤー、山札
	public void startGame(Player p1, Player p2, Deck deck)  {
		Scanner sc = new Scanner(System.in);
		System.out.println("【-----アルゴ-----】" );
		System.out.println("先に相手のカードを全て当てた方が勝ちの、シンプルなカードゲームです。");
		System.out.println("\nカードを配ります。");
		this.handOut(p1, p2, deck); //デッキを配る
		this.playerPosition[0] = p2; this.playerPosition[1] = p1; //テーブル表示用の配列
		this.showField(0);   this.showField(1);   //テーブル表示
		
		//先攻後攻判定
		Random random = new Random();
		if (random.nextInt(2) == 1) {this.atkP = p2; this.defP = p1;  System.out.println(p1.getName() + "の後攻です。");}
			else {this.atkP = p1; this.defP = p2;  System.out.println(p1.getName() + "の先攻です。");}
		
		System.out.println("\nゲームを始めます。");
		
		//ゲーム部分 どちらかの裏枚数が0になるか、山札が尽きるまで続ける
		
		//ターン開始時と、アタック終了ごとにゲーム続行判定を入れる
		while(atkP.getCloseNumber() > 0 && defP.getCloseNumber() > 0 && deck.getSize() > 0) {
			System.out.println("●" + atkP.getName() + "の番です。");
			boolean isAttack = true;
			boolean isClear = true;
			int count = 0; //アタックカウンタ。そのターン中に何回アタックを行ったかを調査
			atkP.drawCard(deck.handOutCard()); //カードを引く
			p2.showField();
			p1.showField();
			//続行判定→アタック処理。1回以上アタックする必要がある。「アタックしない」選択をしたときループを抜ける。
			while(atkP.getCloseNumber() > 0 && defP.getCloseNumber() > 0 && deck.getSize() >= 0) {	
				//続行判定.
				    if(!isClear)break;
					if(count > 0) { //カウンタが0の場合はアタックしなければならない
						if(atkP.getIsHuman() ) { //人間
							System.out.print("続行しますか？【Yes...0/No...1以上】：");
							String input = sc.next();   if(!input.equals("0")) { isAttack = false;}
						} else { //コンピュータ
							int r = random.nextInt(3);
							isAttack = r > 0 ? true: false;
							
							/*List<Integer> publicNumbers = new ArrayList<>(); //公開情報のリスト。値はsize型（実際の大きさ）
							Card c = atkP.getHand();   publicNumbers.add(c.getSize());
							List<Card> list = atkP.getFieldAllCard();
							for(Card card: list) { publicNumbers.add(card.getSize()); }
							list = defP.getFieldAllCard();
							for(Card card: list) { if(card.getIsOpen()) publicNumbers.add(card.getSize()); }
							isAttack = atkP.isContinue(atkP.getHand(), atkP, defP, publicNumbers); */
						}
						if (isAttack) {
						System.out.print("\n☆" + atkP.getName() + "のターンを続行します。 ");
						if(atkP.getIsHuman())
							System.out.println("  手札：" + atkP.getHand().toString());
						playerPosition[0].showField();  playerPosition[1].showField();
						} else {
							System.out.println("\n★" + atkP.getName() + "はこのターンを終わりにした。");
						}
					}
					//攻撃処理.attackメソッドはアタックの成否をbooleanで返す。
					if(isAttack) {isClear = this.attack(); count++;}
					else  break; 
				}
			
			 	if (isClear && deck.getSize()> 0) {atkP.setCard(false);} //不正解せずにターンを終えた場合、カードを裏向きに置く
			
				Player swap = null;  //攻守交替
				swap = atkP; atkP = defP; defP = swap;
		}
		System.out.println("\nゲーム終了！");
		
		Player winner = null;
		if(atkP.getCloseNumber() > defP.getCloseNumber()) { //裏向きのカードの枚数を比較
			winner = atkP;
			System.out.println(winner.getName() + "の勝ちです！");
		} else if  (atkP.getCloseNumber() < defP.getCloseNumber()){
			winner = defP;
			System.out.println(winner.getName() + "の勝ちです！");
		} else System.out.println("引き分けです！");
	}
	//プレイヤーの場を表示する。【引数】配列playerPositionの添え字を指定
	public void showField(int n) {
		playerPosition[n].showField();
	}
	
	//カードを配る。山札の上からお互いの手札にカードを加える。
	public void handOut(Player p1, Player p2, Deck d) {
		int num = 4; 
		for(int i = 0; i < num; i++){  
			Card c = (Card)d.handOutCard();
			p1.putCard(c);
			c =  (Card)d.handOutCard();
			p2.putCard(c);
		}
	}
	
	//攻撃処理。【戻り値】正解＝true,不正解＝false
	public boolean attack() {
		// place:攻撃対象の座標, aNumber:攻撃側が宣言した番号(カードに書いてある数), dNumber:実際のカードの番号
		int place=-1, aNumber = -1,dNumber = -1;
		if(atkP.getIsHuman() == true) {
			System.out.print("どのカードにアタックしますか？(左から数えて何番目かで入力してね)：");
			Scanner sc = new Scanner(System.in);
			place = sc.nextInt() - 1;
			System.out.print("予想する数字を入力してね(0~11)：");
			aNumber = sc.nextInt();
		}
		else {
			//place番目のカードにアタックする。
			//公開情報の整理
			List<Integer> publicNumbers = new ArrayList<>(); //公開情報のリスト。値はsize型（実際の大きさ）
			Card c = atkP.getHand();   publicNumbers.add(c.getSize());
			List<Card> list = atkP.getFieldAllCard();
			for(Card card: list) { publicNumbers.add(card.getSize()); }
			list = defP.getFieldAllCard();
			for(Card card: list) { if(card.getIsOpen()) publicNumbers.add(card.getSize()); }
			
			/*デバッグ用
			System.out.print("公開情報：");
			for(int x: publicNumbers)  System.out.print(x + " ");
			System.out.println();
			*/
			
			place = atkP.decidePlace(defP,publicNumbers); // decidePlace:攻める場所を決定する。【引数】防御側のプレイヤー
			aNumber = atkP.decideNumber(defP.getFieldCard(place)); //decideNumber:宣言する番号を決定する。
		}

		System.out.println(atkP.getName() + "は"  + (place + 1) +"番目のカードに" +aNumber + "を宣言した！");
		
		Card c = defP.getFieldCard(place);
		dNumber =  c.getNumber(); // 宣言されたカードの実際の番号
		//正解。該当カードを表にめくって場を更新する
		if(aNumber == dNumber) {
			System.out.println("正解！\n" + defP.getName() + "はカードを表にした。");
			c.setIsOpen(true);
			return true;
		} else { //不正解。攻撃側の手札を表にして場に置く。
			System.out.println("不正解！");
			atkP.setCard(true); //手札を表向きにしておく。【引数】isOpen
			return false;
		}
	}
}
