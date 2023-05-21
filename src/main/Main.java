package main;

public class Main {

	public static void main(String[] args)  {
		//山札の生成
		Deck deck = new Deck();
		//カードの生成・シャッフル
		deck = createCards(deck);
		deck.shuffle();
		//戦略の生成
		Tactics tactics1 = new Tactics();
		Tactics tactics2 = new Tactics();
		//プレイヤー・審判の生成
		Player player = new Player("あなた",true,tactics1);
		Player computer = new Player("CPU",false,tactics2);
		Judge judge = new Judge();
		
		//【引数】プレイヤー１、２、山札
		judge.startGame(player, computer,deck);

	}
	//24枚のカードを生成して山札にする
	public static Deck createCards(Deck d) {
		Deck deck = d;
		for(int i = 0; i < 24; i++) {
			Card c = new Card(i);
			deck.addCard(c);
		}
		return deck;
	}

}
