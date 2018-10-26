(ns t.clj.routes.device
  (:require [t.clj.routes.impl :as impl]))

(defonce state
  (atom nil))

(impl/defroute create-device [{:keys [body] :as req}]
  (println "receiving")
  (let [id (keyword (get (:query-params req) "id"))
        type (keyword (get (:query-params req) "type"))]
  (println req)
  (swap! state assoc-in [id type] body)
  (impl/ok
    {:echo body})))

