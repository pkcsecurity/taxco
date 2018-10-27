(ns t.clj.routes.device
  (:require [t.clj.routes.impl :as impl]))

(defonce state (atom nil))

(impl/defroute create-device [{{:strs [id type]} :query-params 
                               :keys [body] 
                               :as req}]
  (swap! state assoc-in [id type] body)
  (impl/ok))

(impl/defroute read-device [{{:strs [id]} :query-params :as req}]
  (println :FOOOASODJASKJDA)
  (if id
    (if-let [id-state (get @state id)]
      (impl/ok id-state)
      (impl/not-found))
    (impl/ok @state)))
