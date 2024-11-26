import java.io.*;
import java.util.*;

public class ModelEditor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите путь к входному файлу (inputFile):");
        String inputFile = scanner.nextLine();

        System.out.println("Введите путь для сохранения обработанного файла (outputFile):");
        String outputFile = scanner.nextLine();

        System.out.println("Введите индексы вершин для удаления (через запятую):");
        String[] verticesInput = scanner.nextLine().split(",");
        Set<Integer> verticesToRemove = new HashSet<>();
        for (String vertex : verticesInput) {
            verticesToRemove.add(Integer.parseInt(vertex.trim()));
        }

        try {

            File input = new File(inputFile);
            if (!input.exists()) {
                System.err.println("Входной файл не найден: " + inputFile);
                return;
            }

            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            List<String> processedLines = processModel(lines, verticesToRemove);

            File output = new File(outputFile);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                for (String line : processedLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            System.out.println("Файл успешно обработан. Результат сохранен в: " + outputFile);

        } catch (IOException e) {
            System.err.println("Ошибка при обработке файла: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<String> processModel(List<String> lines, Set<Integer> verticesToRemove) {
        List<String> processedLines = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("v ")) {
                int vertexIndex = extractVertexIndex(line);
                if (verticesToRemove.contains(vertexIndex)) {
                    continue;
                }
            } else if (line.startsWith("f ")) {
                if (polygonContainsVertices(line, verticesToRemove)) {
                    continue;
                }
            }
            processedLines.add(line);
        }
        return processedLines;
    }

    private static int extractVertexIndex(String line) {

        String[] parts = line.split(" ");
        return Integer.parseInt(parts[1]);
    }

    private static boolean polygonContainsVertices(String line, Set<Integer> verticesToRemove) {

        String[] parts = line.split(" ");
        for (int i = 1; i < parts.length; i++) {
            int vertexIndex = Integer.parseInt(parts[i]);
            if (verticesToRemove.contains(vertexIndex)) {
                return true;
            }
        }
        return false;
    }
}