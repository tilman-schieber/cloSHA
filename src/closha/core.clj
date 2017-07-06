(ns closha.core
    (:require [closha.helpers :refer :all]
              [closha.sha256 :refer :all]
              [closha.aes :refer :all])
    (:gen-class))

; CloSHA - SHA algorithms in clojure
;
; Reference:
; http://csrc.nist.gov/publications/fips/fips180-4/fips-180-4.pdf
;
(defn -main
  "Entry point"
  [& args]
  (println (if (empty? args)
               "Please give a string to hash as a parameter"
               (sha256 (first args)))))
