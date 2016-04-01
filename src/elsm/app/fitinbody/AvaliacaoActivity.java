package elsm.app.fitinbody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import elsm.app.fitinbody.ProgramasProvider.Avaliacoes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class AvaliacaoActivity extends BaseTreinoActivity {
	private long mIdAvaliacao;
	static final int DATE_DIALOG_ID = 0;
	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.avaliacao);
        mDateDisplay = (TextView)findViewById(R.id.box_data_avaliacao);

		preencherAvaliacao(getIntent());

		((TextView)findViewById(R.id.box_data_avaliacao)).setOnClickListener(mDatePickListener);

		((Button) findViewById(R.id.ok_button_aval)).setOnClickListener(new View.OnClickListener() {  
			public void onClick(View v) {
				retornarAvaliacao();
			}  
		});

	}
	private void preencherAvaliacao(Intent avaliacao){
		if (avaliacao.getIntExtra("ACAO", 0) ==1){
			Calendar c = Calendar.getInstance();
			mDataPicket = new Date(c.get(Calendar.YEAR) - 1900,
					c.get(Calendar.MONTH),
					c.get(Calendar.DATE));

		}
		else{
			mIdAvaliacao = avaliacao.getLongExtra(ProgramasProvider.Avaliacoes.AVALIACAO_ID, 0);
			mDataPicket = IncValor.StringPraData(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.DT_AVALIACAO));
			((TextView) findViewById(R.id.box_peso_aval)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.PESO));
			((TextView) findViewById(R.id.box_perc_gord)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.PERC_GORDURA));
			((TextView) findViewById(R.id.box_med_pescoco)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.PESCOCO));
			((TextView) findViewById(R.id.box_med_biceps_dir)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.BICEPS_DIREITO));
			((TextView) findViewById(R.id.box_med_biceps_esq)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.BICEPS_ESQUERDO));
			((TextView) findViewById(R.id.box_med_antebraco_dir)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.ANTEBRACO_DIREITO));
			((TextView) findViewById(R.id.box_med_antebraco_esq)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.ANTEBRACO_ESQUERDO));
			((TextView) findViewById(R.id.box_med_peitoral)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.PEITORAIS));
			((TextView) findViewById(R.id.box_med_cintura)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.CINTURA));
			((TextView) findViewById(R.id.box_med_quadril)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.QUADRIS));
			((TextView) findViewById(R.id.box_med_coxa_dir)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.COXA_DIREITA));
			((TextView) findViewById(R.id.box_med_coxa_esq)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.COXA_ESQUERDA));
			((TextView) findViewById(R.id.box_med_panturrilha_dir)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.PANTURRILHA_DIREITA));
			((TextView) findViewById(R.id.box_med_panturrilha_esq)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.PANTURRILHA_ESQUERDA));  

			((TextView)findViewById(R.id.box_obs_aval)).setText(avaliacao.getStringExtra(ProgramasProvider.Avaliacoes.OBSERVACAO));
		}
		updateDisplay();        	

	}

	private void retornarAvaliacao(){
		Intent result = new Intent();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		result.putExtra(ProgramasProvider.Avaliacoes.AVALIACAO_ID, mIdAvaliacao);
		result.putExtra(ProgramasProvider.Avaliacoes.DT_AVALIACAO, dateFormat.format(mDataPicket));
		
		String peso = ((TextView) findViewById(R.id.box_peso_aval)).getText().toString().trim();					
		String perc_gordura = ((TextView)  findViewById(R.id.box_perc_gord)).getText().toString().trim();
		String med_pescoco = ((TextView)  findViewById(R.id.box_med_pescoco)).getText().toString().trim();
		String med_biceps_dir = ((TextView)  findViewById(R.id.box_med_biceps_dir)).getText().toString().trim();
		String med_biceps_esq = ((TextView)  findViewById(R.id.box_med_biceps_esq)).getText().toString().trim();
		String med_antebraco_dir = ((TextView)  findViewById(R.id.box_med_antebraco_dir)).getText().toString().trim();
		String med_antebraco_esq = ((TextView)  findViewById(R.id.box_med_antebraco_esq)).getText().toString().trim();
		String med_peitoirais = ((TextView)  findViewById(R.id.box_med_peitoral)).getText().toString().trim();
		String med_cintura = ((TextView)  findViewById(R.id.box_med_cintura)).getText().toString().trim();
		String med_quadril = ((TextView)  findViewById(R.id.box_med_quadril)).getText().toString().trim();
		String med_coxa_dir = ((TextView)  findViewById(R.id.box_med_coxa_dir)).getText().toString().trim();
		String med_coxa_esq = ((TextView)  findViewById(R.id.box_med_coxa_esq)).getText().toString().trim();
		String med_panturrilha_dir = ((TextView)  findViewById(R.id.box_med_panturrilha_dir)).getText().toString().trim();
		String med_panturrilha_esq = ((TextView)  findViewById(R.id.box_med_panturrilha_esq)).getText().toString().trim();
		
		result.putExtra(ProgramasProvider.Avaliacoes.PESO, (peso.equals("")?"0":peso));					
		result.putExtra(ProgramasProvider.Avaliacoes.PERC_GORDURA, (perc_gordura.equals("")?"0":perc_gordura));
		result.putExtra(ProgramasProvider.Avaliacoes.PESCOCO, (med_pescoco.equals("")?"0":med_pescoco));
		result.putExtra(ProgramasProvider.Avaliacoes.BICEPS_DIREITO, (med_biceps_dir.equals("")?"0":med_biceps_dir));
		result.putExtra(ProgramasProvider.Avaliacoes.BICEPS_ESQUERDO, (med_biceps_esq.equals("")?"0":med_biceps_esq));
		result.putExtra(ProgramasProvider.Avaliacoes.ANTEBRACO_DIREITO, ( med_antebraco_dir.equals("")?"0": med_antebraco_dir));
		result.putExtra(ProgramasProvider.Avaliacoes.ANTEBRACO_ESQUERDO, (med_antebraco_esq.equals("")?"0":med_antebraco_esq));
		result.putExtra(ProgramasProvider.Avaliacoes.PEITORAIS, (med_peitoirais.equals("")?"0":med_peitoirais));
		result.putExtra(ProgramasProvider.Avaliacoes.CINTURA, (med_cintura.equals("")?"0":med_cintura));
		result.putExtra(ProgramasProvider.Avaliacoes.QUADRIS, (med_quadril.equals("")?"0":med_quadril));
		result.putExtra(ProgramasProvider.Avaliacoes.COXA_DIREITA, (med_coxa_dir.equals("")?"0":med_coxa_dir));
		result.putExtra(ProgramasProvider.Avaliacoes.COXA_ESQUERDA, (med_coxa_esq.equals("")?"0":med_coxa_esq));
		result.putExtra(ProgramasProvider.Avaliacoes.PANTURRILHA_DIREITA, (med_panturrilha_dir.equals("")?"0":med_panturrilha_dir));
		result.putExtra(ProgramasProvider.Avaliacoes.PANTURRILHA_ESQUERDA, (med_panturrilha_esq.equals("")?"0":med_panturrilha_esq));
		
		result.putExtra(ProgramasProvider.Avaliacoes.OBSERVACAO, ((TextView) findViewById(R.id.box_obs_aval)).getText());

		this.setResult(Activity.RESULT_OK, result);
		this.finish();

	}
}
