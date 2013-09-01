package al.uax.ejercicio.tema3.travelsummary;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	private ArrayList<TravelInfo> travels = new ArrayList<TravelInfo>();

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
				
				if(posicion != -1){
					//Volcamos la información actualizada al ArrayList (controlando que la fecha no sea vacío)
					int fecha = anyo.getText().toString().equals("") ? 0 : Integer.parseInt(anyo.getText().toString());
					travels.set(posicion, new TravelInfo(ciudad.getText().toString(), 
							pais.getText().toString(), 
							fecha, 
							anotacion.getText().toString()));
					
					//Creamos el Intent que devolverá el ArrayList a la actividad principal
					Intent intent = new Intent();
					intent.putExtra("lista_viajes", (Serializable) travels);
					
					setResult(RESULT_OK, intent);
					finish();
				}
				else{
					//Creamos el Intent que devolverá el nuevo objeto a añadir(controlando que la fecha no sea vacío)
					Intent intent = new Intent();
					int fecha = anyo.getText().toString().equals("") ? 0 : Integer.parseInt(anyo.getText().toString());
					intent.putExtra("nuevo_viaje", (Serializable) new TravelInfo(ciudad.getText().toString(), 
							pais.getText().toString(), 
							fecha, 
							anotacion.getText().toString()));
					
					setResult(RESULT_OK, intent);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.travel, menu);
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
		Log.d("TAG", "Desactivar el listener de telefonia y mensajería");
		super.onDestroy();
		manager.listen(listener, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(receiver);
	}
}
