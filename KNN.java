/* K Nearest Neighbors Algorithm
 * Joseph Maguire
 * To use this application, first run the RandomInputGenerator.java file, then run the KNN.java file.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class KNN {

    static class Point {
        char value; // Used to classify the group the point belongs to
        double x; // The x coordinate of the point
        double y; // The y coordinate of the point
        double distance; // Distance from this point to the test point
    }

    // The comparison method compares the two points and returns
    static class comparison implements Comparator<Point> {
        public int compare(Point first, Point second) {
            if (first.distance < second.distance) {
                return -1;
            } else if (first.distance > second.distance) {
                return 1;
            }
            return 0;
        }
    }

    // The knnClassify method uses the k nearest neighbor algorithm to find which
    // group the testing point is classified to
    static char knnClassify(Point array[], int numOfPoints, int k, Point testPoint) {
        PriorityQueue<Point> heap = new PriorityQueue<>(k, new Comparator<Point>() {
            public int compare(Point p1, Point p2) {
                return Double.compare(p2.distance, p1.distance);
            }
        });

        // Calculate the distance for each point and add to the heap
        for (int i = 0; i < numOfPoints; i++) {
            array[i].distance = Math.sqrt((array[i].x - testPoint.x) * (array[i].x - testPoint.x)
                    + (array[i].y - testPoint.y) * (array[i].y - testPoint.y));

            // Add point to the heap
            if (heap.size() < k) {
                heap.add(array[i]);
            } else if (array[i].distance < heap.peek().distance) {
                // If the current point is closer than the farthest point in the heap, replace
                // it

                heap.poll();
                heap.add(array[i]);
            }
        }

        // Heap contains the k nearest points, classify based on their values
        int amount1 = 0, amount2 = 0;
        while (!heap.isEmpty()) {
            Point p = heap.poll();
            if (p.value == 'A') {
                amount1++;
            } else if (p.value == 'B') {
                amount2++;
            }
        }
        return (amount1 > amount2 ? 'A' : 'B'); // Return the majority class
    }

    // Calculate how accurately the current defined k value is classifying the
    // dataset
    public static double calculateAccuracy(char[] predicted, char[] actual) {
        int numCorrectPredictions = 0;
        for (int i = 0; i < actual.length; i++) {
            if (predicted[i] == actual[i]) {
                numCorrectPredictions++;
            }
        }
        return (double) numCorrectPredictions / actual.length;
    }

    public static void main(String[] args) {
        // Parse and read input csv file for testing with different input sizes
        File inputFile;
        String fileName;
        Scanner scanner;
        String line;

        final int NUM_INPUT_SIZES = 4;
        int fileNum = 1;
        int numPoints;
        Point[] array;

        long startTime, endTime;

        for (int i = 0; i < NUM_INPUT_SIZES; i++) {
            System.out.println("Input File " + fileNum + ": ");
            fileName = "input_size" + fileNum;

            // Read the data
            try {
                inputFile = new File(fileName + ".csv");
                scanner = new Scanner(inputFile);

                numPoints = Integer.parseInt(scanner.nextLine()); // Read the number of inputs
                scanner.nextLine(); // Skip the next line with headers

                array = new Point[numPoints];
                for (int j = 0; j < numPoints; j++) {
                    line = scanner.nextLine(); // Read the next lines
                    String[] data = line.split(","); // Split the current line of text by commas
                    array[j] = new Point(); // Create a new data point
                    array[j].x = Double.parseDouble(data[0].trim());
                    array[j].y = Double.parseDouble(data[1].trim());
                    array[j].value = data[3].charAt(0);
                }
                // Split data into training and test sets
                int numTrainingPoints = (int) (0.7 * numPoints);
                Point[] trainingPoints = new Point[numTrainingPoints];
                System.arraycopy(array, 0, trainingPoints, 0, numTrainingPoints);

                int numTestPoints = numPoints - numTrainingPoints;
                Point[] testPoints = new Point[numTestPoints];
                System.arraycopy(array, numTrainingPoints, testPoints, 0, numTestPoints);

                // Classify the training data in array using some of the training points as test
                // points for the training data to test for accuracy
                startTime = System.currentTimeMillis();
                final int k = 3;
                char[] predicted = new char[numTestPoints];
                char[] actual = new char[numTestPoints];
                for (int l = 0; l < numTestPoints; l++) {
                    predicted[l] = knnClassify(trainingPoints, numTrainingPoints, k, testPoints[l]);
                    actual[l] = testPoints[l].value;
                }

                // Create the points to be classified
                Random random = new Random();
                Point testPoint1 = new Point();
                testPoint1.x = random.nextInt(100) + 1;
                testPoint1.y = random.nextInt(100) + 1;

                // Display the results
                System.out.printf("Accuracy: %.2f%%\n", calculateAccuracy(predicted, actual) * 100);
                System.out.println("Test Point (" + testPoint1.x + ", " + testPoint1.y + ") classified as Group "
                        + knnClassify(array, numPoints, k, testPoint1) + "\n");

                endTime = System.currentTimeMillis();

                System.out.println("Runtime: " + (endTime - startTime) + "ms\n");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("An error occurred while attemping to read" + fileName + ".csv");
            }
            fileNum++;
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}