package elsm.app.fitinbody;

import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.View;
import elsm.comp.picker.*;

public class PopupWindowContagem extends PopupWindow {
	private int tipo;
	public TextView mContagemDisplay;
	public TextView tvDisplay;
	private NumberPicker np1;
	private NumberPicker np2;
	public BaseTreinoActivity baseTreinoActivity;	
	public PopupWindowContagem(View contentView, int width, int height){
		super(contentView, width, height);
		mContagemDisplay = (TextView) getContentView().findViewById(R.id.text_contagem);
		np1 = ((NumberPicker) getContentView().findViewById(R.id.np_contagem01));
		np2 = ((NumberPicker) getContentView().findViewById(R.id.np_contagem02));

		if (np1 != null){
			np1.setOnChangeListener(new NumberPicker.OnChangedListener() {
				
				public void onChanged(NumberPicker picker, int oldVal, int newVal) {
					atualizaDisplay();
					
				}
			});
		}
		
		if (np2 != null){
			np2.setOnChangeListener(new NumberPicker.OnChangedListener() {
				
				public void onChanged(NumberPicker picker, int oldVal, int newVal) {
					atualizaDisplay();
					
				}
			});
		}
		
	}
	public PopupWindowContagem(View contentView){
		super(contentView);
		mContagemDisplay = (TextView) getContentView().findViewById(R.id.text_contagem);
		np1 = ((NumberPicker) getContentView().findViewById(R.id.np_contagem01));
		np2 = ((NumberPicker) getContentView().findViewById(R.id.np_contagem02));

		if (np1 != null){
			np1.setOnChangeListener(new NumberPicker.OnChangedListener() {
				
				public void onChanged(NumberPicker picker, int oldVal, int newVal) {
					atualizaDisplay();
					
				}
			});
		}
		
		if (np2 != null){
			np2.setOnChangeListener(new NumberPicker.OnChangedListener() {
				
				public void onChanged(NumberPicker picker, int oldVal, int newVal) {
					atualizaDisplay();
					
				}
			});
		}
		
	}
	
	public void showAsDropDown (View anchor, int xoff, int yoff, int tipo, String valor){
		atualizaCampo(valor);
		super.showAsDropDown(anchor, xoff, yoff);
		this.tipo = tipo;
	}
	public void atualizaCampo(String valor){
		if(valor.length() == 5 && valor.charAt(2)==':'){
			String ct[]=valor.split(":");
			int valorHora;
			int valorMinuto;
			try 
			{ 
				valorHora = Integer.parseInt(ct[0]);
				valorMinuto = Integer.parseInt(ct[1]);
			} 
			catch( Exception e) 
			{ 
				valorHora = 0; 
				valorMinuto = 0; 
			}
			
			np1.setCurrent(valorHora);
			np2.setCurrent(valorMinuto);
		}
		else if (this.tipo == 2){
			mContagemDisplay.setText(valor);
		}
		else if (this.tipo >1){
			int valorNumero;
			try 
			{ 
				valorNumero = Integer.parseInt(valor);
			} 
			catch( Exception e) 
			{ 
				valorNumero = 0; 
			}
			np1.setCurrent(valorNumero);			
		}
			
	}
	private void atualizaDisplay(){
		final int tipoValor = IncValor.tipoValor(tvDisplay);
		String valorAtualizar;  
		if (tipoValor == 1){
			int valorHora = np1.getCurrent();
			int valorMinuto = np2.getCurrent();
			valorAtualizar = String.format("%02d", valorHora) + ":" + String.format("%02d", valorMinuto);
		}
		else{
			valorAtualizar = "" + np1.getCurrent() + "";

		}
		baseTreinoActivity.atualizaDisplay(tvDisplay, valorAtualizar);
		
	}
	public int getAltura(){
		if (np1 != null)
			return np1.getHeight();
		else
			return mContagemDisplay.getHeight();
	}
	

}
