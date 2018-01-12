(ns closha.core
  (:require [closha.sha256 :refer :all]
            [closha.aes :refer :all])
  (:gen-class))

(defn -main
  "Entry point"
  [& args]
  (println (if (empty? args)
             "Please give a string to hash as a parameter"
             (sha256 (first args)))))
