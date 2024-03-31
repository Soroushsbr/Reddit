import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class Explore extends SubReddit{
    private ArrayList<String> posts = new ArrayList<>();  

    public void viewPosts(JSONObject currentUser){
        Scanner in = new Scanner(System.in);
        Files file = new Files();
        posts.clear();
        file.fileReader(posts , "Explore" , 2);
        System.out.println("☖(-)____________________________⌕(*)");
        int show = 5;
        for(int i = 1 ; i <= posts.size() ; i++){
            JSONObject json =  new JSONObject(posts.get(posts.size() - i));        //I mean this blow is a little long yk
            System.out.println("(" + (i) + ") r/" + json.getString("Subreddit") + "\n     " + json.getString("Title") + "\n     " + json.getString("Post"));
            System.out.println("-------------------------------------");
            show--;
             if(i == posts.size()){     //might need a error handle for entering chars
                String op = in.next();
                if(op.equals("-")){ //home button
                    return;
                }else if(op.equals("*")){ //search button
                    search(currentUser);
                    viewPosts(currentUser);
                }else{
                    try {
                        int index = posts.size() - Integer.parseInt(op);
                        if(index >= 0){
                            JSONObject selectedJson = new JSONObject(posts.get(index));
                            Post post = new Post(selectedJson.getString("Username"));
                            post.selectPost(index ,posts , currentUser);
                            viewPosts(currentUser);
                        } else{
                            viewPosts(currentUser);
                        }
                    } catch(NumberFormatException e){           //this is when the user enter a char that isn't a option
                        viewPosts(currentUser);
                    }
                }
            }
            else if(show == 0){
                System.out.println("        (+) Show More");
                String op = in.next();
                if(op.equals("-")){ //home button
                    return;
                }else if(op.equals("*")){ //search button
                    search(currentUser);
                    viewPosts(currentUser);
                }else if(op.equals("+")){
                    show = 5;
                } else {
                    try {
                        int index = posts.size() - Integer.parseInt(op);
                        if(index >= 0){
                            JSONObject selectedJson = new JSONObject(posts.get(index));
                            Post post = new Post(selectedJson.getString("Username"));
                            post.selectPost(index ,posts , currentUser);
                            viewPosts(currentUser);
                        } else{
                            viewPosts(currentUser);
                        }
                    } catch(NumberFormatException e){
                        viewPosts(currentUser);
                    }
                }
            }

        }
        viewPosts(currentUser);
    }

    public void search(JSONObject currentUser){
        Scanner in = new Scanner(System.in);
        System.out.println("Search Reddit");
        String regex = in.nextLine();
        ArrayList<String> nameFind = new ArrayList<>();
        int perSize = 0;
        if(regex.length() > 2){
            if((regex.substring(0 , 2)).equals("u/")){         //to search for users
                regex = regex.substring(2);
                finder(nameFind , regex , "Account" , "Username");
                System.out.println("People﹀");
                for(int i = 0 ; i < nameFind.size() ;i++){
                    System.out.println("(" + (i + 1) + ")" + nameFind.get(i));
                }
            }
            else if((regex.substring(0 , 2)).equals("r/")){     //to search for subreddit
                regex = regex.substring(2);
                finder(nameFind, regex , "Subreddit" , "Name");
                System.out.println("Communities﹀");
                for(int i = 0 ; i < nameFind.size() ;i++){
                    System.out.println("(" + (i + 1) + ")" + nameFind.get(i));
                }
            }else{      //for both
                perSize = printBoth(regex , nameFind);
            }
        }else{
            perSize = printBoth(regex , nameFind);
        }
        System.out.println("(0) Exit");
        int index = in.nextInt();
        if(index <= nameFind.size()) {
            if (index != 0) {
                System.out.println(perSize);
                if(index > perSize) {
                    Account account = new Account(nameFind.get(index - 1));      //make one for subreddit
                    account.viewProfile(currentUser);
                }else{
                    SubReddit subReddit = new SubReddit(nameFind.get(index - 1));
                    subReddit.viewPage(currentUser);
                }
            } else {
                viewPosts(currentUser);
            }
        }
        else{
            search(currentUser);
        }
    }


    private int printBoth(String regex , ArrayList<String> nameFind ) {
        finder(nameFind, regex , "Subreddit" , "Name");
        System.out.println("Communities﹀");
        for(int i = 0 ; i < nameFind.size() ;i++){
            System.out.println("(" + (i + 1) + ")" + nameFind.get(i));
        }
        int perSize = nameFind.size();
        int i = perSize;
        finder(nameFind , regex , "Account" , "Username");
        System.out.println("People﹀");
        while(i < nameFind.size()){
            System.out.println("(" + (i + 1) + ") " + nameFind.get(i));
            i++;
        }
        return perSize;
    }

    public void finder(ArrayList<String> nameFind , String regex , String address , String name){
        regex = regex + ".*";
        Pattern pattern = Pattern.compile(regex , Pattern.CASE_INSENSITIVE);
        ArrayList<String> nameList = new ArrayList<>();
        Files file = new Files();
        file.fileReader(nameList , address , 2);
        Matcher matcher;
        for(int i = 0 ; i < nameList.size() ; i++){
            JSONObject json = new JSONObject(nameList.get(i));
            matcher = pattern.matcher(json.getString(name));
            if(matcher.find()){
                nameFind.add(json.getString(name));
            }
        }
    }
}
