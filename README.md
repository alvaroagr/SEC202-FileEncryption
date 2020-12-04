# Cifrador/Descifrador de archivos
Realizado por Juan David Carvajal, Alvaro Andres Gomez Rey, Juan David López, Julian Mabesoy

## Introducción
Decidimos escoger éste entre los proyectos disponibles, debido a que se basaba en algo que vimos en clase y que daba la oportunidad de profundizar un poco.

El programa desarrollado tiene dos opciones:
  1. Cifrar archivo: Debe recibir como entrada un archivo cualquiera, y una contraseña. A partir de la contraseña, debe generarse una clave de 128 bits, empleando el algoritmo PBKDF2. Por último, el archivo debe cifrarse con el algoritmo AES, usando la clave obtenida; el resultado debe escribirse a otro archivo, que debe contener también el hash SHA-1 del archivo sin cifrar.
  1. Descifrado. Debe recibir como entrada un archivo cifrado y la contraseña. El programa deberá descifrar el archivo y escribir el resultado en un archivo nuevo. Luego, debe computar el hash SHA-1 del archivo descifrado y compararlo con el hash almacenado con el archivo cifrado.

## Desarrollo

### PBKDF2
En primer lugar, era necesario convertir la contraseña de texto que fuera a ingresarse en una clave de 128-bits, es decir, de 8-bytes, utilizando PBKDF2. Como indican los *Public-Key Cryptography Standards*:
> *PBKDF2 aplica una funcion seudoaleatoria para derivar claves.*
>
> [Request for Comments: 8018 - Password-Based Cryptography Specification Version 2.1][1]

Entonces, la funcion ```PBKDF2 (P, S, c, dkLen)``` tiene los siguientes parametros:
- P: La contraseña, la cual es un string.
- S: El *salt* que es una secuencia de bytes.
- c: El número de iteraciones que se llevara a cabo la funcion seudoaleatoria, un int.
- dkLen: La longitud en bits de la clave que sera generada.

Por ende, se desarollo el siguiente metodo, basandose en [una implementación existente con la función seudoaleatoria HmacSHA12][2]
```java
public byte[] PBKDF2(char[] password, byte[] salt, int c, int length) {
	try {
		SecretKeyFactory kf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		PBEKeySpec spec = new PBEKeySpec(password, salt, c, length);
		SecretKey key = kf.generateSecret(spec);
		return key.getEncoded();
	} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
		throw new RuntimeException(e);
	}
}
```

### Cifrado y descifrado de archivos
Realizar esta parte fue más sencillo, debido a que se trabajo este concepto durante uno de los entregables de la clase. En dicha ocasión, era necesario cifrar mensajes hexadecimales para obtener resultados hexadecimales cifrados ya conocidos utilizando AES. Para resumir, el código relevante para el problema es el siguiente:
```java
// theKey y theMsg son de tipo byte[]
theKey = hexToBytes("00000000000000000000000000000000");
theMsg = hexToBytes("ffffffffffffc0000000000000000000")
KeySpec ks = new SecretKeySpec(theKey, "AES");
Cipher cf = Cipher.getInstance("AES/ECB/NoPadding");
cf.init(Cipher.ENCRYPT_MODE,(SecretKeySpec) ks);
byte[] theCph = cf.doFinal(theMsg);
```
Lo que nos interesa son las ultimas cuatro lineas: creamos una especificacion de claves con la clave dada y especificando el modo AES, inicializamos un cifrado en modo de cifrado con dicha clave y especificando la transformación que deseamos usar, y finalmente ciframos el mensaje a un arreglo de bytes cifrado.

Entonces, si se quiere adaptar dicho codigo al problema, simplemente es cuestión de leer el contenido de un archivo, aplicar el mismo procedimiento, y escribir el contenido cifrado en un archivo con el mismo nombre y que finalize en `.cif`, y para reversarlo es solo cuestion de cambiar el modo a `DECRYPT_MODE`. Los metodos que se crearon fueron estos:
```java
public void encrypt(byte[] key, File in, File out) 
		throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
		IllegalBlockSizeException, BadPaddingException {
	// Initialize the cipher
	KeySpec ks = new SecretKeySpec(key, "AES");
	Cipher cf = Cipher.getInstance("AES/ECB/PKCS5Padding");
	cf.init(Cipher.ENCRYPT_MODE, (SecretKeySpec) ks);
	
	// Initialize the Input and Output streams
	FileInputStream fis = new FileInputStream(in);
	FileOutputStream fos = new FileOutputStream(out);
	
	// Determine the size of the buffer
	int bufferBytes = Math.min(fis.available(), 64);
	byte[] buffer = new byte[bufferBytes];
	
	// While remaining bytes still fit in a 64byte buffer.
	while(buffer.length == 64) {
		fis.read(buffer);
		byte[] encryptedBuffer = cf.update(buffer);
		fos.write(encryptedBuffer);
		bufferBytes = Math.min(fis.available(), 64);
		buffer = new byte[bufferBytes];
	}
	// Last portion of data
	fis.read(buffer);
	byte[] encryptedBuffer = cf.doFinal(buffer);
	fos.write(encryptedBuffer);
	
	// Close the Input and Output streams
	fis.close();
	fos.close();
}

public void decrypt(byte[] key, File in, File out) 
		throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
		IllegalBlockSizeException, BadPaddingException {
	// Initialize the cipher
	KeySpec ks = new SecretKeySpec(key, "AES");
	Cipher cf = Cipher.getInstance("AES/ECB/PKCS5Padding");
	cf.init(Cipher.DECRYPT_MODE, (SecretKeySpec) ks);
	
	// Initialize the Input and Output streams
	FileInputStream fis = new FileInputStream(in);
	FileOutputStream fos = new FileOutputStream(out);
	
	// Determine the size of the buffer
	int bufferBytes = Math.min(fis.available(), 64);
	byte[] buffer = new byte[bufferBytes];
	
	// While remaining bytes still fit in a 64byte buffer.
	while(buffer.length == 64) {
		fis.read(buffer);
		byte[] encryptedBuffer = cf.update(buffer);
		fos.write(encryptedBuffer);
		bufferBytes = Math.min(fis.available(), 64);
		buffer = new byte[bufferBytes];
	}
	// Last portion of data
	fis.read(buffer);
	byte[] encryptedBuffer = cf.doFinal(buffer);
	fos.write(encryptedBuffer);
	
	// Close the Input and Output streams
	fis.close();
	fos.close();
}
```

### Hash SHA-1
La parte del cálculo de hash usando SHA-1 fue interesante. Tuvimos algunos problemas inicialmente, pues los algoritmos encontrados generalmente retornaban el cálculo del hash como bytes. Para la aplicación de nuestro proyecto, era necesario que el hash calculado tuviera su representación en String, para que pueda ser imprimido como texto en un archivo. Observemos el Código:
```java
public static String computeSHA1(File file) throws Exception {
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		FileInputStream fis = new FileInputStream(file);

		byte[] data = new byte[1024];
		int read = 0;
		while ((read = fis.read(data)) != -1) {
			sha1.update(data, 0, read);
		}
		;
		byte[] hashBytes = sha1.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hashBytes.length; i++) {
			sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		String fileHash = sb.toString();
		fis.close();
		return fileHash;

	}
```
Como se puede observar, el problema fue resuelto utilizando un StringBuffer, que efectivamente corría un ciclo sobre los bytes del hash, y lo transformaba a un formato hexadecimal que podía ser interpretado como un String al final del ciclo (en cada iteración se iban guardando los subproductos en el StringBuffer).
Consideramos que la función debería ser estática, pues es de propósito general para cualquier archivo, y no requiere de la creación de instancias de ninguna clase para su ejecución efectiva.
### GUI

#### Carga de archivos

En la parte de cargar los archivos, al principio se manejo solo documentos con extension .docx, cuando se termino el programa y se quizo cargar otro tipo de extensión no nos pemitia hacer el proceso que se venia haciendo anteriormente, entonces se modifico para que reconociera otro tipo de extensiones.

#### Boton "ENCRYPT"

En este boton se hacen algunas validaciones para poder encriptar el archivo, se empieza porque la contraseña que se ingresa no este vacaia, seguido de que se encripta la contraseña y se comprueba que exista un archivo que encriptar, y por ultimo se hace la encriptación, y se generan dos archivos .cif & .hash. Si alguna de las validaciones son incorrectas, se mostrar un mensaje de error.

```java
JButton btnEncrypt = new JButton("ENCRYPT");
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtEncryptContra == null || String.valueOf(txtEncryptContra.getPassword()).equals("") )  {
					JOptionPane.showMessageDialog(frame, "FORMATO NO VALIDO", "ERROR",JOptionPane.ERROR_MESSAGE);
				}else {
					char[] contra = txtEncryptContra.getPassword();
					if(inEnc!= null) {						
						try {
							byte[] key = modelo.PBKDF2(contra, SALT.getBytes(), ITERATIONS, KEY_LENGTH);
							File outEnc = new File(inEnc.getAbsolutePath()+".cif");
							File outHash = new File(inEnc.getAbsolutePath()+".hash");
							
							// Encrypt the file
							modelo.encrypt(key, inEnc, outEnc);
							
							// Generate hash
							modelo.generateSHA1(inEnc, outHash);
//							modelo.computeSHA1(inEnc).getBytes(Charset.forName("UTF-8"));
							
							
							
							
							
							
							JOptionPane.showMessageDialog(frame, "Se ha cifrado el archivo: " +inEnc.getCanonicalPath(), "Encriptar",JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
```

#### Boton "DECRYPT"

Este boton procede a validar que la contraseña ingresada y el archivo cargado  no esten vacios, seguido de desencriptar el archivo cargado, por ultimo al archivo desencriptado se le añade la verificacion del sha-1 para conocer si este ha sido manipulado o no. Si alguna de las validaciones son incorrectas, se mostrar un mensaje de error.

```java
JButton btnDecrypt = new JButton("DECRYPT");
		btnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textDecryptContra == null || String.valueOf(textDecryptContra.getPassword()).equals("") )  {
					JOptionPane.showMessageDialog(frame, "FORMATO NO VALIDO", "ERROR",JOptionPane.ERROR_MESSAGE);
				}else {
					char[] contra = textDecryptContra.getPassword();
					if(inDec!= null) {						
						try {
							String path = inDec.getAbsolutePath();
							path = path.substring(0, path.length() - 4);
							File outDec = new File(path);
							
							byte[] key = modelo.PBKDF2(contra, SALT.getBytes(), ITERATIONS, KEY_LENGTH);
							
							modelo.decrypt(key, inDec, outDec);
							
							if(modelo.verifySHA1(outDec, inHash)) {
								JOptionPane.showMessageDialog(frame, "Su archivo ha sido descifrado. Los hashes coinciden.", "Hash Valido",JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(frame, "Su archivo ha sido descifrado, pero puede que haya sido manipulado. Los hashes no coinciden.", "Hash Invalido",JOptionPane.WARNING_MESSAGE);
							}
							
//							if(modelo.decrypt(modelo.PBKDF2(contra, SALT.getBytes(), ITERATIONS, KEY_LENGTH), inDec, new File(inDec.getParent()+"/"+"decrypt.docx"), inHash)== true) {
//								JOptionPane.showMessageDialog(frame, "El hash del cifrado concide con el decifrado", "Hash Valido",JOptionPane.INFORMATION_MESSAGE);
//							}else {
//								JOptionPane.showMessageDialog(frame, "El hash del cifrado no concide con el decifrado", "Hash Invalido",JOptionPane.ERROR_MESSAGE);
//							}
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(frame, "La contraseña no concide", "ERROR",JOptionPane.ERROR_MESSAGE);
						}
					}	
				}
			}
		});

```

## Dificultades
- Dificultad a la hota de cifrar archivos por encima de los 2.1~ GB
- Agregar el SHA1 al final del archivo. Entendemos como se deberia hacer, pero no supimos hacerlo apropiadamanete, así que lo que se hizo fue que el hash fuese generado como un archivo por aparte que tambien hay que incluir al descifrar archivos.
- Por parte de la interfaz grafica esta se implemento por medio de JavaFx, el cual ninguno de los integrantes manejaba muy bien, se procedió a reaprender este entorno para poder desarrollar la aplicación de forma correcta.
- En la carga de archivos en un principio solo estabamos tomando en cuenta el cifrado para documentos con extensiones .docx, lo cual no era una buena practica, asi que se modifico esta parte, para que pudiese cargar diferentes tipos de extensiones.

## Conclusiones


[1]: https://tools.ietf.org/html/rfc8018#section-5.2
[2]: https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html#secretkeyfactory-algorithms

**Universidad Icesi. Curso de Ciberseguridad**
**2020.**
