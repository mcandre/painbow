# painbow - a scalable rainbow table

# ABOUT

painbow is a scalable rainbow table, able to handle petabytes of passwords and hashes. Just point painbow to a Cassandra cluster node and enter some passwords to index.

# EXAMPLES

```
$ docker run -p 9042:9042 -p 7000:7000 -p 7001:7001 -p 7199:7199 -p 9160:9160 cassandra:2.2

$ gradle shadowJar

$ bin/painbow -c $(boot2docker ip) --migrate

$ bin/painbow -c $(boot2docker ip) -e "What, me worry?"
$ bin/painbow -c $(boot2docker ip) -d c25851553639f94b5e5be71ff22889c4
What, me worry?

$ bin/painbow -c $(boot2docker ip) -a SHA1 -e "Al Jafee"
13b4e0cc1d18b04cfeeb92e30368260a17d337bb
$ bin/painbow -c $(boot2docker ip) -a SHA1 -d 13b4e0cc1d18b04cfeeb92e30368260a17d337bb
Al Jafee

$ bin/seed-painbow
...

$ bin/painbow -c $(boot2docker ip) --size
500

$ bin/painbow -c $(boot2docker ip) -d ec0e2603172c73a8b644bb9456c1ff6e
batman

$ bin/painbow --help
Usage:
  painbow [--contact-point=<host>] --migrate
  painbow [--contact-point=<host>] [--algorithm=<algorithm>] --encrypt=<password>
  painbow [--contact-point=<host>] [--algorithm=<algorithm>] --decrypt=<hash>
  painbow [--contact-point=<host>] [--algorithm=<algorithm>] --size
  painbow --version
  painbow --help
Options:
  -m --migrate                Run migrations.
  -d --decrypt=<hash>         Decrypt a hash.
  -e --encrypt=<password>     Encrypt a password.
  -s --size                   Calculate number of passwords stored.
  -c --contact-point=<host>   Cassandra contact point host [default: 127.0.0.1].
  -a --algorithm=<algorithm>  Hash algorithm [default: MD5].
  -v --version                Show version.
  -h --help                   Print usage info.
```

# REQUIREMENTS

* [Cassandra](http://cassandra.apache.org/) 2+
* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 1.7+
* [gradle](http://gradle.org/) 2.1+

## Optional

* [Sonar](http://www.sonarqube.org/)
* [Infer](http://fbinfer.com/)
* [Docker](https://www.docker.com/)
* [wget](http://www.gnu.org/software/wget/)
* [bash](http://www.gnu.org/software/bash/)
* [shfmt](https://github.com/mvdan/sh) (e.g. `go get github.com/mvdan/sh/cmd/shfmt)
* [bashate](https://github.com/openstack-dev/bashate)
* [shlint](https://rubygems.org/gems/shlint)
* [shellcheck](http://hackage.haskell.org/package/ShellCheck)
* [stank](https://github.com/mcandre/stank) (e.g. `go get github.com/mcandre/stank/...`)

## Debian/Ubuntu

```
$ sudo echo "deb http://downloads.sourceforge.net/project/sonar-pkg/deb binary/" >> /etc/apt/sources.list
$ sudo apt-get update
$ sudo apt-get install docker.io wget gradle sonar
```

## RedHat/Fedora/CentOS

```
$ sudo wget -O /etc/yum.repos.d/sonar.repo http://downloads.sourceforge.net/project/sonar-pkg/rpm/sonar.repo
$ sudo yum install docker-io wget gradle sonar
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
$ brew install boot2docker wget gradle sonar sonar-runner
```

### Windows

* [Chocolatey](https://chocolatey.org/)

```
> chocolatey install docker wget git gradle sonar-runner
```
