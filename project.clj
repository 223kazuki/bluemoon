(defproject bluemoon "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0-alpha12"]
                 [com.stuartsierra/component "0.3.1"]
                 [compojure "1.5.1"]
                 [duct "0.8.0"]
                 [environ "1.0.3"]
                 [ring "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring-jetty-component "0.3.1"]
                 [ring-webjars "0.1.1"]
                 [org.slf4j/slf4j-nop "1.7.21"]
                 [org.webjars/normalize.css "3.0.2"]
                 [duct/hikaricp-component "0.1.0"]
                 [duct/ragtime-component "0.1.4"]

                 [garden "1.3.2"]
                 [com.h2database/h2 "1.4.191"]
                 [org.clojure/algo.monads "0.1.6"]
                 [korma "0.4.3"]
                 [migratus "0.8.27"]
                 [camel-snake-kebab "0.4.0"]

                 [org.clojure/clojurescript "1.9.229"]
                 [org.omcljs/om "1.0.0-alpha46"]
                 [com.cognitect/transit-clj "0.8.288"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [sablono "0.7.4"]
                 [compassus "0.2.1"]

                 [secretary "1.2.3"]
                 [venantius/accountant "0.1.6"
                  :exclusions [org.clojure/tools.reader]]]
  :plugins [[lein-environ "1.0.3"]
            [lein-cljsbuild "1.1.2"]
            [lein-garden "0.2.8"]]
  :main ^:skip-aot bluemoon.main
  :target-path "target/%s/"
  :source-paths ["src/main/clj" "src/main/cljc"]
  :test-paths ["src/test/clj"]
  :resource-paths ["src/main/resources" "target/cljsbuild"]
  :cljsbuild {:builds
              {:main {:jar true
                      :source-paths ["src/main/cljs" "src/main/cljc"]
                      :compiler {:output-to "target/cljsbuild/bluemoon/public/js/main.js"
                                 :optimizations :advanced}}}}
  :profiles
  {:dev  [:project/dev  :profiles/dev]
   :test [:project/test :profiles/test]
   :repl {:resource-paths ^:replace ["src/main/resources" "src/dev/resources" "target/figwheel"]}
   :uberjar {:aot :all}
   :profiles/dev  {}
   :profiles/test {}
   :project/dev   {:dependencies [[duct/generate "0.8.0"]
                                  [reloaded.repl "0.2.2"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [eftest "0.1.1"]
                                  [com.gearswithingears/shrubbery "0.3.1"]
                                  [kerodon "0.7.0"]
                                  [binaryage/devtools "0.6.1"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [duct/figwheel-component "0.3.2"]
                                  [figwheel "0.5.0-6"]
                                  [alembic "0.3.2"]
                                  [me.raynes/fs "1.4.5"]
                                  [lein-light-nrepl "0.3.2"]]
                   :source-paths   ["src/dev/clj"]
                   :resource-paths ["src/dev/resources"]
                   :repl-options {:init-ns user
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl
                                                     lighttable.nrepl.handler/lighttable-ops]}
                   :env {:port "3000"}}
   :project/test  {}})
