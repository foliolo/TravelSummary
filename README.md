TravelSummary
=============

Ejercicios UAX (Tema 4):

  El ejercicio consiste en aumentar la funcionalidad del proyecto TravelList a partir
de la nueva version de esta unidad didactica. Debes incluir las activities TravelActivity
y EditTravelInfo con las modificaciones del ejercicio del apartado 2 para
partir de la version mas completa posible. A partir de esta version debes:
 Modificar la clase EditTravelActivity para implementar la creacion de un
nuevo viaje, guardando la informacion en el ArrayAdapter de la clase
TravelListActivity.
	-Pista: deberas utilizar el metodo startActivityForResult
 Modifica la clase TravelListActivity para implementar la edicion y el borrado
de viajes mediante un menu contextual, que aparezca haciendo
un click prolongado sobre un item de la lista.
	-Pista: debes utilizar los metodos onCreateContextMenu y onContextItemSelected.

  Nota: para editar un item de la lista es suficiente con que elimines
el actual e introduzcas el nuevo.
Para añadir y borrar elementos de la lista de viajes basta con usar los metodos
add() y remove() del Adapter. Haciendo esto podras comprobar la gran utilidad
del Adapter, ya que al añadir o eliminar sus elementos la interfaz grafica cambiara
automaticamente para mostrar los cambios sin que tengas que realizar
ninguna operacion opcional. El Adapter se encarga de ello.
