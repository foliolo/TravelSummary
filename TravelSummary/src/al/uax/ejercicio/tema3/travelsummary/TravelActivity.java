package al.uax.ejercicio.tema3.travelsummary;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TravelActivity extends Activity {
	
	private List<TravelInfo> travels = new ArrayList<TravelInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel);
		
		//Generamos los datos
		travels.add(new TravelInfo("Londres", "UK", 2012, "Anotacion 1"));
		travels.add(new TravelInfo("Paris", "Francia", 2007, "Anotacion 2"));
		travels.add(new TravelInfo("Gotham", "City", 2011, "Anotacion 3"));
		travels.add(new TravelInfo("Hamburgo", "Alemania", 2009, "Anotacion 4"));
		travels.add(new TravelInfo("Pekin", "China", 2011, "Anotacion 5"));
		travels.add(new TravelInfo("Londres", "UK", 2012, "Anotacion 6"));
		travels.add(new TravelInfo("Paris", "Francia", 2007, "Anotacion 7"));
		travels.add(new TravelInfo("Gotham", "City", 2011, "Anotacion 8"));
		travels.add(new TravelInfo("Hamburgo", "Alemania", 2009, "Anotacion 9"));
		travels.add(new TravelInfo("Pekin", "China", 2011, "Anotacion 10"));
		travels.add(new TravelInfo("Londres", "UK", 2012, "Anotacion 11"));
		travels.add(new TravelInfo("Paris", "Francia", 2007, "Anotacion 12"));
		travels.add(new TravelInfo("Gotham", "City", 2011, "Anotacion 13"));
		travels.add(new TravelInfo("Hamburgo", "Alemania", 2009, "Anotacion 14"));
		travels.add(new TravelInfo("Pekin", "China", 2011, "Anotacion 15"));
		
		ArrayAdapter<TravelInfo> adapter = new TravelAdapter();
		ListView list = (ListView) findViewById(R.id.travel_list_view);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(view != null){
					TravelInfo travel = travels.get(position);
					
					String mensaje = "Visita: " + travel.getCiudad() + "(" + travel.getPais() + "), año: " + travel.getAnyo() + 
							"\nAnotación: " + travel.getAnotacion();
					
					Toast.makeText(TravelActivity.this, mensaje, Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel, menu);
		return true;
	}

	//Inner class para el Adaptador que manejara los datos de la clase que hemos creado	
	private class TravelAdapter extends ArrayAdapter<TravelInfo>{
		
		//Constructor
		public TravelAdapter(){
			super(TravelActivity.this, R.layout.item_view, travels);
		}
		
		//Sobrescribimos el método getView donde se creará la vista que vamos a reutilizar
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			
			//Comprobar que tenemos una vista con la que trabajar
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
			}
			
			//Encontrar el elemento con el que trabajar
			TravelInfo currentTravel =  travels.get(position);
			
			//LLenar la vista
			//Ciudad
			TextView ciudad = (TextView) itemView.findViewById(R.id.item_ciudad);
			ciudad.setText(currentTravel.getCiudad());
			//País
			TextView pais = (TextView) itemView.findViewById(R.id.item_pais);
			pais.setText("(" + currentTravel.getPais() + ")");
			//Año
			TextView año = (TextView) itemView.findViewById(R.id.item_anyo);
			año.setText("" + currentTravel.getAnyo());
			//Anotación
			TextView anotacion = (TextView) itemView.findViewById(R.id.item_anotacion);
			anotacion.setText(currentTravel.getAnotacion());
			
			//Retornamos al vista
			return itemView;
		}
	}
}
