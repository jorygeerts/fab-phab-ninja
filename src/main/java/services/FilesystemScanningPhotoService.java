package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fsscanner.Album;
import fsscanner.AlbumIndexer;
import fsscanner.Picture;
import ninja.lifecycle.Start;
import ninja.scheduler.Schedule;
import ninja.utils.NinjaProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by jgeerts on 26-8-15.
 */

@Singleton
public class FilesystemScanningPhotoService implements PhotoService
{
    @Inject
    NinjaProperties ninjaProperties;

    @Override
    public Album getAlbumByPath(String path) {
        return getAlbumInListByPath(getAlbums(), path);
    }

    @Override
    public Picture getPictureByPath(String path) {
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        String pictureName = pathParts.get(pathParts.size()-1);
        pathParts.remove(pictureName);
        String albumPath = "";
        for (String pathPart: pathParts) {
            albumPath += pathPart + "/";
        }
        albumPath = albumPath.substring(0, albumPath.length() - 1);

        Album album = getAlbumByPath(albumPath);

        if (album == null) {
            return null;
        }

        for(Picture picture : album.getPictures()) {
            if (picture.getName().equals(pictureName)) {
                return picture;
            }
        }

        return null;
    }

    @Override
    public ArrayList<Album> getAlbums() {
        if (albums == null) {
            System.out.println("SCAN DIRECTORY NOW!");
            AlbumIndexer indexer = new AlbumIndexer(ninjaProperties.get("photo_root"));
            System.out.println("SCAN DONE!");
            //AlbumIndexer indexer = new AlbumIndexer("/home/jgeerts/Afbeeldingen/Scouting/2004");
            albums = indexer.getAlbums();
        }

        return albums;
    }

    @Override
    public boolean isReady() {
        return albums != null;
    }

    private ArrayList<Album> albums;


    protected Album getAlbumInListByPath(ArrayList<Album> albums, String path)
    {
        String[] pathParts = path.split("/", 2);

        if (pathParts.length == 0) {
            return null;
        }

        String pathStart = pathParts[0];
        for(Album album: albums) {
            if (album.getName().equals(pathStart)) {

                if (pathParts.length == 1) {
                    return album;
                }

                return getAlbumInListByPath(album.getAlbums(), pathParts[1]);
            }
        }

        return null;
    }

    @Schedule(initialDelay = 0, timeUnit = TimeUnit.HOURS, delay = 1)
    public void startService()
    {
        getAlbums();
    }

}
