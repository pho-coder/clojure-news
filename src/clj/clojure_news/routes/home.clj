(ns clojure-news.routes.home
  (:require [clojure-news.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [clojure.data.xml :as xml]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defn clojure-news-get [data]
  (log/info data)
  "success")

(defn clojure-news [data]
  (log/info data)
  (let [body (xml/parse (:body data))
        _ (log/debug body)
        to-user-name (first (:content (first (filter #(= (:tag %) :ToUserName) (:content body)))))
        _ (log/debug to-user-name)
        from-user-name (first (:content (first (filter #(= (:tag %) :FromUserName) (:content body)))))
        _ (log/debug from-user-name)
        content (first (:content (first (filter #(= (:tag %) :Content) (:content body)))))
        _ (log/debug content)
        re-xml (xml/emit-str (xml/element :xml {}
                                          (xml/element :ToUserName {} (xml/cdata from-user-name))
                                          (xml/element :FromUserName {} (xml/cdata to-user-name))
                                          (xml/element :CreateTime {} (quot (System/currentTimeMillis) 1000))
                                          (xml/element :MsgType {} (xml/cdata "text"))
                                          (xml/element :Content {} (xml/cdata content))))
        cut-head-re (.substring re-xml 38)
        _ (log/info cut-head-re)]
    cut-head-re))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/clojure" [data] (clojure-news-get data))
  (POST "/clojure" [] (fn [req] (clojure-news req))))

