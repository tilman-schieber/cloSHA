(ns closha.aes-test
  (:require [clojure.test :refer :all]
            [closha.aes :refer :all]))

(defn- rand-vec [n] (take n (repeatedly #(rand-int 255))))

(deftest ginv-test
  (testing "test the multiplicative inverse elements for all x in GF(2^8)"
    (is (every? (partial = 1) (map #(gmul % (ginv %)) (range 1 255)))))
  (testing "are the first five inverse elements correct?"
    (is (= (range 1 5) (map ginv [1 141 246 203] )))))

(deftest cipher-test
  (let [plaintext [0x00 0x11 0x22 0x33 0x44 0x55 0x66 0x77
                   0x88 0x99 0xaa 0xbb 0xcc 0xdd 0xee 0xff]]
    (testing "AES-128 test FIPS-197 C.1 p.35f"
      (let [key (range 0 16)
            cyphertext [0x69 0xc4 0xe0 0xd8 0x6a 0x7b 0x04 0x30
                        0xd8 0xcd 0xb7 0x80 0x70 0xb4 0xc5 0x5a]]
        (is (= cyphertext (cypher plaintext key)))))
    (testing "AES-192 test FIPS-197 C.2 p.38f"
          (let [key (range 0 24)
                cyphertext [0xdd 0xa9 0x7c 0xa4 0x86 0x4c 0xdf 0xe0
                            0x6e 0xaf 0x70 0xa0 0xec 0x0d 0x71 0x91]]
            (is (= cyphertext (cypher plaintext key)))))

    (testing "AES-256 test FIPS-197 C.3 p.42f"
          (let [key (range 0 32)
                cyphertext [0x8e 0xa2 0xb7 0xca 0x51 0x67 0x45 0xbf
                            0xea 0xfc 0x49 0x90 0x4b 0x49 0x60 0x89]]
            (is (= cyphertext (cypher plaintext key)))))))

(deftest inverse-test
  (testing "InvSubBytes"
    (let [s (rand-vec 16)]
      (is (= s (sub-bytes (inv-sub-bytes s))))))
  (testing "InvShiftRows"
    (let [s (rand-vec 16)]
      (is (= s (shift-rows (inv-shift-rows s))))))
  (testing "InvMixColumns"
    (let [s (rand-vec 16)]
      (is (= s (mix-columns (inv-mix-columns s))))))
  (testing "InvCypher 128bit"
    (let [t (rand-vec 16)
          k (rand-vec 16)]
      (is (= t (inv-cypher (cypher t k) k))))))
