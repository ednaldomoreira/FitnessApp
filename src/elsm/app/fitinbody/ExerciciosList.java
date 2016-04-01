package elsm.app.fitinbody;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import android.util.TypedValue;

public abstract class ExerciciosList {
	public abstract boolean moveNext();
	public abstract String getNomeGrupoTreino();	
	public abstract int getCodigoExercicio();
	public abstract String getNomeExercicio();
	public abstract boolean getEvento(String itemTreino);
	
	public void configuraCabecalhoGrupo(TextView tvD,  TextView tvT, TextView tvS, TextView tvR, TextView tvP){
        configuraTVDescCabec(tvD);
        configuraTVQuadroCabec(tvT);
        configuraTVQuadroCabec(tvS);
        configuraTVQuadroCabec(tvR);
        configuraTVQuadroCabec(tvP);
	}
	public void configuraCabecalhoGrupo(TextView tvD,  TextView tvSEG, TextView tvTER, TextView tvQUA, TextView tvQUI, TextView tvSEX, TextView tvSAB, TextView tvDOM){
        configuraTVDescCabec(tvD);
        configuraTVQuadroCabecLarg(tvSEG);
        configuraTVQuadroCabecLarg(tvTER);
        configuraTVQuadroCabecLarg(tvQUA);
        configuraTVQuadroCabecLarg(tvQUI);
        configuraTVQuadroCabecLarg(tvSEX);
        configuraTVQuadroCabecLarg(tvSAB);
        configuraTVQuadroCabecLarg(tvDOM);
	}
	
	public void configuraExercicio(TextView tvT, TextView tvS, TextView tvR, TextView tvP){
        configuraTVQuadAtiv(tvT);
        configuraTVQuadAtiv(tvS);
        configuraTVQuadAtiv(tvR);
        configuraTVQuadAtiv(tvP);
        tvT.setWidth(dipToPixel(tvT.getContext(),75)); 
        tvS.setWidth(dipToPixel(tvS.getContext(),75)); 
        tvR.setWidth(dipToPixel(tvR.getContext(),75)); 
        tvP.setWidth(dipToPixel(tvP.getContext(),75)); 
	}
	public void configuraExercicio(TextView tvD, TextView tvT, TextView tvS, TextView tvR, TextView tvP){
        configuraTV(tvD);
        configuraExercicio(tvT, tvS, tvR, tvP); 
	}
	public void configuraExercicio(TextView tvSEG, TextView tvTER, TextView tvQUA, TextView tvQUI, TextView tvSEX, TextView tvSAB, TextView tvDOM){
        configuraTVQuadAtiv(tvSEG);
		tvSEG.setWidth(dipToPixel(tvSEG.getContext(),100)); // <item name="android:width">30px</item>
        configuraTVQuadAtiv(tvTER);
		tvTER.setWidth(dipToPixel(tvTER.getContext(),100)); // <item name="android:width">30px</item>
        configuraTVQuadAtiv(tvQUA);
		tvQUA.setWidth(dipToPixel(tvQUA.getContext(),100)); // <item name="android:width">30px</item>
        configuraTVQuadAtiv(tvQUI);
		tvQUI.setWidth(dipToPixel(tvQUI.getContext(),100)); // <item name="android:width">30px</item>
        configuraTVQuadAtiv(tvSEX);
		tvSEX.setWidth(dipToPixel(tvSEX.getContext(),100)); // <item name="android:width">30px</item>
        configuraTVQuadAtiv(tvSAB);
		tvSAB.setWidth(dipToPixel(tvSAB.getContext(),100)); // <item name="android:width">30px</item>
        configuraTVQuadAtiv(tvDOM);
		tvDOM.setWidth(dipToPixel(tvDOM.getContext(),100)); // <item name="android:width">30px</item>
	}
	public void configuraExercicio(TextView tvD, TextView tvSEG, TextView tvTER, TextView tvQUA, TextView tvQUI, TextView tvSEX, TextView tvSAB, TextView tvDOM){
        configuraTV(tvD);
        configuraExercicio(tvSEG, tvTER, tvQUA, tvQUI, tvSEX, tvSAB, tvDOM);
	}
    public static void configuraTV(TextView tv){
		tv.setGravity(Gravity.CENTER_HORIZONTAL); // <item name="android:gravity">center_horizontal</item>
		((MarginLayoutParams) tv.getLayoutParams()).setMargins(1, 1, 1, 1);
//		tv.set <item name="android:textStyle">bold</item>
    	
    }
    
    public static void configuraTVDescCabec(TextView tv){
    	configuraTV(tv);
		tv.setTextSize(dipToPixel(tv.getContext(),15)); // <item name="android:textSize">25px</item>
/*
		tv.setBackgroundColor(Color.WHITE);
		tv.setTextColor(Color.argb(125, 105, 89, 205));
*/		
    }
    
    public static void configuraTVQuadroCabec(TextView tv){
    	configuraTVDescCabec(tv);    	
		tv.setWidth(dipToPixel(tv.getContext(),30)); // <item name="android:width">30px</item>
    }
    public static void configuraTVQuadroCabecLarg(TextView tv){
    	configuraTVDescCabec(tv);    	
		tv.setWidth(dipToPixel(tv.getContext(),100)); // <item name="android:width">30px</item>
    }
    public static void configuraTVDescAtiv(TextView tv){
    	configuraTV(tv);
		tv.setBackgroundColor(Color.argb(125, 105, 89, 205));    	
    }
    public static void configuraTVQuadAtiv(TextView tv){
    	configuraTVDescAtiv(tv);    	
		tv.setWidth(dipToPixel(tv.getContext(),50)); // <item name="android:width">30px</item>
		tv.setTextSize(dipToPixel(tv.getContext(),20)); // <item name="android:textSize">25px</item>
    }
    public static void configuraTVQuadAtivData(TextView tv){
    	configuraTVDescAtiv(tv);    	
		tv.setWidth(dipToPixel(tv.getContext(),200)); // <item name="android:width">30px</item>
		tv.setTextSize(dipToPixel(tv.getContext(),20)); // <item name="android:textSize">25px</item>
    }
    
    public static int dip(Context context, int pixels) {
    	   float scale = context.getResources().getDisplayMetrics().density;
    	   return (int) (pixels * scale + 0.5f);
    	}
	public static int dipToPixel(Context context, int dips){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, context.getResources().getDisplayMetrics());
		
	}
	
}
