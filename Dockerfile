FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/clojure-news.jar /clojure-news/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/clojure-news/app.jar"]
