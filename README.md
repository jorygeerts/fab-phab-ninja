# fab-phab-ninja
Filesystem scanning photo album, based on Ninja Framework

Mostly an experiment, the main goal is to have some fun.

## Trying it out
To try it out, you need Java 1.7 and Maven 3.1. If you have those:
 - Change src/main/java/controllers/PhotoController.java#L29 to point to a directory with pictures on your PC
 - mvn clean install
 - mvn ninja:run

## Roadmap
 - Move configuration out of code
 - Persist data (MongoDB most likely)
 - Authentication (a bit like Flickr their "Guest Pass", but better)
 - Structure by tags and "tag groups"
 - Make directory "just another path". (Maybe?)
 - Editable tags (only in DB, or write into file?)
