(ns covid-19-search.config
  (:require [clojure.java.io]
            [clojure.pprint :as pp]
            [clojure.edn :as edn]))

(def conf (atom nil))

(defn init!
  [filepath]
  (->> (slurp filepath)
       edn/read-string
       (reset! conf)))

(defn get-conf [& path]
  (get-in @conf path))

(defn pprint [] (pp/pprint @conf))
