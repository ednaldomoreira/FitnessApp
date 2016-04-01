package elsm.app.fitinbody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.view.ViewGroup;
import android.widget.TextView;

public class IncValor {
	public static void incremetarValor(TextView tv, boolean longo){
		if (tv.getTag() != null){
			String tag = (String) tv.getTag();
			if(tag.length()==11){ 
				String conteudo=tv.getText().toString().trim();
				if (tag.substring(0, 1).equals("T")){
					tv.setText(IncValor.incrementaLetra(tv.getText().toString()));
					if (!tv.getText().toString().trim().equals("")){
						BaseTreinoActivity bta = (BaseTreinoActivity) tv.getContext();
						ViewGroup vg = (ViewGroup) tv.getParent();						
						TextView tvS = (TextView)vg.findViewWithTag("S"+tv.getTag().toString().substring(1));  
						TextView tvR = (TextView)vg.findViewWithTag("R"+tv.getTag().toString().substring(1));
						if (tvS!=null && tvS.getText().toString().trim().equals("0")){
							tvS.setText(bta.getNumSeries());
						}
						if (tvR!=null && tvR.getText().toString().trim().equals("0")){
							tvR.setText(bta.getNumRepeticoes());
						}
						
					}
				}
				else if(conteudo.length() == 5 && conteudo.charAt(2)==':'){
					tv.setText(IncValor.incrementaTempo(tv.getText().toString(), 5, (longo?10:1))); 
				}
				else {
					int maxValor=1000;
					int incremento=1;
					if (tag.substring(0, 1).equals("S")){
						maxValor = 10;
						incremento = 1;
					}
					else if (tag.substring(0, 1).equals("R")){
						maxValor = 50;
						incremento = (longo?10:1);
					}
					else if (tag.substring(0, 1).equals("P")){
						maxValor = 300;
						incremento = (longo?10:1); 
					} 
					else if (tag.substring(0, 1).equals("C")){
						maxValor = Integer.parseInt(tag.substring(1, 5));
						incremento = (longo?Integer.parseInt(tag.substring(6)):1); 
					}
					tv.setText(IncValor.incrementaValor(tv.getText().toString(), maxValor, incremento)); 
				}

			}
			if(tag.length()==13){
				tv.setText(IncValor.incrementaTempo(tv.getText().toString(), 5, (longo?10:1))); 
			}

		}

	}

	public static String incrementaValor(String valor, int maxValor, int incremento){
		int valorSerie;
		try 
		{ 
			valorSerie = Integer.parseInt(valor);
		} 
		catch( Exception e) 
		{ 
			valorSerie = 0; 
		}

		valorSerie=valorSerie+incremento;
		// valorSerie = (valorSerie > maxValor ?valorSerie = maxValor:(valorSerie/incremento)*incremento);
		valorSerie = (valorSerie/incremento)*incremento;

		if (valorSerie > maxValor){
			valorSerie = 0;
		}
		return "" + valorSerie + "";

	}
	public static String incrementaTempo(String valor, int maxValor, int incremento){
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

		valorMinuto=valorMinuto+incremento;
		valorMinuto = (valorMinuto/incremento)*incremento;

		if (valorMinuto >= 60){
			valorMinuto = valorMinuto%60;
			valorHora++;
		}

		if (valorHora > maxValor){
			valorHora = 0;
			valorMinuto = 0;
		}

		return String.format("%02d", valorHora) + ":" + String.format("%02d", valorMinuto);

	}
	public static String incrementaLetra(String valor){
		if (valor.trim().equals("")){
			valor = "A";
		}
		else{
			char letra = valor.toString().trim().toCharArray()[0];
			if (letra++ == 'G'){
				valor = "";
			}
			else{ 
				valor = String.valueOf(letra);
			}

		}

		return valor;

	}
	public static Date StringPraData(String dataString){
		Date dataRetorno = null;
		if (dataString != null){
			DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			try{ 
				dataRetorno = formato.parse(dataString);
			}
			catch(ParseException ex){
				ex.printStackTrace();
			}
		}
		return dataRetorno;
	}
    public static Date DataAtual(){
  	    Calendar c = Calendar.getInstance();
  	    
  	    return new Date(c.get(Calendar.YEAR) - 1900,
 	                           c.get(Calendar.MONTH),
 	                           c.get(Calendar.DATE));
    	
    }
    public static Date incDia(Date dataIncluirDia, int numDias){
  	    Calendar c = Calendar.getInstance();
  	    
  	    c.setTime(dataIncluirDia); 	      	    
  	    
  	    return new Date(c.get(Calendar.YEAR) - 1900,
 	                           c.get(Calendar.MONTH),
 	                           c.get(Calendar.DATE)+numDias);
    	
    }
    
	public static String mnmDiaSemana(int numDia){
		String nomeDiaSemana=null;
		switch(numDia){
		case 1: nomeDiaSemana = "DOM"; break;
		case 2: nomeDiaSemana = "SEG"; break;
		case 3: nomeDiaSemana = "TER"; break;
		case 4: nomeDiaSemana = "QUA"; break;
		case 5: nomeDiaSemana = "QUI"; break;
		case 6: nomeDiaSemana = "SEX"; break;
		case 7: nomeDiaSemana = "SAB"; break;
		}
		return nomeDiaSemana;
	}

	public static int numDiaSemana(String mnmDia){
		int numDia=0;
		
		if (mnmDia.equals("DOM"))
				numDia=1;	
		else if (mnmDia.equals("SEG"))
				numDia=2;	
		else if (mnmDia.equals("TER"))
				numDia=3;	
		else if (mnmDia.equals("QUA"))
				numDia=4;	
		else if (mnmDia.equals("QUI"))
				numDia=5;	
		else if (mnmDia.equals("SEX"))
				numDia=6;	
		else if (mnmDia.equals("SAB"))
				numDia=7;
		
		return numDia;
	}
	public static int tipoValor(TextView tv){
		int tipo=0;
		if (tv.getTag() != null){
			String tag = (String) tv.getTag();
			if(tag.length()==11){ 
				String conteudo=tv.getText().toString().trim();
				if (tag.substring(0, 1).equals("T")){
					tipo=2;
				}
				else if(conteudo.length() == 5 && conteudo.charAt(2)==':'){
					tipo=1; 
				}
				else {
					if (tag.substring(0, 1).equals("S")){
						tipo=3;
					}
					else if (tag.substring(0, 1).equals("R")){
						tipo=4;
					}
					else if (tag.substring(0, 1).equals("P")){
						tipo=5;						
					} 
					else if (tag.substring(0, 1).equals("C")){
						tipo=6;
					}
					tipo=7; 
				}

			}
			if(tag.length()==13){
				tipo = 1; 
			}

		}
		return tipo;
		
	}

}
