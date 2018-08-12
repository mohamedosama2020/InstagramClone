package tabian.com.instagramclone.Models;

public class Photo {

    private String description;
    private String date_created;
    private String image_path;
    private String photo_id;
    private String tags;


    public Photo() {

    }


    public Photo(String description, String date_created, String image_path, String photo_id, String tags) {
        this.description = description;
        this.date_created = date_created;
        this.image_path = image_path;
        this.photo_id = photo_id;
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "description='" + description + '\'' +
                ", date_created='" + date_created + '\'' +
                ", image_path='" + image_path + '\'' +
                ", photo_id='" + photo_id + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
