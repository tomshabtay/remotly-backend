## REMOTLY JAVA SERVER ##

### On eclipse: ###

1. File > Import > Maven > Existing Maven Projects
2. Browse... > (find the root directory of this project)
3. Click Finish
4. To run on eclipse, run the Hello.java found on 'sparkexample' package

### On docker: ###

1. Download and install Docker (https://store.docker.com/editions/community/docker-ce-desktop-windows)
2. Pop up a PowerShell
3. Use 'cd' to navigate to root directory
4. Run: docker build -t remotly-java .
5. Run: docker run -p 4567:4567 remotly-java

### Shutdown: ###
6. Run: docker ps
7. Find the remotly-java container id
8. Run: docker stop <id-from-step-7>
