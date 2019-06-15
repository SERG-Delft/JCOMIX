# JCOMIX
##### a Search-based Tool to Detect Injection Vulnerabilities in Web Applications.

[![build status](https://gitlab.com/tweet.dimitri/JCOMIX/badges/master/pipeline.svg)](https://gitlab.com/tweet.dimitri/JCOMIX/commits/master)
[![coverage report](https://gitlab.com/tweet.dimitri/JCOMIX/badges/master/coverage.svg)](https://gitlab.com/tweet.dimitri/JCOMIX/commits/master)

This tool if used correctly will generate java test classes that use selenium to test penetrations on your web server.

#### Details
Genetic Algorithm library: [JGA](https://gitlab.com/tweet.dimitri/jga)

## Features

## Getting started

#### Prerequisites
* Java 1.8 (or higher)
* Maven (command line or within IDE)
* Apache Tomcat

###### Tests
* Chrome
* Chrome-driver

#### Running the artifact
TODO

#### Running the example program from source
* Open the command line
    * `cd /path/to/project/`
    * `mvn clean install`
    * `mvn package`

* The program needs a servlet to connect to.
    * `cd applications/apache-tomcat-9.0.19/bin`
    * If you're on a windows use:
        * `start.bat`
    * If you're on a linux/macos use:
        * `start.sh` 
        * You might have problems with privileges, to solve this use:
            * `chmod +x start.sh`
            * `chmod +x catalina.sh`


* Go to the PTG directory.
    * `cd ../PTG/target/`
* Run the tool with the `--init` flag to create the config files:  
    * `java -jar PTG-1.0-SNAPSHOT-jar-with-dependencies.jar --init`

* A proxy and a config file will be created. 
    * In the proxy.json change the `expected-output-path` property.
    * In the config.json change the `save-path` property.
    * In the config.json change the `path` property.
    * In the config.json change the `chromedriver-path` property.

* Next, as you probably don't have any Test Objective XML files yet, you can create them using:
    * `java -jar PTG-1.0-SNAPSHOT-jar-with-dependencies.jar --build-tos`
    * The TO files will be created in a folder called `generated`    

* Now you can run the program with this command:  
    * `java -jar PTG-1.0-SNAPSHOT-jar-with-dependencies.jar [flags*]`
    * '\*' implies zero or more
    * Some flags require an argument as specified in the manual

#### Testing another java servlet webapp

* Change some settings in the config.json.
    * Change the `html-url` property.
    * Change the `proxy-url` property.
    * Change the `inputs` property.

* Change some settings in the proxy.json.
    * Change the `target-entries`
    * Change the `expected-output-path`.

#### Testing another type of webapp
e.g. a node.js server instead of java servlet.

* Create an implementation of the Proxy interface for your type of webapp.

* Change some settings in the config.json.
    * Change the `html-url` property.
    * Change the `proxy-url` property.
    * Change the `inputs` property.
    
* Change some settings in the proxy.json.
    * Change the `target-entries`
    * Change the `expected-output-path`.
    
#### Testing a non-XML webapp
e.g. a webapp that uses json.

* Change some settings in the proxy.json.
    * Change the `output-language`.
    * Change the `expected-output-path`.

#### JUnit and Jest support
Both Jest and JUnit test suites can be generated using this tool.
This can be changed using the `test-suite` property

###### JUnit
To run the test suite do the following after the files have been generated
* Use either your IDE or maven to run the tests

###### Jest
To run the test suite do the following after the files have been generated
* `cd path/to/generated/tests`
* `npm install`
* `npm run test`

#### Small arguments manual
* Use the `-h` or `--help` flag to get the full manual
* Use the `--init` flag to generate a config and proxy file
* Use the `--cli` flag to open the CLI program (Try it!)

#### The config file
* url: the url to connect to.  
Must be a valid url.  
Example: http://localhost:8080/sbank/Affilier3DsecureSevlet

* path: the absolute path to the folder of test objectives.  
Must be a valid path.  
Example(Linux): /home/dimitri/Documents/comix/TestObjectives/sbank3

* save-path: the absolute path to the folder where the tests need to be saved.  
Must be a valid path.  
Example(Linux): /home/dimitri/Documents/comix/src/test/java

* connection: the type of connection to make to the server.   
Currently only supports HttpClient.  
Example: HttpClient

* time: the maximum amount of time to generate tests.  
Must be of the form `(number > 0)[smhd]`.  
Examples: 10s, 500m, 23h, 1d

* inputs: the amount of inputs to try  
Must be of the form `(number > 0)`.  
Examples: 1, 2, 3, 4

* restricted: whether to use the restricted alphabet  
Must be true or false.

* population: the amount of individuals to use  
Must be of the form `(number > 2 * amount_of_problems)`  
Examples: 100, 1500, 5000

* objective: the amount of objectives to run at the same time  
Examples: ONE, ALL

* approach: the approach/environment to use  
Examples: SIMPLE, MULTI

* compare: the compare operator to use  
Examples: SINGLE_OBJECTIVE, MULTI_OBJECTIVE

* score: the score operator to use  
Examples: RANK_SCORING

* crossover: the crossover operator to use  
Examples: SIMPLE_CROSSOVER

* select: the select operator to use  
Example: SIMPLE_ELITISM_SELECTION

* mutate: the mutate operator to use  
Example: ADAPTIVE_FITNESS_BASED_MUTATION

* debug: the debug level to use
Must be of the form `(number > 0 && number =< 3)`

* budgeting: whether to use the budgeting mechanism
Must be true or false.

* fitness-function: the fitness function to use
Note: these function impact the speed of the algorithm a lot for example, the Linear distance function is O(n) while the others are O(n^2)
Examples: REAL_CODED_EDIT_DISTANCE

* use-stall-manager: whether to use the stall manager  
Must be true or false.

* use-migration-actor: whether to use the migration actor  
Note: only works when using a multi environment
Must be true or false.


## Developing Custom Extensions
This tool has been developed to allow for a lot of extension.

#### Explaining the main class
    public static void main(String[] args) {
        // The arguments processor is needed to process the config file and the arguments given to the program.
        ArgumentProcessor processor = new ArgumentProcessor();
        
        // This line is needed to get an instance of a program.
        // This is to let the processor decide what program to make (e.g. GUI CLI or normal).
        Program program = processor.getProgram(args);

        // Here you can add your own flags.
        // How to use flags is explained in the next section.
        // You need to add flags before the initializeMaps() method is called, 
        // otherwise it will not be put in the property maps.
        // Also keep in mind that when you add this, 
        // you will also need to edit your config file to hold this property (except in the non setting case). 
        processor.addFlag("--myflag", new Flag("easter-egg", "Amazing easter egg!"));
        
        // The order in the next 3 function calls is very important as a different order will throw an exception.
        // First, we initialize the maps to hold all the properties and flags.
        processor.initializeMaps();
        // Next, we validate and process the arguments.
        processor.validateAndProcessArguments(args);
        // Last, we check all the properties for conflicting values.
        processor.findConflictsInPropertyValues();

        // Here we start up the real program.
        program.start();
    }

##### Adding flags
As shown in the explanation of the main class, you are able to add your own flags/properties to the program!  
However, to do this you need to keep some stuff in mind.  
For example:
* You can't use flags that are already in use by the program.
* Adding flags that are settings require you to also add them to the config file.
* Flags **can** be any arbitrary string. 

###### Flag types
`addFlag("--myflag", flagObject);`  
Adds a flag object corresponding to the `--myflag` flag.  

`Flag(String property, String description)`  
This flag does not need an argument  
This flag's property needs to be in the config file

`Flag(String property, String description, boolean setting)`  
This flag does not need an argument  
This flag's property needs to be in the config file iff setting is equal to false

`Flag(String property, String description, Set<String> options)`  
This flag needs an argument  
This flag's argument needs to be equal to one of the options  
This flag's property needs to be in the config file  

`Flag(String property, String description, String regex)`  
This flag needs an argument  
This flag's argument needs to match the regex  
This flag's property needs to be in the config file  

##### What if I make my own Program extension?
Lets say you have made your own program extension and want to use a command to initialize it.
In your main method, instead of writing:  
`Program program = processor.getProgram(args);`  
we will write:

    processor.addFlag("--myProgram", new Flag("Start myProgram", "Starts the myProgram instance!", false));
    
    Program program;
    if (args.length == 1 && args[0].equals("--myProgram")) {
        program = new myProgram();
    } else {
        program = processor.getProgram(args);
    }
Now if we run our program with just `--myProgram` as argument it will start the MyProgram instance!
