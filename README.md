# REMOTLY JAVA SERVER

###On eclipse:
==================
-File > Import > Maven > Existing Maven Projects
-Browse... > (find the root directory of this project)
-Click Finish
-To run on eclipse, run the Hello.java found at 'sparkexample' package

###On docker:
==================
-Download and install Docker (https://store.docker.com/editions/community/docker-ce-desktop-windows)
-Pop up a PowerShell
-Use 'cd' to navigate to root directory
-Run: docker build -t remotly-java .
-Run: docker run -p 4567:4567 remotly-java

Shutdown:
-Run: docker ps
-Find the remotly-java container id
-Run: docker stop <id>
