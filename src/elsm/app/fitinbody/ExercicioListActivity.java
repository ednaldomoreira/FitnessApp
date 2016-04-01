package elsm.app.fitinbody;

import java.io.BufferedReader;
import java.io.IOException;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.util.Log;

public class ExercicioListActivity extends ListActivity  {
    private Cursor mCursor;
    public static final int MUSCULO_TRAPEZIO = 1;
    public static final int MUSCULO_DELTOIDE = 2;
    public static final int MUSCULO_BICEPS = 3;    
    public static final int MUSCULO_TRICEPS = 4;
    public static final int MUSCULO_ANTEBRACO = 5;    
    public static final int MUSCULO_PEITORAL = 6;
    public static final int MUSCULO_ABDOMINAIS = 7;
    public static final int MUSCULO_ABDOMINAIS_OBLIQUOS = 8;
    public static final int MUSCULO_DORSAIS = 9;
    public static final int MUSCULO_LOMBARES = 10;    
    public static final int MUSCULO_GLUTEOS = 11;
    public static final int MUSCULO_ADUTORES = 12;
    public static final int MUSCULO_QUADRICEPS = 13;
    public static final int MUSCULO_ISQUIOTIBIAIS = 14;
    public static final int MUSCULO_PANTURRILHA = 15;
    
    public static final int EXERCICIO_AEROBICO = 0;
    public static final int EXERCICIO_PEITORAIS = 1;
    public static final int EXERCICIO_COSTAS = 2;
    public static final int EXERCICIO_OMBROS = 3;
    public static final int EXERCICIO_TRICEPS = 4;
    public static final int EXERCICIO_BICEPS = 5;    
    public static final int EXERCICIO_ANT_COXA = 6;
    public static final int EXERCICIO_POST_COXA = 7;
    public static final int EXERCICIO_GLUTEOS = 8;
    public static final int EXERCICIO_ADUTORES = 9;
    public static final int EXERCICIO_PANTURRILHA = 10;
    public static final int EXERCICIO_ABDOMINAL = 11;
    public static final int EXERCICIOS_CODS[] = new int[] {EXERCICIO_AEROBICO, EXERCICIO_PEITORAIS, EXERCICIO_COSTAS,
        												   EXERCICIO_OMBROS, EXERCICIO_TRICEPS, EXERCICIO_BICEPS,    
        												   EXERCICIO_ANT_COXA, EXERCICIO_POST_COXA, EXERCICIO_GLUTEOS,
        												   EXERCICIO_ADUTORES, EXERCICIO_PANTURRILHA, EXERCICIO_ABDOMINAL};  
    
    public static final String EXERCICIOS[] = new String[] {"Aeróbico", "Peitorais", "Costas", 
    														"Ombros", "Tríceps", "Bíceps", 
        													"Anterior Coxas","Posterior coxas", 
        													"Glúteos", "Adutores",
        													"Panturrilhas", "Abdominais"};  
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercicios);  
        mCursor = this.getContentResolver().query(ProgramasProvider.Exercicios.CONTENT_URI, null, 
                                                  ProgramasProvider.Exercicios.TIPO_EXERCICIO + " = " +
                                                  getIntent().getIntExtra("TIPO_EXERC", 0), null, null);  

        ListAdapter adapter = new SimpleCursorAdapter(  
                              // O primeiro parametro eh o context.  
                              this,  
                              // O segundo, o layout de cada item.  
                              R.layout.list_item_exerc,  
                              // O terceiro parametro eh o cursor que contem os dados  
                              // a serem mostrados  
                              mCursor,  
                              // o quarto parametro eh um array com as colunas do  
                              // cursor que serao mostradas  
                              new String[] {ProgramasProvider.Exercicios.NOME_EXERCICIO},  
                              // o quinto parametro eh um array (com o mesmo  
                              // tamanho do anterior) com os elementos que  
                              // receberao os dados.  
                              new int[] {R.id.text_exerc});  

        setListAdapter(adapter);  
        registerForContextMenu(getListView());
    	
    }

}
