package main;

//�X�̃J�[�h������
public class Card implements Cloneable  {
	//�J�[�h�̐��̑傫����\���B�J�[�h�����̏ꍇ�A���ۂ̐����0.5�𑫂������ƂȂ�
	private int number; //�J�[�h�ɏ����ꂽ����
	private boolean  isWhite; //��/��
	private int size; //���ۂ̑傫��
	private boolean isOpen;  //�\��
	
	//�R���X�g���N�^ �F�Ɛ������󂯂��Ċi�[
	public Card(int size) {
		this.size = size;
		if(size % 2== 0)this.isWhite = true;
		else this.isWhite = false;
		this.number = size / 2;
		this.isOpen = false;
	}
	
	//�J�[�h�̕\���p�̏��([B3]�Ƃ�)���擾
	public String toString() {
		char color = this.isWhite ?  'W' : 'B';  
		String s = color + String.valueOf(this.number);
		return s;
	}
	
	@Override
	public Card clone() {
		Card c = null;
		try {
			c = (Card)super.clone();
		} catch (Exception e){
            e.printStackTrace();
		}
		return c;
	}
	
	
	//�Q�b�^�[
	public boolean getIsWhite() { return this.isWhite;}
	public int getSize() { return this.size;}
	public int getNumber() { return this.number;}
	public boolean getIsOpen() { return this.isOpen;}
	//�Z�b�^�[
	public void setIsOpen(boolean isOpen) {this.isOpen = isOpen;}

}
