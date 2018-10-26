(defproject taxco "0.1.0-SNAPSHOT"
  :dependencies [[bidi "LATEST"]
                 [camel-snake-kebab "0.4.0"]
                 [cheshire "LATEST"]
                 [clojure-msgpack "LATEST"]
                 [compojure "LATEST"]
                 [environ "LATEST"]
                 [garden "LATEST"]
                 [hiccup "LATEST"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "LATEST"]
                 [org.immutant/web "LATEST"]
                 [reagent "LATEST"]
                 [ring/ring-core "LATEST"]
                 [ring/ring-devel "LATEST"]
                 [prismatic/schema "LATEST"]
                 [venantius/accountant "LATEST"]]
  :clean-targets ^{:protect false} ["resources/public/js/development"
                                    "resources/public/js/release"
                                    "target"]
  :resource-paths ["resources"]
  :main ^:skip-aot t.clj.core
  :plugins [[lein-cljsbuild "LATEST"]
            [lein-cljfmt "LATEST"]
            [lein-environ "LATEST"]
            [lein-figwheel "LATEST"]]

  :cljfmt {:indents {#".*" [[:block 0]]}}

  :profiles {:dev [:profiles/dev]
             :uberjar {:main t.clj.core 
                       :aot :all
                       :auto-clean false}
             ;; only edit :profiles/* in profiles.clj
             :profiles/dev  {}}

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/t/cljs"]
                :figwheel true
                :compiler
                {:output-to "resources/public/js/development/index.js"
                 :source-map true
                 :output-dir "resources/public/js/development"
                 :optimizations :none
                 :main t.cljs.core
                 :asset-path "/js/development"
                 :infer-externs false
                 :cache-analysis true
                 :pretty-print true}}
               {:id "release"
                :source-paths ["src/t/cljs"]
                :compiler
                {:output-to "resources/public/js/release/index.js"
                 :source-map "resources/public/js/release/index.js.map"
                 :externs ["externs/externs.js"]
                 :main t.cljs.core
                 :output-dir "resources/public/js/release"
                 :optimizations :advanced
                 :pseudo-names false}}]})
