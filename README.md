#ECMWF-loader

Scala/Java wrapper for downloading meteorological data from the [ECMWF public datsets] (e.g. ERA-Interim climate reanalysis).

The command line Class is implemented in Scala.

The Java source code from org.ecmwf, org.json and org.json.zip have been included.

For other instructions of the ECMWF api see [ECMWF api].

##Usage
This works on Linux platforms. 

###Registration
You should register at the [ECMWF registration]. 
You should then retrieve your key at [ECMWF key file] and copy the content in a file
named *.ecmwfapirc*, and put this in your home directory.

###Downloading data
Call the jar from the command line:

```
java -jar ecmwf-loader-assembly-X.XX.jar [options]
```
(replace X.XX with the version number you have).


See the available options with 

```
java -jar ecmwf-loader-assembly-X.XX.jar --help
```


##Libraries

[scallop]


##Developers
[Boris Pezzatti]


[scallop]:https://github.com/scallop/scallop
[ECMWF public datsets]:http://apps.ecmwf.int/datasets/
[ECMWF registration]:https://apps.ecmwf.int/registration/
[ECMWF key file]:https://apps.ecmwf.int/auth/login/?back=https%3A//api.ecmwf.int/v1/key/
[ECMWF api]:https://software.ecmwf.int/wiki/display/WEBAPI/Access+ECMWF+Public+Datasets
[Boris Pezzatti]:mailto:boris.pezzatti@wsl.ch
