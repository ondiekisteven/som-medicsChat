package cf.somwaki.medicalchat;

public class ChatList_Item {
    String name, uid;

    public ChatList_Item(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ChatList_Item(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }
}
