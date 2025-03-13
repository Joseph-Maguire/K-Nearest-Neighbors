
/* Random Input Generator
 * Joseph Maguire
 * This file generates datasets for the KNN file. A different number of datasets can be chosen, as well as a different number of points for each dataset.
 * For this demonstration, four datasets were created, and each dataset differs in the number of points by an increase of 2500. 
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomInputGenerator {
    public static void main(String[] args) {
        final int NUM_INPUTS = 4;
        int fileNum = 1;
        int numPoints = 0;
        int x, y;
        char category;
        FileWriter writer;
        String fileName;
        Random random = new Random();

        for (int i = 0; i < NUM_INPUTS; i++) {
            numPoints += 2500; // Increments the input size each iteration
            fileName = "input_size" + fileNum; // Update the file name each time the input size is increased
            try {
                // Write to the new file
                writer = new FileWriter(fileName + ".csv");
                writer.write(numPoints + "\n");
                writer.write("Feature 1,Feature 2,Feature 3,Class\n");

                for (int j = 0; j < numPoints; j++) {
                    x = random.nextInt(100) + 1;
                    y = random.nextInt(100) + 1;

                    if (x == y) {
                        category = random.nextBoolean() ? 'A' : 'B';
                    }
                    if (y > x) {
                        category = 'A';
                    } else {
                        category = 'B';
                    }
                    writer.write(x + "," + y + "," + "," + category + "\n");
                }
                System.out.println("Succesfully wrote to " + fileName + ".csv");
                fileNum++;
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred while attempting to write to " + fileName + ".csv");
                e.printStackTrace();
            }
        }
    }
}
