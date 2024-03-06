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
Nada mas abras la aplicación, si tienes el sonido activado, escucharás un sonido de coche de F1
y verás en el dispositivo al mismo tiempo una pantala splash que aparecerá y desaparecerá junto con el sonido unos segundos despues.
![splash](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/abbe5e75-17e2-400e-9d7f-e3aa0a684088)

A coninuación, nos encontramos con la primera o técnicamente la segunda pantalla, la pantalla de login.
![login](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/c7c170f3-7afe-43b5-88a0-7f267ca32421)

Si estás registrado, solo tendras que poner tus credenciales y podras acceder a la pantalla home, 
pero si eres un nuevo usuario o has eliminado tu cuenta anterior, tendrás que pulsar el botón register que te redirigirá a la pantalla
donde puedes llevar a cabo el registro.
![reister](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/28ceb9e5-4c02-4d2c-b42e-9c4e2ea012e0)

Un vez registrado, estarás en la pantala home, donde un mensaje emergente te preguntará si eres
o no mayor de edad(si se pulsa que no, se cerrará la sesion y le redirigirá a la pantalla de login), si somos mayores de edad y continuamos, 
y se nos presentan varias opciones.
![homeAlert](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/2a806138-6f9c-4d42-9b46-fa5ac23be947)

El usuario puede pulsar en el menú desplegable para navegar entre las pantallas de pilotos, circuitos.., tambien puede hacer, si deja pulsado 
la pantalla durante un tiempo, que aparezca un menmú desplegale con dos opciones para ir a una pantalla de información(donde además se encuentra
un video tutorial sobre como apostar en la F1), o ir a ajustes, pantalla a la que tambien se puede acceder pulsando el engranaje de la barra superior
a la derecha(tanto en la pantalla home como en la de user info).
Si elegimos la opción de pulsar el engranaje e ir a ajustes, veremos una pantalla con dos botones para cerrar sesión, y otro para eliminar
la cuenta. Si el usuario pulsa el boton cerrar sesión, le redirigirá al login, en cambio si pulsa eliminar y confirma que quiere borrar
la cuenta, irá a la pantalla de eliminar cuenta, donde después de logearse por última vez, se eliminará su cuenta y le llevará
directo a la pantalla login de nuevo.
![ajustes](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/1004e43c-d874-4f72-9f1a-5e2fcd60c41a)
![confirmDeleteAccount](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/3ab4fbe9-97f4-425a-a491-ad5fd42cdacf)
![deleteFraGMENT](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/a89a4006-ae7a-4391-aaa1-a5f4e47912dc)

Si desplegamos el menu lateral, se podrá ir a las diferentes pantallas. Pilotos, Circuitos, Apuestas y Usuario.
![menuLateral](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/49a84384-6749-408d-85cb-219879151307)

Tanto en la sección de pilotos como en la de circuitos, se puede eliminar, editar y añadir(pulsando los botones de la papelera, el lápiz y
el simbolo más).
![pantallaCircuitos](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/8e16b145-bba8-4cb1-a4fb-a00f1060e453)
![pantallaPiloto](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/51268120-7892-488b-800a-b175f14bff48)
![pantallaCreaCircuito](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/1485bc4b-a27b-4573-b216-fff785dffc6d)
![pantllaEditarPiloto](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/93dc2b55-5f80-4502-b039-7cbbde75347c)
![deletePiloto](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/e4fb0569-b41e-4e9a-b8c3-5a05ccb30357)

En la sección de apuestas se podrá crear y eliminar apuestas(creadas con un piloto y un circuito)
![pantallaApustas](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/10658523-f454-4b6f-95d9-59967f6f1e78)
![pantallaCreateApuesta](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/17708b9e-531b-488a-9c8e-ffef090eac50)

Si vamos al apartado usuario, se mostrará una pantalla con la información del usuario actual y como he dicho antes, estando en esta pantalla
tambien se puede ir a los ajustes de la misma manera que en el home(pulsando el engranaje arriba a la derecha de la pantalla).
![pantallaUsuario](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/6da6d234-945b-4c8a-9a90-6fbc2b3a38c0)

Y por último, si le damos al botón atras, en la pantalla home, aparecerá un mensaje para saber si queremos seguir en la app, o
si por el contrario, el usuario quiere salir de ella, en cuyo caso, se cerrará la aplicación.
![confirmExit](https://github.com/FranSiciliaPerez/F1Bets/assets/72436014/b19aed7f-a987-4965-8b63-994c9523dd05)

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