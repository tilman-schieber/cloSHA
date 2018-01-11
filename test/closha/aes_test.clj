(ns closha.aes-test
  (:require [clojure.test :refer :all]
            [closha.aes :refer :all]))

(deftest ginv-test
  (testing "test the multiplicative inverse elements for all x in GF(2^8)"
    (is (every? (partial = 1) (map #(gmul % (ginv %)) (range 1 255)))))
  (testing "are the first five inverse elements correct?"
    (is (= (range 1 5) (map ginv [1 141 246 203] )))))

(deftest cipher-test
  (testing "AES-128 test FIPS-197 p.35f"
    (let [key (range 0 16)
          plaintext [0x00 0x11 0x22 0x33 0x44 0x55 0x66 0x77 0x88
                     0x99 0xaa 0xbb 0xcc 0xdd 0xee 0xff]
          cyphertext [0x69 0xc4 0xe0 0xd8 0x6a 0x7b 0x04 0x30
                      0xd8 0xcd 0xb7 0x80 0x70 0xb4 0xc5 0x5a]]
      (is (= cyphertext (cypher plain key))))))
