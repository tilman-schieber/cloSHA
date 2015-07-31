(ns closha.helpers-test
  (:require [clojure.test :refer :all]
            [closha.helpers :refer :all]))

; tests focus on the functions needed by SHA-256, i.e. 32 bit width, no rotl needed.

(def powersof2  (iterate (partial * 2) 1 ))


(deftest lsb-test
  (testing "is lsb working correctlyfor w=32?"
    (testing "for the 32 least significant bits?"
      (let [lst (take 32 powersof2)]
        (is (= lst (map lsb lst)))))
    (testing "does it return 0 for values of 2^32 and larger?"
        (is (= (->> powersof2 (drop 32) (take 16) (map lsb))
               (->> (repeat 0) (take 16))))))

  (testing "is lsb working correctly for different bit widths?"
    (is (= (lsb 255) (lsb 255 32) (lsb 255 16) (lsb 255 8)))
    (is (= (lsb 2r01010101010101010101010101010101 8)
                                        2r01010101))))

(deftest rotr-test
  (testing "does rotr rotate 32 bit numbers correctly?"
    (testing "is n=rotr(n,32) true for 32 bit random numbers?"
      (let [lst (take 32 (repeatedly #(rand-int (Integer/MAX_VALUE))))]
        (is (= (map #(rotr % 32) lst) lst))))
    (testing "does rotr(n,1) half even numbers?"
      (let [lst (take 32 (filter even? (repeatedly #(rand-int (Integer/MAX_VALUE)))))]
        (is (= (map #(rotr % 1) lst) (map #(/ % 2) lst)))))
    (testing "do some test cases rotate correctly?"
      (is (= (rotr 256 8) 1))
      (is (= (rotr 256 9) 0x80000000))
      (is (= (rotr 2r01010101010101010101010101010101 3)
             (rotr 2r01010101010101010101010101010101 19)
                   2r10101010101010101010101010101010)))))
