(ns clojure-news.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[clojure-news started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[clojure-news has shut down successfully]=-"))
   :middleware identity})
