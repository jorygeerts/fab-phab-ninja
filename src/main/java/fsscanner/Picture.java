package fsscanner;

import java.util.ArrayList;

/**
 * Created by jgeerts on 6/2/14.
 */
public class Picture {

    protected Album album;

    protected String path;

    protected String name;

    protected ArrayList<Tag> tags;

    public Picture(String path, String name) {
        this.path = path;
        this.name = name;

        this.tags = new ArrayList<Tag>();
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getPath() {
        return album.getPath() + "/" + name;
    }

    public String getFsPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public ArrayList<Tag> getTags() {
        return (ArrayList<Tag>) tags.clone();
    }
}
