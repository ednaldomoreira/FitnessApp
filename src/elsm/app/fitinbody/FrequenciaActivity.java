package elsm.app.fitinbody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class FrequenciaActivity extends Activity {
	private static final int DELETE_ID = Menu.FIRST + 1;
	private Date dataAtual;
	private Date dataInicioTreino;
	private Date dataFinalTreino;
	private long mIdPrograma;
	private boolean comFaltas;
	private View viewContextMenu;
	@Override  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.frequencia);
		Intent programa=getIntent();
		dataAtual=IncValor.DataAtual();
		dataInicioTreino=IncValor.StringPraData(programa.getStringExtra(ProgramasProvider.Programas.DT_INICIO));
		dataFinalTreino=IncValor.StringPraData(programa.getStringExtra(ProgramasProvider.Programas.DT_FIM));
		comFaltas = programa.getIntExtra("EXIBE_FALTAS", 0) > 0;
		mIdPrograma = programa.getLongExtra(ProgramasProvider.Treinos.PROGRAMA_ID, 0);
		if (mIdPrograma==0){
			mIdPrograma = getProgramaCorrente(programa);
			dataInicioTreino=IncValor.StringPraData(programa.getStringExtra(ProgramasProvider.Programas.DT_INICIO));
			dataFinalTreino=IncValor.StringPraData(programa.getStringExtra(ProgramasProvider.Programas.DT_FIM));
		}
		
		montaFrequencia(mIdPrograma);

		((Button) findViewById(R.id.ok_mostrar_faltas_freq_btn)).setOnClickListener(new View.OnClickListener() {  
			public void onClick(View v) {
				comFaltas=!comFaltas;
				if (comFaltas){
					((Button)v).setText("Exibir sem Faltas");
				}
				else{
					((Button)v).setText("Exibir com Faltas");
				}
				montaFrequencia(mIdPrograma);
			}  
		});


	}

	private void montaFrequencia(long idPrograma){
		TableLayout tl = (TableLayout) findViewById(R.id.tabela_frequencia);
		TableRow trD, trT, trS;
		TextView tvD, tvT, tvS;
		Calendar c = Calendar.getInstance();
		Date dataTreino;
		int DiaTreino;
		boolean possuiDados;
		SimpleDateFormat dFormatMD = new SimpleDateFormat("MM-dd");    	
		SimpleDateFormat dformatAMD = new SimpleDateFormat("yyyy-MM-dd");
		Cursor treinos = this.getContentResolver().query(ProgramasProvider.Treinos.CONTENT_URI, null,
				ProgramasProvider.Treinos.PROGRAMA_ID + " = ? ",         		                                      
				new String[] {idPrograma+""}, ProgramasProvider.Treinos.DT_TREINO);
		possuiDados = treinos.moveToNext();

		//		treinos.getCount();

		tl.removeAllViews();
		int valMaxInteracao=3;
		boolean pulaSessao;
		Date dataSessaoAtual = (Date) dataInicioTreino.clone();

		for (int i=0;i<valMaxInteracao;i++){
			trS = new TableRow(this);
			trS.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			trD = new TableRow(this);
			trD.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			trT = new TableRow(this);
			trT.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			tvS = new TextView(this);
			tvS.setText("S");
			tvD = new TextView(this);
			tvD.setText("D");
			tvT = new TextView(this);
			tvT.setText("T");

			trS.addView(tvS);configuraTVQuadroCabec(tvS);
			trD.addView(tvD);configuraTVQuadroCabec(tvD);
			trT.addView(tvT);configuraTVQuadroCabec(tvT);
			((MarginLayoutParams) tvT.getLayoutParams()).setMargins(1, 1, 1, 20);
			pulaSessao=false;
			for (int j=1;j<11;j++){
				tvS = new TextView(this);
				tvD = new TextView(this);
				tvT = new TextView(this);
				if (possuiDados){
					dataTreino = IncValor.StringPraData(treinos.getString(treinos.getColumnIndexOrThrow(ProgramasProvider.Treinos.DT_TREINO)));
					pulaSessao = comFaltas && dataSessaoAtual.before(dataTreino);
					if (!pulaSessao){
						Log.d("--------------->", treinos.getString(treinos.getColumnIndexOrThrow(ProgramasProvider.Treinos.TREINO_ID)));  

						tvS.setText(treinos.getString(treinos.getColumnIndexOrThrow(ProgramasProvider.Treinos.NUM_SESSAO)));
						tvD.setText(dFormatMD.format(dataTreino));
						tvT.setText(treinos.getString(treinos.getColumnIndexOrThrow(ProgramasProvider.Treinos.NUM_TREINO)));
						tvT.setTag(idPrograma+";"+treinos.getInt(treinos.getColumnIndexOrThrow(ProgramasProvider.Treinos.TREINO_ID))+";"+dformatAMD.format(dataTreino));
						tvT.setOnClickListener(mTreinoTextListener);
						registerForContextMenu(tvT);
					}
					else{
						tvD.setText(dFormatMD.format(dataSessaoAtual));						
						tvT.setText("NC");
						tvT.setTag(idPrograma+";0;"+dformatAMD.format(dataSessaoAtual));
						tvT.setOnClickListener(mTreinoTextListener);
					}

				}
				else if (comFaltas && dataSessaoAtual.before(dataAtual)){
					tvD.setText(dFormatMD.format(dataSessaoAtual));						
					tvT.setText("NC");
					tvT.setTag(idPrograma+";0;"+dformatAMD.format(dataSessaoAtual));
					tvT.setOnClickListener(mTreinoTextListener);						
				}

				trS.addView(tvS);configuraTVQuadAtiv(tvS);
				trD.addView(tvD);configuraTVQuadAtiv(tvD);
				trT.addView(tvT);configuraTVQuadAtiv(tvT);

				dataSessaoAtual=IncValor.incDia(dataSessaoAtual, 1);

				if (!pulaSessao)
					possuiDados = treinos.moveToNext();

			}
			tl.addView(trS, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			tl.addView(trD, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			tl.addView(trT, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			if (possuiDados && valMaxInteracao-1==i)
				valMaxInteracao++;


		}


	}
/*
	private void configuraTV(TextView tv){
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		((MarginLayoutParams) tv.getLayoutParams()).setMargins(1, 1, 1, 1);
	}

	private void configuraTVDescCabec(TextView tv){
		configuraTV(tv);
		tv.setTextSize(20); // <item name="android:textSize">25px</item>
		tv.setBackgroundColor(Color.WHITE);    	
	}
*/
	
	private void configuraTVQuadroCabec(TextView tv){
		ExerciciosList.configuraTVDescCabec(tv);    	
		tv.setWidth(ExerciciosList.dipToPixel(tv.getContext(),30)); // <item name="android:width">30px</item>
		tv.setTextSize(25); // <item name="android:textSize">25px</item>
	}
	/*
	private void configuraTVDescAtiv(TextView tv){
		configuraTV(tv);
		tv.setBackgroundColor(Color.DKGRAY);    	
	}
*/
	private void configuraTVQuadAtiv(TextView tv){
		ExerciciosList.configuraTVDescAtiv(tv);    	
		tv.setWidth(ExerciciosList.dipToPixel(tv.getContext(),65)); // <item name="android:width">30px</item>
		tv.setTextSize(25); // <item name="android:textSize">25px</item>
	}
	private OnClickListener mTreinoTextListener = new OnClickListener() {  
		public void onClick(View v) {
			//            TextView tv = (TextView) v;
			String sTag = (String)v.getTag();
			String parTreino[] = sTag.split(";");
			editaTreino(parTreino[0], parTreino[1], parTreino[2]);
		}
	};

	private void editaTreino(String idPrograma, String idTreino, String dataTreino){
		Intent i = new Intent(this, TreinoActivity.class);
		i.putExtra(ProgramasProvider.Treinos.PROGRAMA_ID, mIdPrograma);
		i.putExtra(ProgramasProvider.Treinos.DT_TREINO, dataTreino);

		startActivityForResult(i, ProgramaListActivity.EDIT_TREINO);


	}
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK){
			switch (requestCode){
			case ProgramaListActivity.EDIT_TREINO:
				Date dataTreinoAtual=IncValor.StringPraData(data.getStringExtra(ProgramasProvider.Treinos.DT_TREINO));
				Date dataTreino=null;
				ContentValues values = new ContentValues();
				Cursor treinos = this.getContentResolver().query(ProgramasProvider.Treinos.CONTENT_URI, null,
						ProgramasProvider.Treinos.PROGRAMA_ID + " = ? AND " +
								ProgramasProvider.Treinos.DT_TREINO + " > ? ", 
								new String[] {mIdPrograma+"", data.getStringExtra(ProgramasProvider.Treinos.DT_TREINO)}, 
								ProgramasProvider.Treinos.DT_TREINO);
				int NumSessao[] = new int[treinos.getCount()];
				long IdsTreino[] = new long[treinos.getCount()];
				int contaReg=0;
				while(treinos.moveToNext()){
					NumSessao[contaReg] = treinos.getInt(treinos.getColumnIndexOrThrow(ProgramasProvider.Treinos.NUM_SESSAO)) + 1;
					IdsTreino[contaReg] = treinos.getLong(treinos.getColumnIndexOrThrow(ProgramasProvider.Treinos.TREINO_ID));
					contaReg++;
				}
				treinos.close();
				for (int i=0;i<NumSessao.length;i++){
					values.put(ProgramasProvider.Treinos.NUM_SESSAO, NumSessao[i]);

					getContentResolver().update(  
							ProgramasProvider.Treinos.CONTENT_URI, values ,
							ProgramasProvider.Treinos.TREINO_ID + '=' + IdsTreino[i] + " AND " +
									ProgramasProvider.Treinos.PROGRAMA_ID + '=' + mIdPrograma,
									null);

				}
				montaFrequencia(mIdPrograma);

				break;
			}
		}
	}
	private int getProgramaCorrente(Intent programa){
		int valorRetorno;
		Cursor programaCursor = this.getContentResolver().query(ProgramasProvider.Programas.CONTENT_URI, null,
				ProgramasProvider.Programas.DT_FIM + " is null ",         		                                      
				null, null);
		if (programaCursor.moveToNext()){
			valorRetorno = programaCursor.getInt(programaCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.PROGRAMA_ID));
			programa.putExtra(ProgramasProvider.Programas.DT_INICIO, programaCursor.getString(programaCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.DT_INICIO)));
			programa.putExtra(ProgramasProvider.Programas.DT_FIM, programaCursor.getString(programaCursor.getColumnIndexOrThrow(ProgramasProvider.Programas.DT_FIM)));
		}
		else{
			valorRetorno = 0;
		}
		return valorRetorno;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		viewContextMenu=v;
		menu.add(0, DELETE_ID, 2, "Apagar Treino");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case DELETE_ID:
			String idTreino= (((String)(((TextView)viewContextMenu).getTag())).split(";"))[1];
			getContentResolver().delete(  
					ProgramasProvider.Treinos.CONTENT_URI, 
					ProgramasProvider.Treinos.TREINO_ID + '=' + idTreino,
					null);
			reconstroiNumSessoes();
			montaFrequencia(mIdPrograma);
			return true;
		}
		return super.onContextItemSelected(item);
	}
	private void reconstroiNumSessoes(){
		ContentValues values = new ContentValues();
		Cursor treinos = this.getContentResolver().query(ProgramasProvider.Treinos.CONTENT_URI, null,
				ProgramasProvider.Treinos.PROGRAMA_ID + " = ? ", 
						new String[] {mIdPrograma+""}, 
						ProgramasProvider.Treinos.DT_TREINO);
		int NumSessao[] = new int[treinos.getCount()];
		long IdsTreino[] = new long[treinos.getCount()];
		int contaReg=0;
		while(treinos.moveToNext()){
			NumSessao[contaReg] = treinos.getInt(treinos.getColumnIndexOrThrow(ProgramasProvider.Treinos.NUM_SESSAO)) + 1;
			IdsTreino[contaReg] = treinos.getLong(treinos.getColumnIndexOrThrow(ProgramasProvider.Treinos.TREINO_ID));
			contaReg++;
		}
		treinos.close();
		for (int i=0;i<NumSessao.length;i++){
			values.put(ProgramasProvider.Treinos.NUM_SESSAO, i+1);

			getContentResolver().update(  
					ProgramasProvider.Treinos.CONTENT_URI, values ,
					ProgramasProvider.Treinos.TREINO_ID + '=' + IdsTreino[i] + " AND " +
							ProgramasProvider.Treinos.PROGRAMA_ID + '=' + mIdPrograma,
							null);

		}
		
	}

}
