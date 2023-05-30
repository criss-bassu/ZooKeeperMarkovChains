package prZooKeeper;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Operacion {
	
	private static final int MIN = 60;
	private static final int MAX = 199;
	public static final String PRACTICA2_HOST = System.getenv().getOrDefault("PRACTICA2_HOST", "http://localhost:80");
	private static final String URL_NUEVO = PRACTICA2_HOST + "/nuevo/";

	// Metodo para calcular la media de entre las medidas del fichero "medidas.txt"
	public static void lider() throws IOException, InterruptedException {
		List<String> medidas = leerFichero();
	    int sum = 0;
	    for(String value : medidas) {
	        sum += Integer.parseInt(value);
	    }
	    if (medidas.size() > 0) {
	      int media = sum / medidas.size();	// int y no for porque es lo que espera la API REST
	      System.out.println("Media de los valores: " + media);
	      // Se envía la media como petición
	      peticionHTTP(String.valueOf(media));
	    }
	}

	// Metodo para insertar una medida aleatorio en el fichero "medidas.txt"
	public static void nodo() throws InterruptedException {
		Random rnd = new Random();
    	// Crear valor aleatorio 
		int medida = rnd.nextInt(MIN, MAX);
		System.out.println("Medida generada: " + medida);
		// Escribir en fichero
		insertarEnFichero(String.valueOf(medida));
	}	
	
	// Inserta en el fichero la medida que se le pasa por parametro
	private static void insertarEnFichero(String medida) {
		try(FileWriter fw = new FileWriter(String.valueOf(Nodo.PATH_FILE), true);
			    BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
		    out.println(medida);
		} catch (IOException e) {	}
	}
	
	// Obtiene todas las medidas que hay en el fichero
	private static List<String> leerFichero() {
        try {
			return Files.readAllLines(Nodo.PATH_FILE, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e);
        }
        return new ArrayList<String>();
	}
	
	private static void peticionHTTP(String medicion) throws IOException {
		// Se crea la URL
		URL url = new URL(URL_NUEVO + medicion);
		
		// Se abre la conexión con la URL
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// El método de petición es GET
		conn.setRequestMethod("GET");
		
		// Chquear el resultado de la conexión
		int codigo = conn.getResponseCode();
		if(codigo == 500) {
			System.out.println("The server encountered an error while processing the request.");
		}else if(codigo == 404) {
			System.out.println("The requested resource was not found on the server.");
		}else if(codigo == 200){ // Peticion exitosa
			// Se lee la respuesta
			Reader r = new InputStreamReader(conn.getInputStream());
			BufferedReader rd = new BufferedReader(r);
			StringBuilder res = new StringBuilder();
			String linea;
			while ((linea = rd.readLine()) != null) {
			    res.append(linea);
			}
			rd.close();
	
			// Imprimir la respuesta
			System.out.println(res);
		}else {
			System.out.println("Otro error de conexión.");
		}
	}

}
