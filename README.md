# FXDesCipher

Esta aplicación se desarrolla para implementar el algoritmo DES, la aplicación le ofrese al usuario poder encriptar un texto plano o bien seleccionar un archivo de texto plano para cifrar su contenido.
Dentro de los parametros que tiene el usuario son:
  1. Clave: El usuario tiene la posibilidad tanto de escribir una clave o bien pulsar el boton de generar una contraceña de forma criptograficamente aleatoria.
  2. Algoritmo: Se puede seleccionar tanto el algoritmo DES como 3Des
  3. Modo: Permite al usuario seleccionar el modo de cifrado con tres opciones: ECB (Electronic Code Block), CFB (Cipher Feedback Block) y CBC (Cipher Block Chaining)
  4. Función: Permite al usuario indicar si lo que se desea es encriptar o desencriptar un texto plano.
  5. Texto: Caja de texto que le permite al usuario escribir el mensaje en texto plano que se desea cifrar.
  6. Ruta: Permite al usuario ingresar la ruta de un archivo que contenga el mensaje en texto plano, el cual se desea cifrar

Una vez que el usuario ha llenado los parametros procede a pulsar el boton "Inicio", esto hara que la aplicacion cifre el texto plano y el resultado lo codifica en base64, por ultimo lo muestra al usuario.

La aplicación tambien le permite al usuario guardar los resultados para que posteriormente pueda desencriptar el texto cifrado con los parametros seleccionados anteriormente.

**Autores**:
  - Guillermo Trejos Sosa
  - Yuliana Leiton Alvarez
  - Steven Rey Sequeira

**Dependencias**:
  1. JRE 8+: Para correr la aplicacion solo se requeire del Java Runtime Enviroment, en caso que se desee compilar se require del JDK 8+
  2. FontAwesome: Esta libreria tambien se encuentra en el .zip llamado "lib" o tambien lo pueden descargar del sitio: https://bitbucket.org/Jerady/fontawesomefx 

**Estructura**:
  - src: Contiene el codigo fuente de la aplicación, dentro de este directorio podemos encontrar los siguientes archivos:
    - fxdescipher: 
      - DesCipher.java: Esta clase contiene los metodos necesarios para poder cifrar o descifrar y tambien es la encargada de codificar en base64 el texto cifrado.
      - DialogUtil.java: Esta es una clase de utilidad para generar cuadros de dialogos, los cuales pueden ser informativos, de adevertencia y para mostrar errores
      - FXDesCipher.java: Esta clase contene el metodo "main" el cual es la entrada principal de esta aplicación
        - ctrl:
          - FXMLCipherController.java: Esta clase es el controllador para el archivo mencionado anteriormente, dentro de el se encuentra toda la logica del "negocio", con la que el usuario puede interactuar con la aplicación.
        - enu:
          - Algorithm.java: Esta instancia de tipo enum define los 2 tipos de algoritmos soportados en esta aplicación.
          - Encode.java: Esta instancia de tipo enum define los tipos de codificación en los que se podra codificar o decodifcar el texto cifrado.
        - ui:
          - FXMLCipher.fxml: Contiene la estrcutura en XML de la GUI propiamente
        - xml:
          - CipherParamsXml.java: Esta clase define una estructura que sera usado dentro de un archivo XML, el cual contendra los parametros principales con los que el usuario define para poder cifrar el texto plano.
          - CipherXml.java: Esta clase define una estructura que sera usado dentro de un archivo XML, representa el nodo raiz dentro del archivo XML, tambien contiene la estructura de la clase anterior y tambien posee un nodo para almacenar el texto cifrado
          - ManageXml.java: Esta clase contien 2 metodos estaticos que se encarga de realizar el "parseo" un archivo con formato XML con una estructura especifica a un Objeto Plano de Java.
        - resource:
          - css:
            - style.css: Esta hoja de estilos contiene estilos basicos para el comportamiento basico de los botones
