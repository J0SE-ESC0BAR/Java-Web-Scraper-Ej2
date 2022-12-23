import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {

        //Array de links
        List <String> links = new ArrayList<>();
        links.add("https://www.bbc.com/news/world-us-canada-64061588");
        links.add("https://www.bbc.com/news/world-us-canada-64066584");
        links.add("https://www.bbc.com/news/world-europe-64072570");
        links.add("https://www.bbc.com/news/world-us-canada-64071724");

        /*Stream() permite ejecutar un metodo detras de otro con un punto
         *  parallel() permite ejecutar los metodos en paralelo
         *  forEach() permite ejecutar un metodo para cada elemento de la lista
        
        EJEMPLO
         *  links.stream().parallel().forEach(link -> System.out.println(getWebContenido(link)));

         *  Ahora veamos la diferencia entre parallel() y sin parallel()    */
        //Sin parallel()
        //creo variables para tomar el tiempo de ejecucion
        long startTime = System.currentTimeMillis();
        links.stream().forEach(link -> getWebContenido(link));
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion sin parallel(): "+(endTime-startTime)+" ms");
        //Con parallel()
        startTime = System.currentTimeMillis();
        links.stream().parallel().forEach(link -> getWebContenido(link));
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion con parallel(): "+(endTime-startTime)+" ms");
    }   

    //Metodo para descargar la pagina y retornarla en string
    //Bloquear la funcion para que no se ejecute en paralelo "synchronized" para que no la puedean usar hasta que termine
    
    private synchronized static String getWebContenido(String link){
        System.out.println("Inicio de descarga");
        //Imprime el enlace que esta descargando
        System.out.println("Descargando: "+link);
        try {
        //Download webs
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String iformacion_Pag = conn.getContentEncoding();

        InputStream input = conn.getInputStream();
        
        Stream<String> lines =  new BufferedReader(new InputStreamReader(input))
            .lines();
            System.out.println("Fin de descarga");
            return lines.collect(Collectors.joining("\n"));
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error al descargar la pagina "+e.getMessage());
        }
        return "ERROR";
    }
}