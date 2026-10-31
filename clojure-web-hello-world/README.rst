===================================
Clojure Hello World Web Application
===================================

Bulding and running the Docker image:

.. code-block:: bash

    $ lein uberjar
    $ docker build -t clojure-web-hello-world .
    $ docker run -p 8080:8080 clojure-web-hello-world
