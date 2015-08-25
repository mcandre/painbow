# pinbow - a plug n' playable rainbow table

# EXAMPLES

```
$ mvn package
$ docker run -d -p 9042:9042 cassandra:2.1
$ bin/pinbow --cluster=$(boot2docker ip) --encrypt="What, me worry?"
$ bin/pinbow --cluster=$(boot2docker ip) --decrypt="e4bff95300a644037dc12495fdecf39d"
What, me worry?
```
