# Special folder
This folder contains special files which may be needed in compiling the cordova/teavm application.

## Apache Cordova

When using apache cordova, make sure to add this to the config.xml:  

``` 
<preference name="Fullscreen" value="true" />
<access origin="*" />
<allow-navigation href="*"/>
```

## Using as local dependency

To use this as a local dependency, run this command in the target folder.

```
mvn install:install-file -Dfile=BlackTar-0.0.1.jar -DgroupId=coffee.mason -DartifactId=blacktar -Dversion=0.0.1 -Dpackaging=jar
```
