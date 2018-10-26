(ns t.clj.routes.impl
  (:require 
    [bidi.ring :as bidi]
    [clojure.java.io :as io]
    [environ.core :as env]
    [t.cljc.routes :as cljc-routes]
    [msgpack.core :as msgpack]
    [msgpack.clojure-extensions]
    [cheshire.core :as cheshire]
    [ring.middleware.reload :as rl]
    [schema.core :as s])
  (:import [java.io ByteArrayInputStream]))

(def not-found (constantly {:status 404}))

(defn ok 
  ([] {:status 201})
  ([body] (if body
            {:status 200
             :body body}
            (ok))))

; 500 if the keyword is garbage at runtime, instead of compile time,
; so it doesn't crash reloading
(defn invalid-bidi-route [v]
  (fn [_]
    (throw 
      (ex-info (str "Unable to resolve keyword to class or route"  v)
               {:keyword v}))))

; 1) use try-catch to keep reload from breaking 
; 2) use require to keep from having to require conditionally in the cljc
; 3) kw->symbol->var so we can replace at compile time with classes
(defn walk-route-map [m]
  (into {}
        (for [[k v] m]
          (if (keyword? v)
            (let [nspace (namespace v)
                  sym (symbol nspace (name v))
                  nspace-sym (symbol nspace)]
              (try
                (require nspace-sym)
                (catch Exception e
                  (binding [*out* *err*]
                    (println e))))
              (if-let [vr (ns-resolve *ns* sym)]
                [k vr]
                [k (invalid-bidi-route v)]))
            [k (walk-route-map v)]))))

; build the funky data structure that bidi requires
; https://github.com/juxt/bidi#wrapping-as-a-ring-handler 
(defmacro init-handler [page-routes]
  `(bidi/make-handler
     (let [page-routes# ~page-routes]
       (loop [[host# route# & xs#] page-routes#
              acc# []]
         (if-not host#
           acc#
           (recur xs# (conj acc# host# (walk-route-map route#))))))))

; removed run-dmc because immutant was not reloading cljc, also, the ugly stacktrace and
; opening up a browser process finally pushed me over the edge....
;
; https://media.giphy.com/media/ReImZejkBnqYU/giphy.gif
(defn wrap-reload [handler]
  (if (= "development" (env/env :environment))
    (rl/wrap-reload handler)
    handler))

(defmacro defroute [route-name bindings & body]
  `(let [route-kw# ~(keyword (str *ns*) (str route-name))
         handler# (fn ~bindings (do ~@body))]
     (if-let [[request-schema# response-schema#] (cljc-routes/schemas route-kw#)]
       (def ~route-name 
         (fn [req#]
           (let [req-body# (cheshire/parse-stream (io/reader (:body req#)) true)]
             (s/validate request-schema# req-body#)
             (let [result# (handler# (assoc req# :body req-body#))]
               (s/validate response-schema# (:body result#))
               (update result# 
                       :body 
                       (fn [x#]
                         (cheshire/generate-string x#)))))))
       (throw (ex-info "No matching schemas for route" {:route route-kw#})))))
