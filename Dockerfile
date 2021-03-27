FROM tomcat:9-jdk11
ADD target/battleship-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
