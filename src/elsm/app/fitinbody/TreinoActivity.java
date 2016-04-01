package elsm.app.fitinbody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;  
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TableLayout;
import android.content.Context;
import android.database.Cursor;

public class TreinoActivity extends BaseTreinoActivity {
	private long mIdTreino;
	private long mIdPrograma;
	private String AtividadesPrograma[];
	private String AtividadesTreino[];

	private int numExercMusculacao;
	final static int ADD_EXERCICIO=1;

	@Override  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.treino);

		((TextView) findViewById(R.id.num_treino)).setOnClickListener(mTreinoTextListener);
		carregarTreino();

		((Button) findViewById(R.id.button_adc_exerc)).setOnClickListener(new View.OnClickListener() {  
			public void onClick(View v) {
				adicionarExercicios();            	
			}  
		});

		final Button button = (Button) findViewById(R.id.ok_button_treino);  
		button.setOnClickListener(new View.OnClickListener() {  
			public void onClick(View v) {
				retornarTreino();            	
			}  
		});


	}

	private OnClickListener mTreinoTextListener = new OnClickListener() {  
		public void onClick(View v) {
			TextView tv = (TextView) v;
			tv.setText(IncValor.incrementaLetra(tv.getText().toString().trim()));

			String ValorTreino = tv.getText().toString().trim();
			String ValorTreinoProx = IncValor.incrementaLetra(ValorTreino);
			numExercMusculacao=0;
			copiaTreino(tv.getText().toString().trim());            
			while (numExercMusculacao == 0 && !ValorTreinoProx.equals(ValorTreino)){
				copiaTreino(ValorTreinoProx);            
				tv.setText(ValorTreinoProx);
				ValorTreinoProx=IncValor.incrementaLetra(ValorTreinoProx);
			}

			carregarExerciciosTreino(tv.getText().toString().trim());            

		}  
	};

	private void carregarTreino(){
		Intent treino=getIntent();
		mIdPrograma = treino.getLongExtra(ProgramasProvider.Treinos.PROGRAMA_ID, 0);
		if (mIdPrograma==0){
			mIdPrograma = getProgramaCorrente();
			mDataPicket = IncValor.DataAtual();		
		}
		else{
			mDataPicket = IncValor.StringPraData(treino.getStringExtra(ProgramasProvider.Treinos.DT_TREINO));
		}
		AtividadesPrograma = new String[12];
		Cursor prog = this.getContentResolver().query(ProgramasProvider.Programas.CONTENT_URI, null,
				ProgramasProvider.Programas.PROGRAMA_ID + '=' + mIdPrograma,        		                                      
				null, null);  

		if (!prog.moveToNext()) return;



		AtividadesPrograma[0] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.AEROBICO));
		AtividadesPrograma[1] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.PEITORAIS));
		AtividadesPrograma[2] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.COSTAS));
		AtividadesPrograma[3] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.OMBROS));
		AtividadesPrograma[4] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.TRICEPS));
		AtividadesPrograma[5] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.BICEPS));    
		AtividadesPrograma[6] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.ANT_COXA));
		AtividadesPrograma[7] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.POST_COXA));
		AtividadesPrograma[8] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.GLUTEOS));
		AtividadesPrograma[9] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.ADUTORES));
		AtividadesPrograma[10] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.PANTURRILHA));
		AtividadesPrograma[11] =prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.ABDOMINAL));        


		AtividadesTreino = new String[12];

		SimpleDateFormat dformat = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = dformat.format(mDataPicket);       
		((TextView) findViewById(R.id.box_data_treino)).setText(formattedDate);

		Calendar c = Calendar.getInstance();
		c.setTime(mDataPicket);
		((TextView) findViewById(R.id.box_dia_sem_treino)).setText(IncValor.mnmDiaSemana(c.get(Calendar.DAY_OF_WEEK)));

		Cursor treinoCursor = this.getContentResolver().query(ProgramasProvider.Treinos.CONTENT_URI, null,
				ProgramasProvider.Treinos.PROGRAMA_ID + " = ? AND " +
						ProgramasProvider.Treinos.DT_TREINO + " = ? ",         		                                      
						new String[] {mIdPrograma+"", formattedDate}, null);

		if (treinoCursor.moveToNext()){
			mIdTreino = treinoCursor.getLong(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Treinos.TREINO_ID));					

			((TextView) findViewById(R.id.box_num_sessao_treino)).setText(treinoCursor.getInt(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Treinos.NUM_SESSAO))+"");
			((TextView) findViewById(R.id.num_treino)).setText(treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Treinos.NUM_TREINO)));

			AtividadesTreino[0] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.AEROBICO));
			AtividadesTreino[1] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.PEITORAIS));
			AtividadesTreino[2] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.COSTAS));
			AtividadesTreino[3] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.OMBROS));
			AtividadesTreino[4] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.TRICEPS));
			AtividadesTreino[5] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.BICEPS));    
			AtividadesTreino[6] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.ANT_COXA));
			AtividadesTreino[7] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.POST_COXA));
			AtividadesTreino[8] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.GLUTEOS));
			AtividadesTreino[9] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.ADUTORES));
			AtividadesTreino[10] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.PANTURRILHA));
			AtividadesTreino[11] =treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.ABDOMINAL));        

			carregarExerciciosTreino(((TextView) findViewById(R.id.num_treino)).getText().toString());

		}
		else{
			mIdTreino = 0;
			treinoCursor = this.getContentResolver().query(ProgramasProvider.Treinos.CONTENT_URI, new String[] {"MAX(DT_TREINO) AS max_data_treino"},
					ProgramasProvider.Treinos.PROGRAMA_ID + " = ? AND " +
							ProgramasProvider.Treinos.DT_TREINO + " < ? ",         		                                      
							new String[] {mIdPrograma+"", formattedDate}, null);

			if (treinoCursor.moveToNext()){
				String dateTreino = treinoCursor.getString(treinoCursor.getColumnIndexOrThrow("max_data_treino"));
				if (dateTreino!=null){
					treinoCursor = this.getContentResolver().query(ProgramasProvider.Treinos.CONTENT_URI, null,
							ProgramasProvider.Treinos.PROGRAMA_ID + " = ? AND " +
									ProgramasProvider.Treinos.DT_TREINO + " = ? ",         		                                      
									new String[] {mIdPrograma+"", dateTreino}, null);
					if (treinoCursor.moveToNext()){
						((TextView) findViewById(R.id.box_num_sessao_treino)).setText((treinoCursor.getInt(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Treinos.NUM_SESSAO))+1)+"");
						((TextView) findViewById(R.id.num_treino)).setText(treinoCursor.getString(treinoCursor.getColumnIndexOrThrow(ProgramasProvider.Treinos.NUM_TREINO)));

					}

				}

				else{
					((TextView) findViewById(R.id.box_num_sessao_treino)).setText("1");
					((TextView) findViewById(R.id.num_treino)).setText("");				
				}

			}
			else{
				((TextView) findViewById(R.id.box_num_sessao_treino)).setText("1");
				((TextView) findViewById(R.id.num_treino)).setText("");				
			}

			mTreinoTextListener.onClick((TextView) findViewById(R.id.num_treino));
		}

	}

	private void carregarExerciciosTreino(String numTreino){
		TableLayout tl = (TableLayout) findViewById(R.id.tabela_treino);
		tl.removeAllViews();
		for (int i=0;i<ProgramasProvider.Programas.exercProg.length;i++){
			criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_treino),
					new ExerciciosListMusculacaoPrograma(this, AtividadesTreino[i], i,numTreino));
		}

	};

	private void adicionarExercicios(){
		Intent intent = new Intent(this, AdicionarExercTreinoActivity.class);
		intent.putExtra(ProgramasProvider.Programas.exercProg[0], copiaTreinoAerobico("", AtividadesTreino[0], "00:00"));		
		for (int i=1;i<AtividadesTreino.length;i++){
			intent.putExtra(ProgramasProvider.Programas.exercProg[i], AtividadesTreino[i]);
		}

		startActivityForResult(intent, ADD_EXERCICIO);		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK){
			switch (requestCode){
			case ADD_EXERCICIO:
				for (int i=0;i<AtividadesTreino.length;i++){
					AtividadesTreino[i] = data.getStringExtra(ProgramasProvider.Programas.exercProg[i]);
				}
				carregarExerciciosTreino(((TextView) findViewById(R.id.num_treino)).getText().toString().trim());            
				break;
			}
		}

	}

	private void retornarTreino(){
		Intent result = new Intent();

		ContentValues values = new ContentValues();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		values.put(ProgramasProvider.Treinos.PROGRAMA_ID, mIdPrograma);
		values.put(ProgramasProvider.Treinos.DT_TREINO, dateFormat.format(mDataPicket));
		values.put(ProgramasProvider.Treinos.NUM_SESSAO, Integer.parseInt(((TextView) findViewById(R.id.box_num_sessao_treino)).getText().toString()));  
		values.put(ProgramasProvider.Treinos.NUM_TREINO, ((TextView) findViewById(R.id.num_treino)).getText().toString());
		//		values.put(ProgramasProvider.Treinos.OBSERVACAO, data.getStringExtra(ProgramasProvider.Treinos.OBSERVACAO));  

		TableLayout tl = (TableLayout) findViewById(R.id.tabela_treino);
		TextView tvT, tvS, tvR, tvP;

		Cursor cursorEx = this.getContentResolver().query(ProgramasProvider.Exercicios.CONTENT_URI, null,null, null, null);
		String progExercicio="";
		String numTreino = ((TextView) findViewById(R.id.num_treino)).getText().toString();
		int numExercicio=-1;
		int tipoExercAnt=-1;
		while (cursorEx.moveToNext()){
			if (tipoExercAnt != cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.TIPO_EXERCICIO))){
				if (!progExercicio.equals("")){
					values.put(ProgramasProvider.Programas.exercProg[numExercicio], progExercicio);
				}    		   
				progExercicio = "";
				numExercicio++;
			}
			if (!progExercicio.equals("")){
				progExercicio+=";";
			}
			tvT = ((TextView) tl.findViewWithTag("T"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID)))));
			if (tvT != null){
				tvS = ((TextView) tl.findViewWithTag("S"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID)))));
				tvR = ((TextView) tl.findViewWithTag("R"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID)))));
				tvP = ((TextView) tl.findViewWithTag("P"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID)))));
				progExercicio += numTreino+";";
				progExercicio += tvS.getText().toString()+";";
				progExercicio += tvR.getText().toString()+";";
				progExercicio += tvP.getText().toString();
			}
			else{
				progExercicio += " ; ; ; ";
			}
			tipoExercAnt = cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.TIPO_EXERCICIO));
		}
		values.put(ProgramasProvider.Programas.exercProg[numExercicio], progExercicio);
		if (mIdTreino==0){
			getContentResolver().insert(  
					ProgramasProvider.Treinos.CONTENT_URI, values);  
		}
		else{
			values.put(ProgramasProvider.Treinos.TREINO_ID, mIdTreino);
			getContentResolver().update(  
					ProgramasProvider.Treinos.CONTENT_URI, values,
					ProgramasProvider.Treinos.TREINO_ID + '=' + mIdTreino + " AND " +
							ProgramasProvider.Treinos.PROGRAMA_ID + '=' + mIdPrograma,
							null);
		}
		result.putExtra(ProgramasProvider.Treinos.DT_TREINO, dateFormat.format(mDataPicket));
		this.setResult(Activity.RESULT_OK, result);
		this.finish();
	}
	private void copiaTreino(String numTreino){
		AtividadesTreino[0] = copiaTreinoAerobico(numTreino, AtividadesPrograma[0]);

		for (int i=1;i<AtividadesPrograma.length;i++){
			AtividadesTreino[i] = copiaTreinoMusculacao(numTreino, AtividadesPrograma[i]);
		}

	}

	private String copiaTreinoAerobico(String numTreino, String atividadesGrupoTreino){
		String linha="";
		String exercicio[] = atividadesGrupoTreino.split(";");
		int diaSemana = IncValor.numDiaSemana(((TextView) findViewById(R.id.box_dia_sem_treino)).getText().toString());
		if (diaSemana==1) diaSemana++; 
		for (int i=0;i<exercicio.length;i++){
			if (i%7 ==0){
				if (!linha.equals(""))
					linha+=";";

				if (!exercicio[i + diaSemana - 1].trim().equalsIgnoreCase("") && !exercicio[i + diaSemana - 1].trim().equalsIgnoreCase("00:00")){
					linha += numTreino + ";" + ";" + ";" +  exercicio[i + diaSemana - 1];
				}
				else{
					linha += " ; ; ; ";  				    				
				}

			}

		}

		return linha;
	}
	private String copiaTreinoAerobico(String numTreino, String atividadesGrupoTreino, String valorVazio){
		String linha="";
		String exercicio[] = atividadesGrupoTreino.split(";");
		for (int i=0;i<exercicio.length;i++){
			if (i%4 == 0){
				if (!linha.equals(""))
					linha+=" ;";
				linha += numTreino + ";" +
						exercicio[i + 1] + ";" +
						exercicio[i + 2] + ";";
				if (exercicio[i + 3].trim().equals(""))
					linha += valorVazio;
				else
					linha += exercicio[i + 3];
			}

		}
		return linha;
	}

	private String copiaTreinoMusculacao(String numTreino, String atividadesGrupoTreino){
		String linha="";
		String exercicio[] = atividadesGrupoTreino.split(";");
		for (int i=0;i<exercicio.length;i++){
			if (i%4 == 0){
				if (!linha.equals(""))
					linha+=" ;";
				if (exercicio[i].trim().equalsIgnoreCase(numTreino)){
					numExercMusculacao++;
					linha += numTreino + ";" +
							exercicio[i + 1] + ";" +
							exercicio[i + 2] + ";" +
							exercicio[i + 3];
				}
				else{
					linha += " ; ; ; ";		
				}
			}

		}
		return linha;
	}
	private int getProgramaCorrente(){
		int valorRetorno;
		Cursor programaCursor = this.getContentResolver().query(ProgramasProvider.Programas.CONTENT_URI, null,
				ProgramasProvider.Programas.DT_FIM + " is null ",         		                                      
				null, null);
		if (programaCursor.moveToNext()){
			valorRetorno = programaCursor.getInt(programaCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.PROGRAMA_ID));
		}
		else{
			valorRetorno = 0;
		}
		return valorRetorno;
	}

	private class ExerciciosListMusculacaoPrograma extends ExerciciosListMusculacao{
		private int numExercicio;
		private String exercicios[]=null;
		private int posicaoInicial = -1;
		private Cursor mCursor;
		private String numTreino;
		ExerciciosListMusculacaoPrograma(Context context, String AtividadesGrupoTreino, int numExercicio, String numTreino){
			if (AtividadesGrupoTreino!=null)
				this.exercicios = AtividadesGrupoTreino.split(";");
			this.numTreino=numTreino;
			this.numExercicio = numExercicio;
			mCursor = context.getContentResolver().query(ProgramasProvider.Exercicios.CONTENT_URI, null, 
					ProgramasProvider.Exercicios.TIPO_EXERCICIO + " = " + numExercicio, null, null);  

			Log.d("--------------->", AtividadesGrupoTreino);  

		}
		public boolean moveNext(){

			boolean resultd = mCursor.moveToNext();
			posicaoInicial++;
			if (resultd){
				resultd=exercicios[posicaoInicial*4].equals(numTreino);
				if (!resultd)
					resultd=moveNext();
			}
			return resultd;	

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
			if (exercicios==null || numExercicio==0)
				return " ";
			else
				return exercicios[posicaoInicial*4];
		}

		public String getExercicioSerie(){
			if (exercicios==null || numExercicio==0)
				return " ";
			else
				return exercicios[posicaoInicial*4 + 1];
		}
		public String getExercicioRepeticao(){
			if (exercicios==null || numExercicio==0)
				return " ";
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
			if (itemTreino.equals("T"))
				return false;

			if (itemTreino.equals("S"))				
				return numExercicio==0?false:true;

			if (itemTreino.equals("R"))
				return numExercicio==0?false:true;

			if (itemTreino.equals("P"))
				return true;

			return true;
		}

		@Override
		public void configuraExercicio(TextView tvD, TextView tvT, TextView tvS, TextView tvR, TextView tvP){
			super.configuraExercicio(tvD, tvT, tvS, tvR, tvP);
			tvP.setWidth(dipToPixel(tvP.getContext(),100)); 

		}

	}

}
