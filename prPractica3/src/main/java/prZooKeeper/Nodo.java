package prZooKeeper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Nodo {
	
	private static final String FILE = "medidas.txt";
	public static final Path PATH_FILE = Paths.get("fichero/" + FILE);
	
	public static void main(String[] args) {
		try {
			// Se necesita disponer de un fichero llamado "medidas.txt"
			if (!Files.exists(PATH_FILE)) {
			    // Crear el fichero si no existe
			    try {
			        Files.createFile(PATH_FILE);
			    } catch (IOException e) { }
			}
			
			ElectionNode nd = new ElectionNode();
			nd.start();
			while(true) {
				if(!nd.getImLeading()) {	// seguidor
					System.out.println("Soy seguidor");
					Operacion.nodo();
					Thread.sleep(5000);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

