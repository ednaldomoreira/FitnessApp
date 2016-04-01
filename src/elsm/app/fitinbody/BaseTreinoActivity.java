package elsm.app.fitinbody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.lang.InterruptedException;

import elsm.comp.picker.NumberPicker;
import android.graphics.drawable.PaintDrawable;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TimePicker;
import android.view.LayoutInflater;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.view.MotionEvent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.PaintDrawable;
import android.widget.PopupWindow;
import android.app.Dialog;
public class BaseTreinoActivity extends Activity implements OnGestureListener {
	private boolean EstaComOnlongClick;
	private TextView mLongClickTextView;
	private TextView tvFocused=null;
	protected Date mDataPicket;
	protected TextView mDateDisplay;
	//	protected TextView mContagemDisplay;
	protected PopupWindowContagem pwFloatingContagemText=null;
	protected PopupWindowContagem pwFloatingContagem=null;
	protected PopupWindowContagem pwFloatingContagemTimer;
	protected PopupWindowContagem pwFloatingContagemNumber;
	protected String valorAnteriorBoxCont;
	protected GestureDetector gesturedetector = null;
	protected DismissPopupHandler dismissPopupHandler;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	public static final int SWIPE_MIN_VELOCITY = 100;
	public static final int SWIPE_MIN_DISTANCE = 100;

	@Override  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gesturedetector = new GestureDetector(this, this);

		LayoutInflater layoutInflater 
		= (LayoutInflater)getBaseContext()
		.getSystemService(LAYOUT_INFLATER_SERVICE);

		View popupView = layoutInflater.inflate(R.layout.popupw_contagem, null);
		pwFloatingContagemText = new PopupWindowContagem(
				popupView, 
				LayoutParams.WRAP_CONTENT,  
				LayoutParams.WRAP_CONTENT);

		pwFloatingContagemText.baseTreinoActivity = this;
		
		popupView = layoutInflater.inflate(R.layout.popupw_contagem_picker, null);  
		pwFloatingContagemNumber = new PopupWindowContagem(
				popupView, 
				LayoutParams.WRAP_CONTENT,  
				LayoutParams.WRAP_CONTENT);

		pwFloatingContagemNumber.baseTreinoActivity = this;
		
		popupView = layoutInflater.inflate(R.layout.popupw_contagem_picker_timer, null);  
		pwFloatingContagemTimer = new PopupWindowContagem(
				popupView, 
				LayoutParams.WRAP_CONTENT,  
				LayoutParams.WRAP_CONTENT);

		pwFloatingContagemTimer.baseTreinoActivity = this;

		pwFloatingContagem=pwFloatingContagemTimer;

		dismissPopupHandler = new DismissPopupHandler(); 

	}
	protected void updateDisplay() {

		SimpleDateFormat dformat = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = dformat.format(mDataPicket);        

		mDateDisplay.setText(formattedDate);
	}

	protected void OnClickDatePick(View v){
		showDialog(DATE_DIALOG_ID);

	}

	protected OnClickListener mDatePickListener = new OnClickListener() {  
		public void onClick(View v) {
			OnClickDatePick(v);

		}  
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			Calendar c = Calendar.getInstance();
			int mYear;
			if (mDataPicket==null){
				mYear = c.get(Calendar.YEAR);// - 1900;
			}
			else{
				c.setTime(mDataPicket);
				mYear = c.get(Calendar.YEAR);// - 1900;

			}
			int mMonth = c.get(Calendar.MONTH);
			int mDay = c.get(Calendar.DATE);

			return new DatePickerDialog(this,
					mDateSetListener,
					mYear, mMonth, mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, 0, 0, false);
		}
		return null;
	}

	protected void OnSetDatePick(DatePicker view, int year, 
			int monthOfYear, int dayOfMonth){
		mDataPicket = new Date(year - 1900, monthOfYear, dayOfMonth);

		updateDisplay();

	}

	protected void OnSetTimePick(TimePicker view, int hourOfDay, int minute){

		updateDisplay();

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			OnSetDatePick(view, year, monthOfYear, dayOfMonth);
		}
	};
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = 
			new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			OnSetTimePick(view, hourOfDay, minute);

		}
	};



	protected OnClickListener mExercicioClickListener = new OnClickListener() {  
		public void onClick(View v) {
			TextView tv = (TextView) v;
			if (!EstaComOnlongClick){
				IncValor.incremetarValor((TextView) v, false);
				if (tvFocused == null || !tvFocused.getTag().toString().equals(tv.getTag())){
					tvFocused = tv;
					mostrarContagemWindow(tv);
					dismissPopupHandler.adqSemaph();
					dismissPopupHandler.sleep();
					dismissPopupHandler.releaseSemaph();
				}
				dismissPopupHandler.adqSemaph();
				pwFloatingContagem.atualizaCampo(tv.getText().toString());
				dismissPopupHandler.sleep();
				dismissPopupHandler.releaseSemaph();
			}
			else{

				EstaComOnlongClick = false;
				//				pwFloatingContagem.dismiss();
			}
		}

	};
	
	protected OnLongClickListener mExercicioLongClickListener = new OnLongClickListener() {  
		public boolean onLongClick(View v) {
			OnExercicioLongClickListener((TextView) v);
			return false;
		}

	};

	protected void OnExercicioLongClickListener(TextView tv){
		mLongClickTextView = tv;
		if (tvFocused == null || !tvFocused.getTag().toString().equals(tv.getTag())){
			tvFocused = tv;
			mostrarContagemWindow(tv);
			dismissPopupHandler.adqSemaph();
			dismissPopupHandler.sleep();
			dismissPopupHandler.releaseSemaph();
		}
		EstaComOnlongClick = true;

		//		mostrarContagemWindow(tv);

		valorAnteriorBoxCont=tv.getText().toString();
		(new RefreshHandler()).sleep();

	}
	protected void mostrarContagemWindow(TextView tv){
		int posTela[]=new int[2];
		int alturaPopup=0;

		if (pwFloatingContagem!=null && pwFloatingContagem.isShowing())
			pwFloatingContagem.dismiss();

		if (IncValor.tipoValor(tv)==2){
			pwFloatingContagem=pwFloatingContagemText;
			copiaConfiguracao(tv,  pwFloatingContagemText.mContagemDisplay);

		}
		else if (IncValor.tipoValor(tv)!=1)
			pwFloatingContagem=pwFloatingContagemNumber;
		else
			pwFloatingContagem=pwFloatingContagemTimer;

		pwFloatingContagem.tvDisplay=tv;

		tv.getLocationOnScreen(posTela);
		
		if (pwFloatingContagem.getContentView().getHeight() == 0){
			alturaPopup = pwFloatingContagem.getAltura(); 
		}
		else{
			alturaPopup = pwFloatingContagem.getContentView().getHeight();
		}
			
		if (posTela[1] - alturaPopup > 0)
			pwFloatingContagem.showAsDropDown(tv, 0, - tv.getHeight() - alturaPopup, IncValor.tipoValor(tv), tv.getText().toString()); 
		else
			pwFloatingContagem.showAsDropDown(tv, 0, 0, IncValor.tipoValor(tv), tv.getText().toString()); 

	}
	protected void depurarTabela(ViewGroup vv){
		for(int k=0;k<vv.getChildCount();k++){
			if (vv.getChildAt(k).getTag()==null)
				Log.d("------->", vv.getChildAt(k).getClass().getName() );
			else
				Log.d("------->", vv.getChildAt(k).getClass().getName() + " - Tag -> " +vv.getChildAt(k).getTag().toString());
			if (vv.getChildAt(k) instanceof ViewGroup)
				depurarTabela((ViewGroup) vv.getChildAt(k));

		}   
	}

	public static void ProximaAtividade(ViewFlipper vf){
		vf.setInAnimation(inFromRightAnimation());
		vf.setOutAnimation(outToLeftAnimation());
		vf.showNext();

		if (vf.getCurrentView().getClass().getName().equalsIgnoreCase("android.widget.ScrollView")){
			ViewGroup vgSV = (ViewGroup) vf.getCurrentView();
			if (vgSV.getChildAt(0).getClass().getName().equalsIgnoreCase("android.widget.HorizontalScrollView")){
				ViewGroup vgHSV = (ViewGroup) vgSV.getChildAt(0);
				if (vgHSV.getChildAt(0).getWidth() < vgHSV.getWidth()){
					vgHSV.getChildAt(0).setMinimumWidth(vgHSV.getWidth());
				}
			}


		}

	}
	public static void AtividadeAnterior(ViewFlipper vf){
		vf.setInAnimation(inFromLeftAnimation());
		vf.setOutAnimation(outToRightAnimation());
		vf.showPrevious();
	}
	//for the previous movement
	public static Animation inFromRightAnimation() {

		Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
		inFromRight.setDuration(350);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}
	public static Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
		outtoLeft.setDuration(350);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}    
	// for the next movement
	public static Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
		inFromLeft.setDuration(350);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}
	public static Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
		outtoRight.setDuration(350);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	public void criarTabelaGrupoExercicioAerobico(Activity activity, TableLayout tl,
			ExerciciosListAerobico exerciciosList){
		TableLayout tlInner;
		TableRow tr;
		TextView tvD, tvSEG, tvTER, tvQUA, tvQUI, tvSEX, tvSAB, tvDOM;
		TableRow.LayoutParams params;
		boolean cabCriado = false;
		while (exerciciosList.moveNext()){
			if (!cabCriado){
				tr = new TableRow(activity);
				tr.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				tvD = new TextView(activity);
				tvD.setText(exerciciosList.getNomeGrupoTreino());
				tr.addView(tvD);
/*				
				tvSEG = new TextView(activity);
				tvSEG.setText("SEG");
				tvSEG.setTag("SEG");
				tvTER = new TextView(activity);
				tvTER.setText("TER");
				tvTER.setTag("TER");
				tvQUA = new TextView(activity);
				tvQUA.setText("QUA");
				tvQUA.setTag("QUA");
				tvQUI = new TextView(activity);
				tvQUI.setText("QUI");
				tvQUI.setTag("QUI");
				tvSEX = new TextView(activity);
				tvSEX.setText("SEX");
				tvSEX.setTag("SEX");
				tvSAB = new TextView(activity);
				tvSAB.setText("SAB");
				tvSAB.setTag("SAB");
				tvDOM = new TextView(activity);
				tvDOM.setText("DOM");
				tvDOM.setTag("DOM");
				tr.addView(tvSEG);
				tr.addView(tvTER);
				tr.addView(tvQUA);
				tr.addView(tvQUI);
				tr.addView(tvSEX);
				tr.addView(tvSAB);
				tr.addView(tvDOM);
				exerciciosList.configuraCabecalhoGrupo(tvD, tvSEG, tvTER, tvQUA, tvQUI, tvSEX, tvSAB, tvDOM);
*/				
		        params = (TableRow.LayoutParams)tvD.getLayoutParams();
		        params.span = 7;
		        tvD.setLayoutParams(params);
		        ExerciciosList.configuraTVDescCabec(tvD);
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				cabCriado = true;
			}
			
			tlInner = new TableLayout(activity);
			

			tr = new TableRow(activity);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			tr.addView(tlInner);
			tlInner.setBackgroundColor(Color.argb(120, 100, 80, 205));
			((MarginLayoutParams) tlInner.getLayoutParams()).setMargins(1, 1, 1, 5);
			
	        tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
	        
/* --- ---*/
	        
			tr = new TableRow(activity);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
			tvD = new TextView(activity);
			tvD.setTag("D"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvD.setText(exerciciosList.getNomeExercicio());
			
			tr.addView(tvD);
	        params = (TableRow.LayoutParams)tvD.getLayoutParams();
	        params.span = 7;
	        tvD.setLayoutParams(params);        
	        ExerciciosList.configuraTV(tvD);
	        tvD.setBackgroundColor(Color.WHITE);
	        tvD.setTextColor(Color.argb(125, 105, 89, 205));
	        
	        tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
/* --- */
			
			tr = new TableRow(activity);
			
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
			tvD = new TextView(activity);
			tvD.setText("Segunda");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
			tvD = new TextView(activity);
			tvD.setText("Terça");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
			tvD = new TextView(activity);
			tvD.setText("Quarta");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
	        
	        tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

	        /* --- */
			tr = new TableRow(activity);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			tvSEG = new TextView(activity);
			tvSEG.setTag("SEG"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvSEG.setText(exerciciosList.getExercicioTempoSEG()+"");
			tvSEG.setOnClickListener(mExercicioClickListener);
			tvSEG.setOnLongClickListener(mExercicioLongClickListener);


			tvTER = new TextView(activity);
			tvTER.setTag("TER"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvTER.setText(exerciciosList.getExercicioTempoTER()+"");
			tvTER.setOnClickListener(mExercicioClickListener);
			tvTER.setOnLongClickListener(mExercicioLongClickListener);

			tvQUA = new TextView(activity);
			tvQUA.setTag("QUA"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvQUA.setText(exerciciosList.getExercicioTempoQUA()+"");
			tvQUA.setOnClickListener(mExercicioClickListener);   
			tvQUA.setOnLongClickListener(mExercicioLongClickListener);


			tr.addView(tvSEG);
			tr.addView(tvTER);
			tr.addView(tvQUA);
			tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			/* --- */
/* --- */
			
			tr = new TableRow(activity);
			
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
	        
			tvD = new TextView(activity);
			tvD.setText("Quinta");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
			tvD = new TextView(activity);
			tvD.setText("Sexta");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
			tvD = new TextView(activity);
			tvD.setText("Sabado");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
	        tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

	        /* --- */
			
			tr = new TableRow(activity);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			tvQUI = new TextView(activity);
			tvQUI.setTag("QUI"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvQUI.setText(exerciciosList.getExercicioTempoQUI()+"");
			tvQUI.setOnClickListener(mExercicioClickListener);
			tvQUI.setOnLongClickListener(mExercicioLongClickListener);

			tvSEX = new TextView(activity);
			tvSEX.setTag("SEX"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvSEX.setText(exerciciosList.getExercicioTempoSEX() +"");
			tvSEX.setOnClickListener(mExercicioClickListener);
			tvSEX.setOnLongClickListener(mExercicioLongClickListener);

			tvSAB = new TextView(activity);
			tvSAB.setTag("SAB"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvSAB.setText(exerciciosList.getExercicioTempoSAB()+"");
			tvSAB.setOnClickListener(mExercicioClickListener);
			tvSAB.setOnLongClickListener(mExercicioLongClickListener);

			tr.addView(tvQUI);
			tr.addView(tvSEX);
			tr.addView(tvSAB);
			tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			/* --- */
/* --- */
			
			tr = new TableRow(activity);
			
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
			tvD = new TextView(activity);
			tvD.setText("Domingo");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
	        tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

	        /* --- */
	        
			tr = new TableRow(activity);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			tvDOM = new TextView(activity);
			tvDOM.setTag("DOM"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvDOM.setText(exerciciosList.getExercicioTempoDOM()+"");
			tvDOM.setOnClickListener(mExercicioClickListener);
			tvDOM.setOnLongClickListener(mExercicioLongClickListener);

			tr.addView(tvDOM);
			tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			exerciciosList.configuraExercicio(tvSEG, tvTER, tvQUA, tvQUI, tvSEX, tvSAB, tvDOM);
		}

	}

	public void criarTabelaGrupoExercicioMuscul(Activity activity, TableLayout tl,
			ExerciciosListMusculacao exerciciosList){
		TableLayout tlInner;
		TableRow tr;
		TextView tvD, tvT, tvS, tvR, tvP;
		TableRow.LayoutParams params;
		boolean cabCriado = false;
		while (exerciciosList.moveNext()){
			if (!cabCriado){
				tr = new TableRow(activity);
				tr.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				tvD = new TextView(activity);
				tvD.setText(exerciciosList.getNomeGrupoTreino());
				tr.addView(tvD);
		        params = (TableRow.LayoutParams)tvD.getLayoutParams();
		        params.span = 4;
		        tvD.setLayoutParams(params);
		        ExerciciosList.configuraTVDescCabec(tvD);
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				
/*				
				tr = new TableRow(activity);
				tr.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				tvT = new TextView(activity);
				tvT.setText("T");
				tvS = new TextView(activity);
				tvS.setText("S");
				tvR = new TextView(activity);
				tvR.setText("R");
				tvP = new TextView(activity);
				tvP.setText("P");
				tr.addView(tvD);
				tr.addView(tvT);
				tr.addView(tvS);
				tr.addView(tvR);
				tr.addView(tvP);
				exerciciosList.configuraCabecalhoGrupo(tvD, tvT, tvS, tvR, tvP);
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
*/						
				cabCriado = true;
			}
			
			
			tlInner = new TableLayout(activity);
			

			tr = new TableRow(activity);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			tr.addView(tlInner);
			tlInner.setBackgroundColor(Color.argb(120, 100, 80, 205));
			((MarginLayoutParams) tlInner.getLayoutParams()).setMargins(1, 1, 1, 5);
			
	        tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
	        
/* --- ---*/
	        
			tr = new TableRow(activity);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
			tvD = new TextView(activity);
			tvD.setTag("D"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvD.setText(exerciciosList.getNomeExercicio());
			
			tr.addView(tvD);
	        params = (TableRow.LayoutParams)tvD.getLayoutParams();
	        params.span = 4;
	        tvD.setLayoutParams(params);        
	        ExerciciosList.configuraTV(tvD);
	        tvD.setBackgroundColor(Color.WHITE);
	        tvD.setTextColor(Color.argb(125, 105, 89, 205));
	        tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
/* --- */
			
			tr = new TableRow(activity);
			
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
			tvD = new TextView(activity);
			tvD.setText("Treino");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
			tvD = new TextView(activity);
			tvD.setText("Series");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
			tvD = new TextView(activity);
			tvD.setText("Repet");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
			tvD = new TextView(activity);
			tvD.setText("Peso");			
			tr.addView(tvD);
	        ExerciciosList.configuraTV(tvD);
	        
	        tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

/* --- */		

			tr = new TableRow(activity);
			
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
			tvT = new TextView(activity);
			tvT.setTag("T"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvT.setText(exerciciosList.getExercicioTreino());
			if (exerciciosList.getEvento("T")){
				tvT.setOnClickListener(mExercicioClickListener);
				tvT.setOnLongClickListener(mExercicioLongClickListener);
			}

			tvS = new TextView(activity);
			tvS.setTag("S"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvS.setText(exerciciosList.getExercicioSerie()+"");
			if (exerciciosList.getEvento("S")){
				tvS.setOnClickListener(mExercicioClickListener);
				tvS.setOnLongClickListener(mExercicioLongClickListener);
			}

			tvR = new TextView(activity);
			tvR.setTag("R"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvR.setText(exerciciosList.getExercicioRepeticao()+"");
			if (exerciciosList.getEvento("R")){
				tvR.setOnClickListener(mExercicioClickListener);   
				tvR.setOnLongClickListener(mExercicioLongClickListener);
			}

			tvP = new TextView(activity);
			tvP.setTag("P"+String.format("%10d", exerciciosList.getCodigoExercicio()));
			tvP.setText(exerciciosList.getExercicioPeso()+"");
			tvP.setOnClickListener(mExercicioClickListener);
			tvP.setOnLongClickListener(mExercicioLongClickListener);

			tr.addView(tvT);
			tr.addView(tvS);
			tr.addView(tvR);
			tr.addView(tvP);
			exerciciosList.configuraExercicio(tvT, tvS, tvR, tvP);
			tlInner.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

		}

	}
	private void copiaConfiguracao(TextView tvSource, TextView tvDest){
		((MarginLayoutParams) tvDest.getLayoutParams()).setMargins(1, 1, 1, 1);
		((MarginLayoutParams) tvDest.getLayoutParams()).setMargins(
				((MarginLayoutParams) tvSource.getLayoutParams()).leftMargin, 
				((MarginLayoutParams) tvSource.getLayoutParams()).topMargin, 
				((MarginLayoutParams) tvSource.getLayoutParams()).rightMargin, 
				((MarginLayoutParams) tvSource.getLayoutParams()).bottomMargin);		

		tvDest.setTextSize(tvSource.getTextSize()); 		
		//		tvDest.setBackgroundColor(((PaintDrawable)tvSource.getBackground()).getPaint().getColor());    	
		tvDest.setWidth(tvSource.getWidth());
		tvDest.setGravity(tvSource.getGravity());
	}
	public String getNumSeries(){
		return "0";
	}
	public String getNumRepeticoes(){
		return "0";
	}
	@Override
	public boolean onTouchEvent(MotionEvent touchevent) {
		return gesturedetector.onTouchEvent(touchevent);
	}
	public boolean onDown(MotionEvent e) {
		return false;
	}
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		return false;
	}
	public void onLongPress(MotionEvent e) {

	}
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		return false;
	}
	public void onShowPress(MotionEvent e) {

	}
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public void atualizaDisplay(TextView tvDisplay, String valor){
		dismissPopupHandler.adqSemaph();
		tvDisplay.setText(valor);		
		dismissPopupHandler.sleep();
		dismissPopupHandler.releaseSemaph();
	}
	private void mostrarDialogo(){
		final int tipoValor = IncValor.tipoValor(mLongClickTextView);
		final Dialog dialog = new Dialog(this);
		if (tipoValor==1)
			dialog.setContentView(R.layout.dialog_contagem_picker_timer);
		else
			dialog.setContentView(R.layout.dialog_contagem_picker);

		final NumberPicker np1 = ((NumberPicker) dialog.findViewById(R.id.np_contagem01));
		final NumberPicker np2 = ((NumberPicker) dialog.findViewById(R.id.np_contagem02));
		String valor=mLongClickTextView.getText().toString();
		((Button) dialog.findViewById(R.id.ok_button_popup)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (tipoValor == 1){
					int valorHora = np1.getCurrent();
					int valorMinuto = np2.getCurrent();
					mLongClickTextView.setText(String.format("%02d", valorHora) + ":" + String.format("%02d", valorMinuto));
				}
				else{
					mLongClickTextView.setText("" + np1.getCurrent() + "");

				}

				dialog.dismiss();//encerra o dialog
			}
		});

		((Button) dialog.findViewById(R.id.cancel_button_popup)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				mLongClickTextView.setText(valorAnteriorBoxCont);
				dialog.dismiss();

			}
		});
		if(valor.length() == 5 && valor.charAt(2)==':'){
			String ct[]=valor.split(":");
			int valorHora;
			int valorMinuto;
			try 
			{ 
				valorHora = Integer.parseInt(ct[0]);
				valorMinuto = Integer.parseInt(ct[1]);
			} 
			catch( Exception e) 
			{ 
				valorHora = 0; 
				valorMinuto = 0; 
			}

			np1.setCurrent(valorHora);
			np2.setCurrent(valorMinuto);
		}
		else{
			int valorNumero;
			try 
			{ 
				valorNumero = Integer.parseInt(valor);
			} 
			catch( Exception e) 
			{ 
				valorNumero = 0; 
			}
			np1.setCurrent(valorNumero);			
		}

		dialog.show();//mostra o dialog		
	}

	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (EstaComOnlongClick){
				if (mLongClickTextView.isPressed()){
					IncValor.incremetarValor(mLongClickTextView, true);
					dismissPopupHandler.adqSemaph();
					pwFloatingContagem.atualizaCampo(mLongClickTextView.getText().toString());
					dismissPopupHandler.sleep();
					dismissPopupHandler.releaseSemaph();
				}
				else{
					EstaComOnlongClick = false;
					pwFloatingContagem.dismiss();
					/*					
					if (IncValor.tipoValor(mLongClickTextView)!=2)
						mostrarDialogo();
					 */
				}
			}
			sleep();
		}

		public void sleep() {
			if (EstaComOnlongClick){
				this.removeMessages(0);
				sendMessageDelayed(obtainMessage(0), 500);	    		
			}
		}

	};
	class DismissPopupHandler extends Handler {
		private final Semaphore available = new Semaphore(1, true);
		private boolean adiar=false;
		private int procSleep=0;

		@Override
		public void handleMessage(Message msg) {
			adqSemaph();
//			procSleep--;
			if (adiar){
				sleep();
				adiar=false;
			}
			else if (procSleep == 0){
				pwFloatingContagem.dismiss();
				tvFocused = null;
			}
			releaseSemaph();
		}

		public void sleep() {
//			if (procSleep == 0){
//				procSleep++;
				this.removeMessages(0);
				sendMessageDelayed(obtainMessage(0), 1500);
	//		}
		}

		public void adqSemaph(){
			try{
				available.acquire();				
			}
			catch (InterruptedException e){

			}
		}

		public void releaseSemaph(){
			available.release();			
		}

		public void setaAdiamento(){
			try{
				available.acquire();				
			}
			catch (InterruptedException e){

			}
			adiar = true;
			available.release();			
		}

	};

}
