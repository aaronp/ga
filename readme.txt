An implementation of a genetic algorithm in Java. The intent is to be reusable, so that future problems need only provide a way to decode 'gene sequence' into their problem domain and provide a means of assigning a fitness to a gene sequence.

Please see the wiki page at

http://wiki.github.com/aaronp/ga/

for more information.

## Build/run

sbt docker

and then run either w/ some file input, e.g. given the file ~/input/x on the host system:

docker run -v ~/input/:/foo com.github.aaronp/ga /foo/x

or just specify the numeric inputs directly:


docker run com.github.aaronp/ga 1 2 3 4 5
