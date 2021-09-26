(defproject sundews "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.github.seancorfield/next.jdbc "1.2.709"]
                 [ring "1.9.4"]
                 [ring/ring-anti-forgery "1.2.0"]
                 [ring/ring-defaults "0.3.3"]
                 [compojure "1.6.2"]
                 [hiccup "1.0.5"]
                 [honeysql "1.0.461"]]
  :main ^:skip-aot sundews.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :repl-options {
                 :host "0.0.0.0"
                 :port 7001
                 })
