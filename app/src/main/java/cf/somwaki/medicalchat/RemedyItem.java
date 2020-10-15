package cf.somwaki.medicalchat;

public class RemedyItem {
    String title;
    String body;
    String id;

    public RemedyItem(String id, String title, String body) {
        this.title = title;
        this.body = body;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getId(){return id;}
}
