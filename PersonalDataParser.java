import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class PersonalDataParser {

    private static final String DATA_FORMAT = "dd.MM.yyyy";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите данные в формате: Фамилия Имя Отчество дата_рождения номер_телефона пол");
            String input = scanner.nextLine();

            String[] data = input.split(" ");

            if (data.length != 6) {
                System.out.println("Неверное количество данных. Требуется 6, введено " + data.length);
                return;
            }

            String lastName = data[0];
            String firstName = data[1];
            String middleName = data[2];
            String birthDate = data[3];
            String phoneNumber = data[4];
            String gender = data[5];

            try {
                validateBirthDate(birthDate);
                validatePhoneNumber(phoneNumber);
                validateGender(gender);
            } catch (InvalidDataException e) {
                System.out.println("Неверный формат данных: " + e.getMessage());
                return;
            }

            String fileName = lastName + ".txt";
            File file = new File(fileName);

            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(String.format("%s %s %s %s %s %s\n", lastName, firstName, middleName, birthDate, phoneNumber, gender));
            } catch (IOException e) {
                System.out.println("Ошибка при записи в файл: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("Данные успешно записаны в файл " + fileName);
        }
    }

    private static void validateBirthDate(String birthDate) throws InvalidDataException {
        try {
            LocalDate.parse(birthDate, DateTimeFormatter.ofPattern(DATA_FORMAT));
        } catch (DateTimeParseException e) {
            throw new InvalidDataException("Неверный формат даты рождения. Требуется формат " + DATA_FORMAT, e);
        }
    }

    private static void validatePhoneNumber(String phoneNumber) throws InvalidDataException {
        try {
            Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Неверный формат номера телефона. Требуется целое беззнаковое число", e);
        }
    }

    private static void validateGender(String gender) throws InvalidDataException {
        if (!gender.equals("f") && !gender.equals("m")) {
            throw new InvalidDataException("Неверный пол. Требуется символ f или m");
        }
    }

    private static class InvalidDataException extends Exception {

        public InvalidDataException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidDataException(String message) {
            super(message);
        }
    }
}