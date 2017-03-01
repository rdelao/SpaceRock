# SpaceRock

#Compilation
The preferred method is to use a tool called Gradle (https://gradle.org/).  
You can run it however you would like, but gradle is easy and makes packaging jars simple. The hard part (creating build.gradle) is already done.  


###To create Intellij project files using Gradle:
Run:  
gradle idea  

This will create project Intellij Idea project files already configured.  

Then select "Open Project" in Intellij and select the directory.  
Intellij may ask to import the gradle project: don't bother.  


###Setting up Intellij manually or if you have issues with Gradle
In Intellij, browse to src/main.  
Right click java->Mark Directory As->Sources Root  
Right click resources->Mark Directory As->Resources Root  

If we use junit for unit testing, you will need to do similiar with test resources.  

#Directory Layout
The directories follow the Apache standard.  
See: https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html

###Naming conventions:
General Java rules: http://www.oracle.com/technetwork/java/codeconventions-135099.html  
Note that Packages are named with lowercase to prevent naming conflicts with Classes.    


#Execution
The current Main class set in build.gradle is sensor.demo.Demo. This will undoubtedly change.  
  You can set your own in intellij by clicking Run->Edit Configurations

###To run the application type:
gradle run

###To create a runnable jar type:
gradle build

The jar is found in build/libs/

