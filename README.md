TravelSummary
=============

Ejercicios UAX (Tema 4):

  El ejercicio consiste en aumentar la funcionalidad del proyecto TravelList a partir
de la nueva versi�n de esta unidad did�ctica. Debes incluir las activities TravelActivity
y EditTravelInfo con las modificaciones del ejercicio del apartado 2 para
partir de la versi�n m�s completa posible. A partir de esta versi�n debes:
 Modificar la clase EditTravelActivity para implementar la creaci�n de un
nuevo viaje, guardando la informaci�n en el ArrayAdapter de la clase
TravelListActivity.
	-Pista: deber�s utilizar el m�todo startActivityForResult
 Modifica la clase TravelListActivity para implementar la edici�n y el borrado
de viajes mediante un men� contextual, que aparezca haciendo
un click prolongado sobre un �tem de la lista.
	-Pista: debes utilizar los m�todos onCreateContextMenu y onContextItemSelected.

  Nota: para editar un �tem de la lista es suficiente con que elimines
el actual e introduzcas el nuevo.
Para a�adir y borrar elementos de la lista de viajes basta con usar los m�todos
add() y remove() del Adapter. Haciendo esto podr�s comprobar la gran utilidad
del Adapter, ya que al a�adir o eliminar sus elementos la interfaz gr�fica cambiar�
autom�ticamente para mostrar los cambios sin que tengas que realizar
ninguna operaci�n opcional. El Adapter se encarga de ello.
