package main;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
//戦略クラス。アタックの判断をつかさどる
public class Tactics {
	//宣言済みリストを数ごとに記録したmap。キー：カードサイズ（String）  値：宣言済み値のリスト
	private HashMap<String,ArrayList<Integer>> declaredMap ;
	//各カードごとの候補となる数のリストを格納。キー：カードサイズ（String)  値：候補となる値のリスト
    private HashMap <String, ArrayList<Integer>> optionMap;
	
	//コンストラクタ
	public Tactics() {
		this.declaredMap = new HashMap<>();
		this.optionMap = new HashMap<>();
	}
	
	/*①アタックするカードの選択。【引数】相手の場のカード、自分の場のカード、自分の手札
	 * まず宣言数の候補を格納するリストをリセットする
	 * 次に相手の場の全カードを走査する。選択肢をリセットし、カードが表向きならスキップ。
	 * 左辺、右辺（size値）を取得
	 * judgeNumberメソッドで選択肢リスト(number値)、選択肢の数を取得
	 * 今回の選択肢数がminより少なければ、リストをリセットし、min以下ならばminを更新し、minNumbersに位置データを格納
	 * */
	public int decidePlace(List<Card> enemyCards,List<Card> myCards,Card handCard,List<Integer> publicNumbers) { //ランダム選択
		int leftSide, rightSide, currentOption = 0,min = 99; //【変数】両辺の位置座標、今回の選択候補、最小候補数
		List<Integer> minZahyo = new ArrayList<>(); //最小選択肢の座標一覧
		this.optionMap.clear();//
		
		//防御側の全カードに対して走査する。
		for(int i=0; i<enemyCards.size(); i++) {
			currentOption = 0; //宣言候補数
			Card c = (Card)enemyCards.get(i); //走査するカードを取得
			if (c.getIsOpen()) continue; //表向きならスキップ
			leftSide = this.decideLeft(enemyCards,i);  rightSide = this.decideRight(enemyCards, i); //左辺・右辺の決定
			//System.out.print( "左辺："+ leftSide + " 右辺："  + rightSide); //テスト用表示プログラム
			//候補数判定。【引数】走査するカード、自分の場、自分の手札、相手の場、宣言したカードの座標、左辺、右辺
			//【戻り値】宣言候補が入ったリスト
			List<Integer>  list = this.judgeNumber(c,  enemyCards,i, leftSide, rightSide,publicNumbers);
			currentOption = list.size();
			optionMap.put(String.valueOf(c.getSize()), (ArrayList<Integer>) list); //size/宣言候補をマップで保存
			//System.out.println("  候補数:" + currentOption);//テスト用表示プログラム
			if (currentOption  < min) {minZahyo.clear();} 
			if(currentOption <= min) {minZahyo.add(i);  min = currentOption;}
		}
		//選択候補が複数あった場合、その中からランダムで選ぶ
		Random random = new Random();
		int r = random.nextInt(minZahyo.size());
		int place = minZahyo.get(r);
		
		return place;
	}
	//左辺の決定
	public int decideLeft(List<Card> enemyCards, int place) {
		int leftSide = -1;
		for(int i=place - 1; i >= 0; i--) {
			Card c = (Card)enemyCards.get(i);
			if (!c.getIsOpen() ) continue;
			else {leftSide = c.getSize(); break; }
		}
		return leftSide;		
	}
	//右辺の決定
	public int decideRight(List<Card> enemyCards, int place) {
		int rightSide = 24;
		for(int i=place + 1; i < enemyCards.size(); i++) {
			Card c = (Card)enemyCards.get(i);
			if(!c.getIsOpen()) continue;
			else { rightSide = c.getSize(); break; }
		}
		return rightSide;
	}
	//②宣言対象のカードが取りうる値（候補）を判定する。候補はfieldListに格納する。戻り値は候補の数。
	//【引数】宣言対象のカード、防御側の場のカード、宣言座標、左辺、右辺の値、公開情報
	//【変数】候補リスト、格納用キー、宣言カード・左辺・右辺のカードの座標
	//【戻り値】候補リスト
	public List<Integer> judgeNumber(Card c, List<Card> enemyCards, int zahyo, int leftSide, int rightSide,List<Integer>publicNumbers) {
		String key = String.valueOf(c.getSize()); boolean isWhite=true;
		int leftCoordinate=-1, rightCoordinate=enemyCards.size(), Y=0,Z=0;
		List<Integer> optionNumbers = new ArrayList<>();
		
		//暫定の宣言候補を作成。色が合わない、既に見えている数「以外」の数のnumber値をリストに格納する。
		for( int j = leftSide + 1; j < rightSide; j++) {		
			if (c.getIsWhite() && j%2 == 1) continue; //--白かつ奇数
			if(!c.getIsWhite() && j%2 == 0) continue; //--黒かつ偶数	
			if(publicNumbers.contains(j))  continue;  //--公開情報と一致する。既に見えているカードと同じ値は取り得ない。

			int num = j / 2; //j(size値)をnumber値に変更
			optionNumbers.add(num); //候補リストに格納。
		}
		
		//候補リストから宣言済みのカードを取り除く処理はここに入れるべき。
		List<Integer>declaredList =this.declaredMap.get(String.valueOf(c.getSize()));
		if( declaredList == null) {
			List<Integer> list = new ArrayList<>();
			declaredList = list;
		}
		List<Integer>returnNumbers = new ArrayList<>();
		for(int i: optionNumbers) 
			if(!declaredList.contains(i)) returnNumbers.add(i);  		
		
		//最終候補リストの生成。状況的に取りえない値を消去。	
		for(int k=0; k<enemyCards.size(); k++) {
			Card card = enemyCards.get(k);
			if(card.getSize() == leftSide) leftCoordinate = k;
			if(card.getSize() == rightSide)rightCoordinate = k;
		}
		//デバッグ用 System.out.print("[" + leftCoordinate + "-" + zahyo + "-" + rightCoordinate + "]");
		Y = rightCoordinate - (zahyo + 1);//対象と右辺の間に実存するカードの「枚数」
		//暫定候補リストの右辺から順に取り出していき、存在し得なければ抹消する
		while(returnNumbers.size()>0) {
			int maxInt = returnNumbers.get(returnNumbers.size() - 1) * 2; 
			//↑候補リスト末尾の数（最大数）を取得し、Number値→size値に変換
			if (!c.getIsWhite())  maxInt++;
			for(int k=maxInt+1; k<rightSide; k++) //公開情報になければカウント
				if(!publicNumbers.contains(k)) Z++;
			if(Z >= Y)break;
			else {returnNumbers.remove(returnNumbers.size() - 1); }
		}
		Y = zahyo - (leftCoordinate + 1); Z = 0;
		while(returnNumbers.size()>0) {
			int minInt = returnNumbers.get(0) * 2;
			if(!c.getIsWhite()) minInt++;
			for(int k= leftSide + 1; k< minInt; k++)
				if(!publicNumbers.contains(k))Z++;
			if(Z >= Y)break;
			else {returnNumbers.remove(0);}
			}
		
		//デバッグ用
		/*
		if(returnNumbers!= null) {
			System.out.print(" 宣言候補：");
			for(int test = 0; test< returnNumbers.size(); test++) {
				System.out.print(returnNumbers.get(test) + " ");
			}
		}*/
		return returnNumbers;
	}
	
	//③宣言する数の選択。宣言候補からランダムで１つ選択する。
	//戻り値はあくまで「number」であり、「size」ではないので注意されたし
	public int decideNumber(Card card) { 
		String key = String. valueOf(card.getSize());
		int r, returnNumber;
		List<Integer> optionList = this.optionMap.get(key); //宣言候補リスト
				
		Random random = new Random();
		r = random.nextInt(optionList.size());
		returnNumber = optionList.get(r);
		
		this.setNumber(key, returnNumber,this.declaredMap);
		
		return returnNumber;
	}
	//④続行判断。どちらかの宣言候補数が１なら続行。	
	//【引数】自分の手札、プレイヤー、プレイヤー、公開情報
	public boolean isContinue(Card hand, Player p1, Player P2,List<Integer> publicNumbers) {
		//複製
		int rightSide, leftSide, zahyo=-1,currentOption, min=99;
		Card copiedHand = hand.clone();
		List<Card> copiedMyCards = new ArrayList<>();
		List<Integer> copiedPublicNumbers = new ArrayList<>();
		List<Card> enemyCards = P2.getFieldAllCard();
		for(Card c: p1.getFieldAllCard()) {  
			copiedMyCards.add(c.clone());
		}
		for(int n: publicNumbers) {
			copiedPublicNumbers.add(n);
		}
		copiedMyCards.add(copiedHand);
		copiedMyCards.sort((a,b)-> (int)(a.getSize() * 2) - (int)(b.getSize() * 2 )); //昇順ソート
		
		//相手の最小候補数を調べる
		for(int i=0; i<enemyCards.size(); i++) {
			currentOption = 0;
			Card c = enemyCards.get(i);
			if(c.getIsOpen()) continue;
			leftSide = this.decideLeft(enemyCards,i);  rightSide = this.decideRight(enemyCards, i); //左辺・右辺の決定
			List<Integer> list = this.judgeNumber(c, enemyCards, i, leftSide, rightSide, publicNumbers);			
			currentOption = list.size();
			if(currentOption <= min)  min = currentOption;
		}
		double enemyNumber = (double)min;
		
		//宣言候補１のカードを全て表にする(全部表になったらどうなんの？)
		for(int i=0; i<copiedMyCards.size(); i++) {
			Card c = copiedMyCards.get(i);
			if (c.getSize() == copiedHand.getSize()) zahyo = i;
			if (c.getIsOpen()) continue; //表向きならスキップ
			leftSide = this.decideLeft(copiedMyCards,i);  rightSide = this.decideRight(copiedMyCards, i); //左辺・右辺の決定
			List<Integer> list = this.judgeNumber(c, copiedMyCards, i, leftSide, rightSide, copiedPublicNumbers);
			int number = list.size();
			if(number ==1) {
				c.setIsOpen(true);
				copiedPublicNumbers.add(c.getSize());
				i = -1;
			}
		}
		//手札の候補数を調べる
		leftSide = this.decideLeft(copiedMyCards,zahyo);  rightSide = this.decideRight(copiedMyCards, zahyo);		
		List<Integer> list = this.judgeNumber(copiedHand, copiedMyCards, zahyo, leftSide, rightSide, copiedPublicNumbers);
		 double myNumber = (double)list.size(); //候補数

		 double judge = ( 1 / myNumber) - (1 - (1 / enemyNumber));
		 if (judge >= 0) return true;
		 else return false;
	}

	
	//ゲッター 宣言済み数のリストを返す。リストがnullの場合は[-1]が入ったリストを作成して返す
	public List<Integer> getNumbers(String key,HashMap map){
			if(map.containsKey(key) ) {
			return (List<Integer>) map.get(key);
			} else {
				ArrayList<Integer> rList = new ArrayList<Integer>();
				rList.add(-1);
				return rList;
			}
	}
	//セッター 引数は宣言されたカードのハッシュ値と宣言した数
	public void setNumber(String key, int number,HashMap map) {
		if(map.containsKey(key)) { //既に存在する場合、リストを取り出して追加→置換
			List<Integer> newList = (ArrayList<Integer>) map.get(key);
			newList.add(number);
			map.put(key,newList);
			//System.out.println("宣言済みリスト[" + key + "]に" + number + "を追加しました");
		} else { //新しい場合は新しくリストを作って格納
			List<Integer> newList = new ArrayList<Integer>();
			newList.add(number);
			map.put(key, newList);
			//System.out.println("宣言済みリスト[" + key + "]を作成して" + number + "を追加しました");
		}
	}
}
