(ns net.ci4j.clj.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::long? #(instance? Long %))
(s/def ::big-decimal? #(instance? BigDecimal %))

(s/def ::coll-of-long? (s/coll-of ::long?))
(s/def ::coll-of-integer? (s/coll-of integer?))
(s/def ::coll-of-big-decimal? (s/coll-of ::big-decimal?))

