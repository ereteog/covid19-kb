(ns covid-19-search.import
  (:require [ductile.conn :as esc]
            [clojure.java.io :as io]
            [covid-19-search.config :as c]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.pprint :as pp]
            [covid-19-search.index :as index]
            [clojure.string :as str]))

(def cli-options
  [["-h" "--help"]
   ["-c" "--conf-path CONF" "configuration PATH"
    :default "resources/config.edn"]
   ["-p" "--paper-dir DIR" "papers DIR"
    :default "resources/CORD-19-research-challenge"]
   ["-s" "--store STORE" "the store to load"
    :default :covid
    :parse-fn keyword]])

(defn -main [& args]
  (let [{:keys [options errors summary]} (parse-opts args cli-options)
        {:keys [conf-path paper-dir store]} options]
    (when errors
      (binding  [*out* *err*]
        (println (str/join "\n" errors))
        (println summary))
      (System/exit 1))
    (when (:help options)
      (println summary)
      (System/exit 0))
    (pp/pprint options)
    (c/init! conf-path)
    (c/pprint)
    (let [es-index (c/get-conf store :indexname)
          conn (esc/connect (select-keys
                             (c/get-conf :elasticsearch)
                             [:host :port :protocol]))
          files (->> (io/file paper-dir)
                     file-seq
                     (filter #(str/ends-with? (.getName %) ".json")))]
      (index/init conn)
      (doall
       (pmap #(index/index-paper conn es-index %)
             files)))))
