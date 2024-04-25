# ocpp-server

Open Charge Point Protocol server for monitoring EV charging. It exists to monitor my car while it charges
and to receive the Meter values messages from the charger which contain useful information such as the level
of charge of the car. 

It uses a fork of the Java OCPP library
- Original: https://github.com/ChargeTimeEU/Java-OCA-OCPP
- My fork: https://github.com/warwickhunter/Java-OCA-OCPP

## Building and Running 
This server can be run with the command 
```shell
./gradlew run
```

Distributions of the app can be built with the command 
```shell
./gradlew assembleDist
```

It can be installed with the command
```shell
./gradlew installDist 
```

Warwick Hunter 2024-04-25