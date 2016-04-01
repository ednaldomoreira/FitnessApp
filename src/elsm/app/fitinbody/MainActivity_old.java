package elsm.app.fitinbody;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity_old extends Activity {
    @Override  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main_old);
        
        Intent i = new Intent(this, WelcomeActivity.class);  
        startActivity(i);

        ((Button) findViewById(R.id.menu_programas_button)).setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {
            	iniciarActivityProgramas();
            }  
        });
        
        ((Button) findViewById(R.id.menu_avaliacoes_button)).setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {
            	iniciarActivityAvaliacao();
            }  
        });
        
        ((Button) findViewById(R.id.menu_exerc_aerobicos_button)).setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {
            	iniciarActivityExerciciosAerob();
            }  
        });
        
        ((Button) findViewById(R.id.menu_exerc_musculacao_button)).setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {
            	iniciarActivityExerciciosMusc();
            }  
        });
        
        ((Button) findViewById(R.id.menu_finalizar_button)).setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {
            	finish();
            }  
        });
    	
    }
    private void iniciarActivityProgramas(){
        Intent i = new Intent(this, ProgramaListActivity.class);  
        startActivity(i);
/*    	
        Intent i = new Intent(this, PickerSample.class);  
        startActivity(i);
*/
    	
    }
    private void iniciarActivityAvaliacao(){
        Intent i = new Intent(this, AvaliacaoListActivity.class);  
        startActivity(i);
    	
    }
    private void iniciarActivityExerciciosAerob(){
        Intent i = new Intent(this, ExercicioListActivity.class);
        i.putExtra("TIPO_EXERC", 0);        
        startActivity(i);
        
    }
    private void iniciarActivityExerciciosMusc(){
        Intent i = new Intent(this, ExercMuscActivity.class);
        startActivity(i);
        
    }

}
