(ns t.cljs.core
   (:require [accountant.core :as accountant]
             [bidi.bidi :as bidi]
             [t.cljc.routes :as routes]
             [t.cljs.body :as body]
             [t.cljs.components :as c]
             [t.cljs.xhr :as xhr]
             [reagent.core :as r]))

(def session (atom {}))

(enable-console-print!)

(defn app []
  [:div#app
   [c/style]
   [:div#body
    [body/body]]])

(defn on-js-load []
  (r/render-component [app]
                      (. js/document (getElementById "reagent"))))

(defn -main []
  (accountant/configure-navigation!
    {:nav-handler (fn [path]
                    (let [{page-kw :handler 
                           rps :route-params} (bidi/match-route 
                                                routes/routes 
                                                path)]
                      (swap! session 
                             assoc
                             :route
                             {:current-page page-kw
                              :route-params rps})))
     :path-exists? (fn [path]
                     (boolean (bidi/match-route routes/routes path)))})
  (accountant/dispatch-current!)
  (on-js-load))

(-main)
