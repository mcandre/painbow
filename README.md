# painbow - a plug 'n' play rainbow table

# ABOUT

painbow is a scalable rainbow table, able to handle petabytes of passwords and hashes. Just point painbow to a Cassandra cluster node and enter some passwords to index.

# EXAMPLES

```
$ docker run -p 9042:9042 -p 7000:7000 -p 7001:7001 -p 7199:7199 -p 9160:9160 cassandra:2.2

$ mvn package

$ bin/painbow -c $(boot2docker ip) --migrate

$ bin/painbow -c $(boot2docker ip) -e "What, me worry?"
$ bin/painbow -c $(boot2docker ip) -d c25851553639f94b5e5be71ff22889c4
What, me worry?

$ bin/painbow -c $(boot2docker ip) -a SHA1 -e "Al Jafee"
13b4e0cc1d18b04cfeeb92e30368260a17d337bb
$ bin/painbow -c $(boot2docker ip) -a SHA1 -d 13b4e0cc1d18b04cfeeb92e30368260a17d337bb
Al Jafee

$ bin/painbow -h
Usage:
  painbow [--contact-point=<host>] --migrate
  painbow [--contact-point=<host>] [--algorithm=<algorithm>] --encrypt=<password>
  painbow [--contact-point=<host>] [--algorithm=<algorithm>] --decrypt=<hash>
  painbow --version
  painbow --help
Options:
  -m --migrate                Run migrations.
  -d --decrypt=<hash>         Decrypt a hash.
  -e --encrypt=<password>     Encrypt a password.
  -c --contact-point=<host>   Cassandra contact point host [default: 127.0.0.1].
  -a --algorithm=<algorithm>  Hash algorithm [default: MD5].
  -v --version                Show version.
  -h --help                   Print usage info.
```

# REQUIREMENTS

* [Cassandra](http://cassandra.apache.org/) 2.2+
* [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 1.7+

## Optional

* [Docker](https://www.docker.com/)

## Debian/Ubuntu

```
$ sudo apt-get install docker.io
```

## RedHat/Fedora/CentOS

```
$ sudo yum install docker-io
```

## non-Linux

* [VirtualBox](https://www.virtualbox.org/)
* [Vagrant](https://www.vagrantup.com/)
* [boot2docker](http://boot2docker.io/)

### Mac OS X

* [Xcode](http://itunes.apple.com/us/app/xcode/id497799835?ls=1&mt=12)
* [Homebrew](http://brew.sh/)
* [brew-cask](http://caskroom.io/)

```
$ brew cask install virtualbox vagrant
$ brew install boot2docker
```

### Windows

* [Chocolatey](https://chocolatey.org/)

```
> chocolatey install docker
```
