package operator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LocalDataGenerator {

    public static void main(String[] args) {
        System.out.println("--- Local School Data Generator ---");

        IdGenerator idGenerator = new IdGenerator();
        Operations operations = new Operations();
        
        SchoolUtils schoolUtils = new SchoolUtils(idGenerator);
        SchoolService logicSimulator = new SchoolService(operations, schoolUtils);
        
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of records to generate: ");
        int quantity = scanner.nextInt();
        scanner.close();

        System.out.println("Starting data generation for " + quantity + " records...");

        try {
            byte[] zipData = logicSimulator.generateSchoolData(quantity);

            if (zipData == null) {
                System.err.println("Data generation failed and returned no data. Check logs for errors.");
                return;
            }

            String userHome = System.getProperty("user.home");
            Path desktopPath = Paths.get(userHome, "Desktop", "school_data_" + quantity + ".zip");

            System.out.println("Generation complete. Saving file to: " + desktopPath);

            try (FileOutputStream fos = new FileOutputStream(desktopPath.toFile())) {
                fos.write(zipData);
            }

            System.out.println("File saved successfully to your Desktop!");

        } catch (IOException e) {
            System.err.println("An error occurred while saving the file.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred during data generation.");
            e.printStackTrace();
        }
    }
	
}
