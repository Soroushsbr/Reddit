import java.util.ArrayList;

public class SubReddit {
    private String name;
    private ArrayList<String> admins = new ArrayList<>();
    private ArrayList<String> members = new ArrayList<>();
    private ArrayList<String> posts = new ArrayList<>();

    public SubReddit(String name ,  ArrayList<String> admins , ArrayList<String> members ){
        this.name = name;
        this.admins = admins;
        this.members = members;
    }
    public void setAdmins(ArrayList<String> admins) {
        this.admins = admins;
    }

    public void addAdmin(String admin){
        admins.add(admin);
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public void addMember(String member){
        members.add(member);
    }

    public void postDelete(){
        //todo
    }
}
