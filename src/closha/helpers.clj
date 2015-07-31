;
; Helper functions needed by different SHA algortihms:
; mostly according to §3.2 of FIPS PUB 180-4
; http://csrc.nist.gov/publications/fips/fips180-4/fips-180-4.pdf
;

(ns closha.helpers)


(defn lsb
  "return the w least significant bits of a number, defaults to 32"
  ([x]
   (lsb x 32))
  ([x w]
    (mod x (bit-shift-left 1 w))))

(defn rotr
  "rotate right (circular right shift) a w-bit value x by n positions; defaults to w=32;"
  ([x n w]
  (let [x (lsb x w)
        n (mod n w)]
  (bit-or
    (unsigned-bit-shift-right x n)
    (lsb (bit-shift-left x (- w n)) w))))
  ([x n]
   (rotr x n 32)))

(defn rotl
  "rotate left (circular left shift) a w-bit value x by n positions; defaults to w=32"
  ([x n w]
  (rotr x (- w (mod n w)) w))
  ([x n]
   (rotl x n 32)))

(defn +mod
  "(x+y) mod 2^w, defaults to w=32"
  ([x y w]
  (lsb (+ x y) w))
  ([x y]
  (+mod x y 32)))
