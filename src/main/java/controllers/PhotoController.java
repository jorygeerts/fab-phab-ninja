package controllers;

import com.google.inject.Singleton;
import fsscanner.Album;
import fsscanner.AlbumIndexer;
import fsscanner.Picture;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;
import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jgeerts on 19-8-15.
 */
@Singleton
public class PhotoController {

    private ArrayList<Album> albums;

    protected ArrayList<Album> getAlbums(){
        if (albums == null) {
            AlbumIndexer indexer = new AlbumIndexer("/home/jgeerts/Afbeeldingen/Prive");
            //AlbumIndexer indexer = new AlbumIndexer("/home/jgeerts/Afbeeldingen/Scouting/2004");
            albums = indexer.getAlbums();
        }

        return albums;
    }

    public Result index() {

        Result result = Results.html();

        result.render("albums", getAlbums());

        return result;
    }

    public Result album(@PathParam("path") String path) {

        Album album = getAlbumByPath(path);

        if (album == null) {
            return Results.notFound();
        }

        Result result = Results.html();
        result.render("album", album);
        return result;
    }

    public Result picture(@PathParam("path") String path)
    {
        Picture picture = getPictureByPath(path);

        if (picture == null) {
            return Results.notFound();
        }

        try {
            Result result = Results.ok();
            result.contentType("image/jpeg");
            result.renderRaw(Files.readAllBytes(Paths.get(picture.getFsPath())));
            return result;
        } catch (FileNotFoundException exc) {
            return Results.notFound();
        } catch (IOException exc) {
            return Results.notFound();
        }
    }

    protected Picture getPictureByPath(String path)
    {
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

    protected Album getAlbumByPath(String path)
    {
        return getAlbumInListByPath(getAlbums(), path);
    }

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

}
