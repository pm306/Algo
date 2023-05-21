package main;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Player {
	private List<Card> fieldCards = new ArrayList<Card>();  //場に出ているカード
	private Card handCard;  //山札から引いたカード
	private String name; //プレイヤー名
	private boolean isHuman; //インスタンスがプレイヤーかCPUか
    private Tactics tactics; //戦略アルゴリズム
	
	//コンストラクタ
	public Player(String name,boolean isHuman,Tactics tactics) {
		this.name = name;
		this.isHuman = isHuman; //isHuman;
		if(isHuman == false);
		this.tactics = tactics;
	}
	//カードを受け取り、昇順に場に出す
	public void putCard(Card c) {
		this.fieldCards.add(c);
		fieldCards.sort((a,b)-> (int)(a.getSize() * 2) - (int)(b.getSize() * 2 )); //昇順ソート
	}
	//カードを山札から引いて手札にする
	public void drawCard(Card c) {
		//if(this.handCard == null)
		this.handCard = c;
		System.out.println(this.handCard.toString() + "を引きました。");
			//System.out.println("カードを２枚以上引こうとしています");
	}
	//引いたカードを自分の場に送る
	public void setCard(boolean isOpen) {
		Card c = this.handCard;
		if(isOpen == true) {
			c.setIsOpen(true);
			System.out.println(this.name + "は" + c.toString() + "を表向きにして置いた。\n");
			}  else {
			System.out.println(" " + this.name + "は手札のカードを裏向きのまま置いた。\n"); //置いた位置を表示しないといけない
			}
		this.putCard(c);
	}
	
	//自身の場のカードの情報を画面表示する
	public void showField() {
		System.out.print("【" + this.name + "("+ this.getCloseNumber() + ")】：");
		for(int i=0; i<this.fieldCards.size(); i++) {
			Card c = (Card)this.fieldCards.get(i);
			String s; int n = c.getNumber();
			if(c.getIsOpen()) { 
				s = c.toString() + " ";
			} else if (c.getIsWhite()) {
				if (this.isHuman) s = "□" + n + " ";
				else {s = "□" + " ";}
			} else  {
				if (this.isHuman) s = "■" + n + " ";
				else s =  "■" + " ";
			} 
			System.out.print(s);			
		}
		System.out.println();
	}
	
	//裏になっている場札の枚数を返す
	public int getCloseNumber() {
		int count = 0;
		for(int i=0; i< this.fieldCards.size(); i++) {
			Card c = (Card)this.getFieldCard(i);
			if(c.getIsOpen() == false)
				count++;
		}
		return count;
	}
	//アタック対象のカード位置の決定
	//【戻り値】セットされた戦術の戻り値（int)。
	public int decidePlace(Player dP,List<Integer> publicNumbers) {
		return this.tactics.decidePlace(dP.getFieldAllCard(),this.fieldCards,this.handCard,publicNumbers);
	}
	
	//アタック用アルゴリズムその２．
	public int decideNumber(Card card) {
		return this.tactics.decideNumber(card);
	}
	//続行判定
	public boolean isContinue(Card hand, Player p1, Player p2, List<Integer>publicNumbers)  {
		return this.tactics.isContinue(hand, p1, p2, publicNumbers);
	}
	
	//ゲッター
	public Card getHand() { return this.handCard;}
	public List<Card> getFieldAllCard(){return this.fieldCards;} //自分の場のカード全部渡す
	public Card getFieldCard(int i) {return this.fieldCards.get(i);} //場のi番目のカードを渡す
	public String getName() {return this.name;}
	public boolean getIsHuman() {return this.isHuman;}
	public List<Card> getVisibleCards(Player player){  //引数で渡したプレイヤーの場のカードを全部渡す Visibleだけど全部渡す
		return player.getFieldAllCard();
	}
	//セッター
	public void setHand(Card c) { this.handCard = c;}
}
