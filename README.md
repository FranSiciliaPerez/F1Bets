# F1Bets
Parte en nativo del TFG


## Descripción
F1Bets, es una aplicacion de apuestas movil con la temática del deporte del motor, F1(Fórmula 1).
Para acceder a ella, debes obligatoriamente registrarte primero agregando un email y contraseña.
En esta aplicación se pueden añadir, editar y borrar tanto pilotos como circuitos en sus respectivas pestañas.
Una vez el usuario haya añadido los pilotos y circuitos, solo tendrá que ir al apartado de apuestas para empezar a divertirse y apostar(donde además de añadir, tambien se puede eliminar la apuesta).
Para poder hacer todas estas cosas, es necesario desplegar el menu lateral y verán rapidamente donde se encuentran todas las cosas.
Tambien hay un apartado reservado para ver la información del usuario actual
Por supuesto que el dinero apostado no es real, y la cantidad puede ser imaginaria, además que esta app está hecha solo con fines educativos y de entretenimiento.

## APK
En este enlace, se encuentra el APK de mi aplicacion:
https://github.com/FranSiciliaPerez/F1Bets/releases/tag/V1_Apk
## Dependencias
Estas son las dependencias utilizadas en esta app:

androidx.core:core-ktx:1.12.0
androidx.appcompat:appcompat:1.6.1
com.google.android.material:material:1.11.0
androidx.constraintlayout:constraintlayout:2.1.4
androidx.lifecycle:lifecycle-livedata-ktx:2.7.0
androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0
androidx.navigation:navigation-fragment-ktx:2.7.6
androidx.navigation:navigation-ui-ktx:2.7.6
androidx.annotation:annotation:1.7.1
androidx.room:room-common:2.6.1

junit:junit:4.13.2
androidx.test.ext:junit:1.1.5
androidx.test.espresso:espresso-core:3.5.1

Firebase dependencies:
com.google.firebase:firebase-auth-ktx:22.0.0
com.google.firebase:firebase-storage:20.3.0
com.google.firebase:firebase-auth:
com.google.firebase:firebase-firestore:24.10.1

Glide:
com.github.bumptech.glide:glide:4.12.0
com.github.bumptech.glide:compiler:4.12.0

Y los requisitos minimos para poder instalarla y usarla son:

compileSdkVersion es 34.
minSdkVersion es 26.
targetSdkVersion es 34.

## Manual de instalación
Solo hay que ir a este enlace donde se encuentra el archivo instalable .apk y descargarlo.

Una vez descargado, ejecutala para que comience la instalación.
https://github.com/FranSiciliaPerez/F1Bets/releases/download/V1_Apk/F1Bets.apk

## Tutorial de uso y secciones de la app
Nada mas abras la aplicacion, si tienes el sonido activado, escucharás un sonido de coche de F1
y verás en el dispositivo al mismo tiempo una pantala splash que aparecerá y desaparecerá junto con el sonido en unos segundos.
A coninuación, nos encontramos en la primera o tecnicamente la segunda pantalla, la pantalla de login.
Si estas registrado, solo tendras que poner tus credenciales y podras acceder a la pantalla home, 
pero si eres nuevo usuario o has eliminado tu cuenta anterior, tendras que pulsar el boton register que te redirigirá a la pantalla
donde puedes llevar a cabo el registro. Un vez registrado, estarás en la pantala home, donde un mensaje emergente de preguntará si eres
o no mayor de edad(si se pulsa que no, se cerrará la sesion y le redirigirá a la pantalla d login), si somos mayores de edad y continuamos, 
y se nos presentan varias opciones. 
El usuario puede pulsar en el menú desplegable para navegar a las pantallas de pilotos, circuitos y apuestas, puede, si deja pulsado 
la pantalla durante un tiempo, hacer aparecer un menmú desplegale con dos opciones para ir a una pantalla de informacion(donde además se encuentra
un video tutorial sobre como apostar en la F1), o ir a ajustes, pantalla a la que tambien se puede acceder pulsando el engranaje de la barra superior
a la derecha(tanto en la pantalla home como en la de user info).

Si elegimos la opcion de pulsar el engranaje e ir a ajustes, veremos una pantalla con dos botones para cerrar sesión, y otro para eliminar
la cuenta. Si el usuario pulsa el boton cerrar sesión, le redirigirá al login, en cambio si pulsa eliminar y confirma que quiere borrar
la cuenta, irá a la pantalla de eliminar cuenta, donde despues de logearse ppor ultima vez, se eliminara su cuenta y le llevará
directo a la pantalla login de nuevo.

Si desplegamos el menu lateral, se podrá ir a las diferentes pantallas. Pilotos, Circuitos, Apuestas y Usuario.
Tanto en la seccion de pilotos como en la de circuitos se puede eliminar, editar y añadir(pulsando los botones de la papelera, el lápiz y
el simbolo más).

En la seccion de apuestas, se podrá crear, y eliminar apuestas(creadas con un piloto y un circuito)

Si vamos al apartado usuario, se mostrará una pantalla con la información del usuario actual y como he dicho antes, se puede ir a los ajustes
pulsando el engranaje arriba a la derecha de la pantalla.

Y por ultimo, si le damos al boton atras, en la pantalla home, aparecerá un mensaje para saber si quiere seguir en la app, o
si el usuario quiere salir de ella, en cuyo caso, se cerrará la aplicación.

## Bibliografia
Estos son algunos de los recursos que mas he estado utilizando

-Apuntes del profesor Antonio:

https://docs.google.com/document/d/1NrwmYTzGyDSHsWhuyrN-HTdMP9Mcx5qnFSygPzM0Ffo/edit
https://docs.google.com/document/d/1Rbhk2wHyX0-3msmr6FuSFBTapHFRzTXDuPPctB7me-U/edit

-Documentación oficial:

https://m2.material.io/components/dialogs/android
https://developer.android.com/reference/android/media/MediaPlayer
https://stackoverflow.com/
https://developer.android.com/studio/write/create-app-icons

-Youtube:

https://www.youtube.com/@AristiDevs
https://www.youtube.com/@ProgrammingKnowledge