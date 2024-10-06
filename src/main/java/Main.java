public class Main {
    public static void main(String[] args) {
        try {
            MovieFunctionality.generateHTMLFiles("peliculas.csv");
            System.out.println("HTML files generated successfully.");
        } catch (Exception e) {
            System.err.println("An error occurred during the HTML file generation process.");
            e.printStackTrace();
        }
    }
}
