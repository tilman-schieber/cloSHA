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

(def +m 
  "addition modulo 2^32"
  (comp #(lsb % 32) +))

(defn- bytes2int
    "create a 32 bit int from a seq of 4 bytes (big endian)"
    [[a,b,c,d]]
    (reduce bit-or [(bit-shift-left a 24) (bit-shift-left b 16) (bit-shift-left c 8) d ]))

(defn pad
    "convert string to blocks of 16 32bit integers"
    [s]
    (partition 16 16 (repeat 0)
               (conj
                   (vec (map bytes2int (partition 4 4 (repeat 0)(.getBytes (str s (char 0x80))))))
                   (count(.getBytes (str s ))))))
