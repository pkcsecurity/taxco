(ns t.clj.routes
  (:require [t.clj.routes.impl :as impl]
            [t.cljc.routes :as cljc-routes]
            [ring.middleware.resource :as resource]))

(def handler 
  (impl/init-handler cljc-routes/routes))

(def app
  (-> #'handler
      (impl/wrap-reload)
      (resource/wrap-resource "public")))
