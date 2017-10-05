An implementation of a genetic algorithm in Java.
The intent is to be reusable, so that future problems need only provide a way to decode 'gene sequence' into their
problem domain and provide a means of assigning a fitness to a gene sequence.

Please see the wiki page at

http://wiki.github.com/aaronp/ga/

for more information.

## Build

  sbt docker

## Run

Just run the docker image, either w/ some file input, e.g. given the file ~/input/x on the host system:

  docker run -v ~/input/:/foo porpoiseltd/ga /foo/x

or a shipped example input file:

  docker run porpoiseltd/ga /examples/simple.example

or just specify the numeric inputs directly, the first number being the target number:

  docker run porpoiseltd/ga 1 2 3 4 5
