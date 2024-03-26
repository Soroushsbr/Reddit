import java.util.ArrayList;
import org.json.JSONObject;
import java.util.UUID;
public class Account {
    private String username;
    private String password;
    private int karma = 0;
    private String email;
    private int postCnt = 0;
    private ArrayList<String> subReddits = new ArrayList<>();
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<String> followings = new ArrayList<>();
    private String id;

    public void idMaker(){
        id = (UUID.randomUUID()).toString();
    }

    public Account(String username , String password , String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /*public Account(){
     might use later
    }*/

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public void setPostCnt(int postCnt) {
        this.postCnt = postCnt;
    }

    public int getPostCnt() {
        return postCnt;
    }

    public boolean validatePassword(String enteredPassword){
        return enteredPassword.equals(this.password);
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Username" , this.username);
        json.put("Password", this.password);
        json.put("Email", this.email);
        json.put("Karma", this.karma);
        json.put("Posts", this.postCnt);
        json.put("SubReddit" ,this.subReddits);
        json.put("ID" , this.id);
        return json;
    }



}
