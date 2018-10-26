(ns t.cljs.xhr
  (:require [bidi.bidi :as bidi]
            [t.cljc.routes :as cljc-routes]
            [t.cljs.msgpack :as msgpack]
            [schema.core :as s :include-macros true]))

(defn fetch-roundtrip [kw body]
  (if-let [[request-schema response-schema] (cljc-routes/schemas kw)]
    (-> (bidi/path-for cljc-routes/routes kw)
        (js/fetch 
          (js-obj "method" "POST"
                  "body" (msgpack/pack (s/validate request-schema body))
                  "headers" (js-obj "Content-Type"
                                    "application/octet-stream")))
        (.then #(.arrayBuffer %))
        (.then #(.log js/console 
                      (s/validate response-schema 
                                  (msgpack/unpack %)))))
    (throw (js/Error. (str "Keyword " kw " has no matching schemas")))))
