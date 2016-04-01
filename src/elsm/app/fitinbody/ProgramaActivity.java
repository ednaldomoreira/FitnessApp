package elsm.app.fitinbody;


import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Math;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.ViewFlipper;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ProgramaActivity extends BaseTreinoActivity{  

	private long mIdPrograma;
	private Date mDataInicio;
	private Date mDataFim=null;

	/** 
	 * @see android.app.Activity#onCreate(Bundle) 
	 */  
	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.programa);
		preencherPrograma(getIntent());

		iniciarEventosTreino();

		final Button buttonAnt = (Button) findViewById(R.id.ant_button);
		buttonAnt.setOnClickListener(new View.OnClickListener() {  
			public void onClick(View v) {
				AtividadeAnterior((ViewFlipper)findViewById(R.id.ViewFlipper01));
			}  
		});

		final Button button = (Button) findViewById(R.id.ok_button);  
		button.setOnClickListener(new View.OnClickListener() {  
			public void onClick(View v) {
				retornarPrograma();            	
				finish();

			}  
		});

		final Button buttonProx = (Button) findViewById(R.id.prox_button);  
		buttonProx.setOnClickListener(new View.OnClickListener() {  
			public void onClick(View v) {
				ProximaAtividade((ViewFlipper)findViewById(R.id.ViewFlipper01));
			}  
		});


	}

	private void iniciarEventosTreino(){
		ExerciciosList.configuraTVQuadAtivData(((TextView) findViewById(R.id.box_data_inicial)));
		ExerciciosList.configuraTVQuadAtivData(((TextView) findViewById(R.id.box_data_final)));
		ExerciciosList.configuraTVQuadAtiv(((TextView) findViewById(R.id.num_sessoes)));
		ExerciciosList.configuraTVQuadAtiv(((TextView) findViewById(R.id.box_num_series_geral)));
		ExerciciosList.configuraTVQuadAtiv(((TextView) findViewById(R.id.box_num_repet_geral)));

		((TextView) findViewById(R.id.box_data_inicial)).setOnClickListener(mDatePickListener);
		((TextView) findViewById(R.id.box_data_final)).setOnClickListener(mDatePickListener);

		((TextView) findViewById(R.id.num_sessoes)).setTag("R0000000000");
		((TextView) findViewById(R.id.num_sessoes)).setOnClickListener(mExercicioClickListener);			          
		((TextView) findViewById(R.id.num_sessoes)).setOnLongClickListener(mExercicioLongClickListener);

		((TextView) findViewById(R.id.box_num_series_geral)).setTag("S0000000000");
		((TextView) findViewById(R.id.box_num_series_geral)).setOnClickListener(mExercicioClickListener);			          
		((TextView) findViewById(R.id.box_num_series_geral)).setOnLongClickListener(mExercicioLongClickListener);

		((TextView) findViewById(R.id.box_num_repet_geral)).setTag("R0000000000");
		((TextView) findViewById(R.id.box_num_repet_geral)).setOnClickListener(mExercicioClickListener);			          
		((TextView) findViewById(R.id.box_num_repet_geral)).setOnLongClickListener(mExercicioLongClickListener);

		((View) findViewById(R.id.checkBoxFreqDOM)).setOnClickListener(mFrequenciaClickListener);			          
		((View) findViewById(R.id.checkBoxFreqSEG)).setOnClickListener(mFrequenciaClickListener);			          
		((View) findViewById(R.id.checkBoxFreqTER)).setOnClickListener(mFrequenciaClickListener);			          
		((View) findViewById(R.id.checkBoxFreqQUA)).setOnClickListener(mFrequenciaClickListener);			          
		((View) findViewById(R.id.checkBoxFreqQUI)).setOnClickListener(mFrequenciaClickListener);			          
		((View) findViewById(R.id.checkBoxFreqSEX)).setOnClickListener(mFrequenciaClickListener);			          
		((View) findViewById(R.id.checkBoxFreqSAB)).setOnClickListener(mFrequenciaClickListener);		          

	}

	@Override
	protected void OnClickDatePick(View v){
		if (v.getId() == R.id.box_data_inicial){
			mDateDisplay = (TextView)findViewById(R.id.box_data_inicial);
			mDataPicket = mDataInicio;
		}
		else{
			mDateDisplay = (TextView)findViewById(R.id.box_data_final);
			mDataPicket = mDataFim;
		}

		super.OnClickDatePick(v);

	}
	@Override
	protected void OnSetDatePick(DatePicker view, int year, 
			int monthOfYear, int dayOfMonth){
		super.OnSetDatePick(view, year, monthOfYear, dayOfMonth);

		if (mDateDisplay.getId() == R.id.box_data_inicial){
			mDataInicio = mDataPicket;
		}
		else{
			mDataFim = mDataPicket;
		}



	}
	protected OnClickListener mFrequenciaClickListener = new OnClickListener() {  
		public void onClick(View v) {
			CheckBox tv = (CheckBox) v;
			HabilitarDiasExercAerobico(tv.getText().toString(), tv.isChecked());

		}

	};
	private void HabilitarDiasExercAerobico(String dia, Boolean habilitar){
		TableLayout tlA = (TableLayout) findViewById(R.id.tabela_prog_aerobico);		
		for (int i=0; i<tlA.getChildCount();i++){
			for (int j=0;j<((ViewGroup)tlA.getChildAt(i)).getChildCount();j++){
				if (((ViewGroup)tlA.getChildAt(i)).getChildAt(j).getTag() != null && ((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).getTag().toString().equals(dia)){
					((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setVisibility(habilitar?View.VISIBLE:View.INVISIBLE);
					if (!habilitar){
						((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setWidth(0);
						((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setTextSize(0);
						((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setText("");
					}
					else{
						ExerciciosList.configuraTVQuadroCabecLarg(((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)));
						((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setText(dia);
					}


				}

				else if (((ViewGroup)tlA.getChildAt(i)).getChildAt(j).getTag() != null && ((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).getTag().toString().substring(0, 3).equals(dia)){
					((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setVisibility(habilitar?View.VISIBLE:View.INVISIBLE);   				
					if (!habilitar){
						((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setWidth(0);
						((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setTextSize(0);
						((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setText("");
					}
					else{
						ExerciciosList.configuraTVQuadAtiv(((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)));
						((TextView)((ViewGroup)tlA.getChildAt(i)).getChildAt(j)).setText("00:00");
					}

				}
			}
		}
	}

	private void preencherPrograma(Intent programa){
		mDateDisplay = (TextView)findViewById(R.id.box_data_inicial);
		if (programa.getIntExtra("ACAO", 0) ==1){
			Calendar c = Calendar.getInstance();
			mDataInicio = new Date(c.get(Calendar.YEAR) - 1900,
					c.get(Calendar.MONTH),
					c.get(Calendar.DATE));
			mDataPicket = mDataInicio;
			((CheckBox) findViewById(R.id.checkBoxFreqDOM)).setChecked(false);
			((CheckBox) findViewById(R.id.checkBoxFreqSEG)).setChecked(true);			          
			((CheckBox) findViewById(R.id.checkBoxFreqTER)).setChecked(true);			          
			((CheckBox) findViewById(R.id.checkBoxFreqQUA)).setChecked(true);			          
			((CheckBox) findViewById(R.id.checkBoxFreqQUI)).setChecked(true);			          
			((CheckBox) findViewById(R.id.checkBoxFreqSEX)).setChecked(true);			          
			((CheckBox) findViewById(R.id.checkBoxFreqSAB)).setChecked(true);		          

			updateDisplay();
		}
		else{

			mIdPrograma = programa.getLongExtra(ProgramasProvider.Programas.PROGRAMA_ID, 0);
			mDataInicio = IncValor.StringPraData(programa.getStringExtra(ProgramasProvider.Programas.DT_INICIO));
			mDataPicket = mDataInicio;

			updateDisplay();

			mDataFim = IncValor.StringPraData(programa.getStringExtra(ProgramasProvider.Programas.DT_FIM));
			if (mDataFim != null){
				mDataPicket = mDataFim;
				mDateDisplay = (TextView)findViewById(R.id.box_data_final);
				updateDisplay();   	   		   
			}

			((TextView)findViewById(R.id.num_sessoes)).setText(programa.getStringExtra(ProgramasProvider.Programas.NUM_DIAS));
			((TextView)findViewById(R.id.box_num_series_geral)).setText(programa.getStringExtra(ProgramasProvider.Programas.NUM_SERIES));   		
			((TextView)findViewById(R.id.box_num_repet_geral)).setText(programa.getStringExtra(ProgramasProvider.Programas.NUM_REPETICOES));

			String[] frequencia = programa.getStringExtra(ProgramasProvider.Programas.FREQUENCIA).split(";");

			((CheckBox) findViewById(R.id.checkBoxFreqDOM)).setChecked(frequencia[0].equals("S"));
			((CheckBox) findViewById(R.id.checkBoxFreqSEG)).setChecked(frequencia[1].equals("S"));			          
			((CheckBox) findViewById(R.id.checkBoxFreqTER)).setChecked(frequencia[2].equals("S"));			          
			((CheckBox) findViewById(R.id.checkBoxFreqQUA)).setChecked(frequencia[3].equals("S"));			          
			((CheckBox) findViewById(R.id.checkBoxFreqQUI)).setChecked(frequencia[4].equals("S"));			          
			((CheckBox) findViewById(R.id.checkBoxFreqSEX)).setChecked(frequencia[5].equals("S"));			          
			((CheckBox) findViewById(R.id.checkBoxFreqSAB)).setChecked(frequencia[6].equals("S"));		          

			((EditText)findViewById(R.id.box_obs)).setText(programa.getStringExtra(ProgramasProvider.Programas.OBSERVACAO));

		}

		criarTabelaGrupoExercicioAerobico(this, (TableLayout) findViewById(R.id.tabela_prog_aerobico),
				new ExerciciosListAerobicoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.AEROBICO), ExercicioListActivity.EXERCICIO_AEROBICO));
/*
		TableLayout tlTeste = (TableLayout) findViewById(R.id.tabela_prog_aerobico);
		for(int i=0;i<tlTeste.getChildCount();i++)					
			for(int j=0;j < ((android.view.ViewGroup) tlTeste.getChildAt(i)).getChildCount();j++){
				Log.d("----->", ((TextView)((android.view.ViewGroup) tlTeste.getChildAt(i)).getChildAt(j)).getText().toString());
			}
*/			
		Log.d("<----->", "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
/*
		if (!((CheckBox) findViewById(R.id.checkBoxFreqDOM)).isChecked())
			HabilitarDiasExercAerobico("DOM", ((CheckBox) findViewById(R.id.checkBoxFreqDOM)).isChecked());
		if (!((CheckBox) findViewById(R.id.checkBoxFreqSEG)).isChecked())
			HabilitarDiasExercAerobico("SEG", ((CheckBox) findViewById(R.id.checkBoxFreqSEG)).isChecked());			          
		if (!((CheckBox) findViewById(R.id.checkBoxFreqTER)).isChecked())
			HabilitarDiasExercAerobico("TER", ((CheckBox) findViewById(R.id.checkBoxFreqTER)).isChecked());			          
		if (!((CheckBox) findViewById(R.id.checkBoxFreqQUA)).isChecked())
			HabilitarDiasExercAerobico("QUA", ((CheckBox) findViewById(R.id.checkBoxFreqQUA)).isChecked());			          
		if (!((CheckBox) findViewById(R.id.checkBoxFreqQUI)).isChecked())
			HabilitarDiasExercAerobico("QUI", ((CheckBox) findViewById(R.id.checkBoxFreqQUI)).isChecked());			          
		if (!((CheckBox) findViewById(R.id.checkBoxFreqSEX)).isChecked())
			HabilitarDiasExercAerobico("SEX", ((CheckBox) findViewById(R.id.checkBoxFreqSEX)).isChecked());			          
		if (!((CheckBox) findViewById(R.id.checkBoxFreqSAB)).isChecked())
			HabilitarDiasExercAerobico("SAB", ((CheckBox) findViewById(R.id.checkBoxFreqSAB)).isChecked());		          
*/
/*
		for(int i=0;i<tlTeste.getChildCount();i++)					
			for(int j=0;j < ((android.view.ViewGroup) tlTeste.getChildAt(i)).getChildCount();j++){
				Log.d("----->", ((TextView)((android.view.ViewGroup) tlTeste.getChildAt(i)).getChildAt(j)).getText().toString());
			}
*/			
		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_peitorias),
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.PEITORAIS), ExercicioListActivity.EXERCICIO_PEITORAIS));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_costas),		    
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.COSTAS), ExercicioListActivity.EXERCICIO_COSTAS));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_ombros),
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.OMBROS), ExercicioListActivity.EXERCICIO_OMBROS));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_triceps),
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.TRICEPS), ExercicioListActivity.EXERCICIO_TRICEPS));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_biceps),       		   
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.BICEPS), ExercicioListActivity.EXERCICIO_BICEPS));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_ant_coxa),		    
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.ANT_COXA), ExercicioListActivity.EXERCICIO_ANT_COXA));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_post_coxa),
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.POST_COXA), ExercicioListActivity.EXERCICIO_POST_COXA));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_gluteos),
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.GLUTEOS), ExercicioListActivity.EXERCICIO_GLUTEOS));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_adutores),
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.ADUTORES), ExercicioListActivity.EXERCICIO_ADUTORES));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_panturrilha),
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.PANTURRILHA), ExercicioListActivity.EXERCICIO_PANTURRILHA));

		criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_abdominal),
				new ExerciciosListMusculacaoPrograma(this, programa.getStringExtra(ProgramasProvider.Programas.ABDOMINAL), ExercicioListActivity.EXERCICIO_ABDOMINAL));

	}

	private boolean validarPrograma() {
		return true;
	}

	private void retornarPrograma(){

		Intent result = new Intent();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		result.putExtra(ProgramasProvider.Programas.PROGRAMA_ID, mIdPrograma);
		result.putExtra(ProgramasProvider.Programas.DT_INICIO, dateFormat.format(mDataInicio));
		if (mDataFim!=null)
			result.putExtra(ProgramasProvider.Programas.DT_FIM, dateFormat.format(mDataFim)); 
		else
			result.putExtra(ProgramasProvider.Programas.DT_FIM, mDataFim);

		result.putExtra(ProgramasProvider.Programas.NUM_DIAS, ((TextView)findViewById(R.id.num_sessoes)).getText().toString());
		result.putExtra(ProgramasProvider.Programas.NUM_SERIES, ((TextView)findViewById(R.id.box_num_series_geral)).getText().toString());  
		result.putExtra(ProgramasProvider.Programas.NUM_REPETICOES, ((TextView)findViewById(R.id.box_num_repet_geral)).getText().toString());
		result.putExtra(ProgramasProvider.Programas.FREQUENCIA, 
				(((CheckBox)findViewById(R.id.checkBoxFreqDOM)).isChecked()?"S":"N") + ";" +
						(((CheckBox)findViewById(R.id.checkBoxFreqSEG)).isChecked()?"S":"N") + ";" +
						(((CheckBox)findViewById(R.id.checkBoxFreqTER)).isChecked()?"S":"N") + ";" +
						(((CheckBox)findViewById(R.id.checkBoxFreqQUA)).isChecked()?"S":"N") + ";" +
						(((CheckBox)findViewById(R.id.checkBoxFreqQUI)).isChecked()?"S":"N") + ";" +
						(((CheckBox)findViewById(R.id.checkBoxFreqSEX)).isChecked()?"S":"N") + ";" +
						(((CheckBox)findViewById(R.id.checkBoxFreqSAB)).isChecked()?"S":"N"));
		result.putExtra(ProgramasProvider.Programas.OBSERVACAO, ((EditText)findViewById(R.id.box_obs)).getText().toString());
		Cursor cursorEx = this.getContentResolver().query(ProgramasProvider.Exercicios.CONTENT_URI, null,null, null, null);
		String progExercicio="";
		int numExercicio=-1;
		int tipoExercAnt=-1;
		TableLayout tlExercicios[] = new TableLayout[]{ (TableLayout) findViewById(R.id.tabela_prog_aerobico),
				(TableLayout) findViewById(R.id.tabela_prog_peitorias),
				(TableLayout) findViewById(R.id.tabela_prog_costas),		    
				(TableLayout) findViewById(R.id.tabela_prog_ombros),
				(TableLayout) findViewById(R.id.tabela_prog_triceps),
				(TableLayout) findViewById(R.id.tabela_prog_biceps),       		   
				(TableLayout) findViewById(R.id.tabela_prog_ant_coxa),		    
				(TableLayout) findViewById(R.id.tabela_prog_post_coxa),
				(TableLayout) findViewById(R.id.tabela_prog_gluteos),
				(TableLayout) findViewById(R.id.tabela_prog_adutores),
				(TableLayout) findViewById(R.id.tabela_prog_panturrilha),
				(TableLayout) findViewById(R.id.tabela_prog_abdominal)
		};

		while (cursorEx.moveToNext()){
			if (tipoExercAnt != cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.TIPO_EXERCICIO))){
				if (!progExercicio.equals("")){
					result.putExtra(ProgramasProvider.Programas.exercProg[numExercicio], progExercicio);
				}    		   
				progExercicio = "";
				numExercicio++;
			}
			if (!progExercicio.equals("")){
				progExercicio+=";";
			}
			if (numExercicio==0){
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("DOM"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("SEG"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("TER"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("QUA"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("QUI"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("SEX"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("SAB"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString();
			}
			else{
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("T"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("S"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("R"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
				progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("P"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString();
			}
			tipoExercAnt = cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.TIPO_EXERCICIO));
		}
		result.putExtra(ProgramasProvider.Programas.exercProg[numExercicio], progExercicio);

		this.setResult(Activity.RESULT_OK, result);
		this.finish();

	}
	@Override
	public String getNumSeries(){
		return ((TextView)findViewById(R.id.box_num_series_geral)).getText().toString();
	}
	@Override
	public String getNumRepeticoes(){
		return ((TextView)findViewById(R.id.box_num_repet_geral)).getText().toString();
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		float ev1X = e1.getX();
		float ev2X = e2.getX();

		final float xdistance = Math.abs(ev1X - ev2X);
		final float xvelocity = Math.abs(velocityX);

		//Vận tốc chuyển đổi X > 100 và khoảng cách từ điểm kéo đầu đến điểm kéo cuối > 100
		if( (xvelocity > SWIPE_MIN_VELOCITY) && (xdistance > SWIPE_MIN_DISTANCE) )
		{
			if(ev1X > ev2X)//Switch Left
			{
				ProximaAtividade((ViewFlipper)findViewById(R.id.ViewFlipper01));
			}
			else//Switch Right
			{
				AtividadeAnterior((ViewFlipper)findViewById(R.id.ViewFlipper01));
			}
		}

		return false;
	}

	public static class ExerciciosListMusculacaoPrograma extends ExerciciosListMusculacao{
		private int numExercicio;
		private String exercicios[]=null;
		private int posicaoInicial = -1;
		private Cursor mCursor;
		ExerciciosListMusculacaoPrograma(Context context, String AtividadesGrupoTreino, int numExercicio){
			if (AtividadesGrupoTreino!=null)
				this.exercicios = AtividadesGrupoTreino.split(";");
			this.numExercicio = numExercicio;
			mCursor = context.getContentResolver().query(ProgramasProvider.Exercicios.CONTENT_URI, null, 
					ProgramasProvider.Exercicios.TIPO_EXERCICIO + " = " +
							numExercicio, null, null);  


		}
		public boolean moveNext(){
			if (posicaoInicial > 0){
				posicaoInicial++;				
				return mCursor.moveToNext();
			}
			else{
				posicaoInicial++;				
				return mCursor.moveToNext();
			}
		}

		public String getNomeGrupoTreino(){
			return ExercicioListActivity.EXERCICIOS[numExercicio];
		}
		public int getCodigoExercicio(){
			return mCursor.getInt(mCursor.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID));			
		}
		public String getNomeExercicio(){
			return mCursor.getString(mCursor.getColumnIndexOrThrow(ProgramasProvider.Exercicios.NOME_EXERCICIO));			
		}
		public String getExercicioTreino(){
			if (exercicios==null)
				return "";
			else
				return exercicios[posicaoInicial*4];
		}

		public String getExercicioSerie(){
			if (exercicios==null)
				return "0";
			else
				return exercicios[posicaoInicial*4 + 1];
		}
		public String getExercicioRepeticao(){
			if (exercicios==null)
				return "0";
			else
				return exercicios[posicaoInicial*4 + 2];
		}
		public String getExercicioPeso(){
			if (exercicios==null)
				return "0";
			else
				return exercicios[posicaoInicial*4 + 3];
		}
		public boolean getEvento(String itemTreino){
			return true;
		}

	}
	private class ExerciciosListAerobicoPrograma extends ExerciciosListAerobico{
		private int numExercicio;
		private String exercicios[]=null;
		private int posicaoInicial = -1;
		private Cursor mCursor;
		ExerciciosListAerobicoPrograma(Context context, String AtividadesGrupoTreino, int numExercicio){
			if (AtividadesGrupoTreino!=null)
				this.exercicios = AtividadesGrupoTreino.split(";");
			this.numExercicio = numExercicio;
			mCursor = context.getContentResolver().query(ProgramasProvider.Exercicios.CONTENT_URI, null, 
					ProgramasProvider.Exercicios.TIPO_EXERCICIO + " = " +
							numExercicio, null, null);  
		}

		public boolean moveNext(){
			if (posicaoInicial > 0){
				posicaoInicial++;				
				return mCursor.moveToNext();
			}
			else{
				posicaoInicial++;				
				return mCursor.moveToNext();
			}
		}

		public String getNomeGrupoTreino(){
			return ExercicioListActivity.EXERCICIOS[numExercicio];
		}
		public int getCodigoExercicio(){
			return mCursor.getInt(mCursor.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID));			
		}
		public String getNomeExercicio(){
			return mCursor.getString(mCursor.getColumnIndexOrThrow(ProgramasProvider.Exercicios.NOME_EXERCICIO));			
		}
		public String getExercicioTempoDOM(){
			if (exercicios==null)
				return "00:00";
			else
				return exercicios[posicaoInicial*7];
		}
		public String getExercicioTempoSEG(){
			if (exercicios==null)
				return "00:00";
			else
				return exercicios[posicaoInicial*7 + 1];
		}
		public String getExercicioTempoTER(){
			if (exercicios==null)
				return "00:00";
			else
				return exercicios[posicaoInicial*7 + 2];
		}
		public String getExercicioTempoQUA(){
			if (exercicios==null)
				return "00:00";
			else
				return exercicios[posicaoInicial*7 + 3];
		}
		public String getExercicioTempoQUI(){
			if (exercicios==null)
				return "00:00";
			else
				return exercicios[posicaoInicial*7 + 4];
		}
		public String getExercicioTempoSEX(){
			if (exercicios==null)
				return "00:00";
			else
				return exercicios[posicaoInicial*7 + 5];
		}
		public String getExercicioTempoSAB(){
			if (exercicios==null)
				return "00:00";
			else
				return exercicios[posicaoInicial*7 + 6];
		}
		public boolean getEvento(String itemTreino){
			return true;
		}

	}
}  