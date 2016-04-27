(ns closha.core-test
  (:require [clojure.test :refer :all]
            [closha.core :refer :all]))

(deftest inverse-test
  (testing "test the multiplicative inverse elements for all x in GF(2^8)"
    (is (every? (partial = 1) (map #(gmul % (ginv %)) (range 1 255))))))
