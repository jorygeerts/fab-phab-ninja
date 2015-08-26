package services;

import fsscanner.Album;
import fsscanner.Picture;

import java.util.ArrayList;

/**
 * Created by jgeerts on 26-8-15.
 */
public interface PhotoService {

    public Album getAlbumByPath(String path);
    public Picture getPictureByPath(String path);
    public ArrayList<Album> getAlbums();

}
