(ns covid-19-search.api.format
  (:require [clojure.string :as string]))

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

(defn format-search-docs
  [docs]
  (update docs
          :data
          #(map format-search-doc %)))
