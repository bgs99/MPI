# MPI
## Run
Install jdk that has at least version 8.

If your OS is Windows, install Docker Desktop and launch it, open powershell and do the following command:
```
./build.ps1
```
if your OS is Linux, install docker and docker-compose (make sure docker daemon is running!!!), check if the bash shell is installed and do the following commands:
```
chmod +x build.sh
./build.sh
```

If the script the result is not expected, try clearing the images and volumes in Docker.

If the script fails at the npm install stage, then just restart it.

If the `docker-compose up --build` not starting in Linux, use root privileges (ex. sudo).
