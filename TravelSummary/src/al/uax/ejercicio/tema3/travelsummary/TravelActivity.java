package al.uax.ejercicio.tema3.travelsummary;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
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
	
	//Atributos
	private ArrayList<TravelInfo> travels = new ArrayList<TravelInfo>();
	ArrayAdapter<TravelInfo> adapter = null;
	private ListView list;
	protected static final int MODIF_TRAVEL = 10;
	protected static final int ADD_TRAVEL = 11;
	int posicion;

	
	//Manejo de llamadas entrantes
	private TelephonyManager manager;
	private PhoneStateListener listener = new PhoneStateListener(){
		@Override
		public void onCallStateChanged(int state, String incomingNumber){
			super.onCallStateChanged(state, incomingNumber);
			//Si el estado indica que el teléfono esta sonando mostramos una notificación Toast por pantalla
    		if (state == TelephonyManager.CALL_STATE_RINGING)
    			Toast.makeText(TravelActivity.this, "Llamada entrante de:\n" + incomingNumber , Toast.LENGTH_SHORT).show();
		}
	};
	
	//Manejo de mensajes recibidos
	private BroadcastReceiver smsReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[])bundle.get("pdus");
                    
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    
                    for (int i = 0; i < pdus.length; i++) 
                        messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    
                    if (messages.length > -1)
                    	Toast.makeText(TravelActivity.this, "SMS recibido:\n" + messages[0].getMessageBody(), Toast.LENGTH_LONG).show();
                }
			}
		}
    };
    
    //Control de batería. Uso de Sticky Intent
	private Intent batteryStatus;
    private BroadcastReceiver bateryReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
		
			// Control de si se está cargando
			int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || 
					status == BatteryManager.BATTERY_STATUS_FULL;
			
			if(batteryStatus.getExtras() != null){
				int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
	
				float currBattery = 100 * level / (float)scale;
				
				// Aviso si está en 20% ó 10% de batería (y no está cargándose)
				if(!isCharging)
					if(currBattery>19 && currBattery<21 || currBattery>9 && currBattery<11)
						Toast.makeText(TravelActivity.this, "Cudidado:\n" + currBattery + "% batería", Toast.LENGTH_LONG).show();
			}
			else
				Log.d("TAG", "ERROR CON LA BATERIA");
		}
    };
    
	
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
		
		//Asociamos el adapter al listView
		adapter = new TravelAdapter();
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
		
		//Registramos el menú que saldrá al pulsar sobre los item		
		registerForContextMenu(list);
	
		//Obtenemos la instancia del TelephonyManager
        manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        
        //Registramos el oyente para escuchar los eventos de llamadas
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        
        //Creamos y registramos el BroadcastReceiver para la recepción/envío de SMS's
        IntentFilter smsFilter = new IntentFilter();
		smsFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, smsFilter);
        
        //Creamos y registramos el Sticky Intent para controlar la batería
		IntentFilter bateryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		batteryStatus = this.registerReceiver(null, bateryFilter);
		registerReceiver(bateryReceiver, bateryFilter);
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
//		@SuppressWarnings("unchecked")
//		ArrayAdapter<TravelInfo> listAdapter = (ArrayAdapter<TravelInfo>) list.getAdapter();
		Intent intent;
		
		posicion = info.position;
		
		switch(item.getItemId()){
			case R.id.modif:
				//Creamos el intent que modificara el item seleccionado
				intent = new Intent(this, EditTravelActivity.class);
				
				Bundle data = new Bundle();
				data.putInt("posicion", posicion);
				data.putSerializable("modif_viaje", (Serializable) travels.get(posicion));
				intent.putExtras(data);
				
				startActivityForResult(intent, MODIF_TRAVEL);
				break;
			
			case R.id.delete:
				//Borramos el item seleccionado
				travels.remove(info.position);
				adapter.notifyDataSetChanged();
				break;
				
			case R.id.correo:
				//Creamos el intent que mandará la información al correo.
				intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				
				//Creamos el texto a mandar
				posicion = info.position;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle extras;
		
		if(resultCode == RESULT_OK){
			switch(requestCode){
				case MODIF_TRAVEL:
					extras = data.getExtras();
					if (extras != null){
						travels.remove(posicion);
						travels.add(posicion, (TravelInfo) extras.getSerializable("modif_viaje"));
					}
					
					adapter.notifyDataSetChanged();
					break;
					
				case ADD_TRAVEL:
					extras = data.getExtras();
					if(extras != null)
						travels.add((TravelInfo) extras.getSerializable("nuevo_viaje"));
					
					adapter.notifyDataSetChanged();
					break;
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		Log.e("TAG", "Estado salvado");
		
		outState.putSerializable("lista_viajes", (Serializable) travels);
		super.onSaveInstanceState(outState);
	}
	
	//@Override
	@SuppressWarnings("unchecked")
	protected void onRestoreInstaceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		Log.e("TAG", "Estado restaurado");
		
		if(savedInstanceState != null)
			travels = (ArrayList<TravelInfo>) savedInstanceState.getSerializable("lista_viajes");

		adapter.notifyDataSetChanged();
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d("TAG", "Desactivar el listener de telefonia y mensajería");
		manager.listen(listener, PhoneStateListener.LISTEN_NONE);
		try{
			unregisterReceiver(smsReceiver);
			unregisterReceiver(bateryReceiver);
		}
		catch(IllegalArgumentException ex){
			Log.d("TAG", "Fallo al desregistrar");
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
