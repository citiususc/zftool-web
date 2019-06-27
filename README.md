# ZFTool-web
## Descripción
Herramienta web para la cuantificación de la proliferación celular en vivo del pez cebra a través de métodos de procesamiento de imágenes.
Esta aplicación tiene como objetivo el análisis de la efectividad de fármacos anti-cáncer a partir de imágenes de peces zebra. El software permite al usuario subir fotografías de peces zebra a los que se les han inoculado células cancerígenas teñidas con un componente de fluorescencia y fármacos experimentales. Con estas imágenes, y mediante técnicas de visón artifical, el programa realizará un análisis y elaborará un breve informe de efectividad del fármaco. Este estará compuesto por el umbral óptimo de fluorescencia, el índice de proliferación celular, gráficas de la evolución del área y de la intensidad de la zona fluorescente y de una imagen animada que mostrará esta evolución para las 0h y las 24-48-72h post inyección de la masa tumoral. A mayores para la sección 3D se generará el volumen de la masa tumoral a partir del conjunto de cortes 2D que el usuario ha cargado.


## Instalación
Los pasos para la instalación y el despliegue de la aplicación en un entorno Ubuntu serían los siguientes:

 1. Instalación de Python2 y de los paquetes necesarios mediante el terminal:
	```
		$ sudo apt update
		$ sudo apt upgrade
		$ sudo apt install python2.7 python−pip
		$ sudo pip2 install scikit−image
		$ sudo pip2 install numpy scipy
		$ sudo pip2 install mayavi
	```
 2. Instalación de Java8 (versión mínima a instalar):
	``` 
		 $ sudo add−apt−repository ppa:webupd8team/java
		 $ sudo apt-get update
		 $sudo apt-get install oracle-java8-installer
	```
	 
 3. Mover la librería nativa (/lib/libopencv_java249.so) a un directorio concreto. Podemos crear un directorio en la ruta /usr/share/tomcat7/lib.
 4. Instalar Apache Tomcat7 con el comando `$sudo apt-get install tomcat7`. Editamos el archivo /etc/tomcat7/policy.d/03catalina.policy y añadimos los permisos que se pueden ver a continuación:
	 
	 ```
			grant codeBase "file:/var/lib/tomcat7/webapps/zebrafish/-"{
				permission java.security.AllPermission;                 
			}
			
			grant codeBase "file:/var/lib/tomcat7/webapps/zebrafish.war/-"{
				permission java.security.AllPermission;
			}
     ```
	
 
 5. Creamos el script `setenv.sh` en el directorio /usr/share/tomcat7/bin para definir el tamaño mínimo y máximo del heap para la máquina virtual de Java y el directorio en el que se encuentran las librerías nativas de la aplicación.
	 ```
			export CATALINA_OPTS="$CATALINA_OPTS -Xms512m"
			export CATALINA_OPTS="$CATALINA_OPTS -Xmx512m"
			export CATALINA_OPTS="$CATALINA_OPTS -Djava.library.path=/usr/share/tomcat7/lib"
     ```

	Luego le damos permisos de ejecución al script con el comando `$ sudo chmod +x setenv.sh`

 6.  Nos aseguramos que Tomcat se ejecute con Java8, editando en el archivo /etc/default/tomcat8 modificando la línea:
		```
			JAVA_HOME=/usr/lib/jvm/java-8-oracle
		```
		
 7. Creamos el directorio para almacenar las imágenes enviadas al servidor por ejemplo /opt/ZebraFish y le damos permisos de escritura con los siguientes comandos desde el directorio /opt:
    ```	
		$ sudo mkdir ZebraFish
		$ sudo chown tomcat7 ZebraFish
		$ sudo chgrp tomcat7 ZebraFish
		$ sudo chmod 755 ZebraFish/
    ```
    
 8. En este directorio /opt/ZebraFish copiamos los archivos de Python (*0h_3D.py*,
*0h_reprocesar3D.py*, *48h_3D.py* y *48h_reprocesar3D.py*) que son necesarios para generar el volumen 3D de la masa tumoral y que se encuentran en el proyecto dentro de la carpeta ProcessFiles.
 9. Configuramos el perfil de producción en el archivo del proyecto *pom.xml* para definir el directorio donde se almacenan las imágenes enviadas al servidor. Es necesario detener tomcat con el comando `$ sudo service tomcat7 stop`.
	 ```
			<profile>
				<id>prod</id>
				<properties>
					<deployment.url>http://localhost:8080</deployment.url>
					<files.path>/opt/ZebraFish</files.path>
				</properties>
			</profile>	
	```
 
 10. Copiamos el fichero zebrafish.war en la carpeta webapps del servidor de aplicaciones Java Apache Tomcat (/var/lib/tomcat7/webapps). Reiniciamos tomcat con el comando `$ sudo service tomcat7 start`.
 11.  Una vez arrancado el servidor tenemos acceso a la aplicación **ZFTool** a través del nombre **/zebrafish** en la url del servidor. Si el servidor está corriendo con los parámetros por defecto el aceso a la aplicación sería a través de la url localhost:8080/zebrafish. 

## Autores
María José Carreira Nouche, Nicolás Vila Blanco y Marcos Rodríguez López

## Citación
Si usas **ZFTool**, por favor, cita este artículo:
> P Cabezas-Sainz, J Guerra-Varela, MJ Carreira, J Mariscal, M Roel, JA Rubiolo, A Sciara, M Abal, L Botana, R Lopez e L Sanchez, “Improving zebrafish embryo xenotransplantation conditions by increasing incubation temperature and establishing a proliferation index with ZFTool”, *BMC Cancer*, *Biomed Central*, vol. 3, no. 8, 2018. [https://doi.org/10.1186/s12885-017-3919-8](https://doi.org/10.1186/s12885-017-3919-8)

## Licencia
Este proyecto está licenciado bajo los términos de la [licencia MIT](https://github.com/citiususc/zftool-web/blob/master/LICENSE.md).
