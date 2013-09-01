TravelSummary
=============

Ejercicios UAX (Tema 4):

  El ejercicio consiste en aumentar la funcionalidad del proyecto TravelList a partir
de la nueva versión de esta unidad didáctica. Debes incluir las activities TravelActivity
y EditTravelInfo con las modificaciones del ejercicio del apartado 2 para
partir de la versión más completa posible. A partir de esta versión debes:
 Modificar la clase EditTravelActivity para implementar la creación de un
nuevo viaje, guardando la información en el ArrayAdapter de la clase
TravelListActivity.
	-Pista: deberás utilizar el método startActivityForResult
 Modifica la clase TravelListActivity para implementar la edición y el borrado
de viajes mediante un menú contextual, que aparezca haciendo
un click prolongado sobre un ítem de la lista.
	-Pista: debes utilizar los métodos onCreateContextMenu y onContextItemSelected.

  Nota: para editar un ítem de la lista es suficiente con que elimines
el actual e introduzcas el nuevo.
Para añadir y borrar elementos de la lista de viajes basta con usar los métodos
add() y remove() del Adapter. Haciendo esto podrás comprobar la gran utilidad
del Adapter, ya que al añadir o eliminar sus elementos la interfaz gráfica cambiará
automáticamente para mostrar los cambios sin que tengas que realizar
ninguna operación opcional. El Adapter se encarga de ello.
