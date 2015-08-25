# pinbow - a plug 'n' play rainbow table

# EXAMPLES

```
$ mvn package
$ docker run -d -p 9042:9042 cassandra:2.1
$ bin/pinbow -c $(boot2docker ip) -e "What, me worry?"
$ bin/pinbow -c $(boot2docker ip) -d "e4bff95300a644037dc12495fdecf39d"
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
