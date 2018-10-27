(ns t.cljs.xhr
  (:require [bidi.bidi :as bidi]
            [t.cljc.routes :as cljc-routes]
            [t.cljs.msgpack :as msgpack]
            [schema.core :as s :include-macros true]))

(defn fetch [kw verb body on-result]
  (if-let [[request-schema response-schema] (cljc-routes/schemas kw)]
    (-> (bidi/path-for cljc-routes/routes kw)
        (js/fetch 
          (js-obj "method" (case verb
                             :post "POST"
                             :get "GET")
                  "body" (when-not (= :get verb)
                           (JSON/stringify (s/validate request-schema body)))
                  "headers" (js-obj "Content-Type"
                                    "application/json")))
        (.catch #(.error js/console %))
        (.then #(.json %))
        (.then (fn [res]
                 (s/validate response-schema res)
                 (on-result (js->clj res 
                                     :keywordize-keys true)))))
    (throw (js/Error. (str "Keyword " kw " has no matching schemas")))))
