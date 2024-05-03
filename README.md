# ocpp-server

Open Charge Point Protocol server for monitoring EV charging. It exists to monitor my car while it charges
and to receive the Meter values messages from the charger which contain useful information such as the level
of charge of the car. 

See the file run.log which contains an example run of the server. At the time a BYD Atto 3 was attached to 
the charger and the car had finished charging. The charger is a Fronius WattPilot. 

Sadly the Fronius WattPilot doesn't report the State of Charge (SoC) meter value defined by the OCPP 1.6
specification, so this effort was in vane.

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
