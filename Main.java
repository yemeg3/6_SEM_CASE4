import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DataFileReader fileReader = new DataFileReader();
        DataProcessor dataProcessor = new DataProcessor();
        DataFileWriter fileWriter = new DataFileWriter();

        while (true) {
            System.out.println("\nУкажите метод работы");
            System.out.println("1. Ввод новых данных");
            System.out.println("2. Чтение данных из текстового файла");
            System.out.println("0. Выход");
            String choice = scanner.nextLine();

            try {
                if (choice.equals("1")) {
                    handleUserInput(fileWriter);
                } else if (choice.equals("2")) {
                    handleFileOperations(fileReader, dataProcessor, fileWriter);
                } else if (choice.equals("0")) {
                    break;
                } else {
                    System.out.println("Неправильный выбор.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleUserInput(DataFileWriter fileWriter) throws IOException {
        while (true) {
            System.out.println("\nВведите данные для сохранения в файл (введите 'exit' для сохранения или 0 для выхода):");
            List<String> userInputData = new ArrayList<>();
            String userInput;
            while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {
                if (userInput.equals("0")) {
                    return;
                }
                userInputData.add(userInput);
            }
            if (!userInputData.isEmpty()) {
                fileWriter.writeData("user_output.txt", userInputData);
                System.out.println("Данные пользователя сохранены в файл user_output.txt");
                return;
            } else {
                System.out.println("\nФайл пустой.");
            }
        }
    }

    private static void handleFileOperations(DataFileReader fileReader, DataProcessor dataProcessor, DataFileWriter fileWriter) throws IOException {
        while (true) {
            File folder = new File(".");
            File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

            if (listOfFiles != null && listOfFiles.length > 0) {
                System.out.println("\nДоступные файлы:");
                for (int i = 0; i < listOfFiles.length; i++) {
                    System.out.println((i + 1) + ". " + listOfFiles[i].getName());
                }
                System.out.println("\nВыберите файл для чтения (или 0 для выхода):");

                String input = scanner.nextLine();
                if (input.equals("0")) {
                    return;
                }
                int fileChoice;
                try {
                    fileChoice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный ввод. Пожалуйста, введите число.");
                    continue;
                }

                if (fileChoice > 0 && fileChoice <= listOfFiles.length) {
                    String filePath = listOfFiles[fileChoice - 1].getName();
                    List<String> data = fileReader.readData(filePath);
                    if (!data.isEmpty()) {
                        System.out.println("\nДанные из файла " + filePath + ":");
                        for (String line : data) {
                            System.out.println(line);
                        }
                        while (true) {
                            System.out.println("\nРабота с текстом:");
                            System.out.println("1. Отсортировать в алфавитном порядке");
                            System.out.println("2. Отсортировать в алфавитном порядке наоборот");
                            System.out.println("3. Фильтровать строки, содержащие числа");
                            System.out.println("4. Фильтровать строки, не содержащие числа");
                            System.out.println("5. Фильтровать строки, состоящие только из чисел");
                            System.out.println("6. Фильтровать строки, содержащие указанный текст");
                            System.out.println("0. Назад");

                            String command = scanner.nextLine();
                            List<String> processedData = null;
                            String filterText = "";

                            switch (command) {
                                case "1":
                                    processedData = dataProcessor.sortAlphabetically(data);
                                    System.out.println("\nОтсортированные данные:");
                                    break;
                                case "2":
                                    processedData = dataProcessor.sortReverseAlphabetically(data);
                                    System.out.println("\nОтсортированные данные:");
                                    break;
                                case "3":
                                    processedData = dataProcessor.filterLinesContainingNumbers(data);
                                    System.out.println("\nОтфильтрованные строки, содержащие числа:");
                                    break;
                                case "4":
                                    processedData = dataProcessor.filterLinesWithoutNumbers(data);
                                    System.out.println("\nОтфильтрованные строки, не содержащие числа:");
                                    break;
                                case "5":
                                    processedData = dataProcessor.filterLinesOnlyNumbers(data);
                                    System.out.println("\nОтфильтрованные строки, состоящие только из чисел:");
                                    break;
                                case "6":
                                    System.out.println("Введите текст для фильтрации:");
                                    filterText = scanner.nextLine();
                                    if (filterText.contains("?")) {
                                        System.out.println("Проверьте кодировку вашей консоли или терминала");
                                        break;
                                    }
                                    processedData = dataProcessor.filterLinesContainingText(data, filterText);
                                    System.out.println("\nРезультаты поиска по тексту \"" + filterText + "\":");
                                    break;
                                case "0":
                                    return;
                                default:
                                    System.out.println("Неправильная команда.");
                                    continue;
                            }

                            for (String line : processedData) {
                                System.out.println(line);
                            }

                            while (true) {
                                System.out.println("\n1. Сохранить");
                                System.out.println("0. Назад");

                                String saveChoice = scanner.nextLine();
                                if (saveChoice.equals("1")) {
                                    String newFilePath = generateNewFileName(filePath);
                                    fileWriter.writeData(newFilePath, processedData);
                                    System.out.println("Данные сохранены в файл " + newFilePath);
                                    break;
                                } else if (saveChoice.equals("0")) {
                                    break;
                                } else {
                                    System.out.println("Неправильный выбор.");
                                }
                            }
                        }
                    } else {
                        System.out.println("\nФайл пустой.");
                    }
                } else {
                    System.out.println("\nНеправильный выбор файла.");
                }
            } else {
                System.out.println("\nНет доступных файлов.");
                return;
            }
        }
    }

    private static String generateNewFileName(String baseFileName) {
        int counter = 1;
        String newFileName;
        File file;
        do {
            newFileName = baseFileName.substring(0, baseFileName.lastIndexOf('.')) + "_" + counter + ".txt";
            file = new File(newFileName);
            counter++;
        } while (file.exists());
        return newFileName;
    }
}

class DataFileReader {
    public List<String> readData(String filePath) throws IOException {
        List<String> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath), Charset.defaultCharset().name())) {
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        }
        return data;
    }
}

class DataProcessor {
    public List<String> sortAlphabetically(List<String> data) {
        List<String> sortedData = new ArrayList<>(data);
        sortedData.sort(String::compareTo);
        return sortedData;
    }

    public List<String> sortReverseAlphabetically(List<String> data) {
        List<String> sortedData = new ArrayList<>(data);
        sortedData.sort((s1, s2) -> s2.compareTo(s1));
        return sortedData;
    }

    public List<String> filterLinesContainingNumbers(List<String> data) {
        List<String> filteredData = new ArrayList<>();
        for (String line : data) {
            if (line.matches(".*\\d+.*")) {
                filteredData.add(line);
            }
        }
        return filteredData;
    }

    public List<String> filterLinesWithoutNumbers(List<String> data) {
        List<String> filteredData = new ArrayList<>();
        for (String line : data) {
            if (!line.matches(".*\\d+.*")) {
                filteredData.add(line);
            }
        }
        return filteredData;
    }

    public List<String> filterLinesOnlyNumbers(List<String> data) {
        List<String> filteredData = new ArrayList<>();
        for (String line : data) {
            if (line.matches("\\d+")) {
                filteredData.add(line);
            }
        }
        return filteredData;
    }

    public List<String> filterLinesContainingText(List<String> data, String filterText) {
        List<String> filteredData = new ArrayList<>();
        for (String line : data) {
            if (line.contains(filterText)) {
                filteredData.add(line);
            }
        }
        return filteredData;
    }
}

class DataFileWriter {
    public void writeData(String filePath, List<String> data) throws IOException {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new File(filePath), Charset.defaultCharset().name())) {
            for (String line : data) {
                writer.println(line);
            }
        }
    }
}
