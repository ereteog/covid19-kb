(ns covid-19-search.api.api
  (:require [compojure.api.sweet :refer [api context GET]]
            [ring.util.http-response :as resp]
            [schema.core :as s]
            [schema-tools.core :as st]
            [covid-19-search.index :as index]
            [ductile.conn :as esc]
            [ductile.schemas :as ess]
            [ductile.document :as esd]
            [ring.middleware.reload :refer [wrap-reload]]
            [covid-19-search.api.format :as format]
            [covid-19-search.nlp.plural :as plural]))

(s/defschema AppContext
  {:es-conn ess/ESConn})

(s/defschema Section
  (st/optional-keys
   {:text s/Str
    :section s/Str
    :cite_spans s/Any
    :ref_spans s/Any}))

(s/defschema Paper
  (st/open-schema
   (st/optional-keys
    {:paper_id s/Str
     :abstract [Section]
     :body_text [Section]
     :metadata {s/Any s/Any}
     :bib_entries [{s/Any s/Any}]})))

(s/defschema SearchResponse
  {:data [Paper]
   :paging s/Any})

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

(s/defschema SearchParams
  (st/merge
   {:query String}
   (st/optional-keys
    {:limit s/Int
     :offset s/Int
     :fields [Field]
     :format format/FormatType})))

(s/defn query-string
  [query]
  {:query_string {:query query
                  :default_operator "AND"}})

(s/defn search
  [{:keys [es-conn indexname]} :- AppContext
   {:keys [query limit offset fields format]} :- SearchParams]
  (let [options (cond-> {}
                  fields (assoc :_source
                                (map to-es-fields fields))
                  limit (assoc :limit limit)
                  offset (assoc :offset offset))
        res (esd/search-docs es-conn
                             indexname
                             (query-string query)
                             {}
                             options)]
    (resp/ok
     (format/format-search-docs format res))))

(s/defn get-paper
  [{:keys [es-conn indexname]} :- AppContext
   paper-id :- s/Str
   format :- format/FormatType]
  (if-let [paper (esd/get-doc es-conn
                              indexname
                              paper-id
                              {})]
    (resp/ok (format/format-doc format paper))
    (resp/not-found "no paper found")))

(defn format-bucket
  [{:keys [key score doc_count] :as _bucket}]
  {:word key
   :similarity score
   :paper_count doc_count})

(s/defn related
  [{:keys [es-conn indexname]} :- AppContext
   query :- s/Str
   limit :- s/Int]
  (let [exclude (cons query
                      (plural/pluralize-query query))
        _ (println "exclude ==> " exclude)
        significant-agg {:keywords
                         {:significant_text
                          {:size (min limit 50)
                           :field "body_text.text"
                           :filter_duplicate_text true
                           :jlh {}
                           :exclude exclude}}}
        sampler-agg {:related {:sampler {:shard_size 300}
                               :aggregations significant-agg}}
        res (esd/query es-conn
                       indexname
                       (query-string query)
                       sampler-agg
                       {:limit 0})
        formatted (->> res
                       :aggs :related :keywords :buckets
                       (map format-bucket))]
    (resp/ok formatted)))

(def app
  (let [es-conn (esc/connect {:host "localhost"
                              :port 9200
                              :protocol :http})
        ctx {:es-conn es-conn
             :indexname (index/indexname)}]
    (api
     {:swagger swagger}
     (context "/covid-19" []
              :tags ["covid19"]
              (GET "/related" []
                   :query-params [query :- s/Str
                                  limit :- s/Int]
                   :return s/Any
                   (related ctx query limit))
              (GET "/:paper-id" []
                   :path-params [paper-id :- s/Str]
                   :query-params [format :- format/FormatType]
                   :return Paper
                   (get-paper ctx paper-id format))
              (GET "/" []
                   :query [params SearchParams]
                   :return SearchResponse
                   (search ctx params))))))

(def reloadable-app
  (wrap-reload #'app))
