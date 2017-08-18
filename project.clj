(defproject oncesaid "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [morse "0.2.5-SNAPSHOT"]]
  :plugins [[lein-modules "0.3.11"]]
  :modules {:dirs ["modules/morse"]
            :subprocess nil}
  :main ^:skip-aot oncesaid.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
