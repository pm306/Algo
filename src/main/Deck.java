package main;
import java.util.List;
import java.util.ArrayList;

//�ŏ��ɃJ�[�h��z�邽�߂̎R�D
public class Deck {
	private List<Card> cards = new ArrayList<Card>();
	
	//�V���b�t��
	public void shuffle() {
		int number = cards.size();
		int pos;
		for(int i = 0; i < number * 2; i++) {
			pos = (int)(Math.random() * number);
			Card pickedCard = (Card) cards.remove(pos);
			cards.add(pickedCard);
		}
	}
	//�J�[�h��1��������
	public void addCard(Card c) {
		this.cards.add(c);
	}
	//�f�b�N�g�b�v�̃J�[�h��n��(�l��Ԃ��A�����̃��X�g�������
	public Card handOutCard() {
		Card c = this.cards.get(0);
		this.cards.remove(0);
		return c;
	}
	//i�Ԗڂ̃J�[�h�̏�������
	public void getCardInfo(int i) {
		Card c = (Card)this.cards.get(i);
		System.out.print(c.toString() + " ");
	}
	//i�Ԗڂ̃J�[�h�̑傫��������
	public void getCardSize(int i) {
		Card c = (Card)this.cards.get(i);
		double d = c.getSize();
		System.out.print(d + " ");
	}
	//�J�[�h������Ԃ�
	public int getSize() {
		return this.cards.size();
	}
}

