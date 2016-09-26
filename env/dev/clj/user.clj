(ns user
  (:require [mount.core :as mount]
            clojure-news.core))

(defn start []
  (mount/start-without #'clojure-news.core/repl-server))

(defn stop []
  (mount/stop-except #'clojure-news.core/repl-server))

(defn restart []
  (stop)
  (start))


