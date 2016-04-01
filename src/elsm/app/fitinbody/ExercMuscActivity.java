package elsm.app.fitinbody;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ExercMuscActivity extends Activity{
	@Override  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.exercicios_musculacao);		

		((TextView)findViewById(R.id.tv_exerc_ombros)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_triceps)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_biceps)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_peitorais)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_costas)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_abdominais)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_panturrilhas)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_post_coxa)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_ant_coxa)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_adutores)).setOnClickListener(mInsertListener);
		((TextView)findViewById(R.id.tv_exerc_gluteos)).setOnClickListener(mInsertListener);

	}
	private OnClickListener mInsertListener = new OnClickListener() {  
		public void onClick(View v) {        	
			iniciarActivityExercicios(Integer.parseInt(v.getTag().toString()));  
		}  
	};  
	private void iniciarActivityExercicios(int tipo){
		Intent i = new Intent(this, ExercicioListActivity.class);
		i.putExtra("TIPO_EXERC", tipo);        
		startActivity(i);

	}

}
