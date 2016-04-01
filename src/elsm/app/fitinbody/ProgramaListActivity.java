package elsm.app.fitinbody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.ListActivity;  
import android.os.Bundle;  
import android.util.Log;  
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;  
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.ListAdapter;  
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.content.ContentValues;  
import android.content.Intent;  
import android.database.Cursor; 

public class ProgramaListActivity extends ListActivity {

	public static ProgramaListActivity programaListActivity=null;
	private static final String TAG = "ProgramasMainActivity";  
	public static final int NOVO_PROGRAMA = 1;
	public static final int EDIT_PROGRAMA = 2;
	public static final int EDIT_TREINO = 3;
	public static final int VISU_FREQUENCIA = 4;
	/*		
	public static final int EDIT_PROGRAMA_NOVO_TREINO = 3;
	public static final int EDIT_PROGRAMA_EDIT_TREINO = 4;
	public static final int EDIT_PROGRAMA_FREQUENCIA = 5;
	 */
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int TREINO_ID = Menu.FIRST + 2;
	private static final int FREQ_ID = Menu.FIRST + 3;
	private Cursor mCursor;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		ProgramaListActivity.programaListActivity = this;
		Log.d(TAG, "Criando a MainActivity");  

		setContentView(R.layout.programas);  

		Button insertButton = (Button)findViewById(R.id.insert_button);  
		insertButton.setOnClickListener(mInsertListener);

		String atributosPrograma[] = new String[] {ProgramasProvider.Programas.DT_INICIO, ProgramasProvider.Programas.DT_FIM, ProgramasProvider.Programas.PESO, ProgramasProvider.Programas.NUM_DIAS, "num_sessoes"};
		String atributosPrograma2[] = new String[] {
				ProgramasProvider.Programas.PROGRAMA_ID, 
				ProgramasProvider.Programas.DT_INICIO, 
				ProgramasProvider.Programas.DT_FIM, 
				ProgramasProvider.Programas.PESO, 
				ProgramasProvider.Programas.NUM_DIAS,
				"(SELECT max("+ ProgramasProvider.Treinos.NUM_SESSAO + ") " +
						" from treinos " +
						"WHERE programa_id=programas._id) as num_sessoes "};
		mCursor = this.getContentResolver().query(ProgramasProvider.Programas.CONTENT_URI, 
				atributosPrograma2, 
				null, null, null);  
		ListAdapter adapter = new SimpleCursorAdapter(  
				// O primeiro parametro eh o context.  
				this,  
				// O segundo, o layout de cada item.  
				R.layout.list_item,  
				// O terceiro parametro eh o cursor que contem os dados  
				// a serem mostrados  
				mCursor,  
				// o quarto parametro eh um array com as colunas do  
				// cursor que serao mostradas  
				atributosPrograma,  
				// o quinto parametro eh um array (com o mesmo  
				// tamanho do anterior) com os elementos que  
				// receberao os dados.  
				new int[] {R.id.text_data_inicial_prog, R.id.text_data_final_prog, R.id.text_peso_prog, R.id.text_sessoes_prog, R.id.text_num_sessoes_treino_prog});  


		setListAdapter(adapter);  
		registerForContextMenu(getListView());

	}

	/*  
	 *  Definindo um OnClickListener para o botão "Inserir"  
	 */  
	private OnClickListener mInsertListener = new OnClickListener() {  
		public void onClick(View v) {

			addPrograma();  
		}  
	};  

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, TREINO_ID, 0, "Treino");
		menu.add(0, FREQ_ID, 1, "Frequencia");
		menu.add(0, DELETE_ID, 2, "Apagar Atividade");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info;

		switch(item.getItemId()) {
		case DELETE_ID:
			info = (AdapterContextMenuInfo) item.getMenuInfo();

			getContentResolver().delete(  
					ProgramasProvider.Programas.CONTENT_URI, 
					ProgramasProvider.Programas.PROGRAMA_ID + '=' + info.id,
					null);  
			return true;
		case TREINO_ID:
			info = (AdapterContextMenuInfo) item.getMenuInfo();
			Intent i = new Intent(this, TreinoActivity.class);
			i.putExtra(ProgramasProvider.Treinos.PROGRAMA_ID, info.id);
			SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");			
			i.putExtra(ProgramasProvider.Treinos.DT_TREINO, dformat.format(IncValor.DataAtual()));

			startActivityForResult(i, EDIT_TREINO);

			return true;
		case FREQ_ID:
			info = (AdapterContextMenuInfo) item.getMenuInfo();
			Intent iFreq = new Intent(this, FrequenciaActivity.class);
			iFreq.putExtra(ProgramasProvider.Treinos.PROGRAMA_ID, info.id);

			Cursor progF = this.getContentResolver().query(ProgramasProvider.Programas.CONTENT_URI, null,
					ProgramasProvider.Programas.PROGRAMA_ID + '=' + info.id,        		                                      
					null, null);  

			progF.moveToFirst();

			startManagingCursor(progF);

			iFreq.putExtra(ProgramasProvider.Programas.DT_INICIO, progF.getString(progF.getColumnIndexOrThrow(ProgramasProvider.Programas.DT_INICIO)));
			iFreq.putExtra(ProgramasProvider.Programas.DT_FIM, progF.getString(progF.getColumnIndexOrThrow(ProgramasProvider.Programas.DT_FIM)));

			startActivityForResult(iFreq, VISU_FREQUENCIA);
			return true;
		}
		return super.onContextItemSelected(item);
	}
	/*  
	 * Método responsável por inserir um registro no content provider  
	 */  
	protected void addPrograma() {  
		Intent i = new Intent(this, ProgramaActivity.class);  
		i.putExtra("ACAO", 1);
		startActivityForResult(i, NOVO_PROGRAMA);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, ProgramaActivity.class);

		Cursor prog = this.getContentResolver().query(ProgramasProvider.Programas.CONTENT_URI, null,
				ProgramasProvider.Programas.PROGRAMA_ID + '=' + id,        		                                      
				null, null);  
		prog.moveToFirst();
		startManagingCursor(prog);
		i.putExtra("ACAO", 2);

		i.putExtra(ProgramasProvider.Programas.PROGRAMA_ID, id);
		i.putExtra(ProgramasProvider.Programas.DT_INICIO, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.DT_INICIO)));
		i.putExtra(ProgramasProvider.Programas.DT_FIM, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.DT_FIM)));
		i.putExtra(ProgramasProvider.Programas.NUM_DIAS, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.NUM_DIAS)));
		i.putExtra(ProgramasProvider.Programas.NUM_SERIES, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.NUM_SERIES)));
		i.putExtra(ProgramasProvider.Programas.NUM_REPETICOES, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.NUM_REPETICOES)));	
		i.putExtra(ProgramasProvider.Programas.FREQUENCIA, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.FREQUENCIA)));	
		i.putExtra(ProgramasProvider.Programas.PESO, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.PESO)));  
		i.putExtra(ProgramasProvider.Programas.OBJETIVO, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.OBJETIVO)));
		i.putExtra(ProgramasProvider.Programas.OBSERVACAO, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.OBSERVACAO)));
		i.putExtra(ProgramasProvider.Programas.ABDOMINAL, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.ABDOMINAL)));        
		i.putExtra(ProgramasProvider.Programas.AEROBICO, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.AEROBICO)));
		i.putExtra(ProgramasProvider.Programas.PEITORAIS, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.PEITORAIS)));
		i.putExtra(ProgramasProvider.Programas.COSTAS, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.COSTAS)));
		i.putExtra(ProgramasProvider.Programas.OMBROS, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.OMBROS)));
		i.putExtra(ProgramasProvider.Programas.TRICEPS, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.TRICEPS)));
		i.putExtra(ProgramasProvider.Programas.BICEPS, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.BICEPS)));    
		i.putExtra(ProgramasProvider.Programas.ANT_COXA, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.ANT_COXA)));
		i.putExtra(ProgramasProvider.Programas.POST_COXA, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.POST_COXA)));
		i.putExtra(ProgramasProvider.Programas.GLUTEOS, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.GLUTEOS)));
		i.putExtra(ProgramasProvider.Programas.ADUTORES, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.ADUTORES)));
		i.putExtra(ProgramasProvider.Programas.PANTURRILHA, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Programas.PANTURRILHA)));

		startActivityForResult(i, EDIT_PROGRAMA);
	}

	/** Called when an activity called by using startActivityForResult finishes. */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK){
			switch (requestCode){
			case NOVO_PROGRAMA:
				getContentResolver().insert(  
						ProgramasProvider.Programas.CONTENT_URI, ContentProgramaValues(data));  
				break;
			case EDIT_PROGRAMA:
				getContentResolver().update(  
						ProgramasProvider.Programas.CONTENT_URI, ContentProgramaValues(data),
						ProgramasProvider.Programas.PROGRAMA_ID + '=' + data.getLongExtra(ProgramasProvider.Programas.PROGRAMA_ID, 0), 
						null);  
				break;
			}
		}
		if (requestCode==EDIT_TREINO){
			mCursor.requery();
		}
		
		if (requestCode==VISU_FREQUENCIA){
			mCursor.requery();
		}
	}

	private static ContentValues ContentProgramaValues(Intent data){
		ContentValues values = new ContentValues();

		Log.d(TAG, data.getStringExtra(ProgramasProvider.Programas.DT_INICIO));  

		values.put(ProgramasProvider.Programas.DT_INICIO, data.getStringExtra(ProgramasProvider.Programas.DT_INICIO));
		values.put(ProgramasProvider.Programas.DT_FIM, data.getStringExtra(ProgramasProvider.Programas.DT_FIM));  
		values.put(ProgramasProvider.Programas.NUM_DIAS, data.getStringExtra(ProgramasProvider.Programas.NUM_DIAS));
		values.put(ProgramasProvider.Programas.NUM_SERIES, data.getStringExtra(ProgramasProvider.Programas.NUM_SERIES));
		values.put(ProgramasProvider.Programas.NUM_REPETICOES, data.getStringExtra(ProgramasProvider.Programas.NUM_REPETICOES));
		values.put(ProgramasProvider.Programas.FREQUENCIA, data.getStringExtra(ProgramasProvider.Programas.FREQUENCIA));		
		values.put(ProgramasProvider.Programas.PESO, data.getStringExtra(ProgramasProvider.Programas.PESO));
		values.put(ProgramasProvider.Programas.OBJETIVO, data.getStringExtra(ProgramasProvider.Programas.OBJETIVO));
		values.put(ProgramasProvider.Programas.OBSERVACAO, data.getStringExtra(ProgramasProvider.Programas.OBSERVACAO));  
		values.put(ProgramasProvider.Programas.ABDOMINAL, data.getStringExtra(ProgramasProvider.Programas.ABDOMINAL));  
		values.put(ProgramasProvider.Programas.AEROBICO, data.getStringExtra(ProgramasProvider.Programas.AEROBICO));
		values.put(ProgramasProvider.Programas.PEITORAIS, data.getStringExtra(ProgramasProvider.Programas.PEITORAIS));
		values.put(ProgramasProvider.Programas.COSTAS, data.getStringExtra(ProgramasProvider.Programas.COSTAS));
		values.put(ProgramasProvider.Programas.OMBROS, data.getStringExtra(ProgramasProvider.Programas.OMBROS));
		values.put(ProgramasProvider.Programas.TRICEPS, data.getStringExtra(ProgramasProvider.Programas.TRICEPS));
		values.put(ProgramasProvider.Programas.BICEPS, data.getStringExtra(ProgramasProvider.Programas.BICEPS));    
		values.put(ProgramasProvider.Programas.ANT_COXA, data.getStringExtra(ProgramasProvider.Programas.ANT_COXA));
		values.put(ProgramasProvider.Programas.POST_COXA, data.getStringExtra(ProgramasProvider.Programas.POST_COXA));
		values.put(ProgramasProvider.Programas.GLUTEOS, data.getStringExtra(ProgramasProvider.Programas.GLUTEOS));
		values.put(ProgramasProvider.Programas.ADUTORES, data.getStringExtra(ProgramasProvider.Programas.ADUTORES));
		values.put(ProgramasProvider.Programas.PANTURRILHA, data.getStringExtra(ProgramasProvider.Programas.PANTURRILHA));
		return values;
	}

}