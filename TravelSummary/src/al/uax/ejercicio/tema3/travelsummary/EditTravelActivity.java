package al.uax.ejercicio.tema3.travelsummary;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditTravelActivity extends Activity {
	private TextView ciudad;
	private TextView pais;
	private TextView anyo;
	private TextView anotacion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_travel);
		
		//Obtenemos la referencia de los objetos gráficos
		ciudad = (TextView) findViewById(R.id.ciudad);
		pais = (TextView) findViewById(R.id.pais);
		anyo = (TextView) findViewById(R.id.anyo);
		
		Button boton = (Button) findViewById(R.id.boton);
		
		boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mensaje = "Nueva visita: " + ciudad.getText() + "(" + pais.getText() + "), año: " + anyo.getText();
				Toast.makeText(EditTravelActivity.this, mensaje, Toast.LENGTH_LONG).show();
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
}
