(defproject clojure-web-hello-world "1.0-SNAPSHOT"
    :description "Hello world web application"
    :dependencies [[org.clojure/clojure "1.6.0"]
                   [ring/ring "1.3.1"]
                   [org.clojure/tools.logging "0.3.1"]]
    :main clojure-web-hello-world.core
    :aot :all)
