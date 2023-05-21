package main;

public class Main {

	public static void main(String[] args)  {
		//�R�D�̐���
		Deck deck = new Deck();
		//�J�[�h�̐����E�V���b�t��
		deck = createCards(deck);
		deck.shuffle();
		//�헪�̐���
		Tactics tactics1 = new Tactics();
		Tactics tactics2 = new Tactics();
		//�v���C���[�E�R���̐���
		Player player = new Player("���Ȃ�",true,tactics1);
		Player computer = new Player("CPU",false,tactics2);
		Judge judge = new Judge();
		
		//�y�����z�v���C���[�P�A�Q�A�R�D
		judge.startGame(player, computer,deck);

	}
	//24���̃J�[�h�𐶐����ĎR�D�ɂ���
	public static Deck createCards(Deck d) {
		Deck deck = d;
		for(int i = 0; i < 24; i++) {
			Card c = new Card(i);
			deck.addCard(c);
		}
		return deck;
	}

}
