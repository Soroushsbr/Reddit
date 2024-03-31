import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;
import org.json.JSONException;

public class Post {
    private Files file = new Files();
    //i might dont need it in global todo: make this list for every class so you have to put it in funcs
    private ArrayList<JSONObject> comments = new ArrayList<>();
    private ArrayList<String> upvotes = new ArrayList<>();
    private ArrayList<String> downvotes = new ArrayList<>();
    private String username;
    private int karma = 0;
    private String title;
    private String subreddit;
    private String postId;

    public Post(String username){
        this.username = username;
    }

    public Post(){

    }

    public void newPost(){
        Scanner in = new Scanner(System.in);
        ArrayList<String> subredditList = new ArrayList<>();
        file.fileReader(subredditList , "Subreddit", 2);
        System.out.println("Choose a Community");
        for(int i = 0 ; i < subredditList.size(); i++){
            JSONObject json = new JSONObject(subredditList.get(i));
            System.out.println("(" + (i + 1) + ") " + json.getString("Name"));
        }
        System.out.println("(0) Exit");
        int index = in.nextInt();
        if(index != 0) {
            JSONObject selectedJson = new JSONObject(subredditList.get(index - 1));
            subreddit = selectedJson.getString("Name");
            System.out.println("Add an interesting title");
            in.nextLine();
            title = in.nextLine();
            System.out.println("Add your text ");
            //in.nextLine(); in case if this got problem again
            String data = in.nextLine();
            System.out.println("(1) Post\n(2) Exit");
            if ("1".equals(in.next())) {
                postId = (UUID.randomUUID()).toString();            //to make every post unique
                JSONObject json = new JSONObject();
                json.put("Post", data);
                json.put("Comment", comments);
                json.put("Title", title);
                json.put("Karma", karma);
                json.put("Username", username);
                json.put("Subreddit", subreddit);
                json.put("ID" , postId);
                json.put("Upvote" , upvotes);
                json.put("Downvote" , downvotes);
                file.fileWriter(username, json.toString(), 1, true);
                file.fileWriter(subreddit , json.toString(), 3 , true );
                file.fileWriter("Explore" , json.toString(), 2 , true);
            }
        }
    }

    public void viewPosts(int option , JSONObject currentUser){      //this one is only for profile
        Scanner in = new Scanner(System.in);
        ArrayList<String> postList = new ArrayList<>();
        file.fileReader(postList , username , option);
        if(!(postList.isEmpty())){
            for (int i = 0; i < postList.size(); i++){
                JSONObject json = new JSONObject(postList.get(i));
                System.out.println("(" + (i + 1) + ") " + json.getString("Title") + "\n     " + json.getString("Post"));
            }
        }
        System.out.println("(0) Exit");
        int index = in.nextInt();
        if(index != 0){
            selectPost(index - 1 , postList , currentUser);
        }
    }

    public void selectPost(int index , ArrayList<String> postList , JSONObject currentUser){
        Scanner in = new Scanner(System.in);
        String selectedPost = postList.get(index);
        JSONObject json = new JSONObject(selectedPost);
        System.out.println("r/" + json.getString("Subreddit") + "\nu/" + json.getString("Username") + "\n " + json.getString("Title") + "\n  " + json.getString("Post"));
        System.out.println("-------------------------------------------------\n(1)⬆ " + json.getInt("Karma") + " (2)⬇    (3)Comments    (4)Report    (5)Exit");
        int value;
        switch (in.next()){
            case "1":
                value = validKarma(currentUser, 1 , json);
                karma = json.getInt("Karma");
                json.put("Karma" , karma + value);
                postList.set(index, json.toString());
                saveChanges(postList.get(index)); //todo
                profileKarma(value , this.username);
                selectPost(index , postList , currentUser);
                break;
            case "2":
                value = validKarma(currentUser , -1 , json);
                karma = json.getInt("Karma");
                json.put("Karma" , karma + value);
                postList.set(index , json.toString());
                saveChanges(postList.get(index));
                profileKarma(value , this.username);
                selectPost(index , postList , currentUser);
                break;
            case "3":
                Comment comment =new Comment();
                JSONArray jsonArray = json.getJSONArray("Comment");
                for(int i = 1 ; i <= jsonArray.length(); i++){
                    JSONObject temp = jsonArray.getJSONObject(jsonArray.length() - i);
                    System.out.println("(" + (i) + ") " + temp.getString("Writer") + "\n..." + temp.getString("Text"));
                }
                System.out.println("--------------------\n(+) Add a Comment\n(0) Exit");
                String op = in.next();
                if("+".equals(op)){
                    jsonArray.put(comment.newComment(currentUser));
                    json.put("Comment" , jsonArray);
                    postList.set(index , json.toString());
                    saveChanges(postList.get(index));
                    selectPost(index , postList , currentUser);
                }else if("0".equals(op)){
                    selectPost(index , postList , currentUser);
                }
                else{
                    try{
                        int indexComment = jsonArray.length() - Integer.parseInt(op);
                        comment.selectComment(currentUser , json , indexComment);
                        selectPost(index , postList , currentUser);
                    }
                    catch (NumberFormatException e){
                        selectPost(index , postList , currentUser);
                    }
                }
                break;
            case "4":
                //todo
                break;
            case "5":
                return;
            default:
                selectPost(index, postList , currentUser);
                break;
        }
    }

    public void saveChanges(String changedPost){
        //for account folder
        JSONObject jsonObject = new JSONObject(changedPost);
        ArrayList<String> list = new ArrayList<>();
        file.fileReader(list, jsonObject.getString("Username") , 1 );
        file.postFinder(list , changedPost);
        String data ="";
        for(int i = 0 ; i < list.size() ; i++){
            data += (list.get(i)) + "\n";
        }
        file.fileWriter(jsonObject.getString("Username") , data, 1 , false);
        //for subreddit file
        list.clear();
        file.fileReader(list, jsonObject.getString("Subreddit") , 3 );
        file.postFinder(list , changedPost);
        data ="";
        for(int i = 0 ; i < list.size() ; i++){
            data += (list.get(i)) + "\n";
        }
        file.fileWriter(jsonObject.getString("Subreddit") , data, 3 , false);
        //at the end lets save it in explore
        list.clear();
        file.fileReader(list, "Explore", 2 );
        file.postFinder(list , changedPost);
        data ="";
        for(int i = 0 ; i < list.size() ; i++){
            data += (list.get(i)) + "\n";
        }
        file.fileWriter("Explore", data, 2 , false);
    }

    public void profileKarma(int value , String username){ // might change it
        ArrayList<String> accountList = new ArrayList<>();
        file.fileReader(accountList , "Account" , 2);
        file.karmaSaver(username , accountList , value);
    }

    public int validKarma(JSONObject currentUser , int value , JSONObject json){
        String name = currentUser.getString("Username");
        ArrayList<String> upvotes = new ArrayList<>();
        jsonToList(json.getJSONArray("Upvote") , upvotes);
        ArrayList<String> downvotes = new ArrayList<>();
        jsonToList(json.getJSONArray("Downvote") , downvotes);
        if(value == 1){
            if(upvotes.contains(name)){    //if you already upvoted it removes the upvote
                value *= -1;
                upvotes.remove(name);
                json.put("Upvote" , upvotes);
            }
            else if(downvotes.contains(name)){  //and if you downvoted it deletes it and make it an upvote
                value *= 2;
                downvotes.remove(name);
                json.put("Downvote" , downvotes);
                upvotes.add(name);
                json.put("Upvote" , upvotes);
            } else{
                upvotes.add(name);
                json.put("Upvote" , upvotes);
            }
        } else if(value == -1){
            if(upvotes.contains(name)){
                value *= 2;
                upvotes.remove(name);
                json.put("Upvote" , upvotes);
                downvotes.add(name);
                json.put("Downvote" , downvotes);
            }
            else if(downvotes.contains(name)){     //if you already downvoted
                value *= -1;
                downvotes.remove(name);
                json.put("Downvote" , downvotes);
            }else{
                downvotes.add(name);
                json.put("Downvote" , downvotes);
            }
        }
        return value;
    }

    public void jsonToList(JSONArray jsonArray , ArrayList<String> list){
        for(int i = 0 ; i < jsonArray.length() ; i++){
            list.add(jsonArray.getString(i));
        }
    }

    public void cls(){
        for(int i = 0 ; i < 50 ; i++){
            System.out.println("\n");
        }
    }
}
