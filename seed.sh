#!/bin/sh

wget https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/500-worst-passwords.txt

while read password; do
  bin/painbow -c $(boot2docker ip) -e "$password"
done < 500-worst-passwords.txt
