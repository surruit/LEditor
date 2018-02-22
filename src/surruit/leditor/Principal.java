package surruit.leditor;
/**
 * Clase lanzadora de la aplicación
 * @author Surruit
 */
public class Principal {
    /**
     * Lanza la aplicación
     * @param args parametros de linea de comandos. No usados
     */
    public static void main(String args[]){
        Ventana ventana = new Ventana();
        
        ventana.setVisible(true);
        ventana.setDefaultCloseOperation(Ventana.EXIT_ON_CLOSE);
    }
}
