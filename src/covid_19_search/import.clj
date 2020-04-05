(ns covid-19-search.import
  (:require [ductile.conn :as esc]
            [clojure.java.io :as io]
            [covid-19-search.index :as index]
            [clojure.string :as str]))

(defn -main [& args]
  (let [conn (esc/connect {:host "localhost"
                           :port 9200
                           :protocol :http})
        path "CORD-19-research-challenge"
        files (->> (io/resource path)
                   io/file
                   file-seq
                   (filter #(str/ends-with? (.getName %) ".json")))]
    (index/init conn)
    (doall
     (pmap #(index/index-paper conn %)
           files))))
