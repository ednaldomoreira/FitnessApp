package elsm.app.fitinbody;

import java.lang.reflect.Constructor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class AdicionarExercTreinoActivity  extends BaseTreinoActivity {
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.treino_adicionar_exercicio);
        
        preencherPrograma(getIntent());
        
        final Button buttonAnt = (Button) findViewById(R.id.ant_button_at);
        buttonAnt.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {
        		AtividadeAnterior((ViewFlipper)findViewById(R.id.ViewFlipperAdcExerc));
            }  
        });
        
        final Button button = (Button) findViewById(R.id.ok_button_at);  
        button.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {
            			retornarPrograma();            	
            }  
        });
        
        final Button buttonProx = (Button) findViewById(R.id.prox_button_at);  
        buttonProx.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {
            	ProximaAtividade((ViewFlipper)findViewById(R.id.ViewFlipperAdcExerc));
            }  
        });
    }
    private void preencherPrograma(Intent programa){ 
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_aerobico),
    		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.AEROBICO), ExercicioListActivity.EXERCICIO_AEROBICO));

       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_peitorias),
    		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.PEITORAIS), ExercicioListActivity.EXERCICIO_PEITORAIS));

       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_costas),		    
    		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.COSTAS), ExercicioListActivity.EXERCICIO_COSTAS));
     
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_ombros),
    		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.OMBROS), ExercicioListActivity.EXERCICIO_OMBROS));
     
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_triceps),
    		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.TRICEPS), ExercicioListActivity.EXERCICIO_TRICEPS));
     
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_biceps),       		   
    		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.BICEPS), ExercicioListActivity.EXERCICIO_BICEPS));
     
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_ant_coxa),		    
  		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.ANT_COXA), ExercicioListActivity.EXERCICIO_ANT_COXA));
     
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_post_coxa),
  		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.POST_COXA), ExercicioListActivity.EXERCICIO_POST_COXA));
     
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_gluteos),
  		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.GLUTEOS), ExercicioListActivity.EXERCICIO_GLUTEOS));
     
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_adutores),
  		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.ADUTORES), ExercicioListActivity.EXERCICIO_ADUTORES));
     
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_panturrilha),
  		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.PANTURRILHA), ExercicioListActivity.EXERCICIO_PANTURRILHA));
     
       criarTabelaGrupoExercicioMuscul(this, (TableLayout) findViewById(R.id.tabela_prog_abdominal),
  		   new ExerciciosListMusculacaoAdcTR(this, programa.getStringExtra(ProgramasProvider.Programas.ABDOMINAL), ExercicioListActivity.EXERCICIO_ABDOMINAL));
	   
    }
    
    private boolean validarPrograma() {
    	return true;
    }
   
    private void retornarPrograma(){
    	
    	Intent result = new Intent();
	   
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
    		progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("T"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
    		progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("S"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
    		progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("R"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString()+";";
    		progExercicio += ((TextView) tlExercicios[numExercicio].findViewWithTag("P"+String.format("%10d", cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.EXERCICIO_ID))))).getText().toString();
    	   tipoExercAnt = cursorEx.getInt(cursorEx.getColumnIndexOrThrow(ProgramasProvider.Exercicios.TIPO_EXERCICIO));
    	}
		result.putExtra(ProgramasProvider.Programas.exercProg[numExercicio], progExercicio);
       
    	this.setResult(Activity.RESULT_OK, result);
    	this.finish();

    }
	private class ExerciciosListMusculacaoAdcTR extends ProgramaActivity.ExerciciosListMusculacaoPrograma{
		
		ExerciciosListMusculacaoAdcTR(Context context, String AtividadesGrupoTreino, int numExercicio){
			super(context, AtividadesGrupoTreino, numExercicio);
		}

		@Override
		public void configuraExercicio(TextView tvD, TextView tvT, TextView tvS, TextView tvR, TextView tvP){
			super.configuraExercicio(tvD, tvT, tvS, tvR, tvP);
			tvP.setWidth(dipToPixel(tvP.getContext(),100)); 

		}

	}

}
