(ns closha.sha256-test
  (:require [clojure.test :refer :all]
            [closha.sha256 :refer :all]
            [clojure.string :refer [upper-case]]))



(deftest sha256-test
  (testing "test example digests from csrc.nist.gov/groups/ST/toolkit/documents/Examples/SHA256.pdf"
    (testing "One Block Message Sample"
      (is (= (upper-case (sha256 "abc"))
             "BA7816BF8F01CFEA414140DE5DAE2223B00361A396177A9CB410FF61F20015AD")))
    (testing "Two Block Message Sample"
      (is (= (upper-case (sha256 "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"))
           "248D6A61D20638B8E5C026930C3E6039A33CE45964FF2167F6ECEDD419DB06C1"))))
  (testing "example digests"
    (testing "empty string"
      (is (= (sha256 "")
           "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")))))
