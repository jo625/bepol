package com.bepol.beaconNActivities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.getDirection.Vertex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
/*****************************************************
 *
 * 1. MainActivity에 MyView class 선언
 *
 * 2. activity_main.xml
 *
 *    <beaconTeam.successbeacon.MyView
 *	      android:layout_width="match_parent"
 *	      android:layout_height="match_parent"
 *	  />
 * 
 * 3. MainActivity.java OnCreate()
 *
 *    DisplayMetrics displaymetrics = new DisplayMetrics();
 *	  getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
 *
 *	  float height = displaymetrics.heightPixels;
 *	  float width = displaymetrics.widthPixels;
 *
 *	  navi = new MyView(this);
 *	  navi.setHeight(height);
 *	  navi.setWidth(width);
 *
 *	  navi.setStart("출발지");
 *    navi.setDest("목적지");
 *
 * 4. MainActivity.java onDestroy()
 * 
 *    navi.setStart("");
 *    navi.setDest(""); 
 * 
 * 
 
 * @author kang
 *
 ******************************************************/



@SuppressLint("DrawAllocation")
public class MyView extends View {
	public static float Width, 		Height;
	public static float startx=0, 	starty=0;
	public static float stopx=0, 	stopy=0;
	public static float destx=0, 	desty=0;
	public static float curX=0, 	curY=0;
	public static String Start="", 	Dest="";
	public static Paint paint;
	Bitmap bitmap;
	static Canvas canvas2;
	static Context context;
	public static Vertex v;
	
	public static LinkedList<Vertex> path_view = new LinkedList<Vertex>();
	public static ArrayList<Float> where = new ArrayList<Float>();
	
	
	public MyView(Context context) {
		super(context);
		this.context = context;
		init(context);
		// TODO Auto-generated constructor stub
		
		
	}

	
	
	public MyView(Context context, AttributeSet attrs){
		super(context);
		init(context);
		//setBackgroundColor(Color.BLUE);
	}
	
	private void init(Context context){		// 미리 설정
		paint = new Paint(); 				// initialize Paint object
		paint.setColor(Color.RED); 			// set Paint color
		paint.setStrokeWidth(5);  			// set paint width
		paint.setStrokeCap(Cap.ROUND); 		// 선을 뭉툭하게
		paint.setStrokeJoin(Join.ROUND);	// 선 끝을 뭉툭하게
		paint.setAntiAlias(true);
		
	}


	/* 처음 화면이 실행될 때 셋팅되는 값 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas2 = new Canvas();
		canvas2.setBitmap(bitmap);
		//canvas2.drawColor(Color.WHITE);
		// view와 크기가 같은 bitmap 객체 생성
	}
	
	/* 지정한 path 설정 */
	public static void setPath(LinkedList<Vertex> path){
		path_view = path;
	}
	
	/* 디스플레이 Width 설정 */
	public void setWidth(float width){
		Width = width;
	}
	
	/* 디스플레이 Height 설정 */
	public void setHeight(float height){
		Height = height;		
	}
		
	/* 출발지 설정  */
	public static void setStart(String start){
		Start = start;
	}
	
	/* 목적지 설정 */
	public static void setDest(String dest){
		Dest = dest;
	}
	
	/* 목적지 반환 */
	public static String getDest(){
		return Dest;
	}
	
	/* 출발지 반환 */
	public static String getStart(){
		return Start;
	}
	
	/* x좌표 설정 */
	public void setx(float x){
		curX = x;
	}
	
	/* y좌표 설정 */
	public void sety(float y){
		curY = y;
	}
	
	/* 디스플레이 위에 그림을 그려주는 함수 */
	@Override
	public void onDraw(Canvas canvas){
		
		float[] simpleArray;           // 노드들 담는 array
		int count  = 0;				
		int size = path_view.size();   // path_view 링크드리스트의 크기
		
		for(int i=0 ; i<size ; i++){   // 링크드리스트 처음부터 끝까지
			
			if(i==0 || i==size-1){     //첫번째, 마지막 노드는 한번더 'where' arraylist에 저장
				where.add(path_view.get(i).getX()*Width);  //X좌표를 꺼내서(상대좌표) * Width = 절대 X좌표  
				where.add(path_view.get(i).getY()*Height); //Y좌표를 꺼내서(상대좌표) * Height = 절대 Y좌표
				continue;
			}
			where.add(path_view.get(i).getX()*Width);
			where.add(path_view.get(i).getY()*Height);
			where.add(path_view.get(i).getX()*Width);
			where.add(path_view.get(i).getY()*Height);
			
		}
		Float[] result = new Float[where.size()];
		simpleArray = new float[where.size()];

		for (int j = 0; j < where.size(); j++) {
			result[j] = where.get(j);
		}

		for (int i = 0; i < result.length; i++) {
			simpleArray[i] = result[i];
		}

		canvas2.drawLines(simpleArray, paint);          // simapleArray에 저장된 노드들을 통해서 line을 그려준다
		path_view.clear();                              // path_view 초기화
		where.clear();                                  // where 초기화
	
		if(bitmap != null){
			canvas.drawBitmap(bitmap, 0, 0, null);		// 메모리에 만들어 논 bitmap 객체를 화면에 뿌려줌
		}
		
		
	}
	
}
