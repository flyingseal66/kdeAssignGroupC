package kde.tcd;

import java.io.IOException;

/**
 * Create ontologies
 *
 * @author  Baolei, Rajesh, Sourojit
 */
public class Main {

    public static void main(String[] args) throws IOException {
        

    	OntCreator.createOntology();
    	WebLogin.WebLogin();
        /*QueryWindow window = new QueryWindow();
        window.launch();*/
    }
}
