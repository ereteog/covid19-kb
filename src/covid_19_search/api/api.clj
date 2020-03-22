(ns covid-19-search.api.api
  (:require [compojure.api.sweet :refer [api context GET]]
            [ring.util.http-response :refer [ok]]
            [schema.core :as s]
            [covid-19-search.index :as index]
            [ductile.conn :as esc]
            [ductile.schemas :as ess]
            [ductile.document :as esd]
            [ring.middleware.reload :refer [wrap-reload]]
            [covid-19-search.api.format :as format]
            ))

(s/defschema AppContext
  {:es-conn ess/ESConn})

(s/defschema SearchResponse
  s/Any)

(def fields
  [:paper_id
   :title
   :authors
   :abstract
   :body
   :bib_entries])

(defn to-es-fields
  [field]
  (case field
    :title "metadata.title"
    :authors "metadata.authors"
    :body "body_text"
    (name field)))

(s/defschema Field
  (apply s/enum fields))

(def swagger
  {:ui "/"
    :spec "/swagger.json"
    :data {:info {:title "COVID knowledge base"
                  :description "COVID Knowledge API"}
           :tags [{:name "covid19", :description "Resources on COVID-19"}]}})

(def app
  (let [es-conn (esc/connect {:host "localhost"
                              :port 9200
                              :protocol :http})]
    (api
     {:swagger swagger}
     (context "/covid-19" []
              :tags ["covid19"]
              (GET "/search" []
                   :query-params [query :- String
                                  {limit :- s/Int nil}
                                  {offset :- s/Int nil}
                                  {fields :- [Field] nil}
                                  {raw-doc :- s/Bool nil}]
                   :return SearchResponse
                   (let [es-query {:query_string {:query query
                                                  :default_operator "AND"}}
                         options (cond-> {}
                                   fields (assoc :_source
                                                 (map to-es-fields fields))
                                   limit (assoc :limit limit)
                                   offset (assoc :offset offset))
                         res (esd/search-docs es-conn
                                              (index/indexname)
                                              es-query
                                              {}
                                              options)]
                     (cond-> res
                       (not raw-doc) format/format-search-docs
                       :always ok)))))))
