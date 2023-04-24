# kimai-ui

Java UI to access the API of a running Kimai instance

## Developing

We are using husky and conventional commits to keep us honest. Install them (via npm)

```
mvn install
```

## Structure

```
 - kimai-ui
   +- src
     +- uk
       +- co
         +- neontribe
           +- kimai
             +- api
             +- config
             +- core
             +- desktop
```

## Local Kimai

Start a local kimai instance to dev against:

```bash
docker-compose up -d
```

## Config

For now, we want to pre-configure the system.

```bash
cat <<EOF > ~/.config/neontribe/kimai-ui/config.yml
kimai:
  uri: http://localhost:8001
  uername: superadmin
  password: changemeplease
EOF
```

## Build

```bash
mvn clean compile assembly:single
```

## Run

```bash
java -jar target/kimai-ui-1.0-SNAPSHOT-jar-with-dependencies.jar
```

java -Dswing.aatext=true -Dswing.plaf.metal.controlFont=Tahoma -Dswing.plaf.metal.userFont=Tahoma 