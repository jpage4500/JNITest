//
//  MovieController.hpp
//  Highrise
//
//  Created by Jimmy Xu on 12/19/18.
//  Copyright © 2019 Highrise. All rights reserved.
//

#ifndef MovieController_hpp
#define MovieController_hpp

#include <string>
#include <vector>
#include <map>
#include <jni.h>
#include <android/log.h>

// logging
#define  LOG_TAG    "MovieController"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

namespace movies {
    class Actor {
    public:
        std::string name;
        int age;
        
        //optional challenge 1: Load image from URL
        std::string imageUrl;
    };
    
    class Movie {
    public:
        std::string name;
        int lastUpdated;
        
    };
    
    class MovieDetail {
    public:
        std::string name;
        float score;
        std::vector<Actor> actors;
        std::string description;
    };
    
    class MovieController {
    private:
        std::vector<Movie*> _movies;
        std::map<std::string, MovieDetail*> _details;

    public:
        MovieController() {
            //populate data
            for (int i = 0; i < 10; i++) {
                auto movie = new Movie();
                movie->name = "Top Gun " + std::to_string(i);
                movie->lastUpdated = i * 10000;
                _movies.push_back(movie);
                
                auto movieDetail = new MovieDetail();
                movieDetail->name = movie->name;
                movieDetail->score = rand() % 10;
                movieDetail->description = "As students at the United States Navy's elite fighter weapons school compete to be best in the class, one daring young pilot learns a few things from a civilian instructor that are not taught in the classroom.";
                
                auto tomCruise = Actor();
                tomCruise.name = "Tom Cruise";
                tomCruise.age = 50;
                
                auto valKilmer = Actor();
                valKilmer.name = "Val Kilmer";
                valKilmer.age = 46;
                valKilmer.imageUrl = "https://m.media-amazon.com/images/M/MV5BMTk3ODIzMDA5Ml5BMl5BanBnXkFtZTcwNDY0NTU4Ng@@._V1_UY317_CR4,0,214,317_AL_.jpg";
                
                movieDetail->actors.push_back(tomCruise);
                movieDetail->actors.push_back(valKilmer);
                
                if (i % 2 == 0) {
                    auto timRobbins = Actor();
                    timRobbins.name = "Tim Robbins";
                    timRobbins.age = 55;
                    timRobbins.imageUrl = "https://m.media-amazon.com/images/M/MV5BMTI1OTYxNzAxOF5BMl5BanBnXkFtZTYwNTE5ODI4._V1_UY317_CR16,0,214,317_AL_.jpg";
                    
                    movieDetail->actors.push_back(timRobbins);
                } else {
                    auto jenniferConnelly = Actor();
                    jenniferConnelly.name = "Jennifer Connelly";
                    jenniferConnelly.age = 39;
                    jenniferConnelly.imageUrl = "https://m.media-amazon.com/images/M/MV5BOTczNTgzODYyMF5BMl5BanBnXkFtZTcwNjk4ODk4Mw@@._V1_UY317_CR12,0,214,317_AL_.jpg";
                    
                    movieDetail->actors.push_back(jenniferConnelly);
                }
                
                _details[movie->name] = movieDetail;
            }
        }

        //Returns list of movies
        std::vector<Movie*> getMovies() {
            return _movies;
        }
        
        //Returns details about a specific movie
        MovieDetail* getMovieDetail(std::string name) {
            for (auto item:_details) {
                if (item.second->name == name) {
                    return item.second;
                }
            }
            return nullptr;
        }

    };

    extern "C" {
        //
        JNIEXPORT jlong JNICALL Java_com_github_jpage4500_jnitest_MainActivity_createMovieController__ (JNIEnv *env, jobject obj);

        JNIEXPORT jobjectArray JNICALL Java_com_github_jpage4500_jnitest_MainActivity_getMovies(JNIEnv *env, jobject obj, jlong nativePointer);

        JNIEXPORT jobject JNICALL Java_com_github_jpage4500_jnitest_MainActivity_getMovieDetail(JNIEnv *env, jobject obj, jlong nativePointer, jstring movieName);
    };

    JNIEXPORT jlong JNICALL Java_com_github_jpage4500_jnitest_MainActivity_createMovieController__ (JNIEnv* env, jobject obj) {
        return (jlong)(new MovieController());
    }

    JNIEXPORT jobjectArray JNICALL Java_com_github_jpage4500_jnitest_MainActivity_getMovies(JNIEnv *env, jobject obj, jlong nativePointer) {
        std::vector<Movie*> movies = ((MovieController*)nativePointer)->getMovies();

        jclass movieClass = env->FindClass( "com/github/jpage4500/jnitest/models/Movie" );
        jobjectArray objarray = (env)->NewObjectArray(movies.size(), movieClass, 0);

        for (int i = 0; i < movies.size(); i++) {
            Movie *movie = movies[i];
            jstring js = (env)->NewStringUTF(movie->name.c_str());

            jmethodID constructor = (env)->GetMethodID(movieClass, "<init>", "(Ljava/lang/String;I)V");
            jobject jMovie = (env)->NewObject(movieClass, constructor, js, movie->lastUpdated);
            (env)->SetObjectArrayElement(objarray, i, jMovie);
        }

        return objarray;
    }

    JNIEXPORT jobject JNICALL Java_com_github_jpage4500_jnitest_MainActivity_getMovieDetail(JNIEnv *env, jobject obj, jlong nativePointer, jstring movieName) {
        // convert argument to char*
        const char *movieNameStr = env->GetStringUTFChars(movieName, 0);
        // get movie detail from name
        MovieDetail *movieDetail = ((MovieController*)nativePointer)->getMovieDetail(movieNameStr);
        if (movieDetail == NULL) {
            LOGD("getMovieDetail: no movie found for: %s", movieNameStr);
            return NULL;
        }

        // create Java Actor array and populate
        jclass actorClass = env->FindClass("com/github/jpage4500/jnitest/models/Actor");
        jobjectArray actorArr = (env)->NewObjectArray(movieDetail->actors.size(), actorClass, 0);
        for (int i = 0; i < movieDetail->actors.size(); i++) {
            Actor actor = movieDetail->actors[i];
            jstring jsActorName = (env)->NewStringUTF(actor.name.c_str());
            jstring jsActorUrl = (env)->NewStringUTF(actor.imageUrl.c_str());

            jmethodID constructor = (env)->GetMethodID(actorClass, "<init>", "(Ljava/lang/String;ILjava/lang/String;)V");
            jobject jActor = (env)->NewObject(actorClass, constructor, jsActorName, actor.age, jsActorUrl);
            (env)->SetObjectArrayElement(actorArr, i, jActor);
        }

        // convert description to jstring
        jstring jsDescription = (env)->NewStringUTF(movieDetail->description.c_str());

        // create Java MovieDetail class and populate with results
        jclass movieDetailClass = env->FindClass("com/github/jpage4500/jnitest/models/MovieDetail");
        jmethodID constructor = (env)->GetMethodID(movieDetailClass, "<init>", "(Ljava/lang/String;F[Lcom/github/jpage4500/jnitest/models/Actor;Ljava/lang/String;)V");
        jobject jMovieDetail = (env)->NewObject(movieDetailClass, constructor, movieName, movieDetail->score, actorArr, jsDescription);

        return jMovieDetail;
    }

}

#endif /* MovieController_hpp */
