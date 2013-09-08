package al.uax.ejercicio.tema3.travelsummary;

import java.io.Serializable;

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
	private int posicion; //Posición de elemento que vamos a modificar. Si nos llega -1, indicará que es un elemento nuevo.

	//Manejo de llamadas entrantes
	private TelephonyManager manager;
	private PhoneStateListener listener = new PhoneStateListener(){
		@Override
		public void onCallStateChanged(int state, String incomingNumber){
			super.onCallStateChanged(state, incomingNumber);
			//Si el estado indica que el teléfono esta sonando mostramos una notificación Toast por pantalla
    		if (state == TelephonyManager.CALL_STATE_RINGING)
    			Toast.makeText(EditTravelActivity.this, "Llamada entrante de:\n" + incomingNumber , Toast.LENGTH_SHORT).show();
		}
	};
	
	//Manejo de mensajes recibidos
	private BroadcastReceiver receiver = new BroadcastReceiver(){
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
                    	Toast.makeText(EditTravelActivity.this, "SMS recibido:\n" + messages[0].getMessageBody(), Toast.LENGTH_LONG).show();
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
			
			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

			float currBattery = 100 * level / (float)scale;
			
			// Aviso si está en 20% ó 10% de batería (y no está cargándose)
			if(!isCharging)
				if(currBattery>19 && currBattery<21 || currBattery>9 && currBattery<11)
					Toast.makeText(EditTravelActivity.this, "Cudidado:\n" + currBattery + "% batería", Toast.LENGTH_LONG).show();
		}
    };
	    
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
		Bundle extras = getIntent().getExtras();
		posicion = extras.getInt("posicion", -1);
		
		if (extras != null && posicion != -1)
		{
	    	ciudad.setText( ((TravelInfo) extras.getSerializable("modif_viaje") ).getCiudad());
	    	pais.setText( ((TravelInfo) extras.getSerializable("modif_viaje") ).getPais());
	    	anyo.setText("" + ((TravelInfo) extras.getSerializable("modif_viaje") ).getAnyo());
	    	anotacion.setText( ((TravelInfo) extras.getSerializable("modif_viaje") ).getAnotacion());
		}
		
		//Realizamos la funcionalidad del botón. Se devuelven los datos actualizados a la activity principal.
		Button boton = (Button) findViewById(R.id.boton);
		boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(posicion != -1){
					int fecha = anyo.getText().toString().equals("") ? 0 : Integer.parseInt(anyo.getText().toString());
					
					Bundle data = new Bundle();
					data.putSerializable("modif_viaje", (Serializable) new TravelInfo(ciudad.getText().toString(), 
							pais.getText().toString(), 
							fecha, 
							anotacion.getText().toString()));
					getIntent().putExtras(data);
					
					setResult(RESULT_OK, getIntent());
					finish();
				}
				else{
					//Creamos el Intent que devolverá el nuevo objeto a añadir(controlando que la fecha no sea vacío)
					int fecha = anyo.getText().toString().equals("") ? 0 : Integer.parseInt(anyo.getText().toString());
					
					getIntent().putExtra("nuevo_viaje", (Serializable) new TravelInfo(ciudad.getText().toString(), 
							pais.getText().toString(), 
							fecha, 
							anotacion.getText().toString()));
					
					setResult(RESULT_OK, getIntent());
					finish();
				}
			}
		});
		
		//Obtenemos la instancia del TelephonyManager
        manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        
        //Registramos el oyente para escuchar los eventos de llamadas
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        
        //Creamos y registramos el BroadcastReceiver para la recepción/envío de SMS's
        IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);
        
        //Creamos y registramos el Sticky Intent para controlar la batería
		IntentFilter bateryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		batteryStatus = this.registerReceiver(null, bateryFilter);
		registerReceiver(bateryReceiver, bateryFilter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.travel, menu);
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
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
		
	}
	
	//@Override
	protected void onRestoreInstaceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
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
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("TAG", "Desactivar el listener de telefonia y mensajería");
		manager.listen(listener, PhoneStateListener.LISTEN_NONE);

		try{
			unregisterReceiver(receiver);
			unregisterReceiver(bateryReceiver);
		}
		catch(IllegalArgumentException ex){
			Log.d("TAG", "Fallo al desregistrar");
		}
	}
}
