package fsscanner;

import java.util.ArrayList;

public class Album {

    protected ArrayList<Picture> pictures;

    protected ArrayList<Album> albums;

    protected String name;

    protected Album parent;

    public Album(String name) {
        this.name = name;
        pictures = new ArrayList<Picture>();
        albums = new ArrayList<Album>();
    }

    public Album(String name, Album parent) {
        this.name = name;
        this.parent = parent;
        pictures = new ArrayList<Picture>();
        albums = new ArrayList<Album>();
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;

        for (Album album: this.albums) {
            album.setParent(this);
        }
    }

    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;

        for (Picture picture : pictures) {
            picture.setAlbum(this);
        }
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<Album> getAlbums()
    {
        return albums;
    }

    public ArrayList<Picture> getPictures() {
        return pictures;
    }

    public int countSubalbums()
    {
        int count = albums.size();
        for (Album sub: albums) {
            count += sub.countSubalbums();
        }
        return count;
    }

    public int countPictures()
    {
        int count = pictures.size();
        for (Album sub: albums) {
            count += sub.countPictures();
        }
        return count;
    }

    public String getPath()
    {
        if (parent != null) {
            return parent.getPath() + "/" + getName();
        }

        return getName();
    }

    public void setParent(Album parent) {
        this.parent = parent;
    }
}
