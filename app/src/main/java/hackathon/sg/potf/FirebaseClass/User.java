package hackathon.sg.potf.FirebaseClass;

public class User {
    String uid;
    String name;
    String email;
    double balance;

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.balance = 0;
    }

    public User() {
        this.uid = "";
        this.name = "";
        this.email = "";
        this.balance = 0;
    }

    public void setUid(String uid){
        this.uid = uid;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }

    public String getUid(){return  this.uid;}
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public double getBalance(){
        return this.balance;
    }
}
