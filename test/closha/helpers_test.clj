(ns closha.helpers-test
  (:require [clojure.test :refer :all]
            [closha.helpers :refer :all]))

; tests focus on the functions needed by SHA-256, i.e. 32 bit width, no rotl needed.

(def powersof2  (iterate (partial * 2) 1 ))


(deftest lsb-test
  (testing "does it return 0 for values of 2^32 and larger?"
    (is (= (->> powersof2 (drop 32) (take 16) (map lsb))
           (->> (repeat 0) (take 16))))))

(deftest rotr-test
  (testing "does rotr rotate 32 bit numbers correctly?"
    (testing "is n=rotr(n,32) true for 32 bit random numbers?"
      (let [lst (take 32 (repeatedly #(rand-int (Integer/MAX_VALUE))))]
        (is (= (map #(rotr % 32) lst) lst))))
    (testing "does rotr(n,1) half even numbers?"
      (let [lst (take 32 (filter even? (repeatedly #(rand-int (Integer/MAX_VALUE)))))]
        (is (= (map #(rotr % 1) lst) (map #(/ % 2) lst)))))))
