(ns clojure-news.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [clojure-news.layout :refer [error-page]]
            [clojure-news.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [clojure-news.env :refer [defaults]]
            [mount.core :as mount]
            [clojure-news.middleware :as middleware]
            [clojure-news.routes.clojure-news :refer [clojure-news-routes]]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'clojure-news-routes
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
