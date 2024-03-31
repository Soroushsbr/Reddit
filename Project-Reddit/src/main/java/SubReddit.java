import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
public class SubReddit {
    private String name;
    private String about;
    private ArrayList<String> members = new ArrayList<>();

    public SubReddit(){
    }
    public SubReddit(String name){
        this.name = name;
    }


    public void viewPage(JSONObject currentUser){
        Scanner in = new Scanner(System.in);
        Files file = new Files();
        ArrayList<String> postList = new ArrayList<>();
        JSONObject subJson = new JSONObject(file.subredditFind(name));
        file.fileReader(postList , name , 3);
        System.out.print("             ✦ " + name + " ✦             (0) Exit\n    Members: " + subJson.getJSONArray("Members").length() + "    (*)");
        boolean flag = validateJoin(currentUser, subJson);
        System.out.println("\n" + subJson.getString("About"));
        int show = 5;
        for(int i = 1 ; i <= postList.size() ; i++){
            JSONObject tempJson = new JSONObject(postList.get(postList.size() - i));
            System.out.println("(" + (i) + ") r/" + tempJson.getString("Subreddit") + "\n     " + tempJson.getString("Title") + "\n     " + tempJson.getString("Post"));
            System.out.println("-------------------------------------");
            show--;
            if(i == postList.size()){
                String op = in.next();
                switch (op){
                    case "*":
                        join(currentUser , subJson, flag);
                        viewPage(currentUser);
                        break;
                    case "0":
                        return;
                    default:
                        try {
                            int index = postList.size() - Integer.parseInt(op);
                            if (index >= 0) {
                                JSONObject selectedJson = new JSONObject(postList.get(index));
                                Post post = new Post(selectedJson.getString("Username"));
                                post.selectPost(index, postList, currentUser);
                                viewPage(currentUser);
                            } else {
                                viewPage(currentUser);
                            }
                        }catch (NumberFormatException e){
                            viewPage(currentUser);
                        }
                        break;
                }
            }
            else if(show == 0){
                System.out.println("        (+) Show More");
                String op = in.next();
                switch (op) {
                    case "*":
                        join(currentUser , subJson, flag);
                        viewPage(currentUser);
                        break;
                    case "0":
                        return;
                    case "+":
                        show = 5;
                        break;
                    default:
                        try {
                            int index = postList.size() - Integer.parseInt(op);
                            if (index >= 0) {
                                JSONObject selectedJson = new JSONObject(postList.get(index));
                                Post post = new Post(selectedJson.getString("Username"));
                                post.selectPost(index, postList, currentUser);
                                viewPage(currentUser);
                            } else {
                                viewPage(currentUser);
                            }
                        } catch (NumberFormatException e) {
                            viewPage(currentUser);
                        }
                        break;
                }
            }
        }
    }

    public boolean validateJoin(JSONObject currentUser , JSONObject subJson){
        ArrayList<String> members = new ArrayList<>();
        JSONArray jsonMember = new JSONArray(subJson.getJSONArray("Members"));
        Main.jsonToList(jsonMember , members);
        if(members.contains(currentUser.getString("Username"))){
            System.out.println("Joined");
            return false;
        }else{
            System.out.println("Join");
            return true;
        }
    }

    public void join(JSONObject currentUser , JSONObject subJson , boolean flag){
        Files file = new Files();
        ArrayList<String> joinedSub = new ArrayList<>();
        ArrayList<String> member = new ArrayList<>();
        Main.jsonToList(currentUser.getJSONArray("Joined Subreddit") , joinedSub);
        Main.jsonToList(subJson.getJSONArray("Members") , member);
        if(flag){               //to follow
            joinedSub.add(subJson.getString("Name"));
            currentUser.put("Joined Subreddit" , joinedSub);
            file.profileSave(currentUser);
            // up for user and down for user who we follow
            member.add(currentUser.getString("Username"));
            subJson.put("Members" , member);
            file.subredditSave(subJson); //todo: make func for this
        }else{                  //to unfollow
            joinedSub.remove(subJson.getString("Name"));
            currentUser.put("Joined Subreddit" , joinedSub);
            file.profileSave(currentUser);
            //
            member.remove(currentUser.getString("Username"));
            subJson.put("Members" , member);
            file.subredditSave(subJson);
        }
    }
    public void newSubreddit(String name){
        Scanner in = new Scanner(System.in);
        this.name = name;
        System.out.println("Tell us about your community");
        about = in.nextLine();
        System.out.println("(1) Create\n(2) Exit");
        if("1".equals(in.next())){
            JSONObject json = new JSONObject();
            json.put("Name", this.name);
            json.put("Members" , members);
            json.put("About", about);
            Files file = new Files();
            file.fileWriter("Subreddit" , json.toString(), 2 , true );
        }
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void addMember(String member){
        members.add(member);
    }

    public void postDelete(){
        //todo
    }
}

