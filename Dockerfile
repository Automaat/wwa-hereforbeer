FROM dockerfile/java:oracle-java8

MAINTAINER hereForBeer

ENV ES_PKG_NAME elasticsearch-1.5.0

RUN \
  cd / && \
  wget https://download.elasticsearch.org/elasticsearch/elasticsearch/$ES_PKG_NAME.tar.gz && \
  tar xvzf $ES_PKG_NAME.tar.gz && \
  rm -f $ES_PKG_NAME.tar.gz && \
  mv /$ES_PKG_NAME /elasticsearch

VOLUME ["/data"]

WORKDIR /data

CMD ["/elasticsearch/bin/elasticsearch"]

EXPOSE 9200
EXPOSE 9300