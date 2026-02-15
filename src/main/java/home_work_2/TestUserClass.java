package home_work_2;

import home_work_2.dao.UserDaoPsqlImpl;
import home_work_2.models.User;
import home_work_2.services.UserService;
import home_work_2.utils.HibernateSessionFactoryUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.*;

import static home_work_2.utils.AnsiColors.*;

public class TestUserClass {

    private static final UserService userService = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    private enum LogLevel{
        INFO,
        ERROR,
        WARNING
    };
    private static final BiConsumer<String, LogLevel> stdOutLog = (msg, level) -> {
        var logPrefix = level == LogLevel.INFO  ? GREEN + "[INFO]"  + RESET :
                        level ==LogLevel.ERROR  ? RED   + "[ERROR]" + RESET :
                        YELLOW + "[WARNING]" + RESET;
        System.out.printf("%s %s\n", logPrefix, msg);
    };

    private static final BiPredicate<String, Predicate<Integer>> isValidNumber = (s,inRange) -> {
        try{
            int v = Integer.parseInt(s);
            return inRange.test(v);
        } catch (NumberFormatException e){
            return false;
        }
    };

    public static void main(String[] args) throws Exception {
        renUserApp();
        HibernateSessionFactoryUtil.shutdown();
    }

    public static void renUserApp(){
        boolean exit = false;
        while(!exit){
            mainMenu();
            String userInput = scanner.nextLine();
            if(!isValidNumber.test(userInput, n -> n >= 0 && n <= 5)){
                System.out.println(RED + "Не верный ввод: " + RESET + userInput);
                continue;
            }
            int choice = Integer.parseInt(userInput);

            switch (choice){
                case 1 -> createUser();
                case 2 -> findUserBiId();
                case 3 -> findAllUser();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> {
                    exit = true;
                    scanner.close();
                }
            }
        }
    }

    public static void mainMenu(){
        System.out.println("\n" + CYAN +
                "╔══════════════════════════════════════════════╗\n" +
                "║ Г Л А В Н О Е М Е Н Ю ║\n" +
                "╠══════════════════════════════════════════════╣" + RESET);
        System.out.println("\t\t\t>>> USER APP | MENU <<<");
        System.out.println(GREEN + " 0 " + RESET + " - Выход");
        System.out.println(GREEN + " 1 " + RESET + " - Создать пользователя");
        System.out.println(GREEN + " 2 " + RESET + " - Найти пользователя по id");
        System.out.println(GREEN + " 3 " + RESET + " - Найти всех пользователей");
        System.out.println(GREEN + " 4 " + RESET + " - Обновить данные пользователя");
        System.out.println(GREEN + " 5 " + RESET + " - Удалить пользователя");

        System.out.println(CYAN +
                "╚══════════════════════════════════════════════╝" +
                RESET);

        System.out.print(YELLOW + "Ваш выбор → " + RESET + "Введите число от 0 до 5 : ");
    }

    public static void createUser(){
        User user = validatorInputUserData();
        if(user != null){
            try{
                userService.saveUser(user);
                stdOutLog.accept(String.format("Пользователь [%s] создан ", user.getName()), LogLevel.INFO);
            }
            catch (Exception e){
                stdOutLog.accept(e.getMessage(), LogLevel.ERROR);
            }
        }
    }

    public static User findUserBiId(){
        User user = null;
        System.out.printf("Введите id пользователя (от 0 до %s):\n", Integer.MAX_VALUE);
        String userInputId = scanner.nextLine();
        if(!isValidNumber.test(userInputId, n -> n > 0)){
            stdOutLog.accept("Не верный ввод: " + userInputId, LogLevel.WARNING);
        }
        else {
            try {
                var id = Integer.parseInt(userInputId);
                Optional<User> findUser = userService.findUser(id);
                if (findUser.isEmpty()) {
                    stdOutLog.accept(String.format("Пользователь с id = %d не найден!", id), LogLevel.INFO);
                } else {
                    System.out.printf(MAGENTA + "%-4s%-10s%-25s%-5s%s" + RESET, "ID", "Name", "Email", "Age", "Create");
                    System.out.printf("\n%s\n", findUser.get());
                    user = findUser.get();
                }
            } catch (Exception e) {
                stdOutLog.accept(e.getMessage(), LogLevel.ERROR);
            }
        }
        return user;
    }

    public static void deleteUser(){
        User user = findUserBiId();
        if(user != null){
            try{
                userService.deleteUser(user);
                stdOutLog.accept(String.format("Пользователь с id = %d удален", user.getId()), LogLevel.INFO);
            } catch (Exception e){
                stdOutLog.accept(e.getMessage(), LogLevel.ERROR);
            }
        }
    }

    public static void updateUser(){
        User user = findUserBiId();
        if(user != null){
            User newUserData = validatorInputUserData();
            if(newUserData != null){
                newUserData.setId(user.getId());
                newUserData.setCreated_at(user.getCreated_at());
                try {
                    userService.updateUser(newUserData);
                    stdOutLog.accept("Данные пользователя обновлены", LogLevel.INFO);
                } catch (Exception e){
                    stdOutLog.accept(e.getMessage(), LogLevel.ERROR);
                }
            }
        }
    }

    public static void findAllUser(){
        try{
            List<User> listUsers = userService.findAllUsers();
            if(listUsers.isEmpty()){
                stdOutLog.accept("Список пользователей пуст!", LogLevel.INFO);
            } else{
                System.out.printf(MAGENTA + "%-4s%-10s%-25s%-5s%s" + RESET, "ID", "Name", "Email", "Age", "Create");
                for (User listUser : listUsers)
                    System.out.printf("\n%s", listUser);
                System.out.println();
            }
        } catch(Exception e){
            stdOutLog.accept(e.getMessage(), LogLevel.ERROR);
        }
    }

    private static User validatorInputUserData(){
        System.out.println("Имя (минимум 2 символа):");
        String userName  = scanner.nextLine();
        if(userName.isEmpty() || userName.trim().length() < 2){
            stdOutLog.accept("Не верный ввод: " + userName, LogLevel.WARNING);
            return null;
        }

        System.out.println("Email (минимум 3 символа):");
        String userEmail  = scanner.nextLine();
        if(userEmail.isEmpty() || userEmail.trim().length() < 3){
            stdOutLog.accept("Не верный ввод: " + userEmail, LogLevel.WARNING);
            return null;
        }

        System.out.println("Возраст (от 1 до 200):");
        String userInputAge = scanner.nextLine();
        if(!isValidNumber.test(userInputAge, n -> n > 0 && n <= 200)){
            stdOutLog.accept("Не верный ввод: " + userInputAge, LogLevel.WARNING);
            return null;
        }

        return new User(
                userName.trim(),
                userEmail.trim(),
                Integer.parseInt(userInputAge));
    }
}
