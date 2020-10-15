package cf.somwaki.medicalchat;

public class RemedyItem {
    String title;
    String body;

    public RemedyItem(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
