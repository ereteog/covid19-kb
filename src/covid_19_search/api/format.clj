(ns covid-19-search.api.format
  (:require [clojure.string :as string]
            [schema.core :as s]))

(s/defschema FormatType
  (s/enum :abstract :full-text :raw))

(defn append-paragraph
  [paragraphs
   {:keys [section text] :as paragraph}]
  (let [[{previous-title :section
             previous-text :text}
            & remaining] paragraphs]
    (if (= previous-title section)
      (cons {:text (format "%s\n%s" previous-text text)
             :section section}
            remaining)
      (cons paragraph paragraphs))))

(defn render-text
  [texts]
  (->> (map #(select-keys % [:section :text]) texts)
       (reduce append-paragraph [])
       reverse))

(defn render-author
  [{firstname :first
    lastname :last
    email :email
    {:keys [laboratory institution location]} :affiliation
    :as _author}]
  (let [author-name (->> (remove empty? [firstname lastname])
                         (string/join " "))
        author-affiliation (->> (remove empty? [laboratory institution])
                                (string/join ", "))]
    (cond-> ""
      (not-empty author-name) (str author-name ". ")
      (not-empty (:country location)) (str (:country location) ". ")
      (not-empty author-affiliation) (str author-affiliation ". ")
      (not-empty email) (str email ".")
      :always string/trim)))

(defn render-metadata
  [metadata]
  (update metadata
          :authors
          #(map render-author %)))

(defn render-abstract
  [abstract]
  (->> (render-text abstract)
       (map :text)
       (string/join "\n")))

(defn format-search-doc
  [{:keys [abstract body_text metadata] :as doc}]
  (cond-> (dissoc doc :metadata)
    (seq abstract) (update :abstract render-abstract)
    (seq body_text) (update :body_text render-text)
    :alway (into (render-metadata metadata))))


(defn abstract-format
  [raw-doc]
  (-> (format-search-doc raw-doc)
      (select-keys [:paper_id :title :authors :abstract])))

(s/defn format-fn
  [format :- FormatType]
  (case (or format :abstract)
    :raw identity
    :full-text format-search-doc
    :abstract abstract-format))

(defn format-doc
  [format doc]
  ((format-fn format) doc))

(defn format-search-docs
  [format docs]
  (update docs
          :data
          #(map (format-fn format) %)))
