package al.uax.ejercicio.tema3.travelsummary;

import java.io.Serializable;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EditTravelActivity extends Activity {
	private TextView ciudad;
	private TextView pais;
	private TextView anyo;
	private TextView anotacion;
	private boolean cancel = true; //Flag para controlar que el usuario no cancele la operación sin actualizar los datos correctamente.
	private int posicion; //Posición de elemento que vamos a modificar. Si nos llega -1, indicará que es un elemento nuevo.
	private ArrayList<TravelInfo> travels = new ArrayList<TravelInfo>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_travel);
		
		//Obtenemos la referencia de los objetos gráficos
		ciudad = (TextView) findViewById(R.id.ciudad);
		pais = (TextView) findViewById(R.id.pais);
		anyo = (TextView) findViewById(R.id.anyo);
		anotacion = (TextView) findViewById(R.id.anotacion);
		
		//Rellenamos los elementos con los datos que nos manda la actividad principal. (Siempre que no sea un elemento nuevo)

		//		travels = (ArrayList<TravelInfo>) intent.getSerializableExtra("lista_viajes");
		
		Bundle extras = getIntent().getExtras();
		posicion = extras.getInt("posicion", -1);
		if (extras != null)
		{
		    travels = (ArrayList<TravelInfo>) extras.getSerializable("lista_viajes");

		    if(posicion != -1){
		    	ciudad.setText(travels.get(posicion).getCiudad());
		    	pais.setText(travels.get(posicion).getPais());
		    	anyo.setText("" + travels.get(posicion).getAnyo());
		    	anotacion.setText(travels.get(posicion).getAnotacion());
		    }
		}
		
		
		//Realizamos la funcionalidad del botón. Se devuelven los datos actualizados a la activity principal.
		Button boton = (Button) findViewById(R.id.boton);
		boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Volcamos la información actualizada al ArrayList
				travels.set(posicion, new TravelInfo(ciudad.getText().toString(), 
						pais.getText().toString(), 
						Integer.parseInt(anyo.getText().toString()), 
						anotacion.getText().toString()));
				
				//Creamos el Intent que devolvera el ArrayList a la actividad principal
				Intent intent = new Intent();
				intent.putExtra("lista_viajes", (Serializable) travels);
				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel, menu);
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
		Log.e("TAG", "Estado salvado");
		
		//Localizamos los elementos de pantalla que vamos a guardar
		anotacion = (TextView) findViewById(R.id.anotacion);

		String miCiudad = ciudad.getText().toString();
		String miPais = pais.getText().toString();
		String miAnyo= anyo.getText().toString();
		String miAnotacion = anotacion.getText().toString();
		
		outState.putString("ciudad",miCiudad);
		outState.putString("pais", miPais);
		outState.putString("anyo", miAnyo);
		outState.putString("anotacion", miAnotacion);
		
		super.onSaveInstanceState(outState);
	}
	
	//@Override
	protected void onRestoreInstaceState(Bundle savedInstanceState){
		Log.e("TAG", "Estado restaurado");
		
		if(savedInstanceState != null){
			String miCiudad = savedInstanceState.getString("ciudad");
			String miPais = savedInstanceState.getString("pais");
			String miAnyo= savedInstanceState.getString("anyo");
			String miAnotacion = savedInstanceState.getString("anotacion");
			
			ciudad.setText(miCiudad);
			pais.setText(miPais);
			anyo.setText(miAnyo);
			anotacion.setText(miAnotacion);
		}
		
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(cancel){
			Log.d("TAG", "El usuario cancela la edición de viaje");
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}
	}
}
