(ns closha.helpers-test
  (:require [clojure.test :refer :all]
            [closha.helpers :refer :all]))

; tests focus on the functions needed by SHA-256, i.e. 32 bit width, no rotl needed.

(def powersof2  (iterate (partial * 2) 1))

(deftest lsb-test
  (testing "does it return 0 for values of 2^32 and larger?"
    (is (= (->> powersof2 (drop 32) (take 16) (map lsb))
           (->> (repeat 0) (take 16))))))

(deftest rotr-test
  (testing "does rotr rotate 32 bit numbers correctly?"
    (testing "Testing (rotr x 16) "
      (is (= (mapv #(rotr % 16) [0xabcdef00 0x0000ffff 0xf0005000])
             [0xef00abcd 0xffff0000 0x5000f000])))
    (testing "does (rotr x 1) half even numbers?"
      (let [lst (take 32 (repeatedly #(* 2 (rand-int (Integer/MAX_VALUE)))))]
        (is (= (map #(rotr % 1) lst) (map #(/ % 2) lst)))))))

(deftest pad-test
  (testing "test padding according to csrc.nist.gov/groups/ST/toolkit/documents/Examples/SHA256.pdf"
    (testing "One Block Message Sample"
      (is (= (pad "abc") [0x61626380 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0x18])))
    (testing "Two Block Message Sample"
      (let [msg (pad "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq")]
        (is (= (subvec msg 0 10)
               [0x61626364 0x62636465 0x63646566 0x64656667 0x65666768
                0x66676869 0x6768696A 0x68696A6B 0x696A6B6C 0x6A6B6C6D]))
        (is (= (subvec msg 28)
               [0 0 0 0x1c0]))))))
