;
; SHA-256
;
; http://csrc.nist.gov/publications/fips/fips180-4/fips-180-4.pdf
;

(ns closha.sha256
  (:require [closha.helpers :refer :all]))


;
; §4.1.2 SHA-224 and SHA-256 Functions
;

(defn Ch
  [x y z]
  (bit-xor (bit-and x y) (bit-and (bit-not x) z)))

(defn Maj
  [x y z]
  (bit-xor (bit-and x y) (bit-and x z) (bit-and y z)))

(defn Σ0
  [x]
  (bit-xor (rotr x 2) (rotr x 13) (rotr x 22)))

(defn Σ1
  [x]
  (bit-xor (rotr x 6) (rotr x 11) (rotr x 25)))

(defn σ0
  [x]
  (bit-xor (rotr x 7) (rotr x 18) (unsigned-bit-shift-right x 3)))

(defn σ1
  [x]
  (bit-xor (rotr x 17) (rotr x 19) (unsigned-bit-shift-right x 10)))

;
; §4.2.2 SHA-224 and SHA-256 Constants
;

(def constants
  "SHA-224 and SHA-256 constants according to §4.2.2"
  [0x428a2f98 0x71374491 0xb5c0fbcf 0xe9b5dba5 0x3956c25b 0x59f111f1 0x923f82a4 0xab1c5ed5
   0xd807aa98 0x12835b01 0x243185be 0x550c7dc3 0x72be5d74 0x80deb1fe 0x9bdc06a7 0xc19bf174
   0xe49b69c1 0xefbe4786 0x0fc19dc6 0x240ca1cc 0x2de92c6f 0x4a7484aa 0x5cb0a9dc 0x76f988da
   0x983e5152 0xa831c66d 0xb00327c8 0xbf597fc7 0xc6e00bf3 0xd5a79147 0x06ca6351 0x14292967
   0x27b70a85 0x2e1b2138 0x4d2c6dfc 0x53380d13 0x650a7354 0x766a0abb 0x81c2c92e 0x92722c85
   0xa2bfe8a1 0xa81a664b 0xc24b8b70 0xc76c51a3 0xd192e819 0xd6990624 0xf40e3585 0x106aa070
   0x19a4c116 0x1e376c08 0x2748774c 0x34b0bcb5 0x391c0cb3 0x4ed8aa4a 0x5b9cca4f 0x682e6ff3
   0x748f82ee 0x78a5636f 0x84c87814 0x8cc70208 0x90befffa 0xa4506ceb 0xbef9a3f7 0xc67178f2])

(def initial-hash
  "SHA-256 initial hash values H_0 to H_7 according to §5.3.3"
  [0x6a09e667 0xbb67ae85 0x3c6ef372 0xa54ff53a 0x510e527f 0x9b05688c 0x1f83d9ab 0x5be0cd19])

(defn- mschedule
  "SHA-256 message schedule §6.2.2-1"
  [M]
  (loop [m (vec M)]
    (let [t (count m)]
      (if (= t 64) m
        (recur (conj m
          (lsb (+ (σ1 (m (- t 2))) (m (- t 7))
                  (σ1 (m (- t 15))) (m (- t 16))))))))))


(defn- var-iter
  "re-assign temporary variables a-h according to §6.2.2-3"
  [[a b c d e f g h] K_t W_t]
  (let [T1 (+m (Σ1 e) (Ch e f g) K_t W_t)
        T2 (+m (Σ0 a) (Maj a b c))]
    ;    a      b c d     e     f g h
    [(+m T1 T2) a b c (+m d T1) e f g]))

(defn sha256
  "SHA 256 Algorithm"
  [s]
  (let [msg (pad s)  ; msg is the padded message as a list of N lists of 16 32-bit integers
        N (count msg)
        combine (fn [v1 v2] (map (partial apply +m) (map vector v1 v2)))]
    (loop [H initial-hash
           i 1]
      (if (> i N) (apply str (map (partial format "%x") H))
        (let [W (mschedule (nth msg (dec i))),
              H_new
                (loop [vars H
                       t 0]
                  (if (= t 64) vars
                    (recur (var-iter vars (constants t) (W t)) (inc t))))]
        (recur (combine H H_new) (inc i) ))))))













;
