
Environment Setup
=================================

JDK
1. JDK 1.6 or newer
2. Declare JAVA_HOME environment variable
3. Add %JAVA_HOME%\bin to PATH

GROOVY
1. Download Groovy binary (http://www.groovy-lang.org/download.html)
2. Unpack Groovy somewhere in your drive
3. Declare GROOVY_HOME environment variable
4. Add %GROOVY_HOME%\bin to PATH

GRADLE
1. Download Gradle binary (https://gradle.org/downloads/)
2. Unpack Gradle somewhere in your hard drive
3. Declare GRADLE_HOME environment variable
4. Add %GRADLE_HOME%\bin to PATH


Test that everything runs
=================================
From the command line issue the following commands one at a time, 
they all shall report their versions, if not review environment 
variables names, values and make sure they are added to PATH

java -version
groovy --version
gradle --version


Nice to have
=================================
Eclipse plugin for Groovy
Eclipse plugin for Gradle