# painbow - a plug 'n' play rainbow table

# EXAMPLES

```
$ mvn package
$ docker run -d -p 9042:9042 cassandra:2.1
$ bin/painbow -c $(boot2docker ip) -m
$ bin/painbow -c $(boot2docker ip) -e "What, me worry?"
$ bin/painbow -c $(boot2docker ip) -d c25851553639f94b5e5be71ff22889c4
What, me worry?
```

# REQUIREMENTS

* [Cassandra](http://cassandra.apache.org/) 2.1+

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
