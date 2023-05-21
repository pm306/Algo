package main;

//個々のカードを示す
public class Card implements Cloneable  {
	//カードの数の大きさを表す。カードが黒の場合、実際の数より0.5を足した数となる
	private int number; //カードに書かれた数字
	private boolean  isWhite; //白/黒
	private int size; //実際の大きさ
	private boolean isOpen;  //表裏
	
	//コンストラクタ 色と数字を受けって格納
	public Card(int size) {
		this.size = size;
		if(size % 2== 0)this.isWhite = true;
		else this.isWhite = false;
		this.number = size / 2;
		this.isOpen = false;
	}
	
	//カードの表示用の情報([B3]とか)を取得
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
	
	
	//ゲッター
	public boolean getIsWhite() { return this.isWhite;}
	public int getSize() { return this.size;}
	public int getNumber() { return this.number;}
	public boolean getIsOpen() { return this.isOpen;}
	//セッター
	public void setIsOpen(boolean isOpen) {this.isOpen = isOpen;}

}
