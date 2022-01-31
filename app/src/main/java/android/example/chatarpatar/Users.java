package android.example.chatarpatar;

public class Users {
    String uid;
    String name;
    String email;
    String ImageUri;
    String status;


    public Users() {
    }

    public Users(String uid, String name, String email, String imageUri,String status) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        ImageUri = imageUri;
        this.status = status;

    }

    public  String getStatus(){
        return status;

    }

    public void setStatus(String status){
        this.status = status;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }
}
