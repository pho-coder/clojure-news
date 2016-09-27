(ns clojure-news.routes.clojure-news
  (:require [compojure.core :refer [defroutes GET POST]]
            [clojure.tools.logging :as log]
            [clojure.data.xml :as xml]))

(defn clojure-news-check [req]
  (log/info "req:" req)
  (let [params (:params req)
        echostr (:echostr params)]
    (log/info "echostr:" echostr)
    echostr))

(defn clojure-news [req]
  (log/info "req:" req)
  (let [body (xml/parse (:body req))
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

(defroutes clojure-news-routes
  (GET "/clojure-news" [] (fn [req] (clojure-news-check req)))
  (POST "/clojure-news" [] (fn [req] (clojure-news req))))
