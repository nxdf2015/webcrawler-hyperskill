package crawler;

public class EntityUrl {
    private String url;
    private String title;

    public EntityUrl(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "EntityUrl{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
