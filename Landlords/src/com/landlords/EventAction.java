package com.landlords;

import java.util.List;

import android.util.Log;
import android.view.MotionEvent;

public class EventAction {
	/*
	 * QQ:361106306
	 * by:С��
	 * ת�ش˳����뱣����Ȩ,δ������������������ҵ��;!
	 * */
	MotionEvent event;
	MyView view;

	public EventAction(MyView view, MotionEvent event) {
		this.event = event;
		this.view = view;
	}
	// ������ť�¼�
	public void getButton(){
		if(!view.hideButton){
			float x=event.getX(),y=event.getY();
			//��߰�ť
			if((x>view.screen_width/2-3*view.cardWidth)&&(y>view.screen_height-view.cardHeight*5/2)&&
					(x<view.screen_width/2-view.cardWidth)&&(y<view.screen_height-view.cardHeight*11/6))
			{
				//������
				if(view.buttonText[0].equals("������"))
				{
					//���������
					for(Card card:view.dizhuList)
					{
						card.rear=false;
					}
					view.update();
					view.setTimer(5, 1);
					view.playerList[1].addAll(view.dizhuList);
					view.dizhuList.clear();
					Common.setOrder(view.playerList[1]);
					Common.rePosition(view, view.playerList[1], 1);
					view.dizhuFlag=1;//��������;
					Common.dizhuFlag=view.dizhuFlag;
					view.update();
					view.turn=1;
				}
				//����
				if(view.buttonText[0].equals("����"))
				{
					//ѡ����õĳ���(���ƺ���������)
					List<Card> oppo=null;
					if(view.outList[0].size()<=0&&view.outList[2].size()<=0)
					{
						oppo=null;
					}
					else {
						oppo=(view.outList[0].size()>0)?view.outList[0]:view.outList[2];
					}
					List<Card> mybest=Common.getMyBestCards(view.playerList[1], oppo);
					//Common.getBestAI(view.playerList[1],null);
					if(mybest==null)
						return;
					synchronized (view) {
						//����outlist
						view.outList[1].clear();
						view.outList[1].addAll(mybest);
						//�˳�playerlist
						view.playerList[1].removeAll(mybest);
					}
					Common.rePosition(view, view.playerList[1], 1);
					view.flag[1]=1;
					view.message[1]="";
					view.nextTurn();
					view.update();
				}
				view.hideButton=!view.hideButton;
			}
			//�ұ�
			if(x>view.screen_width/2+view.cardWidth&& y>view.screen_height-view.cardHeight*5/2&&
					x<view.screen_width/2+3*view.cardWidth&&y<view.screen_height-view.cardHeight*11/6)
			{
				//����
				if(view.buttonText[1].equals("����"))
				{
					view.dizhuFlag=Common.getBestDizhuFlag();
					//view.dizhuFlag=0;
					Common.dizhuFlag=view.dizhuFlag;
					for(Card card:view.dizhuList)
					{
						card.rear=false;//����
					}
					view.update();
					view.Sleep(3000);
					for(Card card:view.dizhuList)
					{
						card.rear=true;//����
					}
					view.playerList[view.dizhuFlag].addAll(view.dizhuList);
					view.dizhuList.clear();
					Common.setOrder(view.playerList[view.dizhuFlag]);
					Common.rePosition(view, view.playerList[view.dizhuFlag], view.dizhuFlag);
					view.update();
					view.turn=view.dizhuFlag;
					view.hideButton=true;
				}
				//����
				if(view.buttonText[1].equals("��Ҫ")){
					if(view.outList[0].size()==0&&view.outList[2].size()==0)
					{
						Log.i("mylog", "���ܲ���Ҫ");
						return;
					}
					Log.i("mylog", "��Ҫ");
					view.message[1]="��Ҫ";
					view.hideButton=true;
					view.nextTurn();
					view.flag[1]=0;
					view.update();
				}
				
			}
		}
	}
	// ȡ���������������
	public Card getCard() {
		Card card = null;
		float x = event.getX();// ����x����
		float y = event.getY();// ����y����
		float xoffset = view.cardWidth * 4 / 5f;
		float yoffset = view.cardHeight;
		if (y < view.screen_height - 4 * view.cardHeight / 3)
			return null;
		for (Card card2 : view.playerList[1]) {
			if (card2.clicked) {
				// ��ѯ���Ϸ�Χ��
				if ((x - card2.x > 0)
						&& (y - card2.y > 0)
						&& (((x - card2.x < xoffset) && (y - card2.y < yoffset)) || ((x
								- card2.x < card2.width) && (y - card2.y < card2.height / 3)))) {
					return card2;
				}
			} else {
				// ��ѯ���Ϸ�Χ��
				if ((x - card2.x > 0) && (x - card2.x < xoffset)
						&& (y - card2.y > 0) && (y - card2.y < yoffset)) {
					return card2;
				}
			}
		}

		return card;
	}
}
