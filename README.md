# Project Kendal

Kendal framework is a tool that aims at facilitating Java programmers work. It does it by allowing a programmer
to create some boilerplate pieces of code with a very little effort in an automatic manner. There is a set of
annotations defined by the library. Each of them allows for creation or modification of a code. Whenever programmer
decides he or she needs one of the functionalities supplied with a framework, one can use annotation instead of
manual creation of required code. Later, during project compilation, all the code is created/modified automatically
according to instructions specified by a programmer with all the annotations that he or she used. 

This project is a part of a Bachelor's diploma thesis in the field of Computer Science.

## Authors

* **Konrad Gancarz** - [LinkedIn](https://www.linkedin.com/in/konrad-gancarz-238901127/)
* **Arkadiusz Ryszewski** - [LinkedIn](https://www.linkedin.com/in/arkadiusz-ryszewski-203640b9/)

## References
* Kendal Plugin repository: https://bitbucket.org/ArkadyPL/kendal-plugin
* Diagrams describing Kendal Project: https://drive.google.com/file/d/1p_e8ps9zCD2HsS7Bj2rCgNtAVXcFd9xZ/view?usp=sharing
* Diagrams describing javac: https://drive.google.com/file/d/1XBkKvlFzLuCMnuZSCdfivbncTcyaQW7w/view?usp=sharing
* Thesis document repository: https://bitbucket.org/ArkadyPL/bsc-thesis

## Manual

### Testing compilation scenarios
We use [jtreg](https://openjdk.java.net/jtreg/) for testing negative and positive compilation scenarios.
To set up environment for testing:
1. Download jtreg from [Downloads page](https://ci.adoptopenjdk.net/view/Dependencies/job/jtreg/)
2. Set up intellij plugin - [instructions](https://openjdk.java.net/jtreg/intellij-plugin.html)
3. Set JTREG_HOME environment variable pointing to your jtreg home directory.
4. Set jtreg home dir in jtreg settings in idea

#### Running tests from terminal
Execute in kendal root directory:
```
make compilation-tests
```

#### Running tests from IDEA
1. Add jtreg run configuration
2. Select directory containing TEST.ROOT file as "Directory"
3. Add options:
#####On linux:
```
-cpa:handlers/target/classes:processor/target/classes -javacoption:-Xlint:none -agentvm -workDir tests/target/JTwork -reportDir tests/target/JTreport
```
#####On windows:
```
-cpa:handlers/target/classes;processor/target/classes -javacoption:-Xlint:none -agentvm -workDir tests/target/JTwork -reportDir tests/target/JTreport
```
Configuration will run all tests from selected directory.


## License

This project is licensed under the MIT License - see the [MIT License](https://opensource.org/licenses/MIT) for details.
