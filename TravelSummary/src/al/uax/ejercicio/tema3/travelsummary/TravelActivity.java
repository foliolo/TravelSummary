package al.uax.ejercicio.tema3.travelsummary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
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
	private ListView list;
	protected static final int MODIF_TRAVEL = 10;
	protected static final int ADD_TRAVEL = 11;
	
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
		
		//Asociamos el adapter al listView
		ArrayAdapter<TravelInfo> adapter = new TravelAdapter();
		list = (ListView) findViewById(R.id.travel_list_view);
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
		
		registerForContextMenu(list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent;
		
		switch(item.getItemId()){
			case R.id.action_settings:
				Toast.makeText(TravelActivity.this, "Configuración", Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.menu_add:
				//Creamos el intent donde se introducirá la información del nuevo item.
				intent = new Intent(this, EditTravelActivity.class);
				intent.putExtra("posicion", -1);
				startActivityForResult(intent, ADD_TRAVEL);
				break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.menu_item, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		@SuppressWarnings("unchecked")
		ArrayAdapter<TravelInfo> listAdapter = (ArrayAdapter<TravelInfo>) list.getAdapter();
		Intent intent;
		
		switch(item.getItemId()){
			case R.id.modif:
				//Creamos el intent que modificara el item seleccionado
				intent = new Intent(this, EditTravelActivity.class);
				intent.putExtra("posicion", info.position);
				intent.putExtra("lista_viajes", (Serializable) this.travels);
				startActivityForResult(intent, MODIF_TRAVEL);
				break;
			
			case R.id.delete:
				//Borramos el item seleccionado
				listAdapter.remove(travels.get(info.position));
				listAdapter.notifyDataSetChanged();
				break;

				
			case R.id.correo:
				//Creamos el intent que mandará la información al correo.
				intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				//Creamos el texto a mandar
				int posicion = info.position;
				String textSend = "Viaje realizado:\n"
						+ "Ciudad: " + travels.get(posicion).getCiudad() + "\n"
						+ "País: " + travels.get(posicion).getPais() + "\n"
						+ "Año" + travels.get(posicion).getAnyo() + "\n"
						+ "Anotación: " + travels.get(posicion).getAnotacion();
				
				intent.putExtra(Intent.EXTRA_TEXT, textSend);
				startActivity(Intent.createChooser(intent, getResources().getString(R.string.menu_item_send_choose)));
				break;
		}
		
		return super.onContextItemSelected(item);
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ArrayAdapter<TravelInfo> listAdapter = (ArrayAdapter<TravelInfo>) list.getAdapter();
		Bundle extras;
		
		if(resultCode == RESULT_OK){
			switch(requestCode){
				case MODIF_TRAVEL:
					extras = data.getExtras();
					if (extras != null)
						travels = (ArrayList<TravelInfo>) extras.getSerializable("lista_viajes");
					
					listAdapter.notifyDataSetChanged();
					break;
					
				case ADD_TRAVEL:
					extras = data.getExtras();
					if(extras != null)
						travels.add((TravelInfo) extras.getSerializable("nuevo_viaje"));
					
					listAdapter.notifyDataSetChanged();
					break;
			}
		}
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
