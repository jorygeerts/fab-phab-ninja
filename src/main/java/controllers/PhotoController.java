package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import filters.PhotoServiceReadyFilter;
import fsscanner.Album;
import fsscanner.AlbumIndexer;
import fsscanner.Picture;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;
import org.apache.commons.lang.ArrayUtils;
import services.PhotoService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


@Singleton
@FilterWith(PhotoServiceReadyFilter.class)
public class PhotoController {

    @Inject
    protected PhotoService photoService;

    public Result index() {

        Result result = Results.html();

        result.render("albums", photoService.getAlbums());

        return result;
    }

    public Result album(@PathParam("path") String path) {

        Album album = photoService.getAlbumByPath(path);

        if (album == null) {
            return Results.notFound();
        }

        Result result = Results.html();
        result.render("album", album);
        return result;
    }

    public Result picture(@PathParam("path") String path)
    {
        Picture picture = photoService.getPictureByPath(path);

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

    public Result thumbnail(@PathParam("photo") String photo, @PathParam("type") String type)
    {
        return Results.notFound();
    }


}
