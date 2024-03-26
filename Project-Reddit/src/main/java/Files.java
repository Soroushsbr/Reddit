import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class Files {
    //remember change the prints
    public void FileMaker(String username){
        try {
            File file = new File("C:\\Users\\Lenovo\\Desktop\\Java Projects\\Project-Reddit\\Reddit\\Project-Reddit\\accounts\\" + username + ".txt");
            if (file.createNewFile()) {
                System.out.println("created");
            }
            else{
                System.out.println("Exist");
            }
        }catch (IOException e){
            System.out.println("error");
            e.printStackTrace();
        }
    }

    public void FileDeleter(String username) {
        File file = new File("C:\\Users\\Lenovo\\Desktop\\Java Projects\\Project-Reddit\\Reddit\\Project-Reddit\\accounts\\" + username + ".txt");
        if (file.delete()) {
            System.out.println("Deleted successfully");
        } else {
            System.out.println("Error");
        }
    }
    public String jsonReader(String username){ //might delete later
        try {
            File file = new File("C:\\Users\\Lenovo\\Desktop\\Java Projects\\Project-Reddit\\Reddit\\Project-Reddit\\accounts\\" + username + ".txt");
            Scanner reader = new Scanner(file);
            return reader.nextLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void FileWriter(String username , String data, int option){
        String address;
        if(option == 1){
            address = "C:\\Users\\Lenovo\\Desktop\\Java Projects\\Project-Reddit\\Reddit\\Project-Reddit\\accounts\\" + username + ".txt";
        } else{
            address = username + ".txt";
        }
        try{
            FileWriter writer = new FileWriter(address , true);
            writer.write(data + "\n");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void accountFile(ArrayList<String> accountList){
        try{
            File file = new File("Account.txt");
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                accountList.add(reader.nextLine());
            }
            reader.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public String usernameFind(String username) {
        ArrayList<String> accountList = new ArrayList<>();
        accountFile(accountList);
        if(!(accountList.isEmpty())) {
            for (int i = 0; i < accountList.size(); i++) {
                JSONObject jsonObject = new JSONObject(accountList.get(i));
                if((jsonObject.getString("Username")).equals(username)){
                    return jsonObject.toString();
                }
            }
        }
        return null;
    }

}
