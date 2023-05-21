package main;
import java.util.List;
import java.util.ArrayList;

//最初にカードを配るための山札
public class Deck {
	private List<Card> cards = new ArrayList<Card>();
	
	//シャッフル
	public void shuffle() {
		int number = cards.size();
		int pos;
		for(int i = 0; i < number * 2; i++) {
			pos = (int)(Math.random() * number);
			Card pickedCard = (Card) cards.remove(pos);
			cards.add(pickedCard);
		}
	}
	//カードを1枚加える
	public void addCard(Card c) {
		this.cards.add(c);
	}
	//デックトップのカードを渡す(値を返し、自分のリストから消す
	public Card handOutCard() {
		Card c = this.cards.get(0);
		this.cards.remove(0);
		return c;
	}
	//i番目のカードの情報を見る
	public void getCardInfo(int i) {
		Card c = (Card)this.cards.get(i);
		System.out.print(c.toString() + " ");
	}
	//i番目のカードの大きさを見る
	public void getCardSize(int i) {
		Card c = (Card)this.cards.get(i);
		double d = c.getSize();
		System.out.print(d + " ");
	}
	//カード枚数を返す
	public int getSize() {
		return this.cards.size();
	}
}

