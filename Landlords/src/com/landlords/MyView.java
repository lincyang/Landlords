package com.landlords;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import com.landlords.R;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/*
 * QQ:361106306
 * by:С��
 * ת�ش˳����뱣����Ȩ,δ������������������ҵ��;!
 * */
public class MyView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {

	SurfaceHolder surfaceHolder;
	Canvas canvas;
	Boolean repaint=false;
	Boolean start;
	Thread gameThread,drawThread;
	// �жϵ�ǰ�Ƿ�Ҫ��
	int []flag=new int[3];
	// ��Ļ��Ⱥ͸߶�
	int screen_height;
	int screen_width;
	// ͼƬ��Դ
	Bitmap cardBitmap[] = new Bitmap[54];
	Bitmap bgBitmap;    //����
	Bitmap cardBgBitmap;//ͼƬ����
	Bitmap dizhuBitmap;//����ͼ��
	// ��������
	int cardWidth, cardHeight;
	//����
	Paint paint;
	// �ƶ���
	Card card[] = new Card[54];
	//��ť
	String buttonText[]=new String[2];
	//��ʾ
	String message[]=new String[3];
	boolean hideButton=true;
	// List
	List<Card> playerList[]=new Vector[3];
	//������
	List<Card> dizhuList=new Vector<Card>();
	//˭�ǵ���
	int dizhuFlag=-1;
	//����
	int turn=-1;
	//�ѳ��Ʊ�
	List<Card> outList[]=new Vector[3];
	Handler handler;
	// ���캯��
	public MyView(Context context,Handler handler) {
		super(context);
		Common.view=this;
		this.handler=handler;
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
	}

	// ��ʼ��ͼƬ,����
	public void InitBitMap() {
		for(int i=0;i<3;i++)
			flag[i]=0;
		dizhuFlag=-1;
		turn=-1;
		int count=0;
		for (int i = 1; i <= 4; i++) {
			for (int j = 3; j <= 15; j++) {
				//���������ҳ�ID
				String name = "a" + i + "_" + j;
				ApplicationInfo appInfo = getContext().getApplicationInfo();
				int id = getResources().getIdentifier(name, "drawable",
						appInfo.packageName);
				cardBitmap[count] = BitmapFactory.decodeResource(getResources(),
						id);
				card[count] = new Card(cardBitmap[count].getWidth(),cardBitmap[count].getHeight(), cardBitmap[count]);
				//����Card������
				card[count].setName(name);
				count++;
			}
		}
		//���С��������
		cardBitmap[52] = BitmapFactory.decodeResource(getResources(),
				R.drawable.a5_16);
		card[52]=new Card(cardBitmap[52].getWidth(), cardBitmap[52].getHeight(),cardBitmap[52]);
		card[52].setName("a5_16");
		cardBitmap[53] = BitmapFactory.decodeResource(getResources(),
				R.drawable.a5_17);
		card[53]=new Card(cardBitmap[53].getWidth(), cardBitmap[53].getHeight(),cardBitmap[53]);
		card[53].setName("a5_17");
		cardWidth=card[53].width;
		cardHeight=card[53].height;
		//����ͼ��
		dizhuBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.dizhu);
		//����
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		cardBgBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.cardbg1);
		//��ť
		for(int i=0;i<2;i++)
		{
			buttonText[i]=new String();
		}
		buttonText[0]="������";
		buttonText[1]="����";
		//��Ϣ,�ѳ���
		for(int i=0;i<3;i++)
		{
			message[i]=new String("");
			outList[i]=new Vector<Card>();
		}
		paint=new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(cardWidth*2/3);
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1.0f);
		paint.setTextAlign(Align.CENTER);

	}

	// ������
	public void drawBackground() {
			Rect src = new Rect(0, 0, bgBitmap.getWidth()*3 / 4,
					2*bgBitmap.getHeight() / 3);
			Rect dst = new Rect(0, 0, screen_width, screen_height);
			canvas.drawBitmap(bgBitmap, src, dst, null);
	}
	// �����
	public void drawPlayer(int player){
			if(playerList[player]!=null&&playerList[player].size()>0)
			{
				for(Card card:playerList[player])
					drawCard(card);
			}
	}
	//����
	public void drawCard(Card card){
		Bitmap tempbitBitmap;
		if(card.rear)
			tempbitBitmap=cardBgBitmap;
		else {
			tempbitBitmap=card.bitmap;
		}
		canvas.drawBitmap(tempbitBitmap, card.getSRC(),
				card.getDST(), null);
	}
	//ϴ��
	public void washCards() {
		//����˳��
		for(int i=0;i<100;i++){
			Random random=new Random();
			int a=random.nextInt(54);
			int b=random.nextInt(54);
			Card k=card[a];
			card[a]=card[b];
			card[b]=k;
		}
	}
	//����
	public void handCards(){
		//��ʼ����
		int t=0;
		for(int i=0;i<3;i++){
			playerList[i]=new Vector<Card>();
		}
		for(int i=0;i<54;i++)
		{
			if(i>50)//������
			{
				//���õ�����
				card[i].setLocation(screen_width/2-(3*i-155)*cardWidth/2,0);
				dizhuList.add(card[i]);
				update();
				continue;
			}
			switch ((t++)%3) {
			case 0:
				//������
				card[i].setLocation(cardWidth/2,cardHeight/2+i*cardHeight/21);
				playerList[0].add(card[i]);
				break;
			case 1:
				//��
				card[i].setLocation(screen_width/2-(9-i/3)*cardWidth*2/3,screen_height-cardHeight);
				card[i].rear=false;//����
				playerList[1].add(card[i]);
				break;
			case 2:
				//�ұ����
				card[i].setLocation(screen_width-3*cardWidth/2,cardHeight/2+i*cardHeight/21);
				playerList[2].add(card[i]);
				break;
			}
			update();
			Sleep(100);
		}
		//��������
		for(int i=0;i<3;i++){
			Common.setOrder(playerList[i]);
			Common.rePosition(this, playerList[i],i);
		}
		//�򿪰�ť
		hideButton=false;
		update();
	}
	//sleep();
	public void Sleep(long i){
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//��ť(�����������������ƣ�����)
	public void drawButton(){
		if(!hideButton)
		{
			canvas.drawText(buttonText[0],screen_width/2-2*cardWidth,screen_height-cardHeight*2, paint);
			canvas.drawText(buttonText[1],screen_width/2+2*cardWidth,screen_height-cardHeight*2, paint);
			canvas.drawRect(new RectF(screen_width/2-3*cardWidth, screen_height-cardHeight*5/2,
					screen_width/2-cardWidth,screen_height-cardHeight*11/6), paint);
			canvas.drawRect(new RectF(screen_width/2+cardWidth, screen_height-cardHeight*5/2,
					screen_width/2+3*cardWidth,screen_height-cardHeight*11/6), paint);
		}
		
	}
	//Message
	public void drawMessage(){

		if(!message[1].equals(""))
		{
			canvas.drawText(message[1],screen_width/2,screen_height-cardHeight*2,paint);
		}
		if(!message[0].equals(""))
		{
			canvas.drawText(message[0],cardWidth*3,screen_height/4,paint);
		}
		if(!message[2].equals(""))
		{
			canvas.drawText(message[2],screen_width-cardWidth*3,screen_height/4,paint);
		}
	}
	//��һ�����
	public void nextTurn(){
		turn=(turn+1)%3;
	}
	//������ͷ��
	public void drawDizhuIcon(){
		if(dizhuFlag>=0){
			float x=0f,y=0f;
			if(dizhuFlag==0)
			{
				x=cardWidth/2f;
				y=dizhuBitmap.getHeight()/2;
			}
			if(dizhuFlag==1)
			{
				x=cardWidth*1.5f;
				y=screen_height-2f*cardHeight;
			}
			if(dizhuFlag==2)
			{
				x=screen_width-cardWidth/2f-dizhuBitmap.getWidth();
				y=dizhuBitmap.getHeight()/2;
			}
			canvas.drawBitmap(dizhuBitmap,x,y,null);
		}
	}
	//�����ߵ���
	public void drawOutList(){
		int x=0,y=0;
		for(int i=0,len=outList[1].size();i<len;i++)
		{
			x=screen_width/2+(i-len/2)*cardWidth/3;
			y=screen_height-5*cardHeight/2;
			canvas.drawBitmap(outList[1].get(i).bitmap, x,y, null);
		}
		for(int i=0,len=outList[0].size();i<len;i++)
		{
			x=3*cardWidth;
			y=screen_height/2+(i-len/2-7)*cardHeight/4;
			canvas.drawBitmap(outList[0].get(i).bitmap, x,y, null);
		}
		for(int i=0,len=outList[2].size();i<len;i++)
		{
			x=screen_width-cardWidth*4;
			y=screen_height/2+(i-len/2-7)*cardHeight/4;
			canvas.drawBitmap(outList[2].get(i).bitmap, x,y, null);
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		start=true;
		screen_height = getHeight();
		screen_width = getWidth();
		// ��ʼ��
		InitBitMap();
		// ϴ��
		washCards();
		// ��ʼ��Ϸ����
		gameThread=new Thread(new Runnable() {
			@Override
			public void run() {
				//��ʼ����
				handCards();
				
				//�ȴ�����ѡ��
				while(start){
					switch (turn) {
					case 0:
						player0();
						break;
					case 1:
						player1();
						break;
					case 2:
						player2();
						break;
					default:
						break;
					}
					win();
					
					
					
				}
			}
		});
		gameThread.start();
		// ��ʼ��ͼ����
		drawThread=new Thread(this);
		drawThread.start();
	}
	//player0
	public void player0(){
		//Log.i("mylog", "���0");
		List<Card> player0=null;
		Common.currentFlag=0;
		if(flag[1]==0&&flag[2]==0)
		{
			player0=Common.getBestAI(playerList[0],null);
			
		}
		else if(flag[2]==0)
		{
			Common.oppoerFlag=1;
			player0=Common.getBestAI(playerList[0],outList[1]);
		}
		else {
			Common.oppoerFlag=2;
			player0=Common.getBestAI(playerList[0],outList[2]);
		}
		message[0]="";
		outList[0].clear();
		setTimer(3, 0);
		if(player0!=null)
		{
			outList[0].addAll(player0);
			playerList[0].removeAll(player0);
			Common.rePosition(this, playerList[0], 0);
			message[0]="";
			flag[0]=1;
		}else {
			message[0]="��Ҫ";
			flag[0]=0;
		}
		update();
		nextTurn();
	}
	//player2
	public void player2(){
		//Log.i("mylog", "���2");
		Common.currentFlag=2;
		List<Card> player2=null;
		if(flag[1]==0&&flag[0]==0)
		{
			player2=Common.getBestAI(playerList[2],null);
		}
		else if(flag[1]==0)
		{
			player2=Common.getBestAI(playerList[2],outList[0]);
			Common.oppoerFlag=0;
		}
		else {
			player2=Common.getBestAI(playerList[2],outList[1]);
			Common.oppoerFlag=1;
		}
		message[2]="";
		outList[2].clear();
		setTimer(3, 2);
		if(player2!=null)
		{
			outList[2].addAll(player2);
			playerList[2].removeAll(player2);
			Common.rePosition(this, playerList[2], 2);
			message[2]="";
			flag[2]=1;
		}else {
			message[2]="��Ҫ";
			flag[2]=0;
		}
		update();
		nextTurn();
	}
	//player1
	public void player1(){
		Sleep(1000);
		//��ʼд���Ƶ���
		buttonText[0]="����";
		buttonText[1]="��Ҫ";
		hideButton=false;
		outList[1].clear();
		update();
		//����ʱ
		int i=28;
		while((turn==1)&&(i-->0)){
			//��ʱ������draw timer.������ʱ����
			message[1]=i+"";
			update();
			Sleep(1000);
		}
		hideButton=true;
		update();
		if(turn==1&&i<=0)//˵���û�û���κβ���
		{
			//�Զ���Ҫ������ѡһ������
			message[1]="��Ҫ";
			flag[1]=0;
			nextTurn();
		}
		update();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		start=false;
	}
	//��Ҫ��ͼ�߳�
	@Override
	public void run() {
		while (start) {
			if(repaint)
			{
				onDraw();
				repaint=false;
				Sleep(33);
			}
		}
	}
	//��ͼ����
	public void onDraw(){
		//����
		synchronized (surfaceHolder) {
			try {
				canvas = surfaceHolder.lockCanvas();
				// ������
				drawBackground();
				// ����
				for(int i=0;i<3;i++)
					drawPlayer(i);
				// ������
				for(int i=0,len=dizhuList.size();i<len;i++)
					drawCard(dizhuList.get(i));
				// ����ť( ������,����,����,����)
				drawButton();
				// message���� ��3��String��
				drawMessage();
				// ������ͼ��
				drawDizhuIcon();
				// ���ƽ���(3���ط�,��3��vector��)
				drawOutList();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null)
					surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	//���º���
	public void update(){
		repaint=true;
	}
	//�����¼�
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//ֻ���ܰ����¼�
		if(event.getAction()!=MotionEvent.ACTION_UP)
			return true;
		//��ѡ��
		EventAction eventAction=new EventAction(this,event);
		Card card=eventAction.getCard();
		if(card!=null)
		{
			Log.i("mylog", card.name);
			if(card.clicked)
				card.y+=card.height/3;
			else 
				card.y-=card.height/3;
			card.clicked=!card.clicked;
			update();//�ػ�
		}
		//��ť�¼�
		eventAction.getButton();
		return true;
	}
	//��ʱ��
	public void setTimer(int t,int flag)
	{
		while(t-->0){
			Sleep(1000);
			message[flag]=t+"";
			update();
		}
		message[flag]="";
	}
	//�жϳɹ�
	public void win(){
		int flag=-1;
		if(playerList[0].size()==0)
			flag=0;
		if(playerList[1].size()==0)
			flag=1;
		if(playerList[2].size()==0)
			flag=2;
		if(flag>-1){
			for(int i=0;i<54;i++)
			{
				card[i].rear=false;
			}
			update();
			start=false;
			Message msg=new Message();
			msg.what=0;
			Bundle builder=new Bundle();
			if(flag==1)
				builder.putString("data","��ϲ��Ӯ��");
			if(flag==dizhuFlag&&flag!=1)
				builder.putString("data","��ϲ����"+flag+"Ӯ��");
			if(flag!=dizhuFlag&&flag!=1)
				builder.putString("data","��ϲ��ͬ��Ӯ��");
			for(int i=0;i<54;i++)
				card[i].rear=false;
			msg.setData(builder);
			handler.sendMessage(msg);
			
			
		}
		 
		
	}
	
}
