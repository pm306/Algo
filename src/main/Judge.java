package main;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Judge {
	private Player atkP;  //�U�����̃v���C���[
	private Player defP;  //�h�䑤�̃v���C���[
	private Player[] playerPosition = new Player[2]; //��ʕ\���p�̃|�W�V����

	//�Q�[���i�s�̏����B�y�����z�v���C���[�A�v���C���[�A�R�D
	public void startGame(Player p1, Player p2, Deck deck)  {
		Scanner sc = new Scanner(System.in);
		System.out.println("�y-----�A���S-----�z" );
		System.out.println("��ɑ���̃J�[�h��S�ē��Ă����������́A�V���v���ȃJ�[�h�Q�[���ł��B");
		System.out.println("\n�J�[�h��z��܂��B");
		this.handOut(p1, p2, deck); //�f�b�L��z��
		this.playerPosition[0] = p2; this.playerPosition[1] = p1; //�e�[�u���\���p�̔z��
		this.showField(0);   this.showField(1);   //�e�[�u���\��
		
		//��U��U����
		Random random = new Random();
		if (random.nextInt(2) == 1) {this.atkP = p2; this.defP = p1;  System.out.println(p1.getName() + "�̌�U�ł��B");}
			else {this.atkP = p1; this.defP = p2;  System.out.println(p1.getName() + "�̐�U�ł��B");}
		
		System.out.println("\n�Q�[�����n�߂܂��B");
		
		//�Q�[������ �ǂ��炩�̗�������0�ɂȂ邩�A�R�D���s����܂ő�����
		
		//�^�[���J�n���ƁA�A�^�b�N�I�����ƂɃQ�[�����s���������
		while(atkP.getCloseNumber() > 0 && defP.getCloseNumber() > 0 && deck.getSize() > 0) {
			System.out.println("��" + atkP.getName() + "�̔Ԃł��B");
			boolean isAttack = true;
			boolean isClear = true;
			int count = 0; //�A�^�b�N�J�E���^�B���̃^�[�����ɉ���A�^�b�N���s�������𒲍�
			atkP.drawCard(deck.handOutCard()); //�J�[�h������
			p2.showField();
			p1.showField();
			//���s���聨�A�^�b�N�����B1��ȏ�A�^�b�N����K�v������B�u�A�^�b�N���Ȃ��v�I���������Ƃ����[�v�𔲂���B
			while(atkP.getCloseNumber() > 0 && defP.getCloseNumber() > 0 && deck.getSize() >= 0) {	
				//���s����.
				    if(!isClear)break;
					if(count > 0) { //�J�E���^��0�̏ꍇ�̓A�^�b�N���Ȃ���΂Ȃ�Ȃ�
						if(atkP.getIsHuman() ) { //�l��
							System.out.print("���s���܂����H�yYes...0/No...1�ȏ�z�F");
							String input = sc.next();   if(!input.equals("0")) { isAttack = false;}
						} else { //�R���s���[�^
							int r = random.nextInt(3);
							isAttack = r > 0 ? true: false;
							
							/*List<Integer> publicNumbers = new ArrayList<>(); //���J���̃��X�g�B�l��size�^�i���ۂ̑傫���j
							Card c = atkP.getHand();   publicNumbers.add(c.getSize());
							List<Card> list = atkP.getFieldAllCard();
							for(Card card: list) { publicNumbers.add(card.getSize()); }
							list = defP.getFieldAllCard();
							for(Card card: list) { if(card.getIsOpen()) publicNumbers.add(card.getSize()); }
							isAttack = atkP.isContinue(atkP.getHand(), atkP, defP, publicNumbers); */
						}
						if (isAttack) {
						System.out.print("\n��" + atkP.getName() + "�̃^�[���𑱍s���܂��B ");
						if(atkP.getIsHuman())
							System.out.println("  ��D�F" + atkP.getHand().toString());
						playerPosition[0].showField();  playerPosition[1].showField();
						} else {
							System.out.println("\n��" + atkP.getName() + "�͂��̃^�[�����I���ɂ����B");
						}
					}
					//�U������.attack���\�b�h�̓A�^�b�N�̐��ۂ�boolean�ŕԂ��B
					if(isAttack) {isClear = this.attack(); count++;}
					else  break; 
				}
			
			 	if (isClear && deck.getSize()> 0) {atkP.setCard(false);} //�s���������Ƀ^�[�����I�����ꍇ�A�J�[�h�𗠌����ɒu��
			
				Player swap = null;  //�U����
				swap = atkP; atkP = defP; defP = swap;
		}
		System.out.println("\n�Q�[���I���I");
		
		Player winner = null;
		if(atkP.getCloseNumber() > defP.getCloseNumber()) { //�������̃J�[�h�̖������r
			winner = atkP;
			System.out.println(winner.getName() + "�̏����ł��I");
		} else if  (atkP.getCloseNumber() < defP.getCloseNumber()){
			winner = defP;
			System.out.println(winner.getName() + "�̏����ł��I");
		} else System.out.println("���������ł��I");
	}
	//�v���C���[�̏��\������B�y�����z�z��playerPosition�̓Y�������w��
	public void showField(int n) {
		playerPosition[n].showField();
	}
	
	//�J�[�h��z��B�R�D�̏ォ�炨�݂��̎�D�ɃJ�[�h��������B
	public void handOut(Player p1, Player p2, Deck d) {
		int num = 4; 
		for(int i = 0; i < num; i++){  
			Card c = (Card)d.handOutCard();
			p1.putCard(c);
			c =  (Card)d.handOutCard();
			p2.putCard(c);
		}
	}
	
	//�U�������B�y�߂�l�z������true,�s������false
	public boolean attack() {
		// place:�U���Ώۂ̍��W, aNumber:�U�������錾�����ԍ�(�J�[�h�ɏ����Ă��鐔), dNumber:���ۂ̃J�[�h�̔ԍ�
		int place=-1, aNumber = -1,dNumber = -1;
		if(atkP.getIsHuman() == true) {
			System.out.print("�ǂ̃J�[�h�ɃA�^�b�N���܂����H(�����琔���ĉ��Ԗڂ��œ��͂��Ă�)�F");
			Scanner sc = new Scanner(System.in);
			place = sc.nextInt() - 1;
			System.out.print("�\�z���鐔������͂��Ă�(0~11)�F");
			aNumber = sc.nextInt();
		}
		else {
			//place�Ԗڂ̃J�[�h�ɃA�^�b�N����B
			//���J���̐���
			List<Integer> publicNumbers = new ArrayList<>(); //���J���̃��X�g�B�l��size�^�i���ۂ̑傫���j
			Card c = atkP.getHand();   publicNumbers.add(c.getSize());
			List<Card> list = atkP.getFieldAllCard();
			for(Card card: list) { publicNumbers.add(card.getSize()); }
			list = defP.getFieldAllCard();
			for(Card card: list) { if(card.getIsOpen()) publicNumbers.add(card.getSize()); }
			
			/*�f�o�b�O�p
			System.out.print("���J���F");
			for(int x: publicNumbers)  System.out.print(x + " ");
			System.out.println();
			*/
			
			place = atkP.decidePlace(defP,publicNumbers); // decidePlace:�U�߂�ꏊ�����肷��B�y�����z�h�䑤�̃v���C���[
			aNumber = atkP.decideNumber(defP.getFieldCard(place)); //decideNumber:�錾����ԍ������肷��B
		}

		System.out.println(atkP.getName() + "��"  + (place + 1) +"�Ԗڂ̃J�[�h��" +aNumber + "��錾�����I");
		
		Card c = defP.getFieldCard(place);
		dNumber =  c.getNumber(); // �錾���ꂽ�J�[�h�̎��ۂ̔ԍ�
		//�����B�Y���J�[�h��\�ɂ߂����ď���X�V����
		if(aNumber == dNumber) {
			System.out.println("�����I\n" + defP.getName() + "�̓J�[�h��\�ɂ����B");
			c.setIsOpen(true);
			return true;
		} else { //�s�����B�U�����̎�D��\�ɂ��ď�ɒu���B
			System.out.println("�s�����I");
			atkP.setCard(true); //��D��\�����ɂ��Ă����B�y�����zisOpen
			return false;
		}
	}
}
