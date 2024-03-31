import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static Files file = new Files();
    public static Scanner in = new Scanner(System.in);
    public static void  main(String [] args){
        runMenu();
    }

    public static void runMenu(){
        System.out.println("(1) Log in\n(2) Sign up\n(3) Exit");
        switch (in.next()) {
            case "1" -> loginMenu();
            case "2" -> signupMenu();
            case "3" -> {
                System.exit(0);
                in.close(); //to close the scanner at the end
            }
            default -> runMenu();
        }
    }

    public static void loginMenu(){
        System.out.print("Username: ");
        String username = in.next();
        if(file.usernameFind(username) != null){
            JSONObject jsonObject = new JSONObject(file.usernameFind(username));
            Account account = new Account(username , jsonObject.getString("Password") , jsonObject.getString("Email"));
            while (true) {
                System.out.print("Password: ");
                if (account.validatePassword(in.next())) {
                    System.out.println("---------- Welcome Back ----------");
                    account.setKarma(jsonObject.getInt("Karma"));
                    account.setDisplayName(jsonObject.getString("Display Name"));
                    account.setAbout(jsonObject.getString("About"));
                    account.setAdminSubreddits(jsonObject.getJSONArray("Admin Subreddit"));      //todo: put all these in a func in acc class
                    account.setJoinedSubreddits(jsonObject.getJSONArray("Joined Subreddit"));
                    account.accountMenu();
                } else {
                    System.out.println("------ Wrong Password ------\n(1) Try Again\n(2) Exit");
                    if ("2".equals(in.next())) {
                        runMenu();
                    }
                }
            }
        } else{
            System.out.println("------ Username not found ------\n(1) Try Again\n(2) Exit");
            switch (in.next()) {
                case "1" -> loginMenu();
                case "2" -> runMenu();
                default -> {
                }
            }
        }
        loginMenu();
    }

    public static void signupMenu(){
        System.out.print("Email: "); // i might want to exit from here
        String email = in.next();
        if(!(validateEmail(email))){
            signupMenu();
        }
        String username;
        while(true) {
            System.out.print("Username: ");
            username = in.next();
            if(file.usernameFind(username) == null){
                break;
            }
            else{
                System.out.println("------ Username Already Exist ------\n(1) Try Again\n(2) Change Email\n(3) Exit");
                switch (in.next()){
                    case "2":
                        signupMenu();
                        break;
                    case "3":
                        runMenu();
                        break;
                    default:
                        break;
                }
            }
        }
        System.out.print("Password: ");
        String password = in.next();
        String hashedPass = Integer.toString(password.hashCode());
        Account account = new Account(username , hashedPass , email);
        account.idMaker();
        file.fileWriter("Account" , (account.toJson()).toString() , 0 , true);
        file.fileMaker(username); //unnecessary func if you ask me, might delete later
        account.accountMenu();
    }


    public static boolean validateEmail(String email) { // *** I can push this func to the Files class ***
        ArrayList<String> accountList = new ArrayList<>();
        file.fileReader(accountList , "Account" , 0);
        String regex = "[^\\s]*@[^\\s]*.[^\\s]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        boolean flag = true;
        boolean find = matcher.find();
        if (find) {//if email is valid we have to check if it's unique
            if (!(accountList.isEmpty())) {
                for (int i = 0; i < accountList.size(); i++) {
                    JSONObject jsonObject = new JSONObject(accountList.get(i));
                    String emailList = (jsonObject.getString("Email"));
                    if (emailList.equals(email)) { //todo: make a func to read the file and adjust the list
                        flag = false;
                        System.out.println("Email is Already Exist");
                        break;
                    }
                }
            }
        }
        else{
            System.out.println("Please Enter a Valid Email");
        }
        return (flag && find);
    }

    public static void jsonToList(JSONArray jsonArray , ArrayList<String> list){
        for(int i = 0 ; i < jsonArray.length() ; i++){
            list.add(jsonArray.getString(i));
        }
    }
}
