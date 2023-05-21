package main;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
//�헪�N���X�B�A�^�b�N�̔��f�������ǂ�
public class Tactics {
	//�錾�ς݃��X�g�𐔂��ƂɋL�^����map�B�L�[�F�J�[�h�T�C�Y�iString�j  �l�F�錾�ςݒl�̃��X�g
	private HashMap<String,ArrayList<Integer>> declaredMap ;
	//�e�J�[�h���Ƃ̌��ƂȂ鐔�̃��X�g���i�[�B�L�[�F�J�[�h�T�C�Y�iString)  �l�F���ƂȂ�l�̃��X�g
    private HashMap <String, ArrayList<Integer>> optionMap;
	
	//�R���X�g���N�^
	public Tactics() {
		this.declaredMap = new HashMap<>();
		this.optionMap = new HashMap<>();
	}
	
	/*�@�A�^�b�N����J�[�h�̑I���B�y�����z����̏�̃J�[�h�A�����̏�̃J�[�h�A�����̎�D
	 * �܂��錾���̌����i�[���郊�X�g�����Z�b�g����
	 * ���ɑ���̏�̑S�J�[�h�𑖍�����B�I���������Z�b�g���A�J�[�h���\�����Ȃ�X�L�b�v�B
	 * ���ӁA�E�Ӂisize�l�j���擾
	 * judgeNumber���\�b�h�őI�������X�g(number�l)�A�I�����̐����擾
	 * ����̑I��������min��菭�Ȃ���΁A���X�g�����Z�b�g���Amin�ȉ��Ȃ��min���X�V���AminNumbers�Ɉʒu�f�[�^���i�[
	 * */
	public int decidePlace(List<Card> enemyCards,List<Card> myCards,Card handCard,List<Integer> publicNumbers) { //�����_���I��
		int leftSide, rightSide, currentOption = 0,min = 99; //�y�ϐ��z���ӂ̈ʒu���W�A����̑I�����A�ŏ���␔
		List<Integer> minZahyo = new ArrayList<>(); //�ŏ��I�����̍��W�ꗗ
		this.optionMap.clear();//
		
		//�h�䑤�̑S�J�[�h�ɑ΂��đ�������B
		for(int i=0; i<enemyCards.size(); i++) {
			currentOption = 0; //�錾��␔
			Card c = (Card)enemyCards.get(i); //��������J�[�h���擾
			if (c.getIsOpen()) continue; //�\�����Ȃ�X�L�b�v
			leftSide = this.decideLeft(enemyCards,i);  rightSide = this.decideRight(enemyCards, i); //���ӁE�E�ӂ̌���
			//System.out.print( "���ӁF"+ leftSide + " �E�ӁF"  + rightSide); //�e�X�g�p�\���v���O����
			//��␔����B�y�����z��������J�[�h�A�����̏�A�����̎�D�A����̏�A�錾�����J�[�h�̍��W�A���ӁA�E��
			//�y�߂�l�z�錾��₪���������X�g
			List<Integer>  list = this.judgeNumber(c,  enemyCards,i, leftSide, rightSide,publicNumbers);
			currentOption = list.size();
			optionMap.put(String.valueOf(c.getSize()), (ArrayList<Integer>) list); //size/�錾�����}�b�v�ŕۑ�
			//System.out.println("  ��␔:" + currentOption);//�e�X�g�p�\���v���O����
			if (currentOption  < min) {minZahyo.clear();} 
			if(currentOption <= min) {minZahyo.add(i);  min = currentOption;}
		}
		//�I����₪�����������ꍇ�A���̒����烉���_���őI��
		Random random = new Random();
		int r = random.nextInt(minZahyo.size());
		int place = minZahyo.get(r);
		
		return place;
	}
	//���ӂ̌���
	public int decideLeft(List<Card> enemyCards, int place) {
		int leftSide = -1;
		for(int i=place - 1; i >= 0; i--) {
			Card c = (Card)enemyCards.get(i);
			if (!c.getIsOpen() ) continue;
			else {leftSide = c.getSize(); break; }
		}
		return leftSide;		
	}
	//�E�ӂ̌���
	public int decideRight(List<Card> enemyCards, int place) {
		int rightSide = 24;
		for(int i=place + 1; i < enemyCards.size(); i++) {
			Card c = (Card)enemyCards.get(i);
			if(!c.getIsOpen()) continue;
			else { rightSide = c.getSize(); break; }
		}
		return rightSide;
	}
	//�A�錾�Ώۂ̃J�[�h����肤��l�i���j�𔻒肷��B����fieldList�Ɋi�[����B�߂�l�͌��̐��B
	//�y�����z�錾�Ώۂ̃J�[�h�A�h�䑤�̏�̃J�[�h�A�錾���W�A���ӁA�E�ӂ̒l�A���J���
	//�y�ϐ��z��⃊�X�g�A�i�[�p�L�[�A�錾�J�[�h�E���ӁE�E�ӂ̃J�[�h�̍��W
	//�y�߂�l�z��⃊�X�g
	public List<Integer> judgeNumber(Card c, List<Card> enemyCards, int zahyo, int leftSide, int rightSide,List<Integer>publicNumbers) {
		String key = String.valueOf(c.getSize()); boolean isWhite=true;
		int leftCoordinate=-1, rightCoordinate=enemyCards.size(), Y=0,Z=0;
		List<Integer> optionNumbers = new ArrayList<>();
		
		//�b��̐錾�����쐬�B�F������Ȃ��A���Ɍ����Ă��鐔�u�ȊO�v�̐���number�l�����X�g�Ɋi�[����B
		for( int j = leftSide + 1; j < rightSide; j++) {		
			if (c.getIsWhite() && j%2 == 1) continue; //--�����
			if(!c.getIsWhite() && j%2 == 0) continue; //--��������	
			if(publicNumbers.contains(j))  continue;  //--���J���ƈ�v����B���Ɍ����Ă���J�[�h�Ɠ����l�͎�蓾�Ȃ��B

			int num = j / 2; //j(size�l)��number�l�ɕύX
			optionNumbers.add(num); //��⃊�X�g�Ɋi�[�B
		}
		
		//��⃊�X�g����錾�ς݂̃J�[�h����菜�������͂����ɓ����ׂ��B
		List<Integer>declaredList =this.declaredMap.get(String.valueOf(c.getSize()));
		if( declaredList == null) {
			List<Integer> list = new ArrayList<>();
			declaredList = list;
		}
		List<Integer>returnNumbers = new ArrayList<>();
		for(int i: optionNumbers) 
			if(!declaredList.contains(i)) returnNumbers.add(i);  		
		
		//�ŏI��⃊�X�g�̐����B�󋵓I�Ɏ�肦�Ȃ��l�������B	
		for(int k=0; k<enemyCards.size(); k++) {
			Card card = enemyCards.get(k);
			if(card.getSize() == leftSide) leftCoordinate = k;
			if(card.getSize() == rightSide)rightCoordinate = k;
		}
		//�f�o�b�O�p System.out.print("[" + leftCoordinate + "-" + zahyo + "-" + rightCoordinate + "]");
		Y = rightCoordinate - (zahyo + 1);//�ΏۂƉE�ӂ̊ԂɎ�������J�[�h�́u�����v
		//�b���⃊�X�g�̉E�ӂ��珇�Ɏ��o���Ă����A���݂����Ȃ���Ζ�������
		while(returnNumbers.size()>0) {
			int maxInt = returnNumbers.get(returnNumbers.size() - 1) * 2; 
			//����⃊�X�g�����̐��i�ő吔�j���擾���ANumber�l��size�l�ɕϊ�
			if (!c.getIsWhite())  maxInt++;
			for(int k=maxInt+1; k<rightSide; k++) //���J���ɂȂ���΃J�E���g
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
		
		//�f�o�b�O�p
		/*
		if(returnNumbers!= null) {
			System.out.print(" �錾���F");
			for(int test = 0; test< returnNumbers.size(); test++) {
				System.out.print(returnNumbers.get(test) + " ");
			}
		}*/
		return returnNumbers;
	}
	
	//�B�錾���鐔�̑I���B�錾��₩�烉���_���łP�I������B
	//�߂�l�͂����܂Łunumber�v�ł���A�usize�v�ł͂Ȃ��̂Œ��ӂ��ꂽ��
	public int decideNumber(Card card) { 
		String key = String. valueOf(card.getSize());
		int r, returnNumber;
		List<Integer> optionList = this.optionMap.get(key); //�錾��⃊�X�g
				
		Random random = new Random();
		r = random.nextInt(optionList.size());
		returnNumber = optionList.get(r);
		
		this.setNumber(key, returnNumber,this.declaredMap);
		
		return returnNumber;
	}
	//�C���s���f�B�ǂ��炩�̐錾��␔���P�Ȃ瑱�s�B	
	//�y�����z�����̎�D�A�v���C���[�A�v���C���[�A���J���
	public boolean isContinue(Card hand, Player p1, Player P2,List<Integer> publicNumbers) {
		//����
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
		copiedMyCards.sort((a,b)-> (int)(a.getSize() * 2) - (int)(b.getSize() * 2 )); //�����\�[�g
		
		//����̍ŏ���␔�𒲂ׂ�
		for(int i=0; i<enemyCards.size(); i++) {
			currentOption = 0;
			Card c = enemyCards.get(i);
			if(c.getIsOpen()) continue;
			leftSide = this.decideLeft(enemyCards,i);  rightSide = this.decideRight(enemyCards, i); //���ӁE�E�ӂ̌���
			List<Integer> list = this.judgeNumber(c, enemyCards, i, leftSide, rightSide, publicNumbers);			
			currentOption = list.size();
			if(currentOption <= min)  min = currentOption;
		}
		double enemyNumber = (double)min;
		
		//�錾���P�̃J�[�h��S�ĕ\�ɂ���(�S���\�ɂȂ�����ǂ��Ȃ�́H)
		for(int i=0; i<copiedMyCards.size(); i++) {
			Card c = copiedMyCards.get(i);
			if (c.getSize() == copiedHand.getSize()) zahyo = i;
			if (c.getIsOpen()) continue; //�\�����Ȃ�X�L�b�v
			leftSide = this.decideLeft(copiedMyCards,i);  rightSide = this.decideRight(copiedMyCards, i); //���ӁE�E�ӂ̌���
			List<Integer> list = this.judgeNumber(c, copiedMyCards, i, leftSide, rightSide, copiedPublicNumbers);
			int number = list.size();
			if(number ==1) {
				c.setIsOpen(true);
				copiedPublicNumbers.add(c.getSize());
				i = -1;
			}
		}
		//��D�̌�␔�𒲂ׂ�
		leftSide = this.decideLeft(copiedMyCards,zahyo);  rightSide = this.decideRight(copiedMyCards, zahyo);		
		List<Integer> list = this.judgeNumber(copiedHand, copiedMyCards, zahyo, leftSide, rightSide, copiedPublicNumbers);
		 double myNumber = (double)list.size(); //��␔

		 double judge = ( 1 / myNumber) - (1 - (1 / enemyNumber));
		 if (judge >= 0) return true;
		 else return false;
	}

	
	//�Q�b�^�[ �錾�ςݐ��̃��X�g��Ԃ��B���X�g��null�̏ꍇ��[-1]�����������X�g���쐬���ĕԂ�
	public List<Integer> getNumbers(String key,HashMap map){
			if(map.containsKey(key) ) {
			return (List<Integer>) map.get(key);
			} else {
				ArrayList<Integer> rList = new ArrayList<Integer>();
				rList.add(-1);
				return rList;
			}
	}
	//�Z�b�^�[ �����͐錾���ꂽ�J�[�h�̃n�b�V���l�Ɛ錾������
	public void setNumber(String key, int number,HashMap map) {
		if(map.containsKey(key)) { //���ɑ��݂���ꍇ�A���X�g�����o���Ēǉ����u��
			List<Integer> newList = (ArrayList<Integer>) map.get(key);
			newList.add(number);
			map.put(key,newList);
			//System.out.println("�錾�ς݃��X�g[" + key + "]��" + number + "��ǉ����܂���");
		} else { //�V�����ꍇ�͐V�������X�g������Ċi�[
			List<Integer> newList = new ArrayList<Integer>();
			newList.add(number);
			map.put(key, newList);
			//System.out.println("�錾�ς݃��X�g[" + key + "]���쐬����" + number + "��ǉ����܂���");
		}
	}
}
