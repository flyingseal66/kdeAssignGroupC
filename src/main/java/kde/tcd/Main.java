package kde.tcd;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //FileUtils.fetchData();

        OntCreator.createOntology();

        QueryWindow window = new QueryWindow();
        window.launch();
    }
}
