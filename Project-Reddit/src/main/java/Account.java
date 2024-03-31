import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;
import java.util.UUID;
public class Account {
    private String displayName;
    private String username;
    private String password;
    private int karma = 0;
    private String email;
    private int postCnt = 0;        //might delete it later
    private String about = "";
    private ArrayList<String> joinedSubreddits = new ArrayList<>();
    private ArrayList<String> adminSubreddits = new ArrayList<>();
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<String> followings = new ArrayList<>();
    private String id;

    public void idMaker(){
        id = (UUID.randomUUID()).toString();
    }

    public void setJoinedSubreddits(JSONArray jsonArray) {
        for(int i = 0 ; i < jsonArray.length() ;i++){
            joinedSubreddits.add(jsonArray.getString(i));
        }
    }

    //todo: add set follow

    public void setAdminSubreddits(JSONArray jsonArray) {
        for(int i = 0 ; i < jsonArray.length() ;i++){
            adminSubreddits.add(jsonArray.getString(i));
        }
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Account(String username , String password , String email){
        displayName = username;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Account(String username){
        this.username = username;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
        String hashedPass = Integer.toString(enteredPassword.hashCode());
        return (hashedPass).equals(this.password);
    }

    public void accountMenu(){ //Might push it to Account class
        Scanner in =  new Scanner(System.in);
        System.out.println("【 " + displayName + " 】"); //I can make the symbols dynamic for change profile :))
        System.out.println("u/" + username);
        System.out.println("Karma: " + karma);
        System.out.println("About: " + about);
        System.out.println("(1) View Posts\n(2) New Post\n(3) Explore\n(4) SubReddit\n(5) Change Profile\n(6) Log out"); //todo: I might add friends list later
        Post post = new Post(username);
        switch (in.next()){
            case "1":
                post.viewPosts(1 , toJson()); //todo: if the file is empty it errors
                break;
            case "2":
                post.newPost();
                break;
            case "3":
                Explore explore = new Explore();
                explore.viewPosts(toJson());
                break;
            case "4":
                System.out.println("(1) Create a new community\n(2) Your SubReddits\n(3) Joined SubReddits");
                switch (in.next()){
                    case "1":
                        SubReddit subReddit = new SubReddit();
                        System.out.println("Name of SubReddit");
                        in.nextLine();
                        String name = in.nextLine();
                        subReddit.newSubreddit(name);
                        //todo: should change the file of account
                        addAdmin(name);
                        break;
                    case "2":
                        for(int i = 0 ; i < adminSubreddits.size(); i++){
                            System.out.println("(" + (i + 1) + ") " + adminSubreddits.get(i));
                        }
                        accountMenu();
                        //todo: should be able to manage his subreddits
                        break;
                    case "3":
                        for(int i = 0 ; i < joinedSubreddits.size(); i++){
                            System.out.println("(" + (i + 1) + ") " + joinedSubreddits.get(i));
                        }    //todo: should be able to remove from here and also in explore
                        accountMenu();
                        break;
                    default:
                        accountMenu();
                        break;
                }
                break;
            case "5":
                setProfile();
                break;
            case "6":
                Main.runMenu();
                break;
            default:
                accountMenu();
                break;
        }
        accountMenu();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Username" , this.username);
        json.put("Password", this.password);
        json.put("Email", this.email);
        json.put("Karma", this.karma);
        json.put("Posts", this.postCnt);
        json.put("Joined Subreddit" ,this.joinedSubreddits);
        json.put("Admin Subreddit", adminSubreddits);
        json.put("ID" , this.id);
        json.put("Display Name",this.displayName);
        json.put("About" , this.about);
        json.put("Followers" , followers);
        json.put("Followings" , followings);
        return json;
    }

    public String getUsername() {
        return username;
    }

    public void setProfile() {
        Scanner in = new Scanner(System.in);
        System.out.println("(1) Display Name\n(2) About \n(3) Change Frame\n(4) Change Password\n(5) Delete Account\n(6) Exit");
        switch (in.next()){
            case "1":
                System.out.println(displayName + "\n(1) Change Display Name\n(2) Exit");
                if("1".equals(in.next())){
                    System.out.println("New Name: ");
                    in.nextLine();
                    displayName = in.nextLine();
                }else{
                    setProfile();
                }
                break;
            case "2":
                System.out.println(about + "\n(1) Change About\n(2) Exit");
                if("1".equals(in.next())){
                    System.out.println("About: ");
                    in.nextLine();
                    about = in.nextLine();
                }else{
                    setProfile();
                }
                break;
            case "3":
                //todo: optional
                break;
            case "4":
                System.out.println("Enter Your New Password");
                String newPassword = in.next();
                password = Integer.toString(newPassword.hashCode());
                break;
            case "5":
                //todo: not optional
                break;
            case "6":
                break;
            default:
                setProfile();
        }
        //save the changes
        Files file = new Files();
        file.profileSave(toJson());
    }

    public void addAdmin(String subreddit){
        adminSubreddits.add(subreddit);
        Files file = new Files();
        file.profileSave(toJson());
    }

    public void viewProfile(JSONObject currentUser){
        Scanner in = new Scanner(System.in);
        Files file = new Files();
        JSONObject accountInfo = new JSONObject(file.usernameFind(username));
        System.out.print(accountInfo.getString("Display Name") + "               (0) Exit\nu/" + accountInfo.getString("Username") + "\nKarma: " + accountInfo.getInt("Karma") + "" +
                "           Followers: " + (accountInfo.getJSONArray("Followers")).length() + "    Followings: " + (accountInfo.getJSONArray("Followings")).length() + "\nAbout: " + accountInfo.getString("About") + "                (*)");
        boolean flag = validateFollow(currentUser , accountInfo);
        ArrayList<String> posts = new ArrayList<>();
        file.fileReader(posts, accountInfo.getString("Username") , 1);
        int show = 5;
        for(int i = 1 ; i <= posts.size() ; i++){
            JSONObject jsonPost = new JSONObject(posts.get(posts.size() - i));
            System.out.println("(" + (i) + ") r/" + jsonPost.getString("Subreddit") + "\n     " + jsonPost.getString("Title") + "\n     " + jsonPost.getString("Post"));
            System.out.println("-------------------------------------");
            show--;
            if(i == posts.size()){
                String op = in.next();
                switch (op) {
                    case "*":
                        follow(currentUser , accountInfo , flag);
                        viewProfile(currentUser);
                        break;
                    case "0":
                        return;
                    default:
                        try {
                            int index = posts.size() - Integer.parseInt(op);
                            if (index >= 0) {
                                JSONObject selectedJson = new JSONObject(posts.get(index));
                                Post post = new Post(selectedJson.getString("Username"));
                                post.selectPost(index, posts, currentUser);
                                viewProfile(currentUser);
                            } else {
                                viewProfile(currentUser);
                            }
                        }catch (NumberFormatException e){
                            viewProfile(currentUser);
                        }
                        break;
                }
            }else if(show == 0){
                System.out.println("        (+) Show More");
                String op = in.next();
                switch (op) {
                    case "*":
                        follow(currentUser , accountInfo , flag);
                        viewProfile(currentUser);
                        break;
                    case "0":
                        return;
                    case "+":
                        show = 5;
                        break;
                    default:
                        try {
                            int index = posts.size() - Integer.parseInt(op);
                            if (index >= 0) {
                                JSONObject selectedJson = new JSONObject(posts.get(index));
                                Post post = new Post(selectedJson.getString("Username"));
                                post.selectPost(index, posts, currentUser);
                                viewProfile(currentUser);
                            } else {
                                viewProfile(currentUser);
                            }
                        }catch (NumberFormatException e){
                            viewProfile(currentUser);
                        }
                        break;
                }
            }
        }
    }

    public boolean validateFollow(JSONObject currentUser , JSONObject accountInfo){
        ArrayList<String> follower = new ArrayList<>();
        JSONArray jsonFollower = new JSONArray(accountInfo.getJSONArray("Followers"));
        Main.jsonToList(jsonFollower , follower);
        if(follower.contains(currentUser.getString("Username"))){
            System.out.println("Unfollow");
            return false;
        }else{
            System.out.println("Follow");
            return true;
        }
    }
    public void follow(JSONObject currentUser , JSONObject accountInfo , boolean flag){
        Files file = new Files();
        ArrayList<String> following = new ArrayList<>();
        ArrayList<String> follower = new ArrayList<>();
        Main.jsonToList(currentUser.getJSONArray("Followings") , following);
        Main.jsonToList(accountInfo.getJSONArray("Followers") , follower);
        if(flag){               //to follow
            following.add(accountInfo.getString("Username"));
            currentUser.put("Followings" , following);
            file.profileSave(currentUser);
            // up for user and down for user who we follow
            follower.add(currentUser.getString("Username"));
            accountInfo.put("Followers" , follower);
            file.profileSave(accountInfo);
        }else{                  //to unfollow
            following.remove(accountInfo.getString("Username"));
            currentUser.put("Followings" , following);
            file.profileSave(currentUser);
            //
            follower.remove(currentUser.getString("Username"));
            accountInfo.put("Followers" , follower);
            file.profileSave(accountInfo);
        }
    }
}
