package cf.somwaki.medicalchat;

public class MyMessage {
    private boolean belongsToCurrentUser;
    String text;


    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }
}
