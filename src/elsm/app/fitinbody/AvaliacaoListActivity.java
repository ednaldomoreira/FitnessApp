package elsm.app.fitinbody;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class AvaliacaoListActivity extends ListActivity  {
    public static final int NOVA_AVALIACAO = 1;
    public static final int EDIT_AVALIACAO = 2;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    private Cursor mCursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliacoes);  
        mCursor = this.getContentResolver().query(ProgramasProvider.Avaliacoes.CONTENT_URI, null, null, null, null);  

        ListAdapter adapter = new SimpleCursorAdapter(  
                              // O primeiro parametro eh o context.  
                              this,  
                              // O segundo, o layout de cada item.  
                              R.layout.list_item_aval,  
                              // O terceiro parametro eh o cursor que contem os dados  
                              // a serem mostrados  
                              mCursor,  
                              // o quarto parametro eh um array com as colunas do  
                              // cursor que serao mostradas  
                              new String[] {ProgramasProvider.Avaliacoes.DT_AVALIACAO},  
                              // o quinto parametro eh um array (com o mesmo  
                              // tamanho do anterior) com os elementos que  
                              // receberao os dados.  
                              new int[] {R.id.text_aval_data});  

        ((Button) findViewById(R.id.insert_button_aval)).setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {
            	addAvaliacao();
            }  
        });

        setListAdapter(adapter);  
        registerForContextMenu(getListView());
    	
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 2, "Apagar Avaliação");
    }
    

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info;

    	switch(item.getItemId()) {
            case DELETE_ID:
                info = (AdapterContextMenuInfo) item.getMenuInfo();
                getContentResolver().delete(  
                        ProgramasProvider.Avaliacoes.CONTENT_URI, 
                        ProgramasProvider.Avaliacoes.AVALIACAO_ID + '=' + info.id,
                        null);  
                return true;
        }
        return super.onContextItemSelected(item);
    }
    /*  
     * Método responsável por inserir um registro no content provider  
     */  
    protected void addAvaliacao() {  
        Intent i = new Intent(this, AvaliacaoActivity.class);  
        i.putExtra("ACAO", 1);
        startActivityForResult(i, NOVA_AVALIACAO);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, AvaliacaoActivity.class);

        Cursor prog = this.getContentResolver().query(ProgramasProvider.Avaliacoes.CONTENT_URI, null,
        											  ProgramasProvider.Avaliacoes.AVALIACAO_ID + '=' + id,        		                                      
                                                      null, null);  
        prog.moveToFirst();
        startManagingCursor(prog);
        i.putExtra("ACAO", 2);

        i.putExtra(ProgramasProvider.Avaliacoes.AVALIACAO_ID, id);
	    i.putExtra(ProgramasProvider.Avaliacoes.DT_AVALIACAO, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.DT_AVALIACAO)));
        i.putExtra(ProgramasProvider.Avaliacoes.PESO, String.format("%2.2f", (double)prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.PESO))/100));  
        i.putExtra(ProgramasProvider.Avaliacoes.PERC_GORDURA, String.format("%2.2f", (double)prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.PERC_GORDURA))/100));

        i.putExtra(ProgramasProvider.Avaliacoes.PESCOCO, String.format("%2.2f", (double)prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.PESCOCO))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.BICEPS_DIREITO, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.BICEPS_DIREITO))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.BICEPS_ESQUERDO, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.BICEPS_ESQUERDO))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.ANTEBRACO_DIREITO, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.ANTEBRACO_DIREITO))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.ANTEBRACO_ESQUERDO, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.ANTEBRACO_ESQUERDO))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.PEITORAIS, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.PEITORAIS))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.CINTURA, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.CINTURA))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.QUADRIS, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.QUADRIS))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.COXA_DIREITA, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.COXA_DIREITA))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.COXA_ESQUERDA, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.COXA_ESQUERDA))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.PANTURRILHA_DIREITA, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.PANTURRILHA_DIREITA))/100));
        i.putExtra(ProgramasProvider.Avaliacoes.PANTURRILHA_ESQUERDA, String.format("%2.2f", (double) prog.getInt(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.PANTURRILHA_ESQUERDA))/100));    
        
        i.putExtra(ProgramasProvider.Avaliacoes.NOME_AVALIADOR, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.NOME_AVALIADOR)));        
        i.putExtra(ProgramasProvider.Avaliacoes.OBSERVACAO, prog.getString(prog.getColumnIndexOrThrow(ProgramasProvider.Avaliacoes.OBSERVACAO)));

        startActivityForResult(i, EDIT_AVALIACAO);
    }
    
    /** Called when an activity called by using startActivityForResult finishes. */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if (resultCode == RESULT_OK){
    	    switch (requestCode){
        	case NOVA_AVALIACAO:
                getContentResolver().insert(  
                     ProgramasProvider.Avaliacoes.CONTENT_URI, ContentAvaliacaoValues(data));  
        		break;
        	case EDIT_AVALIACAO:
                getContentResolver().update(  
                        ProgramasProvider.Avaliacoes.CONTENT_URI, ContentAvaliacaoValues(data),
                        ProgramasProvider.Avaliacoes.AVALIACAO_ID + '=' + data.getLongExtra(ProgramasProvider.Avaliacoes.AVALIACAO_ID, 0), 
                        null);  
        		break;
        	}
    	    mCursor.requery();
    	}
    }
    private static ContentValues ContentAvaliacaoValues(Intent data){
        ContentValues values = new ContentValues();

        values.put(ProgramasProvider.Avaliacoes.DT_AVALIACAO, data.getStringExtra(ProgramasProvider.Avaliacoes.DT_AVALIACAO));
        values.put(ProgramasProvider.Avaliacoes.PESO, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.PESO))*100);
        values.put(ProgramasProvider.Avaliacoes.PERC_GORDURA, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.PERC_GORDURA))*100);
        values.put(ProgramasProvider.Avaliacoes.PESCOCO, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.PESCOCO))*100);
        values.put(ProgramasProvider.Avaliacoes.BICEPS_DIREITO, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.BICEPS_DIREITO))*100);
        values.put(ProgramasProvider.Avaliacoes.BICEPS_ESQUERDO, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.BICEPS_ESQUERDO))*100);
        values.put(ProgramasProvider.Avaliacoes.ANTEBRACO_DIREITO, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.ANTEBRACO_DIREITO))*100);
        values.put(ProgramasProvider.Avaliacoes.ANTEBRACO_ESQUERDO, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.ANTEBRACO_ESQUERDO))*100);
        values.put(ProgramasProvider.Avaliacoes.PEITORAIS, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.PEITORAIS))*100);
        values.put(ProgramasProvider.Avaliacoes.CINTURA, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.CINTURA))*100);
        values.put(ProgramasProvider.Avaliacoes.QUADRIS, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.QUADRIS))*100);
        values.put(ProgramasProvider.Avaliacoes.COXA_DIREITA, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.COXA_DIREITA))*100);
        values.put(ProgramasProvider.Avaliacoes.COXA_ESQUERDA, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.COXA_ESQUERDA))*100);
        values.put(ProgramasProvider.Avaliacoes.PANTURRILHA_DIREITA, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.PANTURRILHA_DIREITA))*100);
        values.put(ProgramasProvider.Avaliacoes.PANTURRILHA_ESQUERDA, (int) Double.parseDouble(data.getStringExtra(ProgramasProvider.Avaliacoes.PANTURRILHA_ESQUERDA))*100);    
        values.put(ProgramasProvider.Avaliacoes.OBSERVACAO, data.getStringExtra(ProgramasProvider.Avaliacoes.OBSERVACAO));  
    	return values;
    }

}
