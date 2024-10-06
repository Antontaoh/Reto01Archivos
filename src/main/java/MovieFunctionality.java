import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code MovieFunctionality} class provides functionalities for
 * managing movie data, including creating output folders, reading
 * movie information from a CSV file, reading HTML templates,
 * and generating HTML files for movies.
 */
public class MovieFunctionality {

    /**
     * Creates an output folder at the specified path.
     * If the parent directory does not exist, it is created.
     *
     * @param path The path where the output folder should be created.
     * @return A {@link File} object representing the created output folder.
     * @throws RuntimeException if the parent directory cannot be created.
     */
    public static File createOutputFolder(String path) {
        File file = new File(path);
        File parentDirectory = file.getParentFile();

        if (parentDirectory != null && !parentDirectory.exists()) {
            if (parentDirectory.mkdirs()) {
                System.out.println("Output folder created: " + parentDirectory.getAbsolutePath());
            } else {
                throw new RuntimeException("Failed to create output folder: " + parentDirectory.getAbsolutePath());
            }
        }

        return file;
    }

    /**
     * Reads movie data from a specified CSV file and
     * returns a list of {@link Movie} objects.
     *
     * @param fileName The name of the CSV file containing movie data.
     * @return An {@link ArrayList} of {@link Movie} objects.
     * @throws RuntimeException if there is an error reading the CSV file.
     */
    public static ArrayList<Movie> getMovies(String fileName) {
        ArrayList<Movie> movies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                var movie = new Movie();
                movie.setId(data[0]);
                movie.setTitle(data[1]);
                movie.setYear(Integer.valueOf(data[2]));
                movie.setDirector(data[3]);
                movie.setGenre(data[4]);
                movies.add(movie);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(movies);
        return movies;
    }

    /**
     * Reads the content of an HTML template file.
     *
     * @param html The path to the HTML template file.
     * @return A {@code String} containing the content of the template.
     * @throws RuntimeException if the template file is empty or cannot be read.
     */
    public static String getTemplates(String html) {
        try {
            String content = Files.readString(Paths.get(html));
            if (content == null || content.isEmpty()) {
                throw new RuntimeException("Template file is empty or not read properly: " + html);
            }
            System.out.println("Template read successfully: " + html);
            return content;
        } catch (IOException e) {
            throw new RuntimeException("Error reading the HTML template: " + html, e);
        }
    }

    /**
     * Generates HTML files for movies based on the data read from a CSV file
     * and a specified HTML template.
     *
     * @param csvFile The name of the CSV file containing movie data.
     * @throws RuntimeException if there is an error generating HTML files.
     */
    public static void generateHTMLFiles(String csvFile) {
        for (int i = 0; i < getMovies(csvFile).size(); i++) {
            String path = "Output/";
            String htmlName = getMovies(csvFile).get(i).getId() + " - " + getMovies(csvFile).get(i).getTitle() + ".html";
            String template = getTemplates("template.html");

            String newFile = template
                    .replace("%%2%%", getMovies(csvFile).get(i).getTitle())
                    .replace("%%3%%", String.valueOf(getMovies(csvFile).get(i).getYear()))
                    .replace("%%4%%", getMovies(csvFile).get(i).getDirector())
                    .replace("%%5%%", getMovies(csvFile).get(i).getGenre());
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(createOutputFolder(path + htmlName)))) {
                bw.write(newFile);
            } catch (IOException e) {
                System.err.println("Error generating HTML file for the movie: " + getMovies(csvFile).get(i).getTitle());
                e.printStackTrace();
            }
        }
    }
}