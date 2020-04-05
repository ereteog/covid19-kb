(ns covid-19-search.index
  (:require [covid-19-search.mappings :as mappings]
            [ductile.index :as esi]
            [ductile.document :as esd]
            [clojure.data.json :as json]))

(defn indexname
  []
  ;; TODO put it in config
  "covid-kb")

(defn format-bib-entries
  [bib-entries]
  (map (fn [[k v]]
         (assoc v :ref_id k))
       bib-entries))

(defn prepare-paper
  "format a paper before indexing it"
  [filepath]
  (-> (slurp filepath)
      (json/read-str :key-fn keyword)
      (update :bib_entries format-bib-entries)))

(defn index-paper
  "index a paper"
  [conn index filepath]
  (let [prepared (prepare-paper filepath)]
    (esd/index-doc conn
                   index
                   prepared
                   {:refresh "false"
                    :mk-id :paper_id})))

(def settings {:number_of_shards 3
               :number_of_replicas 1
               :refresh_interval "60s"})

(defn init
  [conn]
  (when-not (esi/index-exists? conn (indexname))
    (esi/create! conn
                 (indexname)
                 {:settings settings
                  :mappings mappings/paper})))
