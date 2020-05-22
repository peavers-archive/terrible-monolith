# Terrible Monolith 
This project takes a collection of micro-services and bundled together as a multi-module monolith project. 

* [terrible-api-service](https://github.com/peavers/terrible-api-service)
* [terrible-search-service](https://github.com/peavers/terrible-search-service)
* [terrible-thumbnail-creator](https://github.com/peavers/terrible-thumbnail-creator)
* [terrible-directory-scanner](https://github.com/peavers/terrible-directory-scanner)
* [terrible-task-processor](https://github.com/peavers/terrible-task-processor)
* [terrible-task-launcher](https://github.com/peavers/terrible-task-launcher) 

## What does this all do
The goal here was to create something a bit like YouTube but using only your local videos for personal use. Given a
directory, this project will scan for media files, generate thumbnails, detect faces in those thumbnails and create
an indexed search collection. You can use the [terrible-spa](https://github.com/peavers/terrible-spa) to interact with 
this service.

## Modules

#### Terrible App
The core of the application, the fat jar you would execute. Holder of the controllers and wiring the services together.

#### Directory Scanner
Reads the file system looking for media files, either video or images. The code is ran as a Spring Batch job, 
managed by the Terrible App. 

#### Thumbnails
Generate 12 unique thumbnails for a media file. Much like the Terrible Directory Scanner this is executed via a
 Spring Batch job. 

#### Face Finder
Use OpenCV to find faces in thumbnails created by the Terrible Thumbnail Library. 

#### Search
Pushes all records from MongoDB into ElasticSearch and handles queries to and from ElasticSearch. Basically a simple
 wrapper which provides search mechanics. 

## Running

The `terrible-library-face-finder` requires the OpenCV native bindings to be on the library path of the JVM. 
This can be achieved using: 
```
-Djava.library.path=$PROJECT_DIR$/terrible-library-face-finder/lib/native
```  

## Dev
This project depends on a few external services. They can be brought up using the `docker-compoose.yaml` file inside
 the `dev` directory of the project root. 
 
#### Services
* MongoDB
* Mongo Express - A UI manager for Mongo, access on `http://localhost:9000`
* Grafana - A UI to monitor the running service, access on `http://localhost:3000`
* Prometheus - Pushes data into Grafana, access on `http://localhost:9090`
* ElasticSearch 
