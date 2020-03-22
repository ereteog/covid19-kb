(defproject covid-19-search "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "1.0.0"]
                 [metosin/compojure-api "1.1.13"]
                 [prismatic/schema "1.1.12"]
                 [metosin/schema-tools "0.12.2"]
                 [threatgrid/ductile "0.1.0-SNAPSHOT"]]

  :ring {:handler covid-19-search.api.api/app}
  :uberjar-name "server.jar"
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.12.5"]]}}
  :resource-paths ["resources"])
