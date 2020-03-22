(ns covid-19-search.mappings)


(def text {:type "text"})
(def token {:type "keyword"})
(def integer {:type "integer"})

(def location
  {:dynamic "false"
   :properties {:addrLine text
                :postCode text
                :settlement text
                :country text}})

(def affilation
  {:dynamic "false"
   :properties {:laboratory text
                :institution text
                :location location}})

(def author
  {:dynamic "false"
   :properties {:first text
                :middle text
                :last text
                :suffix text
                :affiliation affilation
                }})

(def metadata
  {:dynamic "false"
   :properties {:title text
                :authors author}})

(def ref-span
  {:dynamic "false"
   :properties {:start integer
                :end integer
                :text text
                :ref_id token}})

(def section
  {:dynamic "false"
   :properties {:text text
                :section text
                :cite_spans ref-span
                :ref_spans ref-span}})

(def other-ids
  {:dynamic "false"
   :properties {:DOI token}})

(def bib-entry
  {:dynamic "false"
   :properties {:ref_id token
                :title text
                :authors author
                :year integer
                :venue token
                :volume token
                :issn token
                :pages token
                :other_ids other-ids}})

(def paper
  {:dynamic "false"
   :properties
   {:paper_id token
    :metadata metadata
    :abstract section
    :body_text section
    :bib_entries bib-entry}})
