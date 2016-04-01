package elsm.app.fitinbody;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

//MainActivity herda de ActivityGroup, porque vocÊ está lidando
//com várias atividades em um elemento só. Ou seja, o TabHost hospeda um grupo de atividades.
public class MainActivity extends ActivityGroup {
	static TabHost tabHost;
	static int tab = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources();
		tabHost = (TabHost)findViewById(R.id.tabhost);
		tabHost.setup(this.getLocalActivityManager());
		tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		TabHost.TabSpec spec;
		Intent intent;

		//Cada Tab adicionada tem sua própria Activity, que por sua vez , tem seu próprio .xml(layout) e //também tem um elemento Drawable. Esse elemento eu usei para mudar o ícone da Tab.
		//Quando ela não está ativa, o ícone é cinza. Quando ela está ativa, o ícone fica colorido e indica //pro usuário que ele está naquela Tab.

		//Nesse meu exemplo, eu tenho 4 Tabs.4 Activities. 4 Layouts. 4 drawable que faz a mudança de ícone. // 1 para cada Tab.

		//tabHost.getTabWidget().addView(divider, LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);		


		//Adiciona Tab #2
		intent = new Intent(this, TreinoActivity.class);
		spec = tabHost.newTabSpec("0").setIndicator(createTabView(tabHost.getContext(),"Treino")).setContent(intent);
		tabHost.addTab(spec);
		

		//Adiciona Tab #2
		intent = new Intent(this, FrequenciaActivity.class);
		spec = tabHost.newTabSpec("1").setIndicator(createTabView(tabHost.getContext(),"Frequencia")).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ProgramaListActivity.class);
		spec = tabHost.newTabSpec("2").setIndicator(createTabView(tabHost.getContext(),"Programas")).setContent(intent);
		tabHost.addTab(spec);
		
/*		
		intent = new Intent(this, AvaliacaoListActivity.class);
		spec = tabHost.newTabSpec("3").setIndicator(createTabView(tabHost.getContext(),"Avaliação")).setContent(intent);
		tabHost.addTab(spec);
*/
		//Adiciona Tab #1
		//Adiciona Tab #3
		intent = new Intent().setClass(this, ExercMuscActivity.class);
		spec = tabHost.newTabSpec("4").setIndicator(createTabView(tabHost.getContext(),"Exerc")).setContent(intent);
		tabHost.addTab(spec);

		//essa ultima linha indica qual tab será carregada ao iniciar essa activity. No nosso caso, a Primeira!!!

		tabHost.setCurrentTab(0);

	}
	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg_view, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}	

}
