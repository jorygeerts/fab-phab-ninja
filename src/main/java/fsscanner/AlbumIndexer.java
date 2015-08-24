package fsscanner;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.xmp.XmpDirectory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by jgeerts on 6/2/14.
 */
public class AlbumIndexer
{
    protected String rootDirectory;

    public AlbumIndexer(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public ArrayList<Album> getAlbums()
    {
        return getAlbums(new File(rootDirectory));
    }

    protected ArrayList<Album> getAlbums(File directory)
    {
        ArrayList<Album> albums = new ArrayList<Album>();
        File[] children = directory.listFiles();

        Arrays.sort(children, new Comparator<File>() {
            public int compare(File l, File r) {
                return (int) (l.lastModified() - r.lastModified());
            }
        });

        for (File child: children) {

            if (child.isDirectory()) {
                Album album = new Album(child.getName());
                album.setAlbums(getAlbums(child));
                album.setPictures(getPictures(child));
                albums.add(album);
            }
        }

        return albums;
    }

    protected ArrayList<Picture> getPictures(File directory)
    {
        ArrayList<Picture> pictures = new ArrayList<Picture>();
        File[] children = directory.listFiles();

        Arrays.sort(children, new Comparator<File>() {
            public int compare(File l, File r) {
                return (int) (l.lastModified() - r.lastModified());
            }
        });

        for (File child: children) {

            if (child.isFile()) {
                Picture picture = new Picture(child.getPath(), child.getName());
                loadTags(picture);
                pictures.add(picture);
            }
        }

        return pictures;
    }

    protected void loadTags( Picture picture ) {

        try {
            File file = new File(picture.getPath());
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            XmpDirectory xmpDirectory = metadata.getFirstDirectoryOfType(XmpDirectory.class);
            XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
            XMPIterator itr = xmpMeta.iterator();

            XMPPropertyInfo pi;
            String tag;

            while (itr.hasNext()) {
                pi = (XMPPropertyInfo) itr.next();

                if (pi != null && pi.getPath() != null) {

                    tag = pi.getValue().toString();
                    //if ((pi.getPath().endsWith("stArea:w")) || (pi.getPath().endsWith("mwg-rs:Name")) || (pi.getPath().endsWith("stArea:h")))
                    if (isValidTag(tag)) {
                        picture.addTag(new Tag(tag));
                    }
                }
            }
        } catch (final NullPointerException npe) {
            // ignore
        } catch (ImageProcessingException e) {
            //e.printStackTrace();
        } catch (XMPException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    protected boolean isValidTag(String tag) {

        if (tag.length() < 1) {
            return false;
        }

        if (tag.endsWith("stArea:w")) {
            return false;
        }

        if (tag.endsWith("stArea:h")) {
            return false;
        }

        if (tag.endsWith("mwg-rs:Name")) {
            return false;
        }

        if (tag.startsWith("Microsoft Windows")) {
            return false;
        }

        if (tag.startsWith("uuid:")) {
            return false;
        }

        Pattern p = Pattern.compile("^\\d{4}[-]?\\d{2}[-]?\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$");
        if (p.matcher(tag).matches()) {
            return false;
        }

        return true;
    }
}
