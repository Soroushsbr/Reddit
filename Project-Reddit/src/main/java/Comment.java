import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

public class Comment extends Post {
    private String writer = "";
    private String text = "";
    private int karma = 0;
    private ArrayList<String> upvotes = new ArrayList<>();
    private ArrayList<String> downvotes = new ArrayList<>();
    private ArrayList<String> replay = new ArrayList<>();

    public JSONObject newComment(JSONObject currentUser){
        Scanner in = new Scanner(System.in);
        System.out.println("Drop a Comment");
        text = in.nextLine();
        writer = currentUser.getString("Username");
        JSONObject json = new JSONObject();
        json.put("Text" , text);
        json.put("Writer" , writer);
        json.put("Upvote" , upvotes);
        json.put("Downvote" , downvotes);
        json.put("Replay" , replay);
        json.put("karma" , karma);
        return json;
    }

    public void showComments(JSONObject currentUser , JSONObject jsonComments){
        JSONArray jsonArray = jsonComments.getJSONArray("Comment");
        for(int i = 0 ; i < jsonArray.length(); i++){
            JSONObject temp = (JSONObject) jsonArray.get(i);
            System.out.println("(" + (i + 1) + ") " + temp.getString("Writer") + "\n..." + temp.getString("Text"));
        }
        System.out.println("--------------------\n(+) Add a Comment\n(0) Exit");
    }

    public void selectComment(JSONObject currentUser , JSONObject jsonComment , int index){
        Scanner in =  new Scanner(System.in);
        JSONArray jsonArray = jsonComment.getJSONArray("Comment");
        JSONObject selectedComment = jsonArray.getJSONObject(index);
        System.out.println("u/" + selectedComment.getString("Writer") + "\n " + selectedComment.getString("Text"));
        System.out.println("--------------------------------------------");
        System.out.println("(1)⬆ " + selectedComment.getInt("karma") + " (2)⬇    (3)Replay    (4)Exit");
        System.out.println("--------------------------------------------");
        JSONArray replays = selectedComment.getJSONArray("Replay");
        for(int i = 0 ; i < replays.length() ;i++){
            System.out.println(replays.getString(i) + "\n____________________________");
        }
        int value= 0;
        int karma =0;
        ArrayList<JSONObject> comments = new ArrayList<>();
        switch (in.next()){
            case "1":
                value = validKarma(currentUser , 1 , selectedComment);
                 karma = selectedComment.getInt("karma");
                selectedComment.put("karma" , karma + value);
                jsonArrayToList(jsonArray , comments);
                comments.set(index , selectedComment);
                jsonComment.put("Comment" , comments);
                saveChanges(jsonComment.toString());
                profileKarma(value , selectedComment.getString("Writer"));
                selectComment(currentUser , jsonComment , index);
                break;
            case "2":
                value = validKarma(currentUser , -1 , selectedComment);
                karma = selectedComment.getInt("karma");
                selectedComment.put("karma" , karma + value);
                jsonArrayToList(jsonArray , comments);
                comments.set(index , selectedComment);
                jsonComment.put("Comment" , comments);
                saveChanges(jsonComment.toString());
                profileKarma(value , selectedComment.getString("Writer"));
                selectComment(currentUser , jsonComment , index);
                break;
            case "3":
                addReplay(jsonArray , selectedComment , jsonComment , currentUser, index);
                break;
            case "4":
                return;
            default:
                selectComment(currentUser , jsonComment, index);
                break;
        }
        System.out.println();
    }

    @Override
    public void saveChanges(String changedPost) {
        super.saveChanges(changedPost);
    }



    @Override
    public int validKarma(JSONObject currentUser, int value, JSONObject json) {
        return super.validKarma(currentUser, value, json);
    }

    public void addReplay(JSONArray jsonArray ,JSONObject selectedComment , JSONObject jsonComment, JSONObject currentUser  , int index){
        Scanner in = new Scanner(System.in);
        JSONArray replayJson = selectedComment.getJSONArray("Replay");
        System.out.println("Add Replay");
        String replay = in.nextLine();
        replayJson.put(replay);
        selectedComment.put("Replay" , replayJson);
        ArrayList<JSONObject> comments = new ArrayList<>();
        jsonArrayToList(jsonArray , comments);
        comments.set(index , selectedComment);
        jsonComment.put("Comment" , comments);
        saveChanges(jsonComment.toString());
        selectComment(currentUser , jsonComment , index);
    }

    public void jsonArrayToList(JSONArray jsonArray , ArrayList<JSONObject> list){
        for(int i = 0 ; i < jsonArray.length() ; i++){
            JSONObject temp = jsonArray.getJSONObject(i);
            list.add(temp);
        }
    }

    @Override
    public void profileKarma(int value, String username) {
        super.profileKarma(value, username);
    }
}
