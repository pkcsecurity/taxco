(ns t.cljc.routes
  (:require [schema.core :as s]))

(def yolo-schema
  s/Any)

(def routes
  ["/"
   {"" {:get :t.clj.routes.index/index}
    "device" {:get :t.clj.routes.device/read-device
              :post :t.clj.routes.device/create-device}
    true :t.clj.routes.impl/not-found}])

(def schemas
  {:t.clj.routes.device/create-device [yolo-schema
                                       yolo-schema]
   :t.clj.routes.device/read-device [yolo-schema
                                     yolo-schema]})
