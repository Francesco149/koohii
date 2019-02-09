install maven, gnupg2 and openjdk

note to self: sonatype has shitty password rules so I have to use
an unusual password

create ~/.m2/settings.xml
```
<settings>
<servers>
<server>
  <id>ossrh</id>
  <username>your sonatype username</username>
  <password>your sonatype password</password>
</server>
</servers>
<profiles>
<profile>
  <id>gpg</id>
  <activation>
    <activeByDefault>true</activeByDefault>
  </activation>
  <properties>
    <gpg.useagent>true</gpg.useagent>
    <!-- gpg-plugin defaults to trying 'gpg' on the path, this changes that to 'gpg2' instead -->
    <gpg.executable>gpg2</gpg.executable>
    <!-- <gpg.passphrase>secret-passphrase-here</gpg.passphrase> -->
  </properties>
</profile>
</profiles>
</settings>
```

generate gpg key for the first time:

```
gpg2 --gen-key
```

find hash of your key

```
gpg2 --list-keys
```

publish key (replace hash with your hash)

```
gpg2 --keyserver hkp://pool.sks-keyservers.net --send-keys hash
```

add to ~/.gnupg/gpg-agent.conf

```
default-cache-ttl 3600
```

to not enter passphrase multiple times

reload gpg agent:

```
pg-connect-agent reloadagent /bye
```

cache passphrwase

```
touch /tmp/shit
gpg2 -ab /tmp/shit
```

perform a release:

```
mvn release:prepare
mvn release:perform
```

log into https://oss.sonatype.org

go to staging repositories, scroll to bottom, last element should be
your release

tick it and click close and confirm

spam refresh, wait for checks to pass

click release
