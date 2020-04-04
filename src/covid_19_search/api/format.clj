(ns covid-19-search.api.format
  (:require [clojure.string :as string]
            [schema.core :as s]))

(s/defschema FormatType
  (s/enum :abstract :long :raw))

(defn render-text
  [texts]
  (->> (group-by :section texts)
       (map (fn [[section group]]
              {:section section
               :paragraphs (map :text group)}))))

(defn format-search-doc
  [{:keys [abstract body_text metadata] :as doc}]
  (cond-> (dissoc doc :metadata)
    (seq abstract) (update :abstract render-text)
    (seq body_text) (update :body_text render-text)
    :alway (into metadata)))

(defn abstract-format
  [raw-doc]
  (-> (format-search-doc raw-doc)
      (select-keys [:paper_id :metadata :abstract])))

(s/defn format-fn
  [format :- FormatType]
  (case (or format :abstract)
    :raw identity
    :long format-search-doc
    :abstract abstract-format))

(defn format-doc
  [format doc]
  ((format-fn format) doc))

(defn format-search-docs
  [format docs]
  (update docs
          :data
          #(map (format-fn format) %)))
