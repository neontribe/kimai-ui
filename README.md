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
git clone git@github.com:tobybatch/kimai2.git
cd kimai2
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
