;
; Helper functions needed by different SHA algortihms:
; mostly according to §3.2 of FIPS PUB 180-4
; http://csrc.nist.gov/publications/fips/fips180-4/fips-180-4.pdf
;

(ns closha.helpers)

(defn lsb
  "return the 32 least significant bits of a number"
  [x]
    (mod x (bit-shift-left 1 32)))

(defn msb
  "return the 32 most significant bits of a number"
  [x]
    (quot x (bit-shift-left 1 32)))

(defn rotr
  "rotate right (circular right shift) a 32-bit value x by n positions"
  [x n]
  (let [x (lsb x)
        n (mod n 32)]
  (bit-or
    (unsigned-bit-shift-right x n)
    (lsb (bit-shift-left x (- 32 n))))))

(defn rotl
  "rotate left (circular left shift) a 32 bit value x by n positions"
  [x n]
  (rotr x (- 32 (mod n 32))))

(def +m
  "addition modulo 2^32"
  (comp lsb +))

(defn- bytes2int
    "create a 32 bit int from a seq of 4 bytes (big endian)"
    [[a,b,c,d]]
    (reduce bit-or
      [(bit-shift-left a 24)
       (bit-shift-left (bit-and 0xff b) 16)
       (bit-shift-left (bit-and 0xff c) 8)
       (bit-and 0xff d)]))

(defn pad
    "convert string to blocks of 16 32bit integers"
    [s]
    (let [byte-vector (vec (.getBytes s))
          int-vector (mapv bytes2int (partition 4 4 (repeat 0) (conj byte-vector 0x80))),
          length (* 8 (count byte-vector)),
          zero-count (- 16 (mod (+ (count int-vector) 2) 16))
          padding (conj (vec (take zero-count (repeat 0))) (msb length) (lsb length))
      ]
      (into int-vector padding)))
